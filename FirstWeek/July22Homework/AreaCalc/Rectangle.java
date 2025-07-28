package FirstWeek.July22Homework.AreaCalc;

import java.util.Scanner;

public class Rectangle extends Shape {
    private double width;
    private double height;


    public Rectangle(Scanner input) {
        scanner = input;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public String getArea() {
        return width + "*" + height + " = " + (width * height);
    }

    @Override
    public void setValues() {
        System.out.println("Width: ");
        width = scanner.nextInt();

        System.out.println("Height: ");
        height = scanner.nextInt();

    }
}
