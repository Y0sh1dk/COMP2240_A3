import java.util.ArrayList;
import java.util.Iterator;

public class Process implements Cloneable {
    enum State {
        BLOCKED,
        READY,
        FINISHED
    }

    private final int MAX_PAGES = 50;
    private final int SWAP_IN_TIME = 6;

    private int processID;
    private int maxFrames;
    private String processName;
    private int arriveTime;
    private int finishTime;

    private int currentRequest;
    private boolean isReady;

    private int turnaroundTime; // arrive time - finish time
    private ArrayList<Page> pageRequests;
    private ArrayList<Page> pagesInVM; // Pages in virtual memory
    private ArrayList<Page> pagesInMM; // Pages in main memory
    private ArrayList<Integer> faults; // page faults that have occurred with this process

    private int swapInStartTime;

    private State state;

    Process() {
        this.pageRequests = new ArrayList<>();
        this.pagesInVM = new ArrayList<>();
        this.pagesInMM = new ArrayList<>();
        this.faults = new ArrayList<>();
        this.finishTime = 0;
        this.arriveTime = 0; // all arrive at same time
        this.currentRequest = 0;
        this.isReady = true;
        this.state = State.READY;
    }

    Process(int pID, String pName, ArrayList<Integer> pRequests) {
        this();
        this.processID = pID;
        this.processName = pName;
        initializePages(pRequests);
    }

    public void run(int time) { // try to run the process TODO: make this return the state of the process

    }

    public void setState(Process.State s) {
        this.state = s;
    }

    public int numOfRequests() {
        return this.pageRequests.size();
    }

    public boolean isRequestInMM() { // is request in Main Memory
        if(doesContainPage(this.pagesInMM, this.pageRequests.get(this.currentRequest).getPageID())) {
            return true;
        } else {
            return false;
        }
    }

    public int getSwapInStartTime() {
        return this.swapInStartTime;
    }

    public int getSWAP_IN_TIME() {
        return this.SWAP_IN_TIME;
    }

    public void swapInPageToMM(int currentTime) {
        this.swapInStartTime = currentTime;
        this.state = State.BLOCKED;
    }


    public void swapCurrentRequestToMM() {
        int currentID = this.pageRequests.get(this.currentRequest).getPageID();
        if (doesContainPage(this.pagesInVM, currentID)) { // if the page exists in virtual memory
            for (Iterator<Page> i = pagesInVM.iterator(); i.hasNext();) { // find page in VM
                Page p = i.next();
                if (p.getPageID() == currentID) { // when find page, add it to MM and delete from VM
                    this.pagesInMM.add(p);
                    i.remove();
                }
            }
        }
    }

    private void initializePages(ArrayList<Integer> pRequests) {
        for (int i : pRequests) {
            Page temp = new Page(i);
            this.pageRequests.add(temp);
            if (!doesContainPage(pagesInVM, i)) {
                pagesInVM.add(temp);
            }
        }
    }

    private boolean doesContainPage(ArrayList<Page> list, int pageNo) {
        for (Page p : list) {
            if (p.getPageID() == pageNo) {
                return true;
            }
        }
        return false;
    }


    public int getProcessID() {
        return processID;
    }

    public void setProcessID(int processID) {
        this.processID = processID;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public int getMaxFrames() {
        return maxFrames;
    }

    public void setMaxFrames(int maxFrames) {
        this.maxFrames = maxFrames;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public int getCurrentRequest() {
        return currentRequest;
    }

    public void setCurrentRequest(int r) {
        this.currentRequest = r;
    }

    public void incrementCurrentRequest(int r) {
        this.currentRequest += r;
    }

    public void faultOccured(int time) {
        this.faults.add(time);
    }

    public State getState() {
        return this.state;
    }

    //
    //@Override
    //public Object clone() throws CloneNotSupportedException {
    //    Process cloned =  (Process)super.clone();
    //
    //    return cloned;
    //}
    //

}
