import java.util.ArrayList;

public class FirstFit extends MemoryAllocationAlgorithm {

    public FirstFit(int[] availableBlockSizes) {
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

        /* Iterating through availableBlockSizes array to find a suitable memory block */
        for (int i = 0; i < blockLength; i++) {

            /* Checking if the memory block is big enough to store the Process "p" */
            if (this.availableBlockSizes[i] >= memorySize) {
                address = blockFit(blockStart, blockEnd, currentlyUsedMemorySlots, memorySize);

                //If the value of the variable "address" isn't -1 a suitable memory slot has been found
                if (address != -1) {
                    fit = true;
                    break;
                } else if (i + 1 < blockLength) {
                    blockStart = blockEnd;
                    blockEnd += this.availableBlockSizes[i + 1];
                }
            } else if (i + 1 < blockLength) {
                blockStart = blockEnd;
                blockEnd += this.availableBlockSizes[i + 1];
            }
        }

        return address;
    }

    /**
     * The function searches for a suitable memory address to store the process
     * within the given memory block (blockStart, blockEnd)
     *
     * @param blockStart               The memory address where the block begins
     * @param blockEnd                 The memory address where the block ends
     * @param currentlyUsedMemorySlots An array that stores all the used memory addresses
     * @param memorySize               The memory needed to store the Process
     * @return a suitable memory address (int storingAddress)
     */
    private int blockFit(int blockStart, int blockEnd, ArrayList<MemorySlot> currentlyUsedMemorySlots, int memorySize) {
        int storingAddress = blockStart;
        ArrayList<MemorySlot> thisBlockArray = new ArrayList<>();

        /* Iterating through "currentlyUsedMemorySlots" ArrayList */
        for (MemorySlot slot : currentlyUsedMemorySlots) {

            /* If the "slot" object refers to the current memory block
               we store the biggest, used, memory address of the block inside "storingAddress" */
            if ((slot.getBlockStart() == blockStart) && (slot.getBlockEnd() == blockEnd)) {
                thisBlockArray.add(slot);
            }
        }

        if (!thisBlockArray.isEmpty()) {
            for (int i = 0; i < thisBlockArray.size(); i++) {
                MemorySlot nextSlot = findNextSlot(thisBlockArray, storingAddress);
                if (nextSlot == null) {
                    break;
                }

                if ((nextSlot.getStart() - storingAddress) >= memorySize) {
                    return storingAddress;
                } else {
                    storingAddress = nextSlot.getEnd();
                }
            }

        } else {
            return blockStart;
        }


        //Returns the memory address to store the Process if the memory block has enough space.
        //If not returns the value -1
        if ((blockEnd - storingAddress) >= memorySize) {
            return storingAddress;
        } else {
            return -1;
        }

    }

    private MemorySlot findNextSlot(ArrayList<MemorySlot> thisBlockSlots, int minStart) {
        int nextSlotStart = Integer.MAX_VALUE;
        MemorySlot nextSlot = null;
        for (MemorySlot slot : thisBlockSlots) {
            if (slot.getStart() >= minStart && slot.getStart() < nextSlotStart) {
                nextSlotStart = slot.getStart();
                nextSlot = slot;
            }
        }
        return nextSlot;
    }

}
