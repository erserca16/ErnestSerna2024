package ud3.exercises.tictactoe.solution.server;

import ud3.exercises.tictactoe.solution.models.BoardChoice;
import ud3.exercises.tictactoe.solution.models.TicTacToeMessage;
import ud3.exercises.tictactoe.solution.models.TicTacToeMessageType;

import java.io.*;
import java.net.Socket;

import static ud3.exercises.tictactoe.solution.models.TicTacToeMessageType.*;
public class TicTacToeServerPlayerHandler {
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    public TicTacToeServerPlayerHandler(Socket socket) throws IOException {
        this.socket = socket;

        this.in = new ObjectInputStream(socket.getInputStream());
        this.out = new ObjectOutputStream(socket.getOutputStream());
    }

    public void sendMessage(TicTacToeMessageType type, String message) throws IOException {
        out.writeObject(new TicTacToeMessage(type, message));
    }
    public void sendMessage(TicTacToeMessageType type, String message, Object obj) throws IOException {
        TicTacToeMessage ticTacToeMessage = new TicTacToeMessage(type, message);
        ticTacToeMessage.setObject(obj);
        out.writeObject(ticTacToeMessage);
    }
    public void sendMessage(TicTacToeMessageType type) throws IOException {
        out.writeObject(new TicTacToeMessage(type));
    }

    public BoardChoice getChoice() throws IOException, ClassNotFoundException {
        TicTacToeMessage receivedMessage = (TicTacToeMessage) in.readObject();
        while (receivedMessage.getType() != POST){
            sendMessage(ERROR, "Expected a POST message.");
            receivedMessage = (TicTacToeMessage) in.readObject();
        }

        return (BoardChoice) receivedMessage.getObject();
    }

    public void close() throws IOException {
        this.socket.close();
    }
}