package FirstWeek.July22Homework.AreaCalc;

import java.util.Scanner;

public class Circle extends Shape {
    private static final double PI = 3.14;
    private double radius;

    public Circle(Scanner input) {
        scanner = input;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public String getArea() {
        return radius + "*" + radius + "PI = " +radius*radius*PI;
    }

    @Override
    public void setValues() {
        System.out.println("Radius: ");
        radius = scanner.nextInt();

    }
}
