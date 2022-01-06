package code;

public class RoundRobin extends Scheduler {

    private int quantum;
    private Process currentProcess;
    
    public RoundRobin() {
        this.quantum = 1; // default quantum
        currentProcess = null;
    }
    
    public RoundRobin(int quantum) {
        this();
        this.quantum = quantum;
    }

    public int getQuantum(){
        return quantum;
    }

    public void addProcess(Process p) {
        if(p.getPCB().getState() == ProcessState.READY || p.getPCB().getState() == ProcessState.RUNNING){
            processes.add(p);
        }
        else{
            System.out.println("The process isn't on \"READY\" or \"RUNNING\" state - can't be executed");
        }
    }
    
    public Process getNextProcess() {
        if(currentProcess == null){
            currentProcess = processes.get(0);
        }
        else {
            //index of the current process
            int index = processes.indexOf(currentProcess);
            Process previousProcess = currentProcess;
            //TODO: previousProcess.reduceBurstTime - if needed

            //new current process - the process to be returned
            currentProcess = processes.get(index + 1);

            //remove previous process from the start of line
            processes.remove(previousProcess);

            //if the -previous- process isn't TERMINATED, it gets at the end of the line
            if(previousProcess.getPCB().getState() != ProcessState.TERMINATED) {
                addProcess(previousProcess);//TODO: see what happens with quantum - clock?
            }
        }
        return currentProcess;
    }
}
