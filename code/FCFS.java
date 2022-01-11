package code;

public class FCFS extends Scheduler {

    private int flag;

    public FCFS() {
        this.flag = 0;
    }

    public void addProcess(Process p) {
        //if processes stase is ready or running,add process to the list
        if ((p.getPCB().getState() == ProcessState.READY) || (p.getPCB().getState() == ProcessState.RUNNING)) {
            processes.add(p);
        }
    }

    public Process getNextProcess() {
        //check if processes is  empty
        if (processes.size() > 0) {
            if (flag == 0) {
                flag++;
                processes.get(0).run();
            }
            //check if process is ended
            if (processes.get(0).getBurstTime() != 0) {
                processes.get(0).setBurstTime(processes.get(0).getBurstTime() - 1);
                System.out.println("RUNNING"+" "+ processes.get(0).getPCB().getPid());
                System.out.println(CPU.clock);
                return processes.get(0);

            } else {
                //process ended so we have to remove it
                removeProcess(processes.get(0));
                //if we have another process this is the next process
                if (processes.size() > 0) {
                    processes.get(0).run();
                    processes.get(0).setBurstTime(processes.get(0).getBurstTime() - 1);
                    System.out.println("RUNNING"+" "+ processes.get(0).getPCB().getPid());
                    System.out.println(CPU.clock);
                    return processes.get(0);
                }
            }
        }
        //if there are not other processes return null
        return null;
    }

    public static void main(String[] args) {
        Scheduler scheduler = new FCFS();
        Process p1 = new Process(0, 5, 10);
        p1.getPCB().setState(ProcessState.READY, 0);
        Process p2 = new Process(2, 2, 40);
        p2.getPCB().setState(ProcessState.READY, 0);
        Process p3 = new Process(5, 4, 30);
        p3.getPCB().setState(ProcessState.READY, 0);
        Process p4 = new Process(8, 7, 50);
        p4.getPCB().setState(ProcessState.READY, 0);
        scheduler.addProcess(p1);
        scheduler.addProcess(p2);
        scheduler.addProcess(p3);
        scheduler.addProcess(p4);
        for (int i = 0; i < 30; i++) {
            scheduler.getNextProcess();
            CPU.clock++;
        }
    }

}


