package bullscows;

import java.util.*;

class GenerateCode {

    String fullCode;

    private GenerateCode(int codeLength, int numSymbols) {

        if ((codeLength < 1) | (numSymbols < 1)){
            throw new IllegalArgumentException();
        }

        if ((codeLength > 36) | (numSymbols > 36)) {
            throw new IllegalArgumentException();
        }

        if (codeLength > numSymbols) {
            throw new IllegalArgumentException();
        }

        String s = "123456789abcdefghijklmnopqrstuvwxyz".substring(0,numSymbols-1);
        if (numSymbols < 11) {
            System.out.println(String.format("The secret is prepared: %s (0-%s).", "*".repeat(codeLength), s.subSequence(s.length()-1, s.length())));
        } else {
            System.out.println(String.format("The secret is prepared: %s (0-9, a-%s).", "*".repeat(codeLength), s.subSequence(s.length()-1, s.length())));
        }

        var temp = Arrays.stream(s.split("")).toList();
        var candidates = new ArrayList<String>();
        candidates.addAll(temp);

        var sb = new StringBuilder();

        // get first nonzero
        int rnd = new Random().nextInt(candidates.size());
        sb.append(candidates.get(rnd));
        candidates.remove(rnd);

        // add 0 back to candidate pool for remaining choices
        candidates.add("0");

        //get the remaining digits
        for (int i = 1; i < numSymbols; i++) {
            rnd = new Random().nextInt(candidates.size() );
            sb.append(candidates.get(rnd));
            candidates.remove(rnd);
        }

        fullCode = sb.toString();
    }

    public static String using(int codeLength, int numSymbols) {

        GenerateCode it = null;
        String codeString = "";

        try {
            it = new GenerateCode(codeLength, numSymbols);
            codeString = it.fullCode.substring(0,codeLength);
        } catch (IllegalArgumentException e) {
            if (codeLength > 36) {
                System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            } else {
                System.out.println(String.format("Error: it's not possible to generate a code with a length of %d with %d unique symbols.", codeLength, numSymbols));
            }
            System.exit(0);
        }

        return codeString;
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

        int codeLength = 0;
        int numSymbols = 0;

        Scanner scanner = new Scanner(System.in);
        String userInput = "";
        try {
            System.out.println("Input the length of the secret code:");
            userInput = scanner.next();
            codeLength = Integer.parseInt(userInput);
        } catch (NumberFormatException e){
            System.out.println(String.format("Error: \"%s\" isn't a valid number.", userInput ));
            System.exit(0);
        }

        try {
            System.out.println("Input the number of possible symbols in the code:");
            userInput = scanner.next();
            numSymbols = Integer.parseInt(userInput);
        } catch (NumberFormatException e){
            System.out.println(String.format("Error: \"%s\" isn't a valid number.", userInput ));
            System.exit(0);
        }


        var game = BullsCows.from(GenerateCode.using(codeLength, numSymbols));

        System.out.println("Okay, let's start a game!");
        boolean isGuessCorrect = false;
        int round = 1;

        while (!isGuessCorrect) {
            System.out.println(String.format("Turn %d",round));
            isGuessCorrect = game.playRound();
            round++;
        }

        System.out.println("Congratulations! You guessed the secret code.");


    }


}
