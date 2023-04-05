package battleship;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Grid grid = new Grid();
        grid.printBoard();
        System.out.println("Enter submarine placement: ");
        Scanner scan = new Scanner(System.in);
        String position = scan.nextLine();
        int[][] coords = grid.convertCoords(position);
        Ship submarine = new Ship(coords, "S");
        grid.placeShip(submarine);
        grid.printBoard();

    }
}
