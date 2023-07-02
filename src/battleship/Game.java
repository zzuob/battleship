package battleship;

import java.util.Objects;
import java.util.Scanner;

import static battleship.Grid.toAxis;

public class Game {

    public final String[] allClasses = {"A", "B", "S", "C", "D"};
    private final Grid board1;
    private final Grid board2;

    private boolean isPlayer1Turn = true;
    private final boolean debug;

    private final Scanner scan = new Scanner(System.in);

    enum State {
        PLACE, SHOOT, UPDATE, WIN, END
    }

    private void playerInit(Grid board) {
        board.printBoard();
        for (String shipClass : allClasses) {
            // create each ship at a position specified by the user
            Ship ship = new Ship(shipClass);
            boolean invalid = true;
            while (invalid) {
                try {
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
            for (String shipClass : allClasses) {
                Ship ship = new Ship(shipClass);
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
                        if (debug) {
                            System.out.printf(
                                    "[DEBUG] CPU failed to place ship %d time(s), reason: %s\n",
                                    debugCounter, e.getMessage());
                        }
                    }
                }
            }
        }
    }

    private String callShot(Grid board) {
        // validate a shot called by a player
        String message = "";
        boolean invalid = true;
        while (invalid) {
            try {
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

    private String getPlayerNo() { return isPlayer1Turn ? "1" : "2"; }
    private void placeShipsOnGrid(Grid grid) {
        System.out.printf("Player %s, place your ships on the game field\n", getPlayerNo());
        System.out.println("Place (1) automatically or (2) manually?");
        int choice = -1;
        while (!(1 <= choice && choice <= 2)) {
            boolean validInput = false;
            if (scan.hasNextLine()) {
                String number = scan.nextLine();
                if (number.matches("\\d+")) {
                    choice = Integer.parseInt(number);
                    if (choice == 1 || choice == 2) {
                        validInput = true;
                    }
                }
            }
            if (!validInput) {
                System.out.println("Enter 1 or 2");
            }
        }
        boolean autoPlace = choice == 1;
        if (debug || autoPlace) {
            computerInit(grid);
        } else {
            playerInit(grid);
        }
    }

    private void changeTurn() {
        System.out.println("Press Enter and pass the move to another player");
        scan.nextLine();
        isPlayer1Turn = !isPlayer1Turn;
    }

    public void loop() {
        State state = State.PLACE;
        String message = "";
        String divider = "---------------------";
        Grid opponent, player;
        while (state != State.END) {
            if (debug) {
                System.out.printf("[DEBUG] Game state: %s\n", state);
            }
            if (isPlayer1Turn) {
                player = board1;
                opponent = board2;
            } else {
                player = board2;
                opponent = board1;
            }
            switch (state) {
                case PLACE -> {
                    // place all ships on the board
                    placeShipsOnGrid(board1);
                    changeTurn();
                    placeShipsOnGrid(board2);
                    changeTurn();
                    state = State.SHOOT;
                }
                case SHOOT -> {
                    // show boards
                    opponent.visibleShips(false);
                    opponent.printBoard();
                    System.out.println(divider);
                    player.visibleShips(true);
                    player.printBoard();
                    System.out.printf("Player %s, it's your turn:\n", getPlayerNo());
                    message = callShot(opponent); // message = either a hit or a miss
                    state = State.UPDATE;
                }
                case UPDATE -> {
                    // check if a ship has been sunk, and if all ships have been sunk
                    String overwrite = opponent.updateShips(); // check if a ship has been sunk
                    if (!"".equals(overwrite)) {
                        message = overwrite; // sunk notification takes precedence over the hit notification
                    }
                    if (opponent.hasLost()) {
                        state = State.WIN;
                    } else {
                        System.out.println(message);
                        changeTurn();
                        state = State.SHOOT; // not all ships are sunk, go back to SHOOT state
                    }
                }
                case WIN -> {
                    // all ships have been sunk on a grid
                    opponent.printBoard();
                    System.out.println("You sank the last ship. You won. Congratulations!");
                    state = State.END;
                }
            }
        }
    }

    public Game(boolean debug) {
        this.board1 = new Grid();
        this.board2 = new Grid();
        this.debug = debug;
    }
}
