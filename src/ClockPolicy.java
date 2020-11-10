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
import java.util.Arrays;
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

    private void setPageUseBit(int processID, int pageID, int useBit) {
        for (int i = 0; i < mainMemory[processID-1].length; i++) {
            if (mainMemory[processID - 1][i].getPageID() == pageID) {
                mainMemory[processID - 1][i].setUseBit(useBit);
                break;
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
                //setPageUseBit(p.getProcessID(), p.getCurrentPageID(), 1);
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

}
