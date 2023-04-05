package battleship;

public class Ship {
    private boolean isHorizontal;
    private boolean isSunk;
    private int[][] position;
    private String shipClass;

    public int getShipLength() {
        return switch (shipClass) {
            case "Destroyer" -> 2;
            case "Cruiser", "Submarine" -> 3;
            case "Battleship" -> 4;
            case "Aircraft Carrier" -> 5;
            default -> 0; // is impossible in constructor
        };
    }

    public Ship (int[][] position, String shipClass) {
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
        this.position = position; // TODO add method to validate this, given the length
        // TODO add isHorizontal method
    }
}
