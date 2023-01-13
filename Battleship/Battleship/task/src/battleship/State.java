package battleship;

public enum State {
    CARRIER,
    BATTLESHIP,
    SUBMARINE,
    CRUISER,
    DESTROYER,
    OPEN,
    ADJACENT,
    HIT,
    MISS;

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
            case HIT -> "Hit";
            case MISS -> "Miss";
        };
    }
}

