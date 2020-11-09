/**
 *  FileName: Process.java
 *  Assessment: COMP2240 - A3
 *  Author: Yosiah de Koeyer
 *  Student No: c3329520
 *
 *  Description:
 *  Process class used to represent A process trying to execute on a Processor that needs to load its code
 *  from Virtual Memory into Main Memory with a limited frames.
 */

import java.util.ArrayList;
import java.util.Iterator;

public class Process implements Cloneable {
    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    enum State {
        BLOCKED,
        READY,
        FINISHED
    }

    private final int MAX_PAGES = 50;
    private final int SWAP_IN_TIME = 6;     // Time it takes to swap in a page to MM from VM
    private boolean isFull;

    private int processID;                  // ID of the process
    private int maxFrames;                  // Maximum number of frames allocated to the process
    private String processName;             // Name of the process
    private int arriveTime;                 // Arrival time of the process
    private int finishTime;                 // Finish time of the process
    private int currentRequest;             // counter representing the current page request
    private ArrayList<Page> pageRequests;   // list of all requests
    private ArrayList<Page> pagesInVM;      // Pages in virtual memory
    private ArrayList<Page> pagesInMM;      // Pages in main memory
    private ArrayList<Integer> faults;      // page faults that have occurred with this process
    private int swapInStartTime;            // start time of the last occurred swap
    private State state;                    // current state of the process

    Process() {
        this.pageRequests = new ArrayList<>();  // Initialize arrayList
        this.pagesInVM = new ArrayList<>();     // Initialize arrayList
        this.pagesInMM = new ArrayList<>();     // Initialize arrayList
        this.faults = new ArrayList<>();        // Initialize arrayList
        this.finishTime = 0;                    // Initialize finish time
        this.arriveTime = 0;                    // all arrive at same time
        this.finishTime = 0;                    // Initialize finish time
        this.currentRequest = 0;                // Initialize current request counter
        this.state = State.READY;               // Process starts in ready state
        this.isFull = false;
    }

    Process(int pID, String pName, ArrayList<Integer> pRequests) {
        this();
        this.processID = pID;           // set process ID
        this.processName = pName;       // set process name
        initializePages(pRequests);     // creates pages in VM from given requests
    }

    private void initializePages(ArrayList<Integer> pRequests) {
        for (int i : pRequests) {
            Page temp = new Page(i);
            this.pageRequests.add(temp);
            if (!doesContainPage(pagesInVM, i)) {   // If page not in VM
                pagesInVM.add(temp);                // add it to VM
            }
        }
    }

    public void run(int time) { // try to run the process TODO: make this return the state of the process
        for (Page p : pagesInMM) {
            if (p.getPageID() == this.pageRequests.get(this.currentRequest).getPageID()) { // It should always be there
                p.setLastAccessTime(time); // TODO: test this
                p.setUseBit(1);
            }
        }
        this.currentRequest += 1;
    }

    public void setState(Process.State s) {
        this.state = s;
    }

    public int numOfRequests() {
        return this.pageRequests.size();
    }

    public boolean isRequestInMM() { // is request in Main Memory
        if (this.currentRequest < this.pageRequests.size()) { // prevent out of bounds
            if (doesContainPage(this.pagesInMM, this.pageRequests.get(this.currentRequest).getPageID())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
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

    public int getNumOfPagesInMM() {
        return pagesInMM.size();
    }

    public int getMaxFrames() {
        return  this.maxFrames;
    }

    public void swapCurrentRequestToMM() {
        int currentID = this.pageRequests.get(this.currentRequest).getPageID();     // get ID of current request
        if (doesContainPage(this.pagesInVM, currentID)) {                           // if the page exists in virtual memory
            for (Iterator<Page> i = pagesInVM.iterator(); i.hasNext();) {           // find page in VM
                Page p = i.next();
                if (p.getPageID() == currentID) {                                   // when find page, add it to MM
                    this.pagesInMM.add(p);
                    i.remove();                                                     // delete from VM
                }
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

    public void removeLastUsedPageFromMM() {
        int lastUsedPageTime = 10000; // TODO: bad dont like this
        // Have to iterate over all before able to remove
        for (Page p : pagesInMM) { // find the page last accessed
            if (p.getLastAccessTime() < lastUsedPageTime) {
                lastUsedPageTime = p.getLastAccessTime();
            }
        }
        System.out.println(pagesInMM.size());
        // find and remove it
        for (Iterator<Page> i = pagesInMM.iterator(); i.hasNext();) {
            Page p = i.next();
            if (p.getLastAccessTime() == lastUsedPageTime) {
                i.remove();
                break;
            }
        }
    }

    public void clockRemovePage() {
        boolean removed = false;
        //this.pagesInMM.add(0, this.pagesInMM.remove(this.pagesInMM.size()-1));
        while(!removed) {
            Page temp = this.pagesInMM.remove(0);
            if (temp.getUseBit() == 1) {
                temp.setUseBit(0);
                this.pagesInMM.add(temp);
            } else {
                removed = true;
            }
        }
        //boolean removed = false;
        //Page pageToRemove = null;
        //while (!removed) {
        //    for (Page p : pagesInMM) {
        //        if (p.getUseBit() == 0) {
        //            pageToRemove = p;
        //            removed = true;
        //            break;
        //        }
        //        p.setUseBit(0);
        //    }
        //}
        //this.pagesInMM.remove(pageToRemove);
    }


    public int getProcessID() {
        return processID;
    }

    public String getProcessName() {
        return processName;
    }

    public void setMaxFrames(int maxFrames) {
        this.maxFrames = maxFrames;
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


    public void generateFault(int time) {
        this.faults.add(time);
    }

    public State getState() {
        return this.state;
    }


    public int getTurnAroundTime() {
        return this.finishTime - this.arriveTime;
    }

    public int getNumOfFaults() {
        return this.faults.size();
    }

    public String getFaultString() {
        String s = "{";
        for (int i : faults) {
            s += (i + ", ");
        }
        s  = s.substring(0, s.length() - 2) + "}";
        return s;
    }

}
