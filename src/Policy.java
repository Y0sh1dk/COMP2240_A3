import java.util.ArrayList;

public abstract class Policy {
    private String name;
    protected ArrayList<Process> allProcesses;
    protected ArrayList<Process> finishedProcesses;
    private int currentTime;


    protected int getCurrentTime() {
        return currentTime;
    }

    protected void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    protected void incCurrentTime(int t) {
        this.currentTime += t;
    }

    //    Move jobs that have finished executing from currentJobs to finished Jobs
    protected void checkFinished() {
        for (Process p : allProcesses) {
            if (p.getFinishTime() != 0) {
                finishedProcesses.add(p);
            }
        }
        allProcesses.removeIf(p -> p.getFinishTime() != 0); // remove process if it has finished
    }
    
}
