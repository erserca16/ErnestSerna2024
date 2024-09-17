package exam1.sushi;

public class SushiRestaurant extends Thread {
    private int maxSushiPieces;
    private int currentSushiPieces;

    public SushiRestaurant(int maxSushiPieces) {
        super();
        this.maxSushiPieces = maxSushiPieces;
        this.currentSushiPieces = 0;
    }

    public synchronized void makeSushiPiece(){
        if(currentSushiPieces < maxSushiPieces)
            currentSushiPieces++;

        if(currentSushiPieces >= 5)
            notifyAll();
    }

    public synchronized void retrieveShushiDish() throws InterruptedException{
        while (currentSushiPieces < 5) wait();
        currentSushiPieces -= 5;
    }

    @Override
    public void run() {
        try {
            while (true){
                Thread.sleep(1000);
                makeSushiPiece();
                System.out.printf("Sushi restaurant (%d/%d)\n", currentSushiPieces, maxSushiPieces);
            }
        } catch (InterruptedException e) {
            System.out.println("Restaurant tancat.");
        }
    }
}
