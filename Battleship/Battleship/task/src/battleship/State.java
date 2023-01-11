package battleship;

public enum State {
    CARRIER,
    BATTLESHIP,
    SUBMARINE,
    CRUISER,
    DESTROYER,
    OPEN,
    ADJACENT;

    @Override
    public String toString(){
        return switch (this) {
            case CARRIER -> "Aircraft Carrier";
            case BATTLESHIP -> "Battleship";
            case SUBMARINE -> "Submarine";
            case CRUISER -> "Cruiser";
            case DESTROYER -> "Destroyer";
            case OPEN -> "Open";
            case ADJACENT -> "Adjacent";
        };
    }
}

