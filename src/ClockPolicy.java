/**
 *  FileName: ClockPolicy.java
 *  Assessment: COMP2240 - A3
 *  Author: Yosiah de Koeyer
 *  Student No: c3329520
 *
 *  Description:
 *  Extends Policy class and implements Clock strategy
 */

import java.util.ArrayList;
import java.util.Iterator;

public class ClockPolicy extends Policy {
    ClockPolicy(int RRQuant, int maxFrames, ArrayList<Process> processes) {
        super("ClockPolicy", RRQuant, maxFrames, processes);
    }

    /**
     * run() method,
     * Overrides the abstract method in Policy class, tries to schedule the processes using the Round Robin
     * scheduling algorithm.
     * Page replacment is done using Clock policy
     */
    @Override
    void run() {
        System.out.println("Initialising " + this.getName() + " Algorithm...");

        this.initializeMemory();
        this.initializeClockPointers();

        Process runningProcess = null;
        int processStartTime = 0;
        boolean quantFinished = false;

        while (this.readyProcesses.size() + this.blockedProcesses.size() > 0) { // while not all processes have finished
            if (quantFinished) {
                try {
                    this.readyProcesses.add(this.readyProcesses.remove(0)); // move process to end of ready queue

                } catch (Exception e) {

                }
                quantFinished = false;
            }
            this.updateStates(); // update the states of all processes
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
    }

    /**
     * removePage() method
     * Removes a page from memory using the clock page replacement policy
     * @param processID the process you wish to remove a page from its memory
     */
    @Override
    protected void removePage(int processID) {
        boolean removed = false;
        while(!removed) {
            if (clockPointers[processID-1] == mainMemory[processID-1].length) {
                clockPointers[processID-1] = 0;
            }
            if (mainMemory[processID-1][clockPointers[processID-1]].getUseBit() == 1) {
                mainMemory[processID-1][clockPointers[processID-1]].setUseBit(0);
            } else { // the use bit is equal to 0
                mainMemory[processID-1][clockPointers[processID-1]] = null;
                removed = true;
            }
            clockPointers[processID-1]++;
        }
    }

    /**
     * setPageUseBit() method, sets the use bit of the given pages
     * @param processID process the page belongs too
     * @param pageID the ID of the page
     * @param useBit value to set useBit too
     */
    private void setPageUseBit(int processID, int pageID, int useBit) {
        for (int i = 0; i < mainMemory[processID-1].length; i++) {
            if (mainMemory[processID - 1][i].getPageID() == pageID) {
                mainMemory[processID - 1][i].setUseBit(useBit);
                break;
            }
        }
    }

    /**
     * UpdateStates() method
     * Loops through ready and blocked queues, updating the states of the processes based on weather a page has
     * arrived in memory yet, or if a page if the current pages request is not located in memory
     */
    @Override
    protected void updateStates() {
        for (Iterator<Process> i = readyProcesses.iterator(); i.hasNext();) {
            Process p = i.next();
            if (p.getCurrentRequest() >= p.numOfRequests()) {                   // if the process has finished
                p.setState(Process.State.FINISHED);                             // set its state
                p.setFinishTime(this.getCurrentTime());                         // set its finish time
                this.finishedProcesses.add(p);                                  // add it too the finished queue
                i.remove();                                                     // remove from the ready queue
                continue;
            }
            if (this.isPageInMemory(p.getProcessID(),p.getCurrentPageID())) {   // page is in memory
            } else { // page is not in memory
                if (this.getNumOfPagesInMemory(p.getProcessID()) == maxFramesPerProcess) { // if Memory full
                    this.removePage(p.getProcessID());
                }
                p.generateFault(this.getCurrentTime());
                p.setState(Process.State.BLOCKED);                              // set state
                p.setSwapInStartTime(this.getCurrentTime());                    // set the time the swap started
                this.blockedProcesses.add(p);                                   // add process to blocked queue
                i.remove();                                                     // remove from ready queue
            }
        }

        for (Iterator<Process> i = blockedProcesses.iterator(); i.hasNext();) {
            Process p = i.next();
            if (p.getCurrentRequest() >= p.numOfRequests()) {                       // if the process has finished
                p.setState(Process.State.FINISHED);                                 // set its state
                p.setFinishTime(this.getCurrentTime());                             // set its finish time
                this.finishedProcesses.add(p);                                      // add it too the finished queue
                i.remove();                                                         // remove from the ready queue
            } else if (!isPageInMemory(p.getProcessID(), p.getCurrentPageID())) {   // page not in memory
                if (p.getSwapInStartTime() + p.getSWAP_IN_TIME() <= this.getCurrentTime()) {
                    p.setState(Process.State.READY);
                    addPage(p.getProcessID(), p.getCurrentPageID());
                    this.readyProcesses.add(p);                                     // add process to ready queue
                    i.remove();                                                     // remove it from blocked queue
                }
            }
        }
    }

}
