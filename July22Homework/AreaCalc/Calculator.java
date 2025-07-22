package July22Homework.AreaCalc;

import java.util.Scanner;

public class Calculator {
    public Scanner scanner;
    public int input;
    public Shape shape;

    public Calculator(Scanner scanner) {
        this.scanner = scanner;
    }

    public void run(){

        do {
            System.out.println("Pick the shape to calculate");
            System.out.println("1 - Triangle");
            System.out.println("2 - Square");
            System.out.println("3 - Rectangle");
            System.out.println("4 - Circle");
            System.out.println("0 - out");
            input = scanner.nextInt();

            switch (input) {
                case 1:
                    shape = new Triangle(scanner);
                    break;
                case 2:
                    shape = new Square(scanner);
                    break;
                case 3:
                    shape = new Rectangle(scanner);
                    break;
                case 4:
                    shape = new Circle(scanner);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid input");
            }

            shape.setValues();
            System.out.println("Area :" + shape.getArea());

        }while(input != 0);



    }


}
