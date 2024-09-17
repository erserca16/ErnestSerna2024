package ud2.practices.swimmingpool;

import java.util.Random;

public class PersonThread extends Thread {
    private SwimmingPool pool;
    private Random random = new Random();
    private volatile boolean finished = false;

    public PersonThread(String name, SwimmingPool pool) {
        super(name);
        this.pool = pool;
    }

    /**
     * TODO: The persons rests between 1 and 5 seconds
     * @throws InterruptedException
     */
    public void rest() throws InterruptedException {
        int milis = new Random().nextInt(4000) + 1000;
        Thread.sleep(milis);
        System.out.printf("%s està descansant %.2f segons.%n", getName(), milis / 1000.0);
    }

    /**
     * TODO: The persons takes a shower for 2 seconds
     * - Tries to get into a shower
     * - Takes a shower
     * - Leaves the showers
     * @throws InterruptedException
     */
    public void takeShower() throws InterruptedException {
        int milis = 2000;
        System.out.printf("%s vol dutxar-se.%n", getName());
        pool.getShowerSemaphore().acquire();
        System.out.printf("%s està dutxant-se.%n", getName());
        Thread.sleep(milis);
        System.out.printf("%s ha acabat de dutxar-se.%n", getName());
        pool.getShowerSemaphore().release();
    }

    /**
     * TODO: The persons swims between 1 and 10 seconds
     * - Tries to get into the swimming pool
     * - Swims
     * - Leaves the swimming pool
     * @throws InterruptedException
     */
    public void swim() throws InterruptedException {
        int milis = new Random().nextInt(9000) + 1000;
        System.out.printf("%s vol nadar.%n", getName());
        pool.getPoolSemaphore().acquire();
        System.out.printf("%s està nadant %.2f segons.%n", getName(), milis / 1000.0);
        Thread.sleep(milis);
        System.out.printf("%s ha eixit de la piscina.%n", getName());
        pool.getPoolSemaphore().release();
    }
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                rest();
                takeShower();
                swim();
                break;
            } catch (InterruptedException e) {
                break;
            }
        }
        finished = true;
        System.out.printf("%s ha abandonat les instal·lacions.\n", getName());
    }


}






