import java.util.ArrayList;

public class CPU {

    public static int clock = 0; // this should be incremented on every CPU cycle

    private final ArrayList<Process> terminatedProcesses = new ArrayList<>();
    private Scheduler scheduler;
    private MMU mmu;
    private Process[] processes;
    private int currentProcess;

    public CPU(Scheduler scheduler, MMU mmu, Process[] processes) {
        this.scheduler = scheduler;
        this.mmu = mmu;
        this.processes = processes;
    }

    public void run() {
        while (checkToStop()) {
            tick();
            clock++;
        }
    }

    public void tick() {
        /* Checking if a Terminated Process needs to be removed from RAM */
        removeTerminatedProcesses();

        // If the SRTF algorithm is being used or no Processes have been added to the processes ArrayList
        // processToLoad is called. If a process is stored in RAM the current CPU cycle is stopped
        if (isSRTF() || scheduler.processes.size() == 0) {
            if (processToLoad())
                return;
        }

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

    private void removeTerminatedProcess(Process terminatedProcess) {
        for (MemorySlot slot : this.mmu.getCurrentlyUsedMemorySlots()) {
            if (slot.getProcess().equals(terminatedProcess)) {
                this.mmu.getCurrentlyUsedMemorySlots().remove(slot);
                this.terminatedProcesses.add(terminatedProcess);
                break;
            }
        }
    }

    private boolean checkToStop() {
        for (Process process : this.processes) {
            if (!process.getPCB().getState().equals(ProcessState.TERMINATED))
                return true;
        }
        return false;
    }

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