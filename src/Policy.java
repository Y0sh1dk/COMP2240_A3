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

public abstract class Policy {
    private String name;
    protected ArrayList<Process> readyProcesses;
    protected ArrayList<Process> blockedProcesses;
    protected ArrayList<Process> finishedProcesses;
    private int currentTime;
    protected int RRQuant;
    protected int maxFramesPerProcess;
    protected Page[][] mainMemory;


    /**
     * Policy class constructor
     * @param n name of the policy
     * @param RRQuant the time quantum used for the Round Robin scheduling algo
     */
    Policy(String n, int RRQuant, int maxFrames, ArrayList<Process> processes) {
        this.readyProcesses = new ArrayList<>();
        this.blockedProcesses = new ArrayList<>();
        this.finishedProcesses = new ArrayList<>();
        this.name = n;
        this.RRQuant = RRQuant;
        this.currentTime = 0;
        this.maxFramesPerProcess = maxFrames;
        this.addProcesses(processes);
    }



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
        for (Process p : processes) {
            readyProcesses.add(p);
        }
    }

    /**
     * getName() method
     * @return a String containing the name of the policy
     */
    public String getName() {
        return name;
    }

    /**
     * getName method(), sets the name of the policy
     * @param name String containing the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Abstract method run(), needs to be implemented in classes that extend policy
     */
    abstract void run();

    protected boolean isPageInMemory(int processID, int pageID) {
        if (mainMemory[processID-1] == null) {
            return false;
        }
        for (int i = 0; i < mainMemory[processID-1].length; i++) {
            if (mainMemory[processID-1][i] != null) {
                if (mainMemory[processID-1][i].getPageID() == pageID) {
                    return true;
                }
            }
        }
        return false;
    }

    protected int getNumOfPagesInMemory(int processID) {
        if (mainMemory[processID-1] == null) {
            return 0;
        }
        int pages = 0;
        for (int i = 0; i < mainMemory[processID-1].length; i++) {
            if (mainMemory[processID-1][i] != null) {
                pages++;
            }
        }
        return pages;
    }

    abstract void removePage(int processID);


    protected void addPage(int processID, int pageID) {
        if(mainMemory[processID-1] == null) {
            mainMemory[processID-1][0] = new Page(pageID);
            return;
        }
        for (int i = 0; i < mainMemory[processID-1].length; i++) {
            if (mainMemory[processID-1][i] == null) {
                mainMemory[processID-1][i] = new Page(pageID);
                break;
            }
        }
    }

    protected void setPageAccessTime(int processID, int pageID, int time) {
        for (int i = 0; i < mainMemory[processID-1].length; i++) {
            if (mainMemory[processID-1][i] != null) {
                if (mainMemory[processID-1][i].getPageID() == pageID) {
                    mainMemory[processID-1][i].setLastAccessTime(time);
                    break;
                }
            }
        }
    }

    protected void initializeMemory() {
        this.mainMemory = new Page[this.readyProcesses.size()][this.maxFramesPerProcess];
    }

}
