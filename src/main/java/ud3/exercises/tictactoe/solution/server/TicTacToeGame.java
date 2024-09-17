package ud3.exercises.tictactoe.solution.server;

import ud3.exercises.tictactoe.solution.models.Board;
import ud3.exercises.tictactoe.solution.models.BoardChoice;
import ud3.exercises.tictactoe.solution.models.TicTacToeMessageType;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static ud3.exercises.tictactoe.solution.models.TicTacToeMessageType.*;

public class TicTacToeGame extends Thread {
    private final int id;
    private final List<TicTacToeServerPlayerHandler> players;

    private final Board board;
    private boolean finished;
    private int currentPlayer;

    public TicTacToeGame(int id) {
        this.id = id;
        this.players = new ArrayList<>();
        board = new Board();
        finished = false;
    }

    public void addPlayer(Socket client) throws IOException {
        if (players.size() < 2)
            players.add(new TicTacToeServerPlayerHandler(client));
    }

    public boolean enoughPlayers(){
        return players.size() >= 2;
    }

    public TicTacToeServerPlayerHandler getCurrentPlayer(){
        return players.get(currentPlayer);
    }

    public void sendGlobalMessage(TicTacToeMessageType type) throws IOException{
        for(TicTacToeServerPlayerHandler player : players){
            player.sendMessage(type);
        }
    }
    public void sendGlobalMessage(TicTacToeMessageType type, String message) throws IOException{
        for(TicTacToeServerPlayerHandler player : players){
            player.sendMessage(type, message);
        }
    }
    public void sendGlobalMessage(TicTacToeMessageType type, String message, Object obj) throws IOException{
        for(TicTacToeServerPlayerHandler player : players){
            player.sendMessage(type, message, obj);
        }
    }

    public void updateClientBoards(BoardChoice choice) throws IOException {
        sendGlobalMessage(UPDATE_BOARD, "", choice);
    }

    public void changeTurn(){
        currentPlayer = (currentPlayer + 1) % players.size();
    }

    private void close() throws IOException {
        for(TicTacToeServerPlayerHandler player : players){
            player.close();
        }
    }

    @Override
    public void run(){
        try {
            System.out.printf("Game %d has started!\n", id);
            currentPlayer = ThreadLocalRandom.current().nextInt(0, 2);

            sendGlobalMessage(START_GAME, "Game has started!");

            getCurrentPlayer().sendMessage(WAIT_TURN);
            changeTurn();
            getCurrentPlayer().sendMessage(INFO, "You are the first to move!");
            getCurrentPlayer().sendMessage(START_TURN);

            while (!finished){
                BoardChoice choice = getCurrentPlayer().getChoice();
                while (!board.isValid(choice)){
                    getCurrentPlayer().sendMessage(INVALID_CHOICE, "The position is not valid.");
                    choice = getCurrentPlayer().getChoice();
                }
                choice.setPlayer(currentPlayer + 1);
                board.updateBoard(choice);
                updateClientBoards(choice);

                board.getWinner();
                if(board.isFinished()){
                    if (board.getWinner() == 0)
                        sendGlobalMessage(INFO, "It's a draw.");
                    else
                        sendGlobalMessage(INFO, String.format("Player %d is the winner!", board.getWinner()));
                    sendGlobalMessage(END_GAME);
                    finished = true;
                }

                getCurrentPlayer().sendMessage(WAIT_TURN);
                changeTurn();
                getCurrentPlayer().sendMessage(START_TURN);
            }

            close();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}