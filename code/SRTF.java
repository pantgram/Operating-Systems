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
            if (currentProcess != minProcess) {
                currentProcess = minProcess;
                currentProcess.run();
            }
            currentProcess.setBurstTime(currentProcess.getBurstTime()-1);
            System.out.println("RUNNING " + currentProcess.getPCB().getPid() + " IN " + CPU.clock);
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

}
