import java.util.Iterator;

public class LRUPolicy extends Policy {
    LRUPolicy(int RRQuant) {
        super("LRUPolicy", RRQuant);
    }

    @Override
    void run() {
        System.out.println("Initialising " + this.getName() + " Algorithm...");
        Process runningProcess = null;
        int timeRunning = 0;
        int processStartTime = 0;
        boolean quantFinished = false;

        updateStates();

        while (this.finishedProcesses.size() < this.readyProcesses.size() + this.blockedProcesses.size()) { // while not all processes have finished
            this.updateStates();
            System.out.println("time: " + getCurrentTime());
            try {
                Thread.sleep(200);
                System.out.println(runningProcess.getProcessID());
            } catch (Exception e) {

            }
            if (quantFinished) {
                this.readyProcesses.add(this.readyProcesses.remove(0));
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

    private void updateStates() {
        for (Iterator<Process> i = readyProcesses.iterator(); i.hasNext();) {
            Process p = i.next();
        //for (Process p : readyProcesses) {
            if (p.isRequestInMM()) {
                p.setState(Process.State.READY);
            } else { // if request is not in Main Memory
                p.swapInPageToMM(this.getCurrentTime());
                p.setState(Process.State.BLOCKED);
                this.blockedProcesses.add(p);
                i.remove();
            } // If it has done all of its pages requests
            if (p.getCurrentRequest() > p.numOfRequests()) {
                p.setState(Process.State.FINISHED);
                this.finishedProcesses.add(p);
                i.remove();
            }
        }

        for (Iterator<Process> i = blockedProcesses.iterator(); i.hasNext();) {
            Process p = i.next();
            if (!p.isRequestInMM()) {
                if (p.getSwapInStartTime() + p.getSWAP_IN_TIME() <= this.getCurrentTime()) { // page ready to be swapped
                    p.swapCurrentRequestToMM();  // swap the page in!
                    p.setState(Process.State.READY);
                    this.readyProcesses.add(p); // add process to ready queue
                    i.remove(); // remove it from blocked queue
                }
            } else {
                // shouldn't get here?
            }

            if (p.getCurrentRequest() > p.numOfRequests()) {
                p.setState(Process.State.FINISHED);
                this.finishedProcesses.add(p);
                i.remove();
            }
        }


    }

}
