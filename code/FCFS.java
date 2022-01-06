package code;

public class FCFS extends Scheduler {

    private Process currentProcess;
    private int index;

    public FCFS() {
        this.currentProcess = null;
        this.index = 0;
    }

    public void addProcess(Process p) {
        //if processes stase is ready or running,add process to the list
        if ((p.getPCB().getState() == ProcessState.READY) || (p.getPCB().getState() == ProcessState.RUNNING)) {
            processes.add(p);
        }
    }

    public Process getNextProcess() {
        //if none processes executed pick the first one
        if (index == 0) {
            this.currentProcess = processes.get(index);
        } else {
            //else pick the next
            index += 1;
            this.currentProcess = processes.get(index);
        }
        return this.currentProcess;
    }
}
