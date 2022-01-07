package code;

import java.util.ArrayList;

public class WorstFit extends MemoryAllocationAlgorithm {
    
    public WorstFit(int[] availableBlockSizes) {
        super(availableBlockSizes);
    }

    public int fitProcess(Process p, ArrayList<MemorySlot> currentlyUsedMemorySlots) {
        boolean fit = false;
        int address = -1;

        /* Length of availableBlockSizes array */
        int blockLength = this.availableBlockSizes.length;

        /* Variables that represent the beginning and the ending of a memory block */
        int blockStart = 0;
        int blockEnd = this.availableBlockSizes[0];

        /* The required memory to store the Process */
        int memorySize = p.getMemoryRequirements();

        int worstFit = -1;

        /* Iterating through availableBlockSizes array to find a suitable memory block */
        for (int i=0; i<blockLength; i++) {

            /* Checking if the memory block is big enough to store the Process "p" */
            if (this.availableBlockSizes[i] >= memorySize) {

                int tempAddress = blockFit(blockStart, blockEnd, currentlyUsedMemorySlots, memorySize);

                if ( tempAddress != -1 && (blockEnd - tempAddress) > worstFit) {
                    worstFit = blockEnd - tempAddress;
                    address = tempAddress;
                }

            }

            if (i+1 < blockLength) {
                blockStart = blockEnd;
                blockEnd += this.availableBlockSizes[i+1];
            }
        }

        return address;
    }

    /**
     * The function searches for a suitable memory address to store the process
     * within the given memory block (blockStart, blockEnd)
     *
     * @param blockStart The memory address where the block begins
     * @param blockEnd The memory address where the block ends
     * @param currentlyUsedMemorySlots An array that stores all the used memory addresses
     * @param memorySize The memory needed to store the Process
     * @return a suitable memory address (int storingAddress)
     */
    private int blockFit(int blockStart, int blockEnd, ArrayList<MemorySlot> currentlyUsedMemorySlots, int memorySize) {
        int storingAddress = blockStart;

        /* Iterating through "currentlyUsedMemorySlots" ArrayList */
        for (MemorySlot slot : currentlyUsedMemorySlots) {

            /* If the "slot" object refers to the current memory block
               we store the biggest, used, memory address of the block inside "storingAddress" */
            if ((slot.getBlockStart() == blockStart) && (slot.getBlockEnd() == blockEnd)) {

                if (slot.getEnd() > blockStart) {
                    storingAddress = slot.getEnd();
                }
            }
        }

        //Returns the memory address to store the Process if the memory block has enough space.
        //If not returns the value -1
        if ((blockEnd - storingAddress) >= memorySize) {
            return storingAddress;
        } else {
            return -1;
        }

    }

}
