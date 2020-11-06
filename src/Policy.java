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

    private boolean isClockPolicy;

    /**
     * Policy class constructor
     * @param n name of the policy
     * @param RRQuant the time quantum used for the Round Robin scheduling algo
     */
    Policy(String n, int RRQuant) {
        this.readyProcesses = new ArrayList<>();
        this.blockedProcesses = new ArrayList<>();
        this.finishedProcesses = new ArrayList<>();
        this.name = n;
        this.RRQuant = RRQuant;
        this.currentTime = 0;
        this.isClockPolicy = n.equals("ClockPolicy"); // String equals returns boolean
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
    public void addProcesses(ArrayList<Process> processes) {
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

}
