package ud2.exercises.ex4;

public class CounterThread extends Thread {
    private final int inici;
    private final int delay;
    private int comptador;

    public CounterThread(int id, int inici, int delay) {
        this.inici = inici;
        this.delay = delay;
        this.comptador = inici;
        setName(String.format("Fil%d", id));
    }

    public int getComptador(){
        return comptador;
    }

    @Override
    public void run(){
        try {
            while (true){
                Thread.sleep(delay);
                comptador++;
                System.out.printf("%s: %d\n", getName(), comptador);
            }
        } catch (InterruptedException e) {
            System.out.printf("%s interromput.\n", getName());
        }
    }
}