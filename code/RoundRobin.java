package code;

public class RoundRobin extends Scheduler {

    private int quantum;
    private Process currentProcess;
    private Process previousProcess;
    private int pointer;
    private int reachQuantum; //pointer to check if we reached the quantum limit

    public RoundRobin() {
        this.quantum = 1; // default quantum
        currentProcess = null;
        previousProcess = null;
        pointer = -1;
        reachQuantum = 0;
    }

    public RoundRobin(int quantum) {
        this();
        this.quantum = quantum;
    }

    public void addProcess(Process p) {
        if (p.getPCB().getState() == ProcessState.READY || p.getPCB().getState() == ProcessState.RUNNING) {
            processes.add(p);
        }
    }

    public Process getNextProcess() {
        //check if it's the first time using this method - initialize currentProcess + pointer
        if (currentProcess == null) {
            currentProcess = processes.get(0);
            currentProcess.run();
            pointer = 0;
        }

        //if the burst time of current process is 0, it is removed
        if (currentProcess.getBurstTime() == 0) {
            previousProcess = currentProcess;
            removeProcess(currentProcess);
            //get next process - if it exists
            currentProcess = nextProcessExists();
        }

        //if it reaches the quantum limit, get the next process - if it exists
        if (reachQuantum == quantum) {
            reachQuantum = 0;
            pointer++;
            previousProcess = currentProcess;
            if (currentProcess != null)
                currentProcess.waitInBackground();
            currentProcess = nextProcessExists();
        }

        reachQuantum++;
        if (currentProcess != null) {
            currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
            System.out.println("RUNNING " + currentProcess.getPCB().getPid() + " IN " + CPU.clock);
        }

        if (currentProcess == null) System.out.println("No process to be executed.");

        return currentProcess;
    }

    private Process nextProcessExists() {
        if (pointer <= processes.size() - 1) { //if next element of arraylist exists
            reachQuantum = 0;
            currentProcess = processes.get(pointer);
            if(previousProcess != currentProcess && previousProcess!=null)
                currentProcess.run();
            return processes.get(pointer);
        } else if (processes.size() > 0) { //get it from the start
            reachQuantum = 0;
            pointer = 0;
            currentProcess = processes.get(0);
            if(previousProcess != currentProcess && previousProcess!=null)
                currentProcess.run();
            return processes.get(0);
        }
        return null; //no more processes to be executed
    }
}
