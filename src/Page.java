/**
 *  FileName: Page.java
 *  Assessment: COMP2240 - A3
 *  Author: Yosiah de Koeyer
 *  Student No: c3329520
 *
 *  Description:
 *  Page class used to represent a page stored in Main Memory or Virtual Memory
 */

public class Page {
    private int pageID;         // ID of the page
    private int useBit;         // needed for clock policy
    private int lastAccessTime; // Needed for LRU policy

    /**
     * Page class base constructor
     */
    Page() {
        this.lastAccessTime = 0;
        this.useBit = 0;
    }

    /**
     * page constructor
     * @param pID the ID to assign to the page
     */
    Page(int pID) {
        this();
        this.pageID = pID;
    }

    /**
     * getPageID() method
     * @return an int containing the ID of the page
     */
    public int getPageID() {
        return pageID;
    }

    /**
     * getPageID() method
     * @param pageID the given ID to set the page ID
     */
    public void setPageID(int pageID) {
        this.pageID = pageID;
    }

    /**
     * getUseBit() method
     * @return an int containing the use bit of the page
     */
    public int getUseBit() {
        return useBit;
    }

    /**
     * getUseBit() method
     * @param useBit int to set as the use bit of the page
     */
    public void setUseBit(int useBit) {
        this.useBit = useBit;
    }

    /**
     * getLastAccessTime() method
     * @return an int containing the last access time of the page
     */
    public int getLastAccessTime() {
        return lastAccessTime;
    }

    /**
     * setLastAccessTime() method
     * @param lastAccessTime int containg the time to set as the pages last access time
     */
    public void setLastAccessTime(int lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

}
