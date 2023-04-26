package battleship;

import java.util.Objects;
import java.util.Scanner;

import static battleship.Grid.toAxis;

public class Game {

    public final Ship[] allShips = {new Ship("A"), new Ship("B"), new Ship("S"),
            new Ship("C"), new Ship("D")};
    private Grid board1;
    private Grid board2;

    enum State {
        PLACE, SHOOT, UPDATE, WIN, END
    }

    private void playerInit(Grid board) {
        for (Ship ship : allShips) {
            // create each ship at a position specified by the user
            boolean invalid = true;
            while (invalid) {
                try {
                    Scanner scan = new Scanner(System.in);
                    String message = String.format("Enter the coordinates of the %s (%d cells):\n",
                            ship.getShipClass(), ship.getShipLength());
                    System.out.println(message);
                    String pos1, pos2;
                    if (scan.hasNext()) {
                        pos1 = scan.next();
                        if (scan.hasNext()) {
                            pos2 = scan.next();
                            // will throw an error if pos1 and pos2 aren't valid positions for the ship
                            ship.setPosition(board.convertCoords(pos1, pos2));
                            board.placeShip(ship); // update the grid with the new ship
                            board.printBoard();
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

    private void computerInit(Grid board) {
        // randomly generate all 5 ship placements
        boolean finished = false;
        int debugCounter;
        while (!finished) {
            debugCounter = 0; // count failed placements per ship
            for (Ship ship : allShips) {
                // create each ship using a random position
                if (debugCounter > 10) {
                    // after 10 tries to place ship, reset all placements and start over
                    board.clearShips();
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
                        board.placeShip(ship);
                        invalid = false;
                        if (Objects.equals(ship.getShipClass(), "Destroyer")) finished = true;
                    } catch (Exception e) {
                        debugCounter++;
                        System.out.printf(
                                "[DEBUG] CPU failed to place ship %d time(s), reason: %s\n",
                                debugCounter, e.getMessage());
                    }
                }
            }
        }
    }

    private String callShot(Grid board) {
        // validate a shot called by a player
        board.printBoard(); // show the player the current board
        System.out.println("Take a shot!\n"); // input prompt
        String message = "";
        boolean invalid = true;
        while (invalid) {
            try {
                Scanner scan = new Scanner(System.in);
                if (scan.hasNext()) {
                    String position = scan.next();
                    int[] cell = toAxis(position);
                    message = board.addHit(cell);
                    invalid = false;
                }
            } catch (Exception e) {
                System.out.format("\nError - %s", e.getMessage());
            }
        }
        System.out.println();
        return message;
    }

    public void loop(boolean debug) {
        State state = State.PLACE;
        String message = "";
        Grid opponent = board1;
        while (state != State.END) {
            switch (state) {
                case PLACE:
                    // place all ships on the board
                    if (debug) {
                        System.out.printf("[DEBUG] Game state: %s\n", state);
                        computerInit(opponent);
                    } else {
                        playerInit(opponent);
                    }
                    opponent.visibleShips(false);
                    System.out.println("The game starts!\n");
                    state = State.SHOOT;
                    break;
                case SHOOT:
                    // add a hit or miss to the opponents board
                    if (debug) {
                        System.out.printf("[DEBUG] Game state: %s\n", state);
                    }
                    message = callShot(opponent); // message = either a hit or a miss
                    if (debug) {
                        System.out.println("[DEBUG] Board without fog of war:");
                        opponent.visibleShips(true);
                        opponent.printBoard();
                        opponent.visibleShips(false);
                    }
                    state = State.UPDATE;
                    break;
                case UPDATE:
                    // check if a ship has been sunk, and if all ships have been sunk
                    if (debug) {
                        System.out.printf("[DEBUG] Game state: %s\n", state);
                    }
                    String overwrite = opponent.updateShips(); // check if a ship has been sunk
                    if (!"".equals(overwrite)){
                        message = overwrite; // sunk notification takes precedence over the hit notification
                    }
                    if (opponent.hasLost()) {
                        state = State.WIN;
                    } else {
                        System.out.println(message);
                        state = State.SHOOT; // not all ships are sunk, go back to SHOOT state
                    }
                    break;
                case WIN:
                    // all ships have been sunk on a grid
                    if (debug) {
                        System.out.printf("[DEBUG] Game state: %s\n", state);
                    }
                    opponent.printBoard();
                    System.out.println("You sank the last ship. You won. Congratulations!");
                    state = State.END;
                    break;
            }
        }
    }

    public Game() {
        this.board1 = new Grid();
        this.board2 = new Grid();
    }
}
