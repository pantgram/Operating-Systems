package code;

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

    public boolean loadProcessIntoRAM(Process p) {
        boolean fit = false;
        int storingAddress = this.algorithm.fitProcess(p, this.currentlyUsedMemorySlots);

        if (storingAddress != -1) {

            fit = true;
            int storingBlock = findMemoryBlock(storingAddress);

            if (storingBlock == -1) {
                System.out.println("Memory out of bounds");
                return false;
            }
            int blockStart = findBlockStart(storingBlock);
            int blockEnd = findBlockEnd(storingBlock);
            this.currentlyUsedMemorySlots.add(new MemorySlot(storingAddress, (storingAddress + p.getMemoryRequirements()), blockStart, blockEnd));

            System.out.println("Storing Block : " + storingBlock);
            System.out.println("Memory Start : " + storingAddress);
            System.out.println("Memory End : " + (storingAddress + p.getMemoryRequirements()));
            System.out.println("Block Start : " + blockStart);
            System.out.println("Block End : " + blockEnd);
        }
        
        return fit;
    }

    private int findMemoryBlock(int storingAddress) {
        int blockStart = 0;
        int blockEnd = this.availableBlockSizes[0];
        int blockLength = this.availableBlockSizes.length;

        for (int i=0; i<blockLength; i++) {

            if ((blockStart <= storingAddress) && (blockEnd > storingAddress)) {
                return i;

            } else if (i+1 < blockLength) {
                blockStart = blockEnd;
                blockEnd = this.availableBlockSizes[i+1];
            }
        }
        return -1;
    }

    private int findBlockStart(int blockNum) {
        int blockStart = 0;
        for (int i=0; i<blockNum; i++) {
            blockStart += this.availableBlockSizes[i];
        }
        return blockStart;
    }

    private int findBlockEnd(int blockNum) {
        int blockEnd = 0;
        for (int i=0; i<=blockNum; i++) {
            blockEnd += this.availableBlockSizes[i];
        }
        return blockEnd;
    }

    public static void main(String[] args) {
        final int[] availableBlockSizes = {15, 40, 10, 20};
        MemoryAllocationAlgorithm algorithm = new FirstFit(availableBlockSizes);
        MMU mmu = new MMU(availableBlockSizes, algorithm);
        mmu.loadProcessIntoRAM(new Process(0, 5, 14));
        mmu.loadProcessIntoRAM(new Process(0, 5, 40));
    }
}
