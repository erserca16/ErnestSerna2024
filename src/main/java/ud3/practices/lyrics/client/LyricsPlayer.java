package ud3.practices.lyrics.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LyricsPlayer {
    Player player;
    Loader loader;
    List<String> lines;
    boolean end;

    public LyricsPlayer(String host, int port) throws IOException {
        player = new Player(this);
        loader = new Loader(this, host, port);
        lines = new ArrayList<>();
        end = false;
    }

    public int linesSize(){
        return lines.size();
    }

    public synchronized void addLine(String line){
        lines.add(line);
        notify();
    }

    public void setEnd(boolean end) {
        this.end = end;
    }
    public boolean ended() {
        return end;
    }

    public boolean isLineAvailable(int i){
        return i < lines.size();
    }

    public void playLine(int i) throws InterruptedException {
        String[] line;

        synchronized (this){
            while(!isLineAvailable(i)) wait();
            line = lines.get(i).split(" ");
        }

        for (int j = 0; j < line.length; j++) {
            Thread.sleep(500);
            if(j == 0)
                System.out.printf("%d: ", i);
            else
                System.out.print(" ");
            System.out.print(line[j]);
        }
        System.out.println();
    }

    public void start(){
        player.start();
        loader.start();
    }
    public void join() throws InterruptedException {
        player.join();
        loader.join();
    }
    public void interrupt() {
        player.interrupt();
        loader.interrupt();
    }

    public static void main(String[] args) {
        try {
            LyricsPlayer lyricsPlayer = new LyricsPlayer("localhost", 1234);
            lyricsPlayer.start();
            lyricsPlayer.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println("Error connectan-se amb el servidor.");
            System.err.println(e.getMessage());
        }
    }
}