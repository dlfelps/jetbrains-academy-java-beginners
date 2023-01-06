package battleship;

import java.util.ArrayList;

enum Square {
    UNKNOWN, SHIP, HIT, MISS
}

enum ShipType {
    CARRIER, BATTLESHIP, SUBMARINE, CRUISER, DESTROYER
}

record Coordinates(int row, int col) {}

record Ship(ShipType type, Coordinates start, Coordinates end) {
    public boolean spans(Coordinates c) {

    }
}

class GameBoard {

    Square[][] board;
    ArrayList<Ship> ships;

    private Square getBoardState(Coordinates c) {
        //
    }
    private String printRowState(int row){
        for (int col = 1; col < 11; col++) {
            var coord = new Coordinates(row, col);
            ships.stream().anyMatch()
        }
    }
    private String printRow(int i){
        var rows = "-ABCDEFGHIJ";

        return rows.substring(i,i+1) + printRowState(i);
    }

    public void printBoard(){

        for (int i = 0; i<12; i++) {
            switch (i) {
                case 0 -> System.out.println("  1 2 3 4 5 6 7 8 9 10");
                default -> System.out.println(printRow(i));
            };
        }

    }

    private boolean isValidShip(){

    }




}
