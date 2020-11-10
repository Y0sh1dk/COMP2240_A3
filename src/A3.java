/**
 *  FileName: A3.java
 *  Assessment: COMP2240 - A3
 *  Author: Yosiah de Koeyer
 *  Student No: c3329520
 *
 *  Description:
 *  Main class file for A3, accepts parameters from command line args and creates processes and pages from the input
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class A3 {

    /**
     * Entry point for A3 class
     * @param args takes params from cmd args
     * @return Nothing.
     */
    public static void main(String[] args) {
        if (args.length <= 3) { // If wrong amount of args given
            System.out.println("Invalid Arguments, Example: A3 30 3 Process1.txt Process2.txt Process3.txt");
            return;
        }
        A3 main = new A3();
        main.run(args);
    }

    /**
     * run() method, gets passed args where it then generates processes and parameters from the args. Instantiates
     * the replacement policies, runs them and prints out the results.
     * @param args
     */
    private void run(String[] args) {
        // Not much input sanitation
        int totalFrames = Integer.parseInt(args[0]);
        int timeQuantum = Integer.parseInt(args[1]);

        ArrayList<Process> processesLRU = generateProcessesFromFiles(args);     // populate ArrayList with processes from input
        ArrayList<Process> processesClock = generateProcessesFromFiles(args);   // populate ArrayList with processes from input
        int numOfProcesses = processesLRU.size();
        int maxFrames = totalFrames/numOfProcesses;

        LRUPolicy LRU = new LRUPolicy(timeQuantum, maxFrames, processesLRU);
        LRU.run();

        ClockPolicy Clock = new ClockPolicy(timeQuantum, maxFrames, processesClock);
        Clock.run();

        System.out.println("LRU - Fixed:");
        generateStats(processesLRU);
        System.out.println("Clock - Fixed:");
        generateStats(processesClock);

    }

    /**
     * generateStats() method, prints the stats of both simulations
     * @param processes an ArrayLists of processes to extract the stats from
     */
    private void generateStats(ArrayList<Process> processes) {
        System.out.println("PID  Process Name    Turnaround Time  # Faults  Fault Times");
        for (Process p : processes) {
            System.out.println(String.format("%-4s %-15s %-16s %-9s %-9s"
                    , p.getProcessID(), p.getProcessName(), p.getTurnAroundTime()
                    , p.getNumOfFaults(), p.getFaultString()));
        }
    }

    /**
     * generateProcessesFromFiles() method
     * @param args args from the command line input
     * @return an ArrayList of processes from the given files
     */
    private ArrayList<Process> generateProcessesFromFiles(String[] args) {
        ArrayList<Process> processList = new ArrayList<>();
        for (int i = 2; i < args.length; i ++) {
            // for each file
            processList.add(generateProcess(args[i]));
        }
        return processList;
    }

    /**
     * generateProcess() method, generates the process from the given file
     * @param s a String containing the filepath
     * @return an instance of Process
     */
    private Process generateProcess(String s) {
        int id;
        ArrayList<Integer> pageRequests = new ArrayList<>();
        try {
            Scanner inputStream = new Scanner(new File(s));
            inputStream.nextLine();             // skip 'begin'
            while (inputStream.hasNextLine()) {
                String line = inputStream.nextLine();
                if (!line.equals("end")) {      // while not at the end
                    pageRequests.add(Integer.parseInt(line));
                }
            }
            id = Integer.parseInt(s.split("\\.")[0].substring(s.length() - 5)); // ID of process from filename
            return new Process(id, s, pageRequests);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

}
