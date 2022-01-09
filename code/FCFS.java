package code;

public class FCFS extends Scheduler {
    private Process currentProcess;

    public FCFS() {
        this.currentProcess = null;
    }

    public void addProcess(Process p) {
        //if processes stase is ready or running,add process to the list
        if ((p.getPCB().getState() == ProcessState.READY) || (p.getPCB().getState() == ProcessState.RUNNING)) {
            processes.add(p);
        }
    }

    public Process getNextProcess() {
        if (currentProcess == null) {
            currentProcess = processes.get(0);
        } else {
            //index of the current process
            int index = processes.indexOf(currentProcess);
            Process previousProcess = currentProcess;
            //TODO: previousProcess.reduceBurstTime - if needed

            //new current process - the process to be returned
            if (processes.get(index+1) != null) {
                currentProcess = processes.get(index + 1);

                //remove previous process from the start of line
                processes.remove(previousProcess);
            }
        }
        System.out.println(currentProcess.getPCB().getPid());
        return currentProcess;
    }

    public static void main(String[] args) {
        Scheduler scheduler = new FCFS();
        Process p1 = new Process(0, 5, 10);
        p1.getPCB().setState(ProcessState.READY, 0);
        Process p2 = new Process(2, 2, 40);
        p2.getPCB().setState(ProcessState.READY, 6);
        Process p3 = new Process(5, 4, 30);
        p3.getPCB().setState(ProcessState.READY, 8);
        scheduler.addProcess(p1);
        scheduler.addProcess(p2);
        scheduler.addProcess(p3);
        scheduler.getNextProcess();
        scheduler.getNextProcess();
        scheduler.getNextProcess();
    }
}

