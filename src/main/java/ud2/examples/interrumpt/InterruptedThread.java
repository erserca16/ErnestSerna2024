package ud2.examples.interrumpt;

class InterruptedThread extends Thread {
    @Override
    public void run(){
        try {
            for(int i = 0; i < 5; i++) {
                System.out.printf("El fil %s et saluda per %d vegada.\n",
                        Thread.currentThread().getName(), i
                );
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() +" interromput.");
        }
    }
}