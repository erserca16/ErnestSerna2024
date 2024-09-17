package ud2.exercises.ex1;

public class SleepThread extends Thread{
    private final int n;

    public SleepThread(int n){
        this.n = n;
        setName(String.format("Fil%d", n));
    }

    @Override
    public void run(){
        try {
            System.out.printf("%s: Comença execució.\n", getName());
            Thread.sleep(n * 1000L);
            System.out.printf("%s: Acaba execució.\n", getName());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
