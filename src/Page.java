public class Page {
    private int pageID;
    private boolean useBit; // needed for clock policy

    Page() {
        useBit = false;
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

    public boolean isUseBit() {
        return useBit;
    }

    public void setUseBit(boolean useBit) {
        this.useBit = useBit;
    }
}
