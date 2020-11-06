import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class A3 {
    public static void main(String[] args) {
        if (args.length <= 3) { // If wrong amount of args given
            System.out.println("Usage: A1 [file]"); // TODO: fix
            return;
        }
        A3 main = new A3();
        main.run(args);
    }

    private void run(String[] args) {
        // Not much input sanitation
        int totalFrames = Integer.parseInt(args[0]);
        int timeQuantum = Integer.parseInt(args[1]);

        ArrayList<Process> processesLRU = generateProcessesFromFiles(args);
        ArrayList<Process> processesClock = generateProcessesFromFiles(args);
        int numOfProcesses = processesLRU.size();
        setAllProcessMaxFrames(totalFrames/numOfProcesses , processesLRU);
        setAllProcessMaxFrames(totalFrames/numOfProcesses , processesClock);

        LRUPolicy LRU = new LRUPolicy(timeQuantum);
        LRU.addProcesses(processesLRU);
        LRU.run();

        //ClockPolicy Clock = new ClockPolicy(timeQuantum);
        //Clock.addProcesses(processesClock);
        //Clock.run();

        System.out.println("LRU - Fixed:");
        generateStats(processesLRU);
        //System.out.println("LRU - Fixed:");
        //generateStats(processesClock);

    }

    private void generateStats(ArrayList<Process> processes) {
        System.out.println("PID  Process Name    Turnaround Time  # Faults  Fault Times");
        for (Process p : processes) {
            System.out.println(String.format("%-4s %-15s %-16s %-9s %-9s"
                    , p.getProcessID(), p.getProcessName(), p.getTurnAroundTime()
                    , p.getNumOfFaults(), p.getFaultString()));
        }
    }

    private ArrayList<Process> generateProcessesFromFiles(String[] args) {
        ArrayList<Process> processList = new ArrayList<>();
        for (int i = 2; i < args.length; i ++) {
            // for each file
            processList.add(generateProcess(args[i]));
        }
        return processList;
    }

    private Process generateProcess(String s) {
        int id;
        ArrayList<Integer> pageRequests = new ArrayList<>();
        try {
            Scanner inputStream = new Scanner(new File(s));
            inputStream.nextLine(); // skip 'begin'
            while (inputStream.hasNextLine()) {
                String line = inputStream.nextLine();
                if (!line.equals("end")) {
                    pageRequests.add(Integer.parseInt(line));
                }
            }
            id = Integer.parseInt(s.split("\\.")[0].substring(s.length() - 5));
            return new Process(id, s, pageRequests);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    private void setAllProcessMaxFrames(int i, ArrayList<Process> processes) {
        for (Process p : processes) {
            p.setMaxFrames(i);
        }
    }

}
