package code;

public class SRTF extends Scheduler {

    private Process currentProcess;

    public SRTF() {
        this.currentProcess = null;
    }

    public void addProcess(Process p) {
        if ((p.getPCB().getState() == ProcessState.READY) || (p.getPCB().getState() == ProcessState.RUNNING)) {
            processes.add(p);
        }
    }

    public Process getNextProcess() {
        if (currentProcess == null || !processes.contains(currentProcess)) { //check if currentProcess is null or if is not in arraylist processes
            currentProcess = processes.get(0);
            currentProcess.run();
            currentProcess.setBurstTime(currentProcess.getBurstTime()-1);
            System.out.println("RUNNING " + currentProcess.getPCB().getPid() + " IN " + CPU.clock);
        }
        else if(currentProcess.getBurstTime() !=0) {
            Process minProcess = minBurstTime();
            if(currentProcess == minProcess){
                currentProcess.setBurstTime(currentProcess.getBurstTime()-1);
                System.out.println("RUNNING " + currentProcess.getPCB().getPid() + " IN " + CPU.clock);
            }
            else{
                currentProcess = minProcess;
                currentProcess.run();
                currentProcess.setBurstTime(currentProcess.getBurstTime()-1);
                System.out.println("RUNNING " + currentProcess.getPCB().getPid() + " IN " + CPU.clock);
            }
        }
        else{
          removeProcess(currentProcess);
          if(processes.size()>0) {
              currentProcess = minBurstTime();
              currentProcess.run();
              currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
              System.out.println("RUNNING " + currentProcess.getPCB().getPid() + " IN " + CPU.clock);
          }else{System.out.println("No processes to be executed");}
        }
        return currentProcess;
    }


    private Process minBurstTime(){ //Process with min burst time
       int min = Integer.MAX_VALUE;
       Process minProcess = processes.get(0);
       for(Process process : processes){
           if(process.getBurstTime()<=min)
           {
               min=process.getBurstTime();
               minProcess = process;
           }
       }
       return minProcess;
    }

    public static void main(String[] args) {
        final Process[] processes = {
                // Process parameters are: arrivalTime, burstTime, memoryRequirements (kB)
                new Process(0, 7, 10),
                new Process(1, 3, 40),
                new Process(3, 4, 25),
        };
        Scheduler SRTFTEST = new SRTF();
        processes[0].getPCB().setState(ProcessState.READY, 0);
        processes[1].getPCB().setState(ProcessState.READY, 0);
        processes[2].getPCB().setState(ProcessState.READY, 0);
        SRTFTEST.addProcess(processes[0]);
        SRTFTEST.addProcess(processes[1]);
        SRTFTEST.addProcess(processes[2]);
        SRTFTEST.getNextProcess();
        SRTFTEST.getNextProcess();
        SRTFTEST.getNextProcess();
        SRTFTEST.getNextProcess();
        SRTFTEST.getNextProcess();
        SRTFTEST.getNextProcess();
        SRTFTEST.getNextProcess();
        SRTFTEST.getNextProcess();
        SRTFTEST.getNextProcess();
        SRTFTEST.getNextProcess();
        SRTFTEST.getNextProcess();
        SRTFTEST.getNextProcess();
        SRTFTEST.getNextProcess();


    }
}
