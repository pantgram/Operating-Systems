import java.util.ArrayList;

public class CPU {

    public static int clock = 0; // this should be incremented on every CPU cycle

    private final ArrayList<Process> terminatedProcesses = new ArrayList<>();
    private Scheduler scheduler;
    private MMU mmu;
    private Process[] processes;

    public CPU(Scheduler scheduler, MMU mmu, Process[] processes) {
        this.scheduler = scheduler;
        this.mmu = mmu;
        this.processes = processes;
    }

    /**
     * Call tick() method for each clock cycle - call checkToStop() to terminate.
     */
    public void run() {
        while (checkToStop()) {
            tick();
            clock++;
        }
    }

    /**
     * Check if there are processes to be removed. Then, decide if the current process continues to execute,
     * or get next process for execution - it depends on the scheduler.
     * Also, check if there are processes to load.
     */
    public void tick() {
        /* Checking if a Terminated Process needs to be removed from RAM */
        removeTerminatedProcesses();

        /* If the SRTF algorithm is being used or no Processes have been added to the processes ArrayList
           processToLoad is called. If a process is stored in RAM the current CPU cycle is stopped.
         */
        if (isSRTF() || scheduler.processes.size() == 0) {
            if (processToLoad())
                return;
        }

        /*
        When current process p gets null value, it is time to check if there is
        another process to load.
         */
        if (scheduler.processes.size() > 0) {
            Process p = scheduler.getNextProcess();
            if(p == null && isRR())
                clock++;
            if (p == null && !isSRTF()) {
                while (processToLoad())
                    clock++;
                clock--;
            }

        }

    }

    /**
     * @return true if the SRTF algorithm is being used
     */
    private boolean isSRTF() {
        return this.scheduler.getClass().getName().equals("SRTF");
    }

    /**
     * @return true if the Round Robin algorithm is being used
     */
    private boolean isRR(){
        return this.scheduler.getClass().getName().equals("RoundRobin");
    }

    /**
     * delete processes that are terminated and exist in RAM
     */
    private void removeTerminatedProcesses() {
        for (Process process : this.processes) {
            if (process.getPCB().getState().equals(ProcessState.TERMINATED) && processIsInRAM(process)) {
                removeTerminatedProcess(process);
            }
        }
    }

    /**
     * @param process Process object
     * @return true if the Process object is not in the terminatedProcesses ArrayList
     */
    private boolean processIsInRAM(Process process) {
        return !this.terminatedProcesses.contains(process);
    }

    /**
     * delete given process that is terminated and exists in RAM
     * @param terminatedProcess given process
     */
    private void removeTerminatedProcess(Process terminatedProcess) {
        for (MemorySlot slot : this.mmu.getCurrentlyUsedMemorySlots()) {
            if (slot.getProcess().equals(terminatedProcess)) {
                this.mmu.getCurrentlyUsedMemorySlots().remove(slot);
                this.terminatedProcesses.add(terminatedProcess);
                break;
            }
        }
    }

    /**
     * @return true if there are no terminated processes, false if there is at
     * least one terminated process
     */
    private boolean checkToStop() {
        for (Process process : this.processes) {
            if (!process.getPCB().getState().equals(ProcessState.TERMINATED))
                return true;
        }
        return false;
    }

    /**
     * Add one process, which state is NEW, to arraylist processes when it is necessary.
     * @return true when at least one process is added in arraylist processes
     */
    private boolean processToLoad() {
        for (Process process : this.processes) {
            if (process.getArrivalTime() <= CPU.clock && process.getPCB().getState().equals(ProcessState.NEW)) {
                if (this.mmu.loadProcessIntoRAM(process)) {
                    this.scheduler.addProcess(process);
                    return true;
                }
            }
        }
        return false;
    }
}