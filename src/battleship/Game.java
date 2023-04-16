package battleship;

import java.util.Objects;
import java.util.Scanner;

import static battleship.Grid.toAxis;

public class Game {

    public final Ship[] allShips = {new Ship("A"), new Ship("B"), new Ship("S"),
            new Ship("C"), new Ship("D")};
    private Grid playerBoard;
    private Grid opponentBoard;

    private void playerInit() {
        for (Ship ship : allShips) {
            // create each ship using the positioning specified by the user
            boolean invalid = true;
            while (invalid) {
                try {
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

    private int get1To10(){ return (int) (Math.random()*10)+1; }

    private void opponentInit() {
        // randomly generate all 5 ship placements
        boolean finished = false;
        int debugCounter;
        while (!finished) {
            debugCounter = 0; // count failed placements per ship
            for (Ship ship : allShips) {
                // create each ship using a random position
                if (debugCounter > 10) {
                    // after 10 tries to place ship, reset all placements and start over
                    opponentBoard.clearShips();
                    break;
                }
                int length = ship.getShipLength();
                boolean invalid = true;
                debugCounter = 0;
                while (invalid) {
                    int[] pos1 = new int[]{get1To10(), get1To10()};
                    // 50:50 horizontal to vertical placement
                    boolean isHorizontal = Math.random() < 0.5;
                    int x2, y2;
                    if (isHorizontal) {
                        y2 = pos1[0];
                        x2 = pos1[1] + length - 1;

                    } else {
                        y2 = pos1[0] + length - 1;
                        x2 = pos1[1];
                    }
                    int[] pos2 = new int[]{y2, x2};
                    int[][] position = new int[][]{pos1, pos2};
                    try {
                        ship.setPosition(position);
                        opponentBoard.placeShip(ship);
                        invalid = false;
                        //opponentBoard.printBoard();
                        if (Objects.equals(ship.getShipClass(), "Destroyer")) finished = true;
                    } catch (Exception e) {
                        debugCounter++;
                        //opponentBoard.printBoard();
                        //System.out.printf("CPU failed to place ship %d time(s), reason: %s\n", debugCounter, e.getMessage());
                    }
                }
            }
        }
    }

    public void callShot() {
        // validate a shot called by a player
        boolean invalid = true;
        while (invalid) {
            try {
                Scanner scan = new Scanner(System.in);
                System.out.println("Take a shot!\n");
                if (scan.hasNext()) {
                    String position = scan.next();
                    int[] cell = toAxis(position);
                    playerBoard.addHit(cell); // TODO change to opponent board
                    invalid = false;
                }
            } catch (Exception e) {
                String message = String.format("\nError - %s", e.getMessage());
                System.out.println(message);
            }
        }
        System.out.println();
        playerBoard.printBoard(); // TODO change to opponent board
    }

    public Game() {
        this.playerBoard = new Grid();
        this.opponentBoard = new Grid();
        opponentInit();
        playerInit();
        //playerBoard = opponentBoard; // for testing
        playerBoard.hideShips();
        System.out.println("The game starts!");
        System.out.println();
        playerBoard.printBoard();


    }
}
