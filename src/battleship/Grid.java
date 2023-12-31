package battleship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Grid {

    private final int[] gridSize;
    private String[][] board;
    private List<Ship> ships = new ArrayList<>(); // list of all ships on grid

    public void addShip(Ship ship) { ships.add(ship); }

    public void visibleShips(boolean show) {
        // make ship cells "O" visible or hidden
        String replace;
        if (show) {
            replace = "O";
        } else {
            replace = "~";
        }
        for (Ship ship: ships) {
            //System.out.println(ship.getShipClass()+" - "+ Arrays.deepToString(ship.getCells()));
            for (int[] cell: ship.getCells()) {
                if (!board[cell[0]][cell[1]].matches("[XM]")) {
                    // ignore hits and misses but replace all other ship indicators
                    //System.out.println("\t"+board[cell[0]][cell[1]]);
                    board[cell[0]][cell[1]] = replace;
                }
            }
        }
    }

    public void clearShips() {
        // clear all graphics & ships from grid
        ships = new ArrayList<>();
        board = makeNewBoard();
    }

    public String updateShips() {
        String message = "";
        for (Ship ship : ships) {
            if (!ship.isSunk()) {
                int hits = 0;
                for (int[] cell : ship.getCells()) {
                    if (board[cell[0]][cell[1]].matches("X")) {
                        hits++; // count hits
                    }
                } // a ship is sunk if it's been hit on each occupied cell
                boolean sunk = hits == ship.getShipLength();
                if (sunk) {
                    ship.setSunk(true);
                    message = ("You sank a ship!");
                }
            }
        }
        return message;
    }

    public boolean hasLost() {
        // have all ships on the grid been sunk?
        int sunkCount = 0;
        for (Ship ship : ships) {
            if (ship.isSunk()) {
                sunkCount++;
            }
        }
        return sunkCount == ships.size();
    }

    public String addHit(int[] cell) {
        // add hit to game board
        String message;
        if (isOutOfBounds(cell)) {
            throw new IllegalArgumentException("position outside grid area");
        }
        int shipIndex = -1;
        for (int i = 0; i < ships.size(); i++) { // check if the selected cell matches a ship
            for (int[] shipCell: ships.get(i).getCells()
                 ) {
                if (cell[0]==shipCell[0] && cell[1]==shipCell[1]) {
                    shipIndex = i;
                    break;
                }
            }
            if (shipIndex != -1) break;
        }
        // update the board
        String symbol;
        if (shipIndex > -1 || "X".equals(board[cell[0]][cell[1]])) {
            // doesn't overwrite previous hits
            symbol = "X";
            message = "You hit a ship!";
        } else {
            symbol = "M";
            message = "You missed!";
        }
        board[cell[0]][cell[1]] = symbol;
        return message;

    }

    private boolean isCellValid(int[] cell) {
        // cell passes if unoccupied and has space (fog) on all sides (excluding diagonal)
        boolean unoccupied = Objects.equals(board[cell[0]][cell[1]], "~");
        boolean topSpace = cell[0] == 1 || Objects.equals(board[cell[0] - 1][cell[1]], "~");
        boolean bottomSpace = cell[0] == gridSize[0]-1 || Objects.equals(board[cell[0] + 1][cell[1]], "~");
        boolean rightSpace = cell[1] == gridSize[1]-1 || Objects.equals(board[cell[0]][cell[1] + 1], "~");
        boolean leftSpace = cell[1] == 1 || Objects.equals(board[cell[0]][cell[1] - 1], "~");
        return unoccupied && topSpace && bottomSpace && leftSpace && rightSpace;
    }

    private int[][] getShipCells(int start, int repeat, int inputLength, boolean isHorizontal) {
        // return the cell positions that the ship occupies
        int[][] cells = new int[inputLength][2];
        int[] startCell = isHorizontal ? new int[]{repeat, start} : new int[]{start, repeat};
        cells[0] = startCell;
        for (int i = 1; i < inputLength; i++) {
            int[] newCell = isHorizontal ? new int[]{repeat, start+i} : new int[]{start+i, repeat};
            cells[i] = newCell;
        }
        return cells;
    }

    private boolean isOutOfBounds(int[] position) {
        // check if a 2D point is within the grid
        if (gridSize == null) throw new NullPointerException("grid size is not set");
        if (position.length != 2) throw new IllegalArgumentException("positions are 2D");
        boolean valid = true;
        for (int i = 0; i < gridSize.length; i++) {
            valid = 1 <= position[i] && position[i] < gridSize[i];
            if (!valid) break;
        }
        return !valid;
    }

    public static int[] toAxis(String cell) {
        // convert coordinates to a 2D position e.g. A1 -> { 1, 1}
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
        // A1, B1 -> {{1, 1}, {2, 1}}
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
        // generate new board according the grid's size, fill all spaces with fog of war "~"
        String[][] newBoard = new String[gridSize[0]][gridSize[1]];
        for (String[] line: newBoard
        ) {
            Arrays.fill(line, "~");
        }
        for (int i = 0; i < newBoard[0].length; i++) {
            newBoard[0][i] = Integer.toString(i);
        }
        char start = '@'; // char before 'A', header starts at [1][0]
        for (int i = 0; i < newBoard.length; i++) {
            char startChar = (char) (start+i);
            newBoard[i][0] = String.valueOf(startChar);
        }
        newBoard[0][0] = " "; // no character in top left corner
        return newBoard;
    }
    public void printBoard() {
        // print the current game board
        for (String[] line: board
             ) {
            for (String symbol: line
                 ) {
                System.out.print(symbol+" ");
            }
            System.out.println();
        }
    }

    public void placeShip(Ship ship) {
        // place a ship on the game board, adding it to the list of ships on the board
        int length = ship.getShipLength();
        int[] pos1 = ship.getPosition()[0]; // pos1 = { y1, x1 }
        int[] pos2 = ship.getPosition()[1]; // pos2 = { y2, x2 }
        boolean valid = pos1[0] == pos2[0] || pos1[1] == pos2[1];
        boolean isHorizontal = pos1[0] == pos2[0];
        // check if positions are valid in relation to the game board and each-other
        if (!valid) {
            throw new IllegalArgumentException("ship cannot be diagonal");
        }
        if (isOutOfBounds(pos1) || isOutOfBounds(pos2)) {
            throw new IllegalArgumentException("position outside grid area");
        }
        int inputLength, start, repeat;
        if (isHorizontal) {
            inputLength = Math.abs(pos1[1] - pos2[1]) + 1;
            start = Math.min(pos1[1], pos2[1]);
            repeat = pos1[0];
        } else {
            inputLength = Math.abs(pos1[0] - pos2[0]) + 1; // +1 to include all cells, not just difference
            start = Math.min(pos1[0], pos2[0]);
            repeat = pos1[1];
        }
        // check if positions are valid according to the ship to be placed
        if (length != inputLength) {
            String message = String.format("wrong length of %s", ship.getShipClass());
            throw new IllegalArgumentException(message);
        }
        int[][] cells = getShipCells(start, repeat, inputLength, isHorizontal);
        boolean invalidDestination = false;
        for (int[] cell : cells
        ) {
            invalidDestination = !(isCellValid(cell));
            if (invalidDestination) break;
        }
        // check if ship has space on all sides
        if (invalidDestination) {
            throw new IllegalArgumentException("destination is too close to another ship");
        }
        // update the ship with its cells and the board with the ship
        ship.setCells(cells);
        addShip(ship);
        for (int i = start; i < start + length; i++) { // board[y][x]
            if (isHorizontal) board[repeat][i] = "O";
            else board[i][repeat] = "O";
        }
    }



    public Grid (){
        this.gridSize = new int[]{11,11};
        this.board = makeNewBoard();
    }

    @Deprecated
    public Grid (int[] size){
        this.gridSize = size;
        this.board = makeNewBoard();
    }
}
