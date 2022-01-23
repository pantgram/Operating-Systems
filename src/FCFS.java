public class FCFS extends Scheduler {

    private int flag;
    private boolean f;

    public FCFS() {
        this.flag = 0;
        this.f=false;
    }

    public void addProcess(Process p) {
        //if processes stase is ready or running,add process to the list
        if ((p.getPCB().getState() == ProcessState.READY) || (p.getPCB().getState() == ProcessState.RUNNING)) {
            processes.add(p);
        }
    }

    public Process getNextProcess() {
        //check if processes is  empty
        if (flag == 0) {
            flag++;
            processes.get(0).run();
        }
        //check if process is ended
        if (processes.get(0).getBurstTime() != 0) {
            processes.get(0).setBurstTime(processes.get(0).getBurstTime() - 1);
            System.out.println("Process " + processes.get(0).getPCB().getPid() + " - running in clock cycle: " + CPU.clock);
            return processes.get(0);

        } else {
            //process ended so we have to remove it
            removeProcess(processes.get(0));
            flag=0;
            return null;
        }
    }
}

