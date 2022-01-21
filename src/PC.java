public class PC {

    public static void main(String[] args) {
        final Process[] processes = {
                // Process parameters are: arrivalTime, burstTime, memoryRequirements (kB)
                new Process(0, 5, 10), //4
                new Process(2, 2, 40), //
                new Process(3, 1, 20), //
                new Process(4, 3, 12) //
        };
        final int[] availableBlockSizes = {15, 40, 10, 20}; // sizes in kB
        MemoryAllocationAlgorithm algorithm = new BestFit(availableBlockSizes);
        MMU mmu = new MMU(availableBlockSizes, algorithm);        
        Scheduler scheduler = new RoundRobin();
        CPU cpu = new CPU(scheduler, mmu, processes);
        cpu.run();
    }

}
