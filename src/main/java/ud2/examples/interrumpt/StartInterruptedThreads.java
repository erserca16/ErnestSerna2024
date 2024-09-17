package ud2.examples.interrumpt;

public class StartInterruptedThreads {
    public static void main(String[] args) {
        Thread.currentThread().setName("Fil principal");

        InterruptedThread thread1 = new InterruptedThread();
        thread1.setName("Fil1");
        InterruptedThread thread2 = new InterruptedThread();
        thread2.setName("Fil2");
        InterruptedThread thread3 = new InterruptedThread();
        thread3.setName("Fil3");

        thread1.start();
        thread2.start();
        thread3.start();

        try {
            Thread.sleep(500);
            thread1.interrupt();
            Thread.sleep(500);
            thread2.interrupt();
            Thread.sleep(500);
            thread3.interrupt();
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() +" interromput.");
        }
    }
}