package battleship;

import static battleship.State.*;

public class Main {

    public static void main(String[] args) {
        var board = Battleship.empty();
        board.printTrueState();
        board.setupBoard();
        board.play();

    }
}
