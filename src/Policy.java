import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;

public abstract class Policy {
    private String name;
    protected ArrayList<Process> readyProcesses;
    protected ArrayList<Process> blockedProcesses;
    protected ArrayList<Process> finishedProcesses;
    private int currentTime;
    protected int RRQuant;

    private boolean isClockPolicy;

    Policy(String n, int RRQuant) {
        this.readyProcesses = new ArrayList<>();
        this.blockedProcesses = new ArrayList<>();
        this.finishedProcesses = new ArrayList<>();
        this.name = n;
        this.RRQuant = RRQuant;
        this.currentTime = 0;
        this.isClockPolicy = n.equals("ClockPolicy"); // String equals returns boolean
    }

    protected int getCurrentTime() {
        return currentTime;
    }

    protected void incCurrentTime(int t) {
        this.currentTime += t;
    }


    public void addProcesses(ArrayList<Process> processes) {
        for (Process p : processes) {
            readyProcesses.add(p);
        }
    }

    protected void addAllProcessesToCurrent(ArrayList<Process> processes) {
        for (Process p : processes) {
            readyProcesses.add(p);
        }
    }

    protected void moveProcessToEnd() { // moves job currently at top to the bottom
        if (readyProcesses.size() > 0) { // avoid errors if list is empty
            Process temp = this.readyProcesses.get(0);
            readyProcesses.remove(0);
            readyProcesses.add(temp); // automatically adds to bottom of list
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    abstract void run();

    protected void updateStates() {
        for (Iterator<Process> i = readyProcesses.iterator(); i.hasNext();) {
            Process p = i.next();
            if (p.getCurrentRequest() >= p.numOfRequests()) {
                System.out.println(p.getProcessID() + ": FINISHED (time=" + getCurrentTime() + ")");
                p.setState(Process.State.FINISHED);
                p.setFinishTime(this.getCurrentTime());
                this.finishedProcesses.add(p);
                i.remove();
            } else if (p.isRequestInMM()) {
                p.setState(Process.State.READY);
            } else { // if request is not in Main Memory
                p.generateFault(this.getCurrentTime());
                p.swapInPageToMM(this.getCurrentTime());
                p.setState(Process.State.BLOCKED);
                this.blockedProcesses.add(p);
                i.remove();
            } // If it has done all of its pages requests
        }

        for (Iterator<Process> i = blockedProcesses.iterator(); i.hasNext();) {
            Process p = i.next();
            if (p.getCurrentRequest() >= p.numOfRequests()) {
                System.out.println(p.getProcessID() + ": FINISHED (time=" + getCurrentTime() + ")");
                p.setState(Process.State.FINISHED);
                p.setFinishTime(this.getCurrentTime());
                this.finishedProcesses.add(p);
                i.remove();
            } else if (!p.isRequestInMM()) {
                if (p.getSwapInStartTime() + p.getSWAP_IN_TIME() <= this.getCurrentTime()) { // page ready to be swapped
                    p.swapCurrentRequestToMM();  // swap the page in!
                    p.setState(Process.State.READY);
                    System.out.println(p.getProcessID() + ": READY (time=" + getCurrentTime() + ")");
                    this.readyProcesses.add(p); // add process to ready queue
                    i.remove(); // remove it from blocked queue
                }
            }
        }
    }

}
