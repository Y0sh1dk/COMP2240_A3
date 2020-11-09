/**
 *  FileName: ClockPolicy.java
 *  Assessment: COMP2240 - A3
 *  Author: Yosiah de Koeyer
 *  Student No: c3329520
 *
 *  Description:
 *  Extends Policy class and implements Clock strategy
 */

import java.util.Iterator;

public class ClockPolicy extends Policy {
    ClockPolicy(int RRQuant, int maxFrames) {
        super("ClockPolicy", RRQuant, maxFrames);
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
        Process runningProcess = null;
        int processStartTime = 0;
        boolean quantFinished = false;

        //updateStates();
        while (this.readyProcesses.size() + this.blockedProcesses.size() > 0) { // while not all processes have finished
            if (quantFinished) {
                try {
                    this.readyProcesses.add(this.readyProcesses.remove(0)); // move to end of ready
                } catch (Exception e) {

                }
                quantFinished = false;
            }
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

}
