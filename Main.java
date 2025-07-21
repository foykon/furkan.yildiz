import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World");

        playGuessingGame(0, 100);
    }
    public static void playGuessingGame(int min, int max) {
        Scanner in = new Scanner(System.in);
        int randomNumber = (int)(Math.random() * (max - min + 1)) + min;
        int userGuess = 0;

        System.out.println("U ve 5 guess!");

        for (int i = 1; i <= 5; i++) {
            while (!in.hasNextInt()) {
                System.out.print("Incorrect value type");
                in.next();
            }
            userGuess = in.nextInt();

            if (userGuess == randomNumber) {
                System.out.println("Congrats. Correct." + randomNumber);
                return;
            } else if (userGuess < randomNumber) {
                System.out.println("Higher.");
            } else {
                System.out.println("Lower.");
            }
        }

        System.out.println("It was.:" + randomNumber);
    }

}
