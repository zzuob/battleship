package battleship;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Grid grid = new Grid();
        grid.printBoard();
        System.out.println("J10 - " + Arrays.toString(grid.toAxis("J10")));
    }
}
