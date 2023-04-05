package battleship;

import java.util.Arrays;

public class Grid {
    private String[][] board;
    private Ship[] ships;

    // TODO setShip - create ship object at valid location and add to grid.ships

    public int[] toAxis(String cell) {
        char[] components = cell.toCharArray();
        int[] result = new int[2];
        int start = ((int) 'A') - 1; // grid starts at (1,1), not (0,0)
        result[0] = ((int) components[0]) - start; // convert letter
        StringBuilder x = new StringBuilder();
        for (int i = 1; i < cell.length(); i++) {
            x.append(cell.charAt(i)); // convert digit(s)
        }
        result[1] = Integer.parseInt(String.valueOf(x));
        return result;
    }
    public int[][] convertCoords(String inputLine) {
        String[] coords = inputLine.trim().split(" ");
        int[][] result = new int[2][2];
        for (int i = 0; i < coords.length; i++) {
            result[i] = toAxis(coords[i]);
        }
        return result;
    }
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
