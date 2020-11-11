/**
 *  FileName: Policy.java
 *  Assessment: COMP2240 - A3
 *  Author: Yosiah de Koeyer
 *  Student No: c3329520
 *
 *  Description:
 *  Abstract policy class that is extended by LRU and Clock algo classes
 */

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Policy {
    private String name;                                // Name of the policy instance
    protected ArrayList<Process> readyProcesses;        // Ready queue
    protected ArrayList<Process> blockedProcesses;      // Blocked queue
    protected ArrayList<Process> finishedProcesses;     // Finished queue
    private int currentTime;                            // current time of simulation
    protected int RRQuant;                              // the Round Robin time quant for process scheduling
    protected int maxFramesPerProcess;                  // max frames in Memory allowed per process
    protected Page[][] mainMemory;                      // 2d array of pages to represent main memory
    protected int[] clockPointers;                      // pointers to memory locations used for clock replacement


    /**
     * Policy class constructor
     * @param n name of the policy
     * @param RRQuant the time quantum used for the Round Robin scheduling algo
     */
    Policy(String n, int RRQuant, int maxFrames, ArrayList<Process> processes) {
        this.readyProcesses = new ArrayList<>();    // initialize ready queue
        this.blockedProcesses = new ArrayList<>();  // initialize blocked queue
        this.finishedProcesses = new ArrayList<>(); // initialize finished queue
        this.name = n;
        this.RRQuant = RRQuant;
        this.currentTime = 0;
        this.maxFramesPerProcess = maxFrames;
        this.addProcesses(processes);               // add all given processes to ready queue
    }

    /**
     * Abstract method run(), needs to be implemented in classes that extend policy
     */
    abstract void run();

    /**
     * Abstract removePage() method, needs to be implemented in classes that extend policy
     * @param processID
     */
    abstract void removePage(int processID);

    /**
     * Abstract updateStates() method, needs to be implemented in classes that extend policy
     */
    abstract void updateStates();

    /**
     * getCurrentTime() method
     * @return an int containing the current time
     */
    protected int getCurrentTime() {
        return currentTime;
    }

    /**
     * incCurrentTime() method
     * @param t an int number of how much to increment the current time by
     */
    protected void incCurrentTime(int t) {
        this.currentTime += t;
    }

    /**
     * addProcesses() method
     * @param processes an ArrayList of processes to add to the algo
     */
    private void addProcesses(ArrayList<Process> processes) {
        readyProcesses.addAll(processes); // add all given process to ready queue
    }

    /**
     * getName() method
     * @return a String containing the name of the policy
     */
    public String getName() {
        return name;
    }

    /**
     * getName() method, sets the name of the policy
     * @param name String containing the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * isPageInMemory() method
     * @param processID ID of process
     * @param pageID ID of page
     * @return boolean, true if page is in Memory, false if not in Memory
     */
    protected boolean isPageInMemory(int processID, int pageID) {
        if (mainMemory[processID-1] == null) {                          // if the array is empty
            return false;
        }
        for (int i = 0; i < mainMemory[processID-1].length; i++) {
            if (mainMemory[processID-1][i] != null) {                   // if there is a page there
                if (mainMemory[processID-1][i].getPageID() == pageID) { // if the ID of the page is same as given
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * getNumOfPagesInMemory() method
     * @param processID the process you want to find how many pages it has in Memory
     * @return int containing the number of pages
     */
    protected int getNumOfPagesInMemory(int processID) {
        if (mainMemory[processID-1] == null) { // if the array is empty
            return 0;
        }
        int pages = 0;
        for (int i = 0; i < mainMemory[processID-1].length; i++) {
            if (mainMemory[processID-1][i] != null) { // if there is a page there
                pages++;
            }
        }
        return pages;
    }

    /**
     * addPage() method
     * @param processID The Process that you want to add the page too
     * @param pageID the ID of the page you want to add
     */
    protected void addPage(int processID, int pageID) {
        if(mainMemory[processID-1] == null) { // if the array is empty
            mainMemory[processID-1][0] = new Page(pageID); // add the page there
            return;
        }
        for (int i = 0; i < mainMemory[processID-1].length; i++) {
            if (mainMemory[processID-1][i] == null) { // if there is no page there
                mainMemory[processID-1][i] = new Page(pageID); // add the page there
                break;
            }
        }
    }

    /**
     * setPageAccessTime() method
     * @param processID the process who's page you want to set access time of
     * @param pageID the page of the process that you want to set the access time of
     * @param time the time to set the last access time of the page
     */
    protected void setPageAccessTime(int processID, int pageID, int time) {
        for (int i = 0; i < mainMemory[processID-1].length; i++) {
            if (mainMemory[processID-1][i] != null) { // if there is a page in memory
                if (mainMemory[processID - 1][i].getPageID() == pageID) { // find the page
                    mainMemory[processID - 1][i].setLastAccessTime(time); // set its access time
                    break;
                }
            }
        }
    }

    /**
     *  initializeMemory() method, set the size of Main Memory based on given params
     */
    protected void initializeMemory() {
        this.mainMemory = new Page[this.readyProcesses.size()][this.maxFramesPerProcess];
    }

    /**
     * initializeClockPointers() method, makes an array of ints representing pointers to memory for clock policy
     */
    protected void initializeClockPointers() {
        this.clockPointers = new int[this.readyProcesses.size()];
        Arrays.fill(this.clockPointers, 0); // fill the array with 0's
    }

}
