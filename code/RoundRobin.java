package code;

public class RoundRobin extends Scheduler {

    private int quantum;
    private Process currentProcess;
    private int pointer;
    private int reachQuantum; //pointer to check if we reached the quantum limit

    public RoundRobin() {
        this.quantum = 1; // default quantum
        currentProcess = null;
        pointer = -1;
        reachQuantum = 0;
    }

    public RoundRobin(int quantum) {
        this();
        this.quantum = quantum;
    }

    public int getQuantum() {
        return quantum;
    }

    public void addProcess(Process p) {
        if (p.getPCB().getState() == ProcessState.READY || p.getPCB().getState() == ProcessState.RUNNING) {
            processes.add(p);
        }
    }

    public Process getNextProcess() {
        //check if it's the first time using this method - initialize currentProcess + pointer
        if (currentProcess == null && processes.size() > 0) {
            currentProcess = processes.get(0);
            pointer = 0;
        } else if (currentProcess == null) {
            System.out.println("No more processes to be executed.");
            return null;
        }

        //if the burst time of current process is 0, it is removed
        if (currentProcess.getBurstTime() == 0) {
            removeProcess(currentProcess);
            //get next process - if it exists
            currentProcess = nextProcessExists();
        }

        //if it reaches the quantum limit, get the next process - if it exists
        if (reachQuantum == quantum) {
            reachQuantum = 0;
            pointer++;
            currentProcess = nextProcessExists();
        }

        reachQuantum++;
        if (currentProcess != null) currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);

        //TODO: might delete souts
        if (currentProcess != null) System.out.println("Pid: " + currentProcess.getPCB().getPid());
        if (currentProcess == null) System.out.println("No more processes to be executed.");

        return currentProcess;
    }

    private Process nextProcessExists() {
        if (pointer <= processes.size() - 1) { //if next element of arraylist exists
            reachQuantum = 0;
            return processes.get(pointer);
        } else if (processes.size() != 0) { //get it from the start
            reachQuantum = 0;
            pointer = 0;
            return processes.get(0);
        }
        return null; //no more processes to be executed
    }

    public static void main(String[] args) {
        Process p1 = new Process(0, 1, 10);
        Process p2 = new Process(2, 3, 40);
        Process p3 = new Process(3, 1, 25);
        Process p4 = new Process(4, 3, 30);
        Scheduler s = new RoundRobin(2);
        p1.getPCB().setState(ProcessState.READY, 0);
        p2.getPCB().setState(ProcessState.READY, 0);
        p3.getPCB().setState(ProcessState.READY, 0);
        p4.getPCB().setState(ProcessState.READY, 0);
        s.addProcess(p1);
        s.addProcess(p2);
        s.addProcess(p3);
        s.addProcess(p4);
        for (int i = 0; i < 10; i++) {
            s.getNextProcess();
        }
    }
}
