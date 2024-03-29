import java.util.Scanner;

public class GoGame {
    private static final int BOARD_SIZE = 9;
    private char[][] board;

    public GoGame(){
        board = new char[BOARD_SIZE][BOARD_SIZE];
        initializeBoard();
    }

    private void initializeBoard(){
        for (int i = 0; i < BOARD_SIZE; i++){
            for (int j = 0; j < BOARD_SIZE; j++){
                board[i][j] = '+';
            }
        }
    }

    private void printBoard(){
        for (int i = 0; i < BOARD_SIZE; i++){
            for (int j = 0; j < BOARD_SIZE; j++){
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void placeStone(int row, int col, char stone){
        board[row][col] = stone;
    }

    private void capture(int row, int col, char stone){
        char opponent = (stone == 'B') ? 'W' : 'B';
        
        boolean[][] visited = new boolean[BOARD_SIZE][BOARD_SIZE];
        markConnectedStones(row, col, visited, stone);

        for (int i = 0; i < BOARD_SIZE; i++){
            for (int j = 0; j < BOARD_SIZE; j++){
                if (visited[i][j] && !hasLiberty(i, j, opponent)){
                    captureGroup(i, j, opponent);
                }
            }
        }
    }

    /*private boolean isSurrounded(int row, int col){
        char currentPlayer = board[row][col];
        char opponent = (currentPlayer == 'B') ? 'W' : 'B';
        
        boolean[][] visited = new boolean[BOARD_SIZE][BOARD_SIZE];
        markConnectedStones(row, col, visited);

        for (int i = 0; i < BOARD_SIZE; i++){
            for (int j = 0; j < BOARD_SIZE; j++){
                if (visited[i][j] && hasLiberty(i, j, opponent)){
                    return false;
                }
            }
        }

        return true;
    }*/

    private void markConnectedStones(int row, int col, boolean[][] visited, char stone) {
        visited[row][col] = true;

        if (row > 0 && board[row - 1][col] == stone && !visited[row - 1][col]){
            markConnectedStones(row - 1, col, visited, stone);
        }
        if (row < BOARD_SIZE - 1 && board[row + 1][col] == stone && !visited[row + 1][col]){
            markConnectedStones(row + 1, col, visited, stone);
        }
        if (col > 0 && board[row][col - 1] == stone && !visited[row][col - 1]){
            markConnectedStones(row, col - 1, visited, stone);
        }
        if (col < BOARD_SIZE - 1 && board[row][col + 1] == stone && !visited[row][col + 1]){
            markConnectedStones(row, col + 1, visited, stone);
        }
    }

    /*private boolean isOpponentOrEdge(int row, int col, char opponent){
        return (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE || board[row][col] == opponent);
    } */

    private boolean hasLiberty(int row, int col, char stoneColor){
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE){
            return false;
        }

        if (board[row][col] == '+'){
            return true;
        }

        if (board[row][col] == 'B' || board[row][col] == 'W'){
            return hasLiberty(row - 1, col, stoneColor) || hasLiberty(row + 1, col, stoneColor) || hasLiberty(row, col - 1, stoneColor) || hasLiberty(row, col + 1, stoneColor);
        }

        return false;
    }
    

    private void captureGroup(int row, int col, char stone){
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE || board[row][col] != stone){
            return;
        }

        board[row][col] = '+';

        captureGroup(row - 1, col, stone);
        captureGroup(row + 1, col, stone);
        captureGroup(row, col - 1, stone);
        captureGroup(row, col + 1, stone);
    }
    
    public static void main(String[] args) throws Exception {
        GoGame game = new GoGame();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Go!");
        System.out.println("Enter row and column to place your stone:");

        char currentPlayer = 'B';

        while (true){
            game.printBoard();
            System.out.println("Player " + currentPlayer + "'s turn:");

            int row = scanner.nextInt() - 1;
            int col = scanner.nextInt() - 1;

            if (row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE && game.board[row][col] == '+'){
                game.placeStone(row, col, currentPlayer);
                game.capture(row, col, currentPlayer);

                currentPlayer = (currentPlayer == 'B') ? 'W' : 'B';
            } else{
                System.out.println("Invlaid move. Try again.");
            }
        }
    }
}
