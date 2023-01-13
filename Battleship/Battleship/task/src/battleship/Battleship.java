package battleship;


import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static battleship.Player.*;
import static battleship.State.*;

enum Player {
    ONE, TWO;
}

public class Battleship {

    private HashMap<Coordinates,State> player1;
    private HashMap<Coordinates,State> player2;
    private Player turn;

    private Battleship(Map<Coordinates,State> initalPlayer1, Map<Coordinates,State> initalPlayer2){
        player1 = new HashMap(100);
        player1.putAll(initalPlayer1);
        player2 = new HashMap(100);
        player2.putAll(initalPlayer2);
        turn = ONE;
    }

    public static Battleship empty() {
        var emptyBoard = new Battleship(new HashMap<Coordinates,State>(), new HashMap<Coordinates,State>());
        for (int row = 1; row < 11; row++) {
            for (int col = 1; col < 11; col++) {
                emptyBoard.updateBoard(ONE, new Coordinates(col, row), OPEN);
                emptyBoard.updateBoard(TWO, new Coordinates(col, row), OPEN);
            }
        }
        return emptyBoard;
    }

    public void updateBoard(Player turn, Coordinates key, State value){
        switch (turn) {
            case ONE -> player1.put(key, value);
            case TWO -> player2.put(key, value);
        }

    }

    public State getState(Player turn, Coordinates c){
        return switch (turn){
            case ONE -> player1.getOrDefault(c, OPEN);
            case TWO -> player2.getOrDefault(c, OPEN);
        };
    }

    public void printTrueState(Player turn) {
        Consumer<State> printTrue = state -> {
            switch (state) {
                case OPEN, ADJACENT -> System.out.print(" ~");
                case BATTLESHIP, CARRIER, CRUISER, DESTROYER, SUBMARINE  -> System.out.print(" O");
                case HIT -> System.out.print(" X");
                case MISS -> System.out.print(" M");
            }
        };

        printBoard(turn, printTrue);
    }

    public void printFoggyState(Player turn) {
        Consumer<State> printFoggy = state -> {
            switch (state) {
                case OPEN, ADJACENT -> System.out.print(" ~");
                case BATTLESHIP, CARRIER, CRUISER, DESTROYER, SUBMARINE  -> System.out.print(" ~");
                case HIT -> System.out.print(" X");
                case MISS -> System.out.print(" M");
            }
        };

        printBoard(turn, printFoggy);
    }

    public void printHiddenState(Player turn) {

        Consumer<State> printHidden = state -> {
            switch (state) {
                case OPEN, ADJACENT -> System.out.print(" ~");
                case BATTLESHIP, CARRIER, CRUISER, DESTROYER, SUBMARINE  -> System.out.print(" ~");
                case HIT -> System.out.print(" ~");
                case MISS -> System.out.print(" ~");
            }
        };

        printBoard(turn, printHidden);
    }

    public void printBoard(Player turn, Consumer<State> printFunction) {
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
                var state = getState(turn, new Coordinates(col, row));
                printFunction.accept(state);
            }
            System.out.println();
        }

    }

    public void setupBoard(Player turn) {

        addShip(turn, CARRIER,5);
        addShip(turn, BATTLESHIP,4);
        addShip(turn, SUBMARINE,3);
        addShip(turn, CRUISER,3);
        addShip(turn, DESTROYER,2);
    }

    private void addShip(Player turn, State ship, int shipLength) {
        Optional<HashMap<Coordinates, State>> boardWithShip = Optional.empty();
        while (boardWithShip.isEmpty()) {
            boardWithShip = tryPlaceShip(turn, ship, shipLength);
        }
        //successful placement
        switch (turn) {
            case ONE -> player1 = boardWithShip.get();
            case TWO -> player2 = boardWithShip.get();
        }
        printTrueState(turn);
    }

    private Optional<HashMap<Coordinates,State>> tryPlaceShip(Player turn, State shipType, int shipLength) {
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
                    .map(key -> getState(turn, key))
                    .allMatch(state -> state == OPEN);


            if (isOpen) { // add the ship to the board
                var updatedGame = new Battleship(player1, player2);
                shipSurround.forEach(coord -> updatedGame.updateBoard(turn, coord, ADJACENT));
                shipSpan.forEach(coord -> updatedGame.updateBoard(turn, coord, shipType));
                newBoard = switch (turn) {
                    case ONE -> Optional.of(updatedGame.player1);
                    case TWO -> Optional.of(updatedGame.player2);
                };
            } else {
                throw new IllegalArgumentException("Error! You placed it too close to another one. Try again:");
            }
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
        return newBoard;
    }

    public void play() {

        Scanner scanner = new Scanner(System.in);
        while (true){
            printFullBoard(ONE);
            System.out.println("Player 1, it's your turn:");
            takeShot(TWO);
            if (checkSunk(TWO)){
                break;
            }

            System.out.println("Press Enter and pass the move to another player");
            System.out.print("...");
            scanner.nextLine();

            printFullBoard(TWO);
            System.out.println("Player 2, it's your turn:");
            takeShot(ONE);
            if (checkSunk(ONE)){
                break;
            }

            System.out.println("Press Enter and pass the move to another player");
            System.out.print("...");
            scanner.nextLine();

        }

        System.out.println("You sank the last ship. You won. Congratulations!");
    }

    private void printFullBoard(Player turn) {
        printHiddenState(turn);
        System.out.println("---------------------");
        printTrueState(turn);
    }

    private boolean checkSunk(Player turn) {
        Predicate<State> notCarrier = Predicate.not(Predicate.isEqual(CARRIER));
        Predicate<State> notBattleship = Predicate.not(Predicate.isEqual(BATTLESHIP));
        Predicate<State> notSubmarine = Predicate.not(Predicate.isEqual(SUBMARINE));
        Predicate<State> notCruiser = Predicate.not(Predicate.isEqual(CRUISER));
        Predicate<State> notDestroyer = Predicate.not(Predicate.isEqual(DESTROYER));
        var board = switch (turn){
            case ONE -> player1;
            case TWO -> player2;
        };
        return board.values().stream().allMatch(notCarrier
                .and(notBattleship)
                .and(notSubmarine)
                .and(notCruiser)
                .and(notDestroyer));
    }

    private void takeShot(Player turn) {
        Optional<HashMap<Coordinates, State>> boardWithShot = Optional.empty();
        while (boardWithShot.isEmpty()) {
            boardWithShot = tryTakeShot(turn);
        }
        //successful placement
        switch (turn) {
            case ONE -> player1 = boardWithShot.get();
            case TWO -> player2 = boardWithShot.get();
        }
    }
    private Optional<HashMap<Coordinates, State>>  tryTakeShot(Player turn) {

        Optional<HashMap<Coordinates,State>> newBoard = Optional.empty();

        try {
            var scanner = new Scanner(System.in);
            var missile = Coordinates.fromString(scanner.next());
            State result = launch(turn, missile);
            var updatedGame = new Battleship(player1, player2);
            updatedGame.updateBoard(turn, missile, result);
            newBoard = switch (turn) {
                case ONE -> Optional.of(updatedGame.player1);
                case TWO -> Optional.of(updatedGame.player2);
            };
            switch (result) {
                case HIT -> System.out.println("You hit a ship!");
                case MISS -> System.out.println("You missed!");
            }
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
        }

        return newBoard;
    }

    private State launch(Player turn, Coordinates missile) {
        var newState = switch(getState(turn, missile)) {
            case MISS, OPEN, ADJACENT -> MISS;
            case HIT, CARRIER, CRUISER, DESTROYER, SUBMARINE, BATTLESHIP -> HIT;
        };
        return newState;
    }
}

