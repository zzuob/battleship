package battleship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Grid {

    private final int[] gridSize;
    private String[][] board;
    private Ship[] ships;

    public void setShips(Ship[] ships) {
        this.ships = ships;
    }

    private boolean inBounds(int[] position) {
        if (gridSize == null) throw new NullPointerException("grid size is not set");
        if (position.length != 2) throw new IllegalArgumentException("positions are 2D");
        boolean valid = true;
        for (int i = 0; i < gridSize.length; i++) {
            valid = 1 <= position[i] && position[i] < gridSize[i];
            if (!valid) break;
        }
        return valid;
    }

    public int[] toAxis(String cell) {
        if (cell == null) throw new NullPointerException("coordinate cannot be null");
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
    public int[][] convertCoords(String pos1, String pos2) {
        if (pos1 == null || pos1.length() < 2 || pos2 == null || pos2.length() < 2) {
            throw new IllegalArgumentException("incomplete coordinates");
        }
        String[] coords = {pos1, pos2};
        int[][] result = new int[2][2];
        for (int i = 0; i < coords.length; i++) {
            result[i] = toAxis(coords[i]);
        }
        return result;
    }

    private String[][] makeNewBoard() {
        String[][] newBoard = new String[gridSize[0]][gridSize[1]];
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
        newBoard[0][0] = " ";
        return newBoard;
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

    /* TODO add additional validation for place ship:
        - cannot sit on occupied cell
        - cannot be within 1 cell of another "O"
     */
    public void placeShip(Ship ship) {
        int length = ship.getShipLength();
        int[] pos1 = ship.getPosition()[0];
        int[] pos2 = ship.getPosition()[1];
        boolean valid = pos1[0] == pos2[0] || pos1[1] == pos2[1];
        boolean isHorizontal = pos1[0] == pos2[0];
        if (valid) {
            if (inBounds(pos1) && inBounds(pos2)) {
                int inputLength, start, repeat;
                if (isHorizontal) {
                    inputLength = Math.abs(pos1[1]-pos2[1])+1;
                    start = Math.min(pos1[1], pos2[1]);
                    repeat = pos1[0];

                } else {
                    inputLength = Math.abs(pos1[0]-pos2[0])+1; // +1 to include all cells, not just difference
                    start = Math.min(pos1[0], pos2[0]);
                    repeat = pos1[1];
                }
                if (length != inputLength) {
                    String message = String.format("wrong length of %s", ship.getShipClass());
                    throw new IllegalArgumentException(message);
                }
                for (int i = start; i < start+length; i++) {
                    if (isHorizontal) board[repeat][i] = "O";
                    else board[i][repeat] = "O";
                }

            } else throw new IllegalArgumentException("position out of grid bounds");
        }
        else throw new IllegalArgumentException("ship cannot be diagonal");

    }
    public Grid (){
        this.gridSize = new int[]{11,11};
        this.board = makeNewBoard();
    }
    public Grid (int[] size){
        this.gridSize = size;
        this.board = makeNewBoard();
    }
}
