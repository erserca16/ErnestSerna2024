package pruebas.pruebasThread;

import java.util.concurrent.ThreadLocalRandom;

public class StartRunnables {
    public static void main(String[] args) throws InterruptedException {
        HelloRunnable rc = new HelloRunnable();

        Thread thread1 = new Thread(rc);
        thread1.setName("Fil1");
        Thread thread2 = new Thread(rc);
        thread2.setName("Fil2");
        Thread thread3 = new Thread(rc);
        thread3.setName("Fil3");

        thread1.start();
        thread2.start();
        thread3.start();

        for(int i = 0; i < 5; i++) {
            int sleepTime = ThreadLocalRandom.current().nextInt(500, 1000);
            Thread.sleep(sleepTime);
            System.out.printf("El fil principal et saluda per %d vegada.\n", i );
        }

    }
}