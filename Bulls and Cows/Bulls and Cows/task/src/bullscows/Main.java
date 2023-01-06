package bullscows;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.List;

class GenerateCode {

    String fullCode;

    private GenerateCode() {
        var candidates = new ArrayList<String>();
        candidates.addAll(List.of("1","2","3","4","5", "6", "7", "8", "9"));

        var s = new StringBuilder();

        // get first nonzero
        int rnd = new Random().nextInt(candidates.size());
        s.append(candidates.get(rnd));
        candidates.remove(rnd);

        // add 0 back to candidate pool for remaining choices
        candidates.add("0");

        //get the remaining digits
        for (int i = 0; i < 9; i++) {
            rnd = new Random().nextInt(candidates.size() );
            s.append(candidates.get(rnd));
            candidates.remove(rnd);
        }

        fullCode = s.toString();
    }

    public static String withLength(int codeLength) {
        var it = new GenerateCode();
        return it.fullCode.substring(0,codeLength);
    }

}

class BullsCows {

    private String code;

    private BullsCows(String code) {
        this.code = code;
    }

    public static BullsCows from(String code) {
        return new BullsCows(code);
    }

    public boolean nextGuess(String userGuess) {

        int cows = 0;
        int bulls = 0;


        for (int i = 0; i < userGuess.length(); i++) {
            String currentLetter = userGuess.substring(i,i+1);

            if (isCow(currentLetter, i)) {
//                System.out.println(String.format("Number %s is Cow @ %d", currentLetter, i));
                cows++;
            }
        }

        for (int i = 0; i < userGuess.length(); i++) {
            String currentLetter = userGuess.substring(i,i+1);

            if (isBull(currentLetter, i)) {
//                System.out.println(String.format("Number %s is Bull @ %d", currentLetter, i));
                bulls++;
            }
        }

        if ((cows + bulls) == 0) {
            System.out.println(String.format("Grade: None. The secret code is %s", this.code));
        } else if (bulls == 0 ){
            System.out.println(String.format("Grade: %d cow(s). The secret code is %s", cows, this.code));
        } else if (cows == 0 ){
            System.out.println(String.format("Grade: %d bulls(s). The secret code is %s", bulls, this.code));
        } else {
            System.out.println(String.format("Grade: %d bull(s) and %d cow(s). The secret code is %s", bulls, cows, this.code));
        }

        return userGuess.equals(this.code);
    }

    private boolean isCow(String oneGuess, int position) {
        return this.code.contains(oneGuess) && !isBull(oneGuess, position);
    }

    private boolean isBull(String oneGuess, int position) {
        return this.code.substring(position, position + 1).equals(oneGuess);
    }

    public boolean playRound() {
        Scanner scanner = new Scanner(System.in);
        var userGuess = scanner.next();
        return nextGuess(userGuess);
    }
}

public class Main {
    public static void main(String[] args) {

        System.out.println("Please enter the secret code's length:");

        Scanner scanner = new Scanner(System.in);
        int codeLength = scanner.nextInt();

        if (codeLength < 11) {
            var game = BullsCows.from(GenerateCode.withLength(codeLength));

            boolean isGuessCorrect = false;
            int round = 1;

            while (!isGuessCorrect) {
                System.out.println(String.format("Turn %d",round));
                isGuessCorrect = game.playRound();
                round++;
            }

            System.out.println("Congratulations! You guessed the secret code.");
        } else {
            System.out.println(String.format("Error: can't generate a secret number with a length of %d because there aren't enough unique digits.", codeLength));
        }

    }


}
