import java.util.Arrays;
import java.util.Iterator;

/**
 *  FileName: LRUPolicy.java
 *  Assessment: COMP2240 - A3
 *  Author: Yosiah de Koeyer
 *  Student No: c3329520
 *
 *  Description:
 *  Extends Policy class and implements LRU strategy
 */



public class LRUPolicy extends Policy {
    LRUPolicy(int RRQuant, int maxFrames) {
        super("LRUPolicy", RRQuant, maxFrames);
    }

    /**
     * run() method,
     * Overrides the abstract method in Policy class, tries to schedule the processes using the Round Robin
     * scheduling algorithm.
     * Page replacment is done using LRU policy
     */
    @Override
    void run() {
        System.out.println("Initialising " + this.getName() + " Algorithm...");

        this.initializeMemory();

        Process runningProcess = null;
        int processStartTime = 0;
        boolean quantFinished = false;

        while (this.readyProcesses.size() + this.blockedProcesses.size() > 0) { // while not all processes have finished
            if (quantFinished) {
                try {
                    this.readyProcesses.add(this.readyProcesses.remove(0));

                } catch (Exception e) {

                }
                quantFinished = false;
            }
            this.updateStates();
            if (this.readyProcesses.size() > 0) { // if processes are ready
                if (!(runningProcess == readyProcesses.get(0))) { // if not same process as last time
                    processStartTime = getCurrentTime();
                }
                runningProcess = readyProcesses.get(0); // get the process of the top
                setPageAccessTime(runningProcess.getProcessID(), runningProcess.getCurrentPageID(), this.getCurrentTime());
                runningProcess.run(this.getCurrentTime());


                if (processStartTime + this.RRQuant - 1 <= this.getCurrentTime()) { // time quant is up
                    quantFinished = true;
                }
                incCurrentTime(1); // increment time by 1
            } else {
                incCurrentTime(1); // increment time by 1
            }
        }
        System.out.println("test");
    }

    private void setPageAccessTime(int processID, int pageID, int time) {
        for (int i = 0; i < mainMemory[processID-1].length; i++) {
            if (mainMemory[processID-1][i] != null) {
                if (mainMemory[processID-1][i].getPageID() == pageID) {
                    mainMemory[processID-1][i].setLastAccessTime(time);
                    break;
                }
            }
        }
    }

    private void updateStates() {
        for (Iterator<Process> i = readyProcesses.iterator(); i.hasNext();) {
            Process p = i.next();
            if (p.getCurrentRequest() >= p.numOfRequests()) { // if the process has finished
                System.out.println(p.getProcessID() + ": FINISHED (time=" + getCurrentTime() + ")");
                p.setState(Process.State.FINISHED); // set its state
                p.setFinishTime(this.getCurrentTime()); // set its finish time
                this.finishedProcesses.add(p); // add it too the finished queue
                i.remove(); // remove from the ready queue
                continue;
            }
            if (this.isPageInMemory(p.getProcessID(),p.getCurrentPageID())) { // page is in memory
                // Do nothing?
            } else { // page is not in memory
                System.out.println(p.getProcessID() + ": BLOCKED (time=" + getCurrentTime() + ")");
                if (this.getNumOfPagesInMemory(p.getProcessID()) == maxFramesPerProcess) {
                    this.removePage(p.getProcessID());
                }
                p.generateFault(this.getCurrentTime());
                p.setState(Process.State.BLOCKED); // set state
                p.setSwapInStartTime(this.getCurrentTime());
                this.blockedProcesses.add(p); // add process to blocked queue
                i.remove(); // remove from ready queue
            }
        }

        for (Iterator<Process> i = blockedProcesses.iterator(); i.hasNext();) {
            Process p = i.next();
            if (p.getCurrentRequest() >= p.numOfRequests()) { // if the process has finished
                System.out.println(p.getProcessID() + ": FINISHED (time=" + getCurrentTime() + ")");
                p.setState(Process.State.FINISHED); // set its state
                p.setFinishTime(this.getCurrentTime()); // set its finish time
                this.finishedProcesses.add(p); // add it too the finished queue
                i.remove(); // remove from the ready queue
            } else if (!isPageInMemory(p.getProcessID(), p.getCurrentPageID())) { // page not in memory
                if (p.getSwapInStartTime() + p.getSWAP_IN_TIME() <= this.getCurrentTime()) {
                    p.setState(Process.State.READY);
                    addPage(p.getProcessID(), p.getCurrentPageID());
                    System.out.println(p.getProcessID() + ": READY (time=" + getCurrentTime() + ")");
                    this.readyProcesses.add(p); // add process to ready queue
                    i.remove(); // remove it from blocked queue
                }
            }
        }
    }

    private void addPage(int processID, int pageID) {
        if(mainMemory[processID-1] == null) {
            mainMemory[processID-1][0] = new Page(pageID);
            return;
        }
        for (int i = 0; i < mainMemory[processID-1].length; i++) {
            if (mainMemory[processID-1][i] == null) {
                mainMemory[processID-1][i] = new Page(pageID);
                break;
            }
        }
    }

    private void removePage(int processID) {
        int lastUsed = 100000;
        int lastUsedIndex = 0;
        for (int i = 0; i < mainMemory[processID-1].length; i++) {
            if (mainMemory[processID-1][i] != null) {
                if (mainMemory[processID-1][i].getLastAccessTime() < lastUsed) {
                    lastUsed = mainMemory[processID-1][i].getLastAccessTime();
                    lastUsedIndex = i;
                }
            }
        }
        mainMemory[processID-1][lastUsedIndex] = null; // remove the page
    }

    private int getNumOfPagesInMemory(int processID) {
        if (mainMemory[processID-1] == null) {
            return 0;
        }
        int pages = 0;
        for (int i = 0; i < mainMemory[processID-1].length; i++) {
            if (mainMemory[processID-1][i] != null) {
                pages++;
            }
        }
        return pages;
    }

    private boolean isPageInMemory(int processID, int pageID) {
        if (mainMemory[processID-1] == null) {
            return false;
        }
        for (int i = 0; i < mainMemory[processID-1].length; i++) {
            if (mainMemory[processID-1][i] != null) {
                if (mainMemory[processID-1][i].getPageID() == pageID) {
                    return true;
                }
            }
        }
        return false;
    }



    private void initializeMemory() {
        this.mainMemory = new Page[this.readyProcesses.size()][this.maxFramesPerProcess];
        //Arrays.fill(mainMemory, null);
    }

}
