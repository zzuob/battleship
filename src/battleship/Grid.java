package battleship;

import java.util.Arrays;

public class Grid {
    private String[][] board;

    public void printBoard() {
        for (String[] line: board
             ) {
            for (String symbol: line
                 ) {
                System.out.print(symbol+" ");
            }
            System.out.println();
        }
    }
    public Grid (){
        String[][] newBoard = new String[11][11];
        for (String[] line: newBoard
             ) {
            Arrays.fill(line, "~");
        }
        for (int i = 0; i < newBoard[0].length; i++) {
            newBoard[0][i] = Integer.toString(i);
        }
        char start = '@'; // char before 'A'
        for (int i = 1; i < newBoard.length; i++) {
            char startChar = (char) (start+i);
            newBoard[i][0] = String.valueOf(startChar);
        }
        newBoard[0][0] = "";
        this.board = newBoard;

    }
}
