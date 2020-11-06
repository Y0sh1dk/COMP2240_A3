import java.util.Iterator;

public class LRUPolicy extends Policy {
    LRUPolicy(int RRQuant) {
        super("LRUPolicy", RRQuant);
    }

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


    private void updateStates() {
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
                System.out.println(p.getProcessID() + ": BLOCKED (time=" + getCurrentTime() + ")");
                if (p.getNumOfPagesInMM() >= p.getMaxFrames()) {
                    p.removeLastUsedPageFromMM();
                }
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
