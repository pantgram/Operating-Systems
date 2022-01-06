package code;

public class SRTF extends Scheduler {

     private  Process currentProcess;

    public SRTF() {
        this.currentProcess = null;
    }

    public void addProcess(Process p) {
        if((p.getPCB().getState() == ProcessState.READY) || (p.getPCB().getState() == ProcessState.RUNNING)){
            processes.add(p);
        }
    }
    
    public Process getNextProcess() {
        if(currentProcess == null || !processes.contains(currentProcess)){
            currentProcess = processes.get(0);
        }
        else
        {
            int index = processes.indexOf(currentProcess);
            double min=99999;
            int new_index=0;
            for(int i=index; i<=processes.size(); i++){
              if(processes.get(i).getResponseTime()<=min){
                  min = processes.get(i).getResponseTime();
                  new_index = i;
              }
            }
            currentProcess=processes.get(new_index);
        }
        return currentProcess;
    }
}
