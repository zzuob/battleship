package battleship;

public class Ship {
    private boolean isSunk;
    private int[][] position;
    private int[][] cells;
    private String shipClass;

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
        if (shipClass.length() == 1) {
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
            if (shipClass == null) {
                throw new IllegalArgumentException("shipClass is required for each ship");
            }
            String[] shipClasses = {"Aircraft Carrier", "Battleship", "Submarine", "Cruiser", "Destroyer"};
            boolean valid = false;
            for (String ship: shipClasses
                 ) {
                if (ship.equals(shipClass)) {
                    valid = true;
                    break;
                }
            }
            if (valid) this.shipClass = shipClass;
            else {
                String message = String.format("shipClass %s does not exist", shipClass);
                throw new IllegalArgumentException(message);
            }
        }
    }
}
