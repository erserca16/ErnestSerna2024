package ud3.exercises.tictactoe.solution.models;

import java.io.Serializable;

public class TicTacToeMessage implements Serializable {
    private TicTacToeMessageType type;
    private String message;
    private Object object;

    public TicTacToeMessage(TicTacToeMessageType type, String message) {
        this.type = type;
        this.message = message;
        this.object = null;
    }

    public TicTacToeMessage(TicTacToeMessageType type) {
        this(type, "");
    }

    public TicTacToeMessageType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}