package code;

public class CPU {

    public static int clock = 0; // this should be incremented on every CPU cycle

    private boolean readyRunning;
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
        while (true) {
            if (checkToStop())
                break;
            tick();
            clock++;
        }
    }

    public void tick() {
        if (processToLoad()) {
            return;
        }
        if (scheduler.processes.size() > 0) {
            scheduler.getNextProcess();
            return;
        }


    }

    private boolean checkToStop() {
        for (Process process : this.processes) {
            if (!process.getPCB().getState().equals(ProcessState.TERMINATED))
                return false;
        }
        return true;
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