package July21;

import java.util.Scanner;

public class July21Questions {

    /*
        The user is given five chances to guess the number the computer picks using clues.
     */
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

    /*
        A function that takes the average of the given numbers and stops when 101 is entered.
     */
    public static double gradeCalculator(){
        Scanner in = new Scanner(System.in);

        int gradeCount = 0;
        double grade = 0;
        while(true){
            int input = in.nextInt();
            if(input == 101){
                break;
            }
            else if(input >= 0 && input <= 100){
                gradeCount++;
                grade += input;


            }else {
                System.out.println("Wrong input. Try a number between 0 and 100");
            }
        }
        return grade/gradeCount;
    }

}
