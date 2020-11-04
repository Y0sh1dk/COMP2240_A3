
public class Fault {
    private int time;
    private int processID;

    Fault() {

    }

    Fault(int t, int id) {
        this();
        this.time = t;
        this.processID = id;
    }


    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getProcessID() {
        return processID;
    }

    public void setProcessID(int processID) {
        this.processID = processID;
    }
}
