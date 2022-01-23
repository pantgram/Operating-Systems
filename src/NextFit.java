import java.util.ArrayList;

public class NextFit extends MemoryAllocationAlgorithm {

    private int lastAddress = 0;
    private int lastBlock = 0;
    private boolean hasRestarted = false;


    public NextFit(int[] availableBlockSizes) {
        super(availableBlockSizes);
    }

    public int fitProcess(Process p, ArrayList<MemorySlot> currentlyUsedMemorySlots) {
        boolean fit = false;
        int address = -1;

        /* Length of availableBlockSizes array */
        int blockLength = this.availableBlockSizes.length;

        /* Variables that represent the beginning and the ending of a memory block */
        int blockStart = blockStartSum(this.lastBlock);
        int blockEnd = blockEndSum(this.lastBlock);

        /* The required memory to store the Process */
        int memorySize = p.getMemoryRequirements();

        /* Iterating through availableBlockSizes array to find a suitable memory block */
        for (int i = lastBlock; i < blockLength; i++) {

            /* Checking if the memory block is big enough to store the Process "p" */
            if (this.availableBlockSizes[i] >= memorySize) {

                address = blockFit(blockStart, blockEnd, currentlyUsedMemorySlots, memorySize);

                /* All the memory has been searched for a suitable memory address but there isn't enough space for the
                 Process right now */
                if (address == -2) {
                    this.hasRestarted = false;
                    return -1;
                }

                /* A suitable memory address is found */
                if (address != -1) {
                    this.lastBlock = i;
                    this.lastAddress = address + p.getMemoryRequirements();
                    fit = true;
                    break;
                }

            }
            if (i + 1 < blockLength) {
                blockStart = blockEnd;
                blockEnd += this.availableBlockSizes[i + 1];
            }
            if (blockLength - 1 == i && !this.hasRestarted) {
                i = -1;
                blockStart = 0;
                blockEnd = this.availableBlockSizes[0];
                this.hasRestarted = true;
            }
        }
        this.hasRestarted = false;
        return address;
    }

    private int blockStartSum(int blockNum) {
        int sum = 0;
        for (int i = 0; i < blockNum; i++) {
            sum += this.availableBlockSizes[i];
        }
        return sum;
    }

    private int blockEndSum(int blockNum) {
        int sum = 0;
        for (int i = 0; i <= blockNum; i++) {
            sum += this.availableBlockSizes[i];
        }
        return sum;
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
        if (blockStart < this.lastAddress && !this.hasRestarted) {
            storingAddress = this.lastAddress;
        }
        ArrayList<MemorySlot> thisBlockArray = new ArrayList<>();

        /* Iterating through "currentlyUsedMemorySlots" ArrayList */
        for (MemorySlot slot : currentlyUsedMemorySlots) {

            /* If the "slot" object refers to the current memory block
               we store the biggest, used, memory address of the block inside "storingAddress" */
            if ((slot.getBlockStart() == blockStart) && (slot.getBlockEnd() == blockEnd)) {
                thisBlockArray.add(slot);
            }
        }

        /* Checks if the thisBlockArray isn't empty */
        if (!thisBlockArray.isEmpty()) {

            /* Iterate all the values in thisBlockArray array */
            for (int i = 0; i < thisBlockArray.size(); i++) {

                /* Getting the next MemorySlot in this block */
                MemorySlot nextSlot = findNextSlot(thisBlockArray, storingAddress);

                /* If there are no MemoryBlocks the loop ends */
                if (nextSlot == null) {
                    break;
                }

                /* If we have started looping through the memory again and the point from which the previous loop
                 had begun is passed or reached the loop ends */
                if (nextSlot.getStart() >= this.lastAddress && this.hasRestarted) {
                    return -2;
                }

                if ((nextSlot.getStart() - storingAddress) >= memorySize) {
                    return storingAddress;
                } else {
                    storingAddress = nextSlot.getEnd();
                }
            }

        } else {
            return storingAddress;
        }


        //Returns the memory address to store the Process if the memory block has enough space.
        //If not returns the value -1
        if ((blockEnd - storingAddress) >= memorySize) {
            return storingAddress;
        } else {
            return -1;
        }

    }

    /**
     * Finds and returns the MemorySlot object that is stored the closest after the minStart memory address.
     * If no MemorySlot object such as this exists the null value is returned
     *
     * @param thisBlockSlots ArrayList with all the Memory slots in a specific memory block
     * @param minStart       Memory address after which a suitable memory address must be found
     * @return MemorySlot object
     */
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
