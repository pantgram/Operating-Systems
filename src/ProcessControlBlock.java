import java.util.ArrayList;

public class ProcessControlBlock {

    private final int pid;
    private ProcessState state;
    // the following two ArrayLists should record when the process starts/stops
    // for statistical purposes
    private ArrayList<Integer> startTimes; // when the process starts running
    private ArrayList<Integer> stopTimes;  // when the process stops running

    private static int pidTotal = 0;

    public ProcessControlBlock() {
        this.state = ProcessState.NEW;
        this.startTimes = new ArrayList<Integer>();
        this.stopTimes = new ArrayList<Integer>();
        //update the number of the total processes
        pidTotal += 1;
        this.pid = pidTotal;
    }

    public ProcessState getState() {
        return this.state;
    }

    public void setState(ProcessState state, int currentClockTime) {
        //if a process starts
        if (this.state == ProcessState.READY && state == ProcessState.RUNNING)
            this.startTimes.add(currentClockTime);
            //if a process stops
        else if ((this.state == ProcessState.RUNNING && state == ProcessState.READY) || (this.state == ProcessState.RUNNING && state == ProcessState.TERMINATED))
            this.stopTimes.add(currentClockTime);
        //set state with the new one
        this.state = state;
    }

    public int getPid() {
        return this.pid;
    }

    public ArrayList<Integer> getStartTimes() {
        return startTimes;
    }

    public ArrayList<Integer> getStopTimes() {
        return stopTimes;
    }

}
