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

    /**
     * Process class base constructor
     * Initializes arrays and initial values
     */
    Process() {
        this.pageRequests = new ArrayList<>();  // Initialize arrayList
        this.faults = new ArrayList<>();        // Initialize arrayList
        this.finishTime = 0;                    // Initialize finish time
        this.arriveTime = 0;                    // all arrive at same time
        this.finishTime = 0;                    // Initialize finish time
        this.currentRequest = 0;                // Initialize current request counter
        this.state = State.READY;               // Process starts in ready state
    }

    /**
     * Process class constructor
     * @param pID the ID of the process
     * @param pName the name of the process
     * @param pRequests ArrayList containing the page requests of the process
     */
    Process(int pID, String pName, ArrayList<Integer> pRequests) {
        this();
        this.processID = pID;           // set process ID
        this.processName = pName;       // set process name
        this.pageRequests = pRequests;
    }

    /**
     * run() method
     * 'Runs' the current page loaded into Memory
     * @param time the current time of the simulation
     */
    public void run(int time) { // try to run the process TODO: make this return the state of the process

        this.currentRequest += 1;
    }

    /**
     * setState() method, sets the current state of the process
     * @param s the state to set
     */
    public void setState(Process.State s) {
        this.state = s;
    }

    /**
     * numOfRequests() method
     * @return an into containing the number of page requests
     */
    public int numOfRequests() {
        return this.pageRequests.size();
    }

    /**
     * getSwapInStartTime() method
     * @return an int containing the time the last swap in started
     */
    public int getSwapInStartTime() {
        return this.swapInStartTime;
    }

    /**
     * setSwapInStartTime() method
     * @param t int representing the time to set as the swap in start time
     */
    public void setSwapInStartTime(int t) {
        this.swapInStartTime = t;
    }

    /**
     * getSWAP_IN_TIME() method
     * @return int containing the const time it takes to swap in a page
     */
    public int getSWAP_IN_TIME() {
        return this.SWAP_IN_TIME;
    }

    /**
     * getProcessID() method
     * @return an int containing the ID of the process
     */
    public int getProcessID() {
        return processID;
    }

    /**
     * getProcessName() method
     * @return a String containing the name of the process
     */
    public String getProcessName() {
        return processName;
    }

    /**
     * setFinishTime() method
     * @param finishTime time to set as finish time of the process
     */
    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    /**
     * getCurrentRequest() method
     * @return an int containing the index of the current request
     */
    public int getCurrentRequest() {
        return currentRequest;
    }

    /**
     * getCurrentPageID() method
     * @return an int containing the ID of the current page request
     */
    public int getCurrentPageID() {
        return this.pageRequests.get(this.currentRequest);
    }

    /**
     * generateFault() method
     * @param time the time that the fault occurred
     */
    public void generateFault(int time) {
        this.faults.add(time);
    }

    /**
     * getState() method
     * @return the current state of the process
     */
    public State getState() {
        return this.state;
    }

    /**
     * getTurnAroundTime() method
     * @return an int containing the turn around time of the process,
     * calculated from the finish and arrive time.
     */
    public int getTurnAroundTime() {
        return this.finishTime - this.arriveTime;
    }

    /**
     * getNumOfFaults() method
     * @return an int containing the number of page faults that
     * have occured
     */
    public int getNumOfFaults() {
        return this.faults.size();
    }

    /**
     * getFaultString() method
     * @return a String containing the times of all the faults that ha
     */
    public String getFaultString() {
        String s = "{";
        for (int i : faults) {
            s += (i + ", ");
        }
        s  = s.substring(0, s.length() - 2) + "}";
        return s;
    }

}
