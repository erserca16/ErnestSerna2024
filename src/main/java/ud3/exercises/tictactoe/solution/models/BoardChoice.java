package ud3.exercises.tictactoe.solution.models;

import java.io.Serializable;

public class BoardChoice implements Serializable {
    private final int x;
    private final int y;
    private int player;

    public BoardChoice(int x, int y, int player) {
        this.x = x;
        this.y = y;
        this.player = player;
    }
    public BoardChoice(int x, int y) {
        this(x, y, 0);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }
}