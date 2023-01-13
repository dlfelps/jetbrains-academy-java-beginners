package battleship;

import java.util.Scanner;

import static battleship.State.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        var board = Battleship.empty();


        System.out.println("Player 1, place your ships on the game field");
        board.printTrueState(Player.ONE);
        board.setupBoard(Player.ONE);
        System.out.println("Press Enter and pass the move to another player");
        System.out.print("...");
        scanner.nextLine();

        System.out.println("Player 2, place your ships on the game field");
        board.printTrueState(Player.TWO);
        board.setupBoard(Player.TWO);
        System.out.println("Press Enter and pass the move to another player");
        System.out.print("...");
        scanner.nextLine();

        board.play();

    }
}
