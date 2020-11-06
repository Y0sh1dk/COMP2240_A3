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

    Page() {
        this.lastAccessTime = 0;
        this.useBit = 0;
    }

    Page(int pID) {
        this();
        this.pageID = pID;
    }


    public int getPageID() {
        return pageID;
    }

    public void setPageID(int pageID) {
        this.pageID = pageID;
    }

    public int getUseBit() {
        return useBit;
    }

    public void setUseBit(int useBit) {
        this.useBit = useBit;
    }

    public int getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(int lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }
}
