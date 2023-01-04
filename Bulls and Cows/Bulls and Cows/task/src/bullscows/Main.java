package bullscows;

import java.util.Scanner;

class BullsCows {

    private String code;

    private BullsCows(String code) {
        this.code = code;
    }

    public static BullsCows from(String code) {
        return new BullsCows(code);
    }

    public void nextGuess(String userGuess) {

        int cows = 0;
        int bulls = 0;


        for (int i = 0; i < 4; i++) {
            String currentLetter = userGuess.substring(i,i+1);

            if (isCow(currentLetter, i)) {
//                System.out.println(String.format("Number %s is Cow @ %d", currentLetter, i));
                cows++;
            }
        }

        for (int i = 0; i < 4; i++) {
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
    }

    private boolean isCow(String oneGuess, int position) {
        return this.code.contains(oneGuess) && !isBull(oneGuess, position);
    }

    private boolean isBull(String oneGuess, int position) {
        return this.code.substring(position, position + 1).equals(oneGuess);
    }

}

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String userGuess = scanner.next();

        var game = BullsCows.from("1234");
        game.nextGuess(userGuess);
    }


}
