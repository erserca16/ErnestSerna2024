package ud3.exercises.tictactoe.solution.client;

import ud3.exercises.tictactoe.solution.models.Board;
import ud3.exercises.tictactoe.solution.models.BoardChoice;
import ud3.exercises.tictactoe.solution.models.TicTacToeMessage;
import ud3.exercises.tictactoe.solution.models.TicTacToeMessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import static ud3.exercises.tictactoe.solution.models.TicTacToeMessageType.*;

public class TicTacToeClient {
    private final Socket socket;
    private final Scanner scanner;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    private Board board;
    private boolean finished;

    public TicTacToeClient(String host, int port) throws IOException {
        this.scanner = new Scanner(System.in);
        this.socket = new Socket(host, port);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.board = new Board();
        this.finished = false;
    }

    private TicTacToeMessage readMessage() throws IOException, ClassNotFoundException {
        return (TicTacToeMessage) in.readObject();
    }
    private void sendMessage(TicTacToeMessageType type, String message, Object obj) throws IOException {
        TicTacToeMessage ticTacToeMessage = new TicTacToeMessage(type, message);
        ticTacToeMessage.setObject(obj);
        out.writeObject(ticTacToeMessage);
    }

    private void playMove() throws IOException {
        System.out.println("Specify the position (X, Y) where you want to play:");
        int x = scanner.nextInt();
        int y = scanner.nextInt();
        sendMessage(POST, "", new BoardChoice(x, y));
    }

    public void game() throws IOException, ClassNotFoundException {
        System.out.println("Connected to the server.");
        System.out.println("Waiting for another player...");

        TicTacToeMessage message;
        while (!finished && (message = readMessage()) != null) {
            if (message.getType() == INFO){
                System.out.println(message.getMessage());
            } else if (message.getType() == START_TURN) {
                playMove();
            } else if (message.getType() == WAIT_TURN) {
                System.out.println("Waiting for your opponent...");
            } else if (message.getType() == START_GAME) {
                System.out.println(message.getMessage());
                board.render();
            } else if (message.getType() == INVALID_CHOICE) {
                System.err.println(message.getMessage());
                playMove();
            } else if (message.getType() == UPDATE_BOARD) {
                BoardChoice boardChoice = (BoardChoice) message.getObject();
                board.updateBoard(boardChoice);
                board.render();
            } else if (message.getType() == END_GAME) {
                finished = true;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Connectant-se amb el servidor...");
        try {
            TicTacToeClient ticTacToeClient = new TicTacToeClient("localhost", 1234);
            ticTacToeClient.game();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}