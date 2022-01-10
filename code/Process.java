package code;

public class Process {
    private ProcessControlBlock pcb;
    private int arrivalTime;
    private int burstTime;
    private int memoryRequirements;
    
    public Process(int arrivalTime, int burstTime, int memoryRequirements) {
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.memoryRequirements = memoryRequirements;
        this.pcb = new ProcessControlBlock();
    }
    
    public ProcessControlBlock getPCB() {
        return this.pcb;
    }

    public void run() {
        pcb.setState(ProcessState.RUNNING,CPU.clock);
    }
    
    public void waitInBackground() {
        /* TODO: you need to add some code here
         * Hint: this should run every time a process stops running */

        //change currentClockTime
        pcb.setState(ProcessState.READY,CPU.clock);
    }

    public double getWaitingTime() {
        int waitingTime=0;
        int end=pcb.getStartTimes().size()-1;

        //add all periods that the process is in waiting situation,
        //since the first starting time
        for(int i=1; i<=end; i++){
            int temp=pcb.getStartTimes().get(i)-pcb.getStopTimes().get(i-1);
            waitingTime+=temp;
        }

        //add response time on total
        waitingTime+=getResponseTime();

        return waitingTime;
    }
    
    public double getResponseTime() {
        int startingTime = pcb.getStartTimes().get(0);
        //first element of startTimes pcb-arraylist

        return startingTime - arrivalTime;
    }
    
    public double getTurnAroundTime() {
        int finishingTime = pcb.getStopTimes().get(pcb.getStopTimes().size()-1);
        //last element of stopTimes pcb-arraylist

        return finishingTime - arrivalTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getMemoryRequirements() {
        return this.memoryRequirements;
    }
    public int getBurstTime(){
        return this.burstTime;
    }
    public int getArrivalTime(){
        return this.arrivalTime;
    }
}
