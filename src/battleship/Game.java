package battleship;

import java.util.Scanner;

public class Game {

    private Grid playerBoard;

    public Game() {
        this.playerBoard = new Grid();
        playerBoard.printBoard();
        Ship[] allShips = {new Ship("A"), new Ship("B"), new Ship("S"),
                new Ship("C"), new Ship("D")};
        Ship[] newShips = new Ship[allShips.length];
        for (int i = 0; i < allShips.length; i++){
            // create each ship using the positioning specified by the user
            Ship ship = allShips[i];
            boolean invalid = true;
            while (invalid) {
                try  {
                    Scanner scan = new Scanner(System.in);
                    String message = String.format("Enter the coordinates of the %s (%d cells):\n",
                            ship.getShipClass(), ship.getShipLength());
                    System.out.println(message);
                    String pos1;
                    String pos2;
                    if (scan.hasNext()) {
                        pos1 = scan.next();
                        if (scan.hasNext()) {
                            pos2 = scan.next();
                            ship.setPosition(playerBoard.convertCoords(pos1, pos2));
                            playerBoard.placeShip(ship);
                            newShips[i] = ship;
                            playerBoard.printBoard();
                            invalid = false;
                        }
                    }

                } catch (Exception e) {
                    String message = String.format("\nError - %s", e.getMessage());
                    System.out.println(message);
                }
            }
        }

    }
}
