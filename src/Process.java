import java.util.ArrayList;

public class Process {
    private final int MAX_PAGES = 50;
    private int processID;
    private int maxFrames;
    private String processName;
    private ArrayList<Page> pageRequests;

    Process() {
        pageRequests = new ArrayList<>();
    }

    Process(int pID, String pName, ArrayList<Integer> pRequests) {
        this();
        this.processID = pID;
        this.processName = pName;

        for (int i : pRequests) {
            this.pageRequests.add(new Page(i));
        }
    }




    public int getProcessID() {
        return processID;
    }

    public void setProcessID(int processID) {
        this.processID = processID;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public int getMaxFrames() {
        return maxFrames;
    }

    public void setMaxFrames(int maxFrames) {
        this.maxFrames = maxFrames;
    }
}
