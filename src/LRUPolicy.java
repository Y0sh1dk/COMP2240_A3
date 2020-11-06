/**
 *  FileName: LRUPolicy.java
 *  Assessment: COMP2240 - A3
 *  Author: Yosiah de Koeyer
 *  Student No: c3329520
 *
 *  Description:
 *  Extends Policy class and implements LRU strategy
 */

import java.util.Iterator;


public class LRUPolicy extends Policy {
    LRUPolicy(int RRQuant) {
        super("LRUPolicy", RRQuant);
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
        Process runningProcess = null;
        int processStartTime = 0;
        boolean quantFinished = false;

        updateStates();
        while (this.readyProcesses.size() + this.blockedProcesses.size() > 0) { // while not all processes have finished
            if (quantFinished) {
                try {
                    this.readyProcesses.add(this.readyProcesses.remove(0));
                    //this.updateStates();
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


    /**
     * updateStates() method
     * Is called from inside the main run() method of the policy. It updates the states of all the processes based on
     * what is avaiable in the processes Main Memory, as well as its finished page requests
     */
    private void updateStates() {
        for (Iterator<Process> i = readyProcesses.iterator(); i.hasNext();) { // iterates through all ready processes
            Process p = i.next();
            if (p.getCurrentRequest() >= p.numOfRequests()) { // if the process has finished
                System.out.println(p.getProcessID() + ": FINISHED (time=" + getCurrentTime() + ")");
                p.setState(Process.State.FINISHED); // set its state
                p.setFinishTime(this.getCurrentTime()); // set its finish time
                this.finishedProcesses.add(p); // add it too the finished queue
                i.remove(); // remove from the ready queue
            } else if (p.isRequestInMM()) { // if the current request is stored in Main Memory
                p.setState(Process.State.READY); // set state
            } else { // if request is not in Main Memory
                System.out.println(p.getProcessID() + ": BLOCKED (time=" + getCurrentTime() + ")");
                if (p.getNumOfPagesInMM() >= p.getMaxFrames()) { // If the process has run out of frames
                    p.removeLastUsedPageFromMM(); // remove last used page from MM
                }
                p.generateFault(this.getCurrentTime()); // generate a page fault
                p.swapInPageToMM(this.getCurrentTime()); // swap page from VM to MM
                p.setState(Process.State.BLOCKED); // set state
                this.blockedProcesses.add(p); // add process to blocked queue
                i.remove(); // remove from ready queue
            }
        }
        for (Iterator<Process> i = blockedProcesses.iterator(); i.hasNext();) { // iterates through all blocked processes
            Process p = i.next();
            if (p.getCurrentRequest() >= p.numOfRequests()) { // if the process has finished
                System.out.println(p.getProcessID() + ": FINISHED (time=" + getCurrentTime() + ")");
                p.setState(Process.State.FINISHED); // set its state
                p.setFinishTime(this.getCurrentTime()); // set its finish time
                this.finishedProcesses.add(p); // add it too the finished queue
                i.remove(); // remove from the ready queue
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
