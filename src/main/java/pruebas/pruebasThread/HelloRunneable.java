package pruebas.pruebasThread;

import java.util.concurrent.ThreadLocalRandom;

class HelloRunnable implements Runnable {
    public void run(){
        for(int i = 0; i < 5; i++) {
            int sleepTime = ThreadLocalRandom.current().nextInt(500, 1000);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.printf("El fil %s et saluda per %d vegada.\n",
                    Thread.currentThread().getName(), i
            );
        }

    }
}
