public class Page {
    private int pageID;
    private int useBit; // needed for clock policy
    private int lastAccessTime;

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
