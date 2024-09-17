package ud3.exercises.tictactoe.solution.models;

public class Board {
    private final int[][] board;
    private int moveCount;
    private int winner;
    private boolean finished;

    public Board() {
        this.board = new int[3][3];
        this.moveCount = 0;
        this.winner = 0;
        this.finished = false;
    }

    public boolean isValid(BoardChoice boardChoice){
        return this.board[boardChoice.getX()][boardChoice.getY()] == 0;
    }

    public void updateBoard(BoardChoice boardChoice){
        int x = boardChoice.getX();
        int y = boardChoice.getY();
        int player = boardChoice.getPlayer();
        this.board[x][y] = player;
    }

    public void render(){
        for (int y = 2; y >= 0; y--) {
            System.out.println("-------------");
            for (int x = 0; x <= 2; x++) {
                int v = board[x][y];
                System.out.printf("| %s ", v > 0 ? v : " ");
            }
            System.out.println("|");
        }
        System.out.println("-------------");
    }

    public void addChoice(BoardChoice boardChoice){
        int x = boardChoice.getX();
        int y = boardChoice.getY();
        int player = boardChoice.getPlayer();
        this.board[x][y] = player;
        moveCount++;

        checkRow(y, player);
        if (!finished) checkColumn(x, player);
        if (!finished) checkDiagonal(x, y, player);
        if (!finished) checkAntiDiagonal(x, y, player);
        if (!finished) finished = moveCount == 9;
    }

    public void checkRow(int y, int player){
        for (int x = 0; x < 3; x++){
            if (board[x][y] != player) return;
        }

        this.winner = player;
        this.finished = true;
    }

    public void checkColumn(int x, int player){
        for (int y = 0; y < 3; y++){
            if (board[x][y] != player) return;
        }

        this.winner = player;
        this.finished = true;
    }

    public void checkDiagonal(int x, int y, int player){
        if (x != y)
            return;

        for (int i = 0; i < 3; i++){
            if (board[i][i] != player) return;
        }

        this.winner = player;
        this.finished = true;
    }

    public void checkAntiDiagonal(int x, int y, int player){
        if (x + y != 2)
            return;

        for (int i = 0; i < 3; i++){
            if (board[i][2 - i] != player) return;
        }

        this.winner = player;
        this.finished = true;
    }

    public boolean isFinished() {
        return finished;
    }
    public int getWinner() {
        return winner;
    }
}