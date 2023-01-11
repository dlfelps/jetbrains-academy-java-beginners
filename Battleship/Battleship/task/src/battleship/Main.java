package battleship;

import static battleship.State.*;

public class Main {

    public static void main(String[] args) {
        var board = Battleship.empty();
        board.printBoard();
        board.setupBoard();
//        board.addShip(CRUISER,3);
    }
}
