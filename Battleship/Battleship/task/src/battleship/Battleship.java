package battleship;


import java.util.*;
import java.util.stream.Collectors;

import static battleship.State.*;

public class Battleship {

    private HashMap<Coordinates,State> board;

    private Battleship(Map<Coordinates,State> initialState){
        board = new HashMap(100);
        board.putAll(initialState);
    }

    public static Battleship empty() {
        var emptyBoard = new Battleship(new HashMap<Coordinates,State>());
        for (int row = 1; row < 11; row++) {
            for (int col = 1; col < 11; col++) {
                emptyBoard.updateBoard(new Coordinates(col, row), OPEN);
            }
        }
        return emptyBoard;
    }

    public void updateBoard(Coordinates key, State value){
        board.put(key, value);
    }

    public State getState(Coordinates c){
        return board.getOrDefault(c, OPEN);
    }

    public void printBoard() {

        System.out.println("  1 2 3 4 5 6 7 8 9 10");

        for (int row = 1; row < 11; row++) {
            switch (row){
                case 1 -> System.out.print("A");
                case 2 -> System.out.print("B");
                case 3 -> System.out.print("C");
                case 4 -> System.out.print("D");
                case 5 -> System.out.print("E");
                case 6 -> System.out.print("F");
                case 7 -> System.out.print("G");
                case 8 -> System.out.print("H");
                case 9 -> System.out.print("I");
                case 10 -> System.out.print("J");
            }

            for (int col = 1; col < 11; col++) {
                switch (getState(new Coordinates(col, row))) {
                    case OPEN, ADJACENT -> System.out.print(" ~");
                    case BATTLESHIP, CARRIER, CRUISER, DESTROYER, SUBMARINE  -> System.out.print(" O");
                    case HIT -> System.out.print(" X");
                    case MISS -> System.out.print(" M");
                }
            }
            System.out.println();
        }
    }

    public void setupBoard() {

        addShip(CARRIER,5);
        addShip(BATTLESHIP,4);
        addShip(SUBMARINE,3);
        addShip(CRUISER,3);
        addShip(DESTROYER,2);
    }

    private void addShip(State ship, int shipLength) {
        Optional<HashMap<Coordinates, State>> boardWithShip = Optional.empty();
        while (boardWithShip.isEmpty()) {
            boardWithShip = tryPlaceShip(ship, shipLength);
        }
        //successful placement
        board = boardWithShip.get();//gets the value from optional
        printBoard();
    }

    private Optional<HashMap<Coordinates,State>> tryPlaceShip(State shipType, int shipLength) {
        System.out.println(String.format("Enter the coordinates of the %s (%d cells):", shipType, shipLength));
        var scanner = new Scanner(System.in);

        Optional<HashMap<Coordinates,State>> newBoard = Optional.empty();

        try {
            var input = scanner.nextLine();
            var temp = input.split(" ");
            var start = Coordinates.fromString(temp[0]);
            var stop = Coordinates.fromString(temp[1]);
            var shipSpan = start.spans(stop);
            if (start.distanceTo(stop) != shipLength) {
                throw new IllegalArgumentException(String.format("Error! Wrong length of the %s! Try again:", shipType));
            }
            // ensure all spots are open
            var shipSurround = shipSpan.stream()
                    .flatMap(Coordinates::surroundingPoints)
                    .collect(Collectors.toList());

            var isOpen = shipSpan.stream()
                    .map(key -> getState(key))
                    .allMatch(state -> state == OPEN);


            if (isOpen) { // add the ship to the board
                var updatedGame = new Battleship(board);
                shipSurround.forEach(coord -> updatedGame.updateBoard(coord, ADJACENT));
                shipSpan.forEach(coord -> updatedGame.updateBoard(coord, shipType));
                newBoard = Optional.of(updatedGame.board);
            } else {
                throw new IllegalArgumentException("Error! You placed it too close to another one. Try again:");
            }
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
        return newBoard;
    }

    public void play() {
        System.out.println("The game starts!");
        printBoard();
        takeShot();
    }
    private void takeShot() {
        Optional<HashMap<Coordinates, State>> boardWithShot = Optional.empty();
        while (boardWithShot.isEmpty()) {
            boardWithShot = tryTakeShot();
        }
        //successful placement
        board = boardWithShot.get();//gets the value from optional
    }
    private Optional<HashMap<Coordinates, State>>  tryTakeShot() {

        Optional<HashMap<Coordinates,State>> newBoard = Optional.empty();

        System.out.println("Take a shot!");
        try {
            var scanner = new Scanner(System.in);
            var missile = Coordinates.fromString(scanner.next());
            State result = launch(missile);
            var updatedGame = new Battleship(board);
            updatedGame.updateBoard(missile, result);
            newBoard = Optional.of(updatedGame.board);
            updatedGame.printBoard();
            switch (result) {
                case HIT -> System.out.println("You hit a ship!");
                case MISS -> System.out.println("You missed!");
            }
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
        }

        return newBoard;
    }

    private State launch(Coordinates missile) {
        var newState = switch(getState(missile)) {
            case MISS, OPEN, ADJACENT -> MISS;
            case HIT, CARRIER, CRUISER, DESTROYER, SUBMARINE, BATTLESHIP -> HIT;
        };
        return newState;
    }
}

