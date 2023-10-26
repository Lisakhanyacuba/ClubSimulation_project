package clubSimulation;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Random;
import java.util.concurrent.*;

public class Barman extends Thread {

    // Attributes
    public PeopleLocation barLocation;
    private int ID;
    public GridBlock currentBlockB;
    private Random rand;
    public int movingSpeed;
    public static ClubGrid grid;
    public int count;
    public static AtomicBoolean served = new AtomicBoolean(false);

    // Constructor
    Barman(int ID, PeopleLocation loc, int speed) {
        this.ID = ID;
        this.movingSpeed = speed; // range of speeds for customers
        this.barLocation = loc; // for easy lookups
        count = 0;
    }

    // Private Methods
    private int getY() {
        return currentBlockB.getY();
    }

    private int getX() {
        return currentBlockB.getX();
    }

    // Move to Patron Method
    private synchronized void moveToPatron(int x) throws InterruptedException {
        int x_mv = Integer.signum(x - currentBlockB.getX());
        currentBlockB = grid.move(currentBlockB, x_mv, barLocation);
        System.out.println("Thread " + this.ID + " moved to position: " + currentBlockB.getX() + " " + currentBlockB.getY());
        sleep(movingSpeed / 10);
    }

    // Enter Club Method
    public void inClub() throws InterruptedException {
        currentBlockB = grid.BarmanLocation(barLocation); // enter through entrance
        System.out.println("Thread " + this.ID + " entered club at position: " + currentBlockB.getX() + " " +
                currentBlockB.getY());
        sleep(movingSpeed / 2); // wait a bit at door
    }

    // Run Method
    @Override
    public void run() {
        try {
            barLocation.setArrived();
            inClub();

            while (true) {
                System.out.print("");
                if (Clubgoer.positions.size() != 0) {
                     count =Math.abs((Clubgoer.positions.get(0).getX())-currentBlockB.getX());
                    while (count > 0) {
                        moveToPatron(Clubgoer.positions.get(0).getX());
                        count--;
                        }
                     serveDrinks(Clubgoer.positions.get(0));
                    System.out.println("Drink is served to patron at position " + Clubgoer.positions.get(0).getX() +
                            " " + Clubgoer.positions.get(0).getY());
                    Clubgoer.positions.remove(0);
                }

            }

        } catch (InterruptedException e1) {

        }
    }

    // Serve Drinks Method
    private void serveDrinks(GridBlock c) throws InterruptedException {
        served.set(true);
        synchronized(c)
        {
         c.notify();
        }
      sleep(movingSpeed);
    }
}
