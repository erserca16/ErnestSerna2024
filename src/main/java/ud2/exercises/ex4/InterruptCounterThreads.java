package ud2.exercises.ex4;

import java.time.Duration;
import java.time.LocalTime;

public class InterruptCounterThreads {
    public static void main(String[] args) {
        CounterThread t1 = new CounterThread(1, 1, 1000);
        CounterThread t2 = new CounterThread(2, 10, 100);
        CounterThread t3 = new CounterThread(3, 25, 400);
        CounterThread t4 = new CounterThread(4, 1, 1300);

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        LocalTime start = LocalTime.now();
        int comptadorFil4QuanFil1Interromput = -1;

        while (t1.isAlive() || t2.isAlive() || t3.isAlive() || t4.isAlive()){
            LocalTime now = LocalTime.now();
            if (t1.isAlive() && t1.getComptador() >= 10){
                t1.interrupt();
                comptadorFil4QuanFil1Interromput = t4.getComptador();
            }
            if (t2.isAlive() && t2.getComptador() >= 50){
                t2.interrupt();
            }
            if (t3.isAlive() && Duration.between(start, now).toSeconds() >= 3){
                t3.interrupt();
            }
            if (t4.isAlive()
                    && comptadorFil4QuanFil1Interromput != -1
                    && t4.getComptador() - comptadorFil4QuanFil1Interromput >= 10){
                t4.interrupt();
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}