package ud2.practices.lyrics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LyricsPlayer {
    Player player;
    Loader loader;
    List<String> lines;
    boolean end;
    Object lock;

    public LyricsPlayer(String filename) {
        player = new Player(this);
        loader = new Loader(this, filename);
        lines = new ArrayList<>();
        end = false;
        lock = new Object();
    }

    public int addLine(String line) {
        synchronized (lock) {
            lines.add(line);
            lock.notifyAll();
            return lines.size() - 1;
        }
    }

    public void setEnd(boolean end) {
        this.end = end;
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public boolean ended() {
        return end;
    }

    public boolean isLineAvailable(int i) {
        synchronized (lock) {
            return i < lines.size();
        }
    }

    public void playLine(int i) throws InterruptedException {
        String[] line;
        synchronized (lock) {
            while (i >= lines.size() && !end) {
                lock.wait();
            }
            if (end) {
                return;
            }
            line = lines.get(i).split(" ");
        }

        for (int j = 0; j < line.length; j++) {
            Thread.sleep(500);
            if (j == 0)
                System.out.printf("%d: ", i);
            else if (j != 0)
                System.out.print(" ");
            System.out.print(line[j]);
        }
        System.out.println();
    }

    public void start() {
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
        LyricsPlayer lyricsPlayer = new LyricsPlayer("files/ud2/lyrics.txt");
        lyricsPlayer.start();

        try {
            lyricsPlayer.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

