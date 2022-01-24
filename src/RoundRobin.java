public class RoundRobin extends Scheduler {

    private int quantum;
    private Process currentProcess;
    private Process previousProcess;
    private int pointer; //it shows the index of process element - current process
    private int reachQuantum; //pointer to check if we reached the quantum limit
    private boolean f; //it gets true if the process reaches quantum or gets TERMINATED

    public RoundRobin() {
        this.quantum = 1; // default quantum
        currentProcess = null;
        previousProcess = null;
        pointer = -1;
        reachQuantum = 0;
        f = false;
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
        //if currentProcess reached quantum or got TERMINATED, it's time to get the next process
        if (f) {
            currentProcess = nextProcessExists();
            f = false;
        }

        //Check if it's the first time using this method or if arraylist-processes
        //is empty, which results in null currentProcess from nextProcessExists().
        //Initialize currentProcess and pointer
        if (currentProcess == null) {
            currentProcess = processes.get(0);
            currentProcess.run();
            pointer = 0;
        }

        reachQuantum++;

        //reduce burstTime and print
        if (currentProcess != null) {
            currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
            System.out.println("Process " + currentProcess.getPCB().getPid() + " - running in clock cycle: " + CPU.clock);
        }

        //if the burst time of current process is 0, it is removed
        if (currentProcess.getBurstTime() == 0) {
            previousProcess = currentProcess;
            removeProcess(currentProcess);
            f = true;
            return null; //CPU will check for new processes to load
        }

        //if it reaches the quantum limit, get the next process - if it exists
        if (reachQuantum == quantum) {
            reachQuantum = 0;
            pointer++;
            previousProcess = currentProcess;
            if (currentProcess != null)
                currentProcess.waitInBackground();
            f = true;
            return null; //CPU will check for new processes to load
        }

        /* return null, because in CPU, when this method returns null,
           it is time to check if there is any process to load in memory,
           else return the current Process
         */
        return currentProcess;
    }

    /**
     * Check if there is an element after the currentProcess element - in processes arraylist.
     * If there is that element, that's the next process to execute. If not, start again
     * the execution from the first element of processes. Those happen when processes.size()>0.
     * @return next process to be executed or null (no more processes)
     */
    private Process nextProcessExists() {
        if (pointer <= processes.size() - 1) { //if next element of arraylist exists
            reachQuantum = 0;
            currentProcess = processes.get(pointer);
            if (previousProcess != currentProcess && previousProcess != null)
                currentProcess.run();
            return processes.get(pointer);
        } else if (processes.size() > 0) { //get it from the start
            reachQuantum = 0;
            pointer = 0;
            currentProcess = processes.get(0);
            if (previousProcess != currentProcess && previousProcess != null)
                currentProcess.run();
            return processes.get(0);
        }
        return null; //no more processes to be executed
    }
}
