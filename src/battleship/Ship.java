package battleship;

public class Ship {
    private boolean isSunk;
    private int[][] position;
    private int[][] cells;
    private final String shipClass;

    public boolean isSunk() {
        return isSunk;
    }

    public void setSunk(boolean sunk) {
        isSunk = sunk;
    }

    public void setPosition(int[][] position) {
        this.position = position;
    }

    public int[][] getPosition() {
        return position;
    }

    public void setCells(int[][] cells) {
        this.cells = cells;
    }

    public int[][] getCells() {
        return cells;
    }

    public String getShipClass() {
        return shipClass;
    }

    public int getShipLength() {
        return switch (shipClass) {
            case "Destroyer" -> 2;
            case "Cruiser", "Submarine" -> 3;
            case "Battleship" -> 4;
            case "Aircraft Carrier" -> 5;
            default -> 0; // is impossible in constructor
        };
    }

    public Ship (String shipClass) {
        // create a new ship object
        this.isSunk = false;
        // validate ship's class
        if (shipClass.length() == 1) { // single-letter abbreviations for each class
            this.shipClass = switch(shipClass) {
                case "D" -> "Destroyer";
                case "C" -> "Cruiser";
                case "S" -> "Submarine";
                case "B" -> "Battleship";
                case "A" -> "Aircraft Carrier";
                default -> {
                    String message = String.format("shipClass abbreviation %s does not exist", shipClass);
                    throw new IllegalArgumentException(message);
                }
            };
        } else {
            String[] shipClasses = {"Aircraft Carrier", "Battleship", "Submarine", "Cruiser", "Destroyer"};
            boolean valid = false;
            for (String ship: shipClasses
                 ) {
                if (ship.equals(shipClass)) {
                    valid = true;
                    break;
                }
            }
            if (valid) {
                this.shipClass = shipClass;
            } else {
                String message = String.format("shipClass %s does not exist", shipClass);
                throw new IllegalArgumentException(message);
            }
        }
    }
}
