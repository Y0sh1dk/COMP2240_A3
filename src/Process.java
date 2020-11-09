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

public class Process implements Cloneable {
    enum State {
        BLOCKED,
        READY,
        FINISHED
    }

    private final int MAX_PAGES = 50;
    private final int SWAP_IN_TIME = 6;     // Time it takes to swap in a page to MM from VM

    private int processID;                  // ID of the process
    private String processName;             // Name of the process
    private int arriveTime;                 // Arrival time of the process
    private int finishTime;                 // Finish time of the process
    private int currentRequest;             // counter representing the current page request
    private ArrayList<Integer> pageRequests;   // list of all requests


    private ArrayList<Integer> faults;      // page faults that have occurred with this process
    private int swapInStartTime;            // start time of the last occurred swap
    private State state;                    // current state of the process

    Process() {
        this.pageRequests = new ArrayList<>();  // Initialize arrayList
        this.faults = new ArrayList<>();        // Initialize arrayList
        this.finishTime = 0;                    // Initialize finish time
        this.arriveTime = 0;                    // all arrive at same time
        this.finishTime = 0;                    // Initialize finish time
        this.currentRequest = 0;                // Initialize current request counter
        this.state = State.READY;               // Process starts in ready state
    }

    Process(int pID, String pName, ArrayList<Integer> pRequests) {
        this();
        this.processID = pID;           // set process ID
        this.processName = pName;       // set process name
        this.pageRequests = pRequests;
    }

    public void run(int time) { // try to run the process TODO: make this return the state of the process

        this.currentRequest += 1;
    }

    public void setState(Process.State s) {
        this.state = s;
    }

    public int numOfRequests() {
        return this.pageRequests.size();
    }

    public int getSwapInStartTime() {
        return this.swapInStartTime;
    }

    public void setSwapInStartTime(int t) {
        this.swapInStartTime = t;
    }

    public int getSWAP_IN_TIME() {
        return this.SWAP_IN_TIME;
    }

    public void swapInPageToMM(int currentTime) {
        this.swapInStartTime = currentTime;
        this.state = State.BLOCKED;
    }


    public int getProcessID() {
        return processID;
    }

    public String getProcessName() {
        return processName;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public int getCurrentRequest() {
        return currentRequest;
    }

    public int getCurrentPageID() {
        return this.pageRequests.get(this.currentRequest);
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
