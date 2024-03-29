import java.util.ArrayList;

public class MMU {

    private final int[] availableBlockSizes;
    private MemoryAllocationAlgorithm algorithm;
    private ArrayList<MemorySlot> currentlyUsedMemorySlots;

    public MMU(int[] availableBlockSizes, MemoryAllocationAlgorithm algorithm) {
        this.availableBlockSizes = availableBlockSizes;
        this.algorithm = algorithm;
        this.currentlyUsedMemorySlots = new ArrayList<MemorySlot>();
    }

    public ArrayList<MemorySlot> getCurrentlyUsedMemorySlots() {
        return currentlyUsedMemorySlots;
    }

    public boolean loadProcessIntoRAM(Process p) {
        boolean fit = false;
        if (memoryOverload(p)) {
            return false;
        }

      /*The memory address where the Process is going to be stored
        If storingAddress = -1 then a suitable address has not been found.*/
        int storingAddress = this.algorithm.fitProcess(p, this.currentlyUsedMemorySlots);

        if (storingAddress != -1) {
            fit = true;
            int storingBlock = findMemoryBlock(storingAddress);

            if (storingBlock == -1) {
                return false;
            }

            /* Adding the Memory slot that was used to store the Process into the currentlyUsedMemorySlots ArrayList */
            int blockStart = findBlockStart(storingBlock);
            int blockEnd = findBlockEnd(storingBlock);
            MemorySlot newSlot = new MemorySlot(storingAddress, (storingAddress + p.getMemoryRequirements()), blockStart, blockEnd);
            newSlot.setProcess(p);
            p.getPCB().setState(ProcessState.READY, CPU.clock);
            System.out.println("Process " + p.getPCB().getPid() + " - Ready in clock cycle: " + CPU.clock);
            this.currentlyUsedMemorySlots.add(newSlot);
        }
        return fit;
    }

    /**
     * If a Process is too big for all blocks in availableBlockSizes the Process is instantly TERMINATED
     * and the value true is returned.
     * If not the value false is returned
     *
     * @param process The current Process object
     * @return true if the process is too big to be stored in RAM
     */
    private boolean memoryOverload(Process process) {
        for (int availableBlockSize : this.availableBlockSizes) {
            if (process.getMemoryRequirements() <= availableBlockSize) {
                return false;
            }
        }
        process.getPCB().setState(ProcessState.TERMINATED, CPU.clock);
        return true;
    }

    /**
     * @param storingAddress The memory address where the Process is going to be stored.
     * @return The index of the memory block that the Process is going to be stored in the availableBlockSizes array.
     * If the block is not found the value -1 is returned
     */
    private int findMemoryBlock(int storingAddress) {
        int blockStart = 0;
        int blockEnd = this.availableBlockSizes[0];
        int blockLength = this.availableBlockSizes.length;

        for (int i = 0; i < blockLength; i++) {

            if ((blockStart <= storingAddress) && (blockEnd > storingAddress)) {
                return i;

            } else if (i + 1 < blockLength) {
                blockStart = blockEnd;
                blockEnd += this.availableBlockSizes[i + 1];
            }
        }
        return -1;
    }

    /**
     * @param blockNum The index of the memory block in the "availableBlockSizes" array
     * @return The memory address where the memory block starts
     */
    private int findBlockStart(int blockNum) {
        int blockStart = 0;
        for (int i = 0; i < blockNum; i++) {
            blockStart += this.availableBlockSizes[i];
        }
        return blockStart;
    }

    /**
     * @param blockNum The index of the memory block in the "availableBlockSizes" array
     * @return The memory address where the memory block ends
     */
    private int findBlockEnd(int blockNum) {
        int blockEnd = 0;
        for (int i = 0; i <= blockNum; i++) {
            blockEnd += this.availableBlockSizes[i];
        }
        return blockEnd;
    }
}
