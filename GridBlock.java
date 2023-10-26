package clubSimulation;

import java.util.concurrent.atomic.AtomicInteger;

// GridBlock class to represent a block in the club.
// only one thread at a time "owns" a GridBlock

public class GridBlock {
    // Attributes
    private int isOccupied;
    private final boolean isExit;  // is this the exit door?
    private final boolean isBar;   // is it a bar block?
    private final boolean isDance; // is it the dance area?
    private int[] coords; // the coordinate of the block.

    // Constructor for GridBlock without coordinates
    GridBlock(boolean exitBlock, boolean barBlock, boolean danceBlock) throws InterruptedException {
        isExit = exitBlock;
        isBar = barBlock;
        isDance = danceBlock;
        isOccupied = -1;
    }

    // Constructor for GridBlock with coordinates
    GridBlock(int x, int y, boolean exitBlock, boolean refreshBlock, boolean danceBlock) throws InterruptedException {
        this(exitBlock, refreshBlock, danceBlock);
        coords = new int[]{x, y};
    }

    // Getters for X and Y coordinates
    public int getX() {
        return coords[0];
    }

    public int getY() {
        return coords[1];
    }

    // Get Method: Checks if a thread can enter this block
     public synchronized boolean get(int threadID) throws InterruptedException {
        if (isOccupied == threadID) return true; // thread Already in this block
        if (isOccupied >= 0) return false; // space is occupied
        isOccupied = threadID; // set ID to thread that had block
        return true;
    }

    // Release Method: Releases the block after a thread leaves
    public synchronized void release() {
        isOccupied = -1;
    }

    // Check if Block is Occupied
    public synchronized boolean occupied() {
        if (isOccupied == -1) return false;
        return true;
    }

    // Check if the Block is an Exit
     public synchronized boolean isExit() {
        return isExit;
    }

    // Check if the Block is a Bar
     public synchronized boolean isBar() {
        return isBar;
    }

    // Check if the Block is a Dance Floor
    public synchronized boolean isDanceFloor() {
        return isDance;
    }
}
