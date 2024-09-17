package ud2.exercises.ex2;

public class FibonnaciThread extends Thread {
    private final int n;
    private int result;

    public FibonnaciThread(int id, int n) {
        this.n = n;
        this.result = 0;
        setName(String.format("Fil%d", id));
    }

    public int getResult() {
        return result;
    }

    @Override
    public void run(){
        int n1 = 0;
        int n2 = 1;

        System.out.printf("%s: Pas 1 de %d: %d\n", getName(), n, n1);
        if (n > 0) {
            result = n2;
            System.out.printf("%s: Pas 2 de %d: %d\n", getName(), n, n2);
        }

        for (int i = 2; i < n; i++) {
            int next = n1 + n2;
            n1 = n2;
            n2 = next;
            result = n2;
            System.out.printf("%s: Pas %d de %d: %d\n", getName(), i+1, n, n2);
        }

    }
}