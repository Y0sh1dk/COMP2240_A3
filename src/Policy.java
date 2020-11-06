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

}
