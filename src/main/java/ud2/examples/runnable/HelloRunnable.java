package ud2.examples.runnable;

import java.util.concurrent.ThreadLocalRandom;

class HelloRunnable implements Runnable {
    public void run(){
        for(int i = 0; i < 5; i++) {
            int sleepTime = ThreadLocalRandom.current().nextInt(500, 1000);
            //Thread.sleep(sleepTime);
            System.out.printf("El fil %s et saluda per %d vegada.\n",
                    Thread.currentThread().getName(), i
            );
        }

    }
}