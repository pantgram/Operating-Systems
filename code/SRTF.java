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
        } else {
            int index = processes.indexOf(currentProcess);
            double min = 99999;
            int new_index = 0;
            for (int i = index; i <= processes.size()-1; i++) { //next process in the arraylist is the one with the min burst time in srtf
                if (processes.get(i).getBurstTime() <= min) {
                    min = processes.get(i).getBurstTime();
                    new_index = i;
                }
            }
            currentProcess = processes.get(new_index);
        }
        System.out.println(currentProcess);
        return currentProcess;
    }

    public static void main(String[] args) {
        final Process[] processes = {
                // Process parameters are: arrivalTime, burstTime, memoryRequirements (kB)
                new Process(0, 5, 10),
                new Process(2, 2, 40),
                new Process(3, 1, 25),
                new Process(4, 3, 30)
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
    }
}
