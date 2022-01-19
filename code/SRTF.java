package code;

public class SRTF extends Scheduler {

    private Process currentProcess;
    private Process previousProcess;
    private int index;

    public SRTF() {
        this.currentProcess = null;
        this.previousProcess = null;
        this.index=0;
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
            index++;
        }
        else if(previousProcess == null){  //second time running
            previousProcess=currentProcess;
            currentProcess = processes.get(index);
            index++;
            if(currentProcess.getBurstTime()<=previousProcess.getBurstTime()) {
                currentProcess.run();
                currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
            }
        }
        else if(currentProcess.getBurstTime()==0){// if the process ended
            previousProcess = currentProcess;
            removeProcess(currentProcess);
            currentProcess = nextProcessExists();
        }
        else if(currentProcess.getBurstTime()<=previousProcess.getBurstTime()){// checking if remaining time of curr process is less than prev
            currentProcess.run();
            currentProcess.setBurstTime(currentProcess.getBurstTime()-1);
            previousProcess=currentProcess;
            currentProcess = nextProcessExists();
            index++;
        }
        else{
            currentProcess.run();
            currentProcess.setBurstTime(currentProcess.getBurstTime()-1);
        }
        if (currentProcess != null) {
            currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
            System.out.println("RUNNING " + currentProcess.getPCB().getPid() + " IN " + CPU.clock);
        }
        else{System.out.println("No process to be executed");}
        return currentProcess;
    }

    private Process nextProcessExists() {
        if (index <= processes.size() - 1) { //if next element of arraylist exists
            currentProcess = processes.get(index);
            if(previousProcess != currentProcess && previousProcess!=null)
                currentProcess.run();
            return processes.get(index);
        } else if (processes.size() > 0) { //get it from the start
            index = 0;
            currentProcess = processes.get(0);
            if(previousProcess != currentProcess && previousProcess!=null)
                currentProcess.run();
            return processes.get(0);
        }
        return null; //no more processes to be executed
    }


    public static void main(String[] args) {
        final Process[] processes = {
                // Process parameters are: arrivalTime, burstTime, memoryRequirements (kB)
                new Process(0, 7, 10),
                new Process(1, 3, 40),
                new Process(3, 4, 25),
                new Process(6, 2, 30)
        };
        Scheduler SRTFTEST = new SRTF();
        processes[0].getPCB().setState(ProcessState.READY, 0);
        processes[1].getPCB().setState(ProcessState.READY, 0);
        processes[2].getPCB().setState(ProcessState.READY, 0);
        processes[3].getPCB().setState(ProcessState.READY, 0);
        SRTFTEST.addProcess(processes[0]);
        SRTFTEST.addProcess(processes[1]);
        SRTFTEST.addProcess(processes[2]);
        SRTFTEST.addProcess(processes[3]);
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
