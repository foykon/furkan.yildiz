package FirstWeek.July22Homework.AreaCalc;

import java.util.Scanner;

public class Triangle extends Shape {
    private int baseSide;
    private int height;

    public Triangle(Scanner input) {
        scanner = input;
    }

    public Triangle(int baseSide, int height) {
        this.baseSide = baseSide;
        this.height = height;
    }

    public int getBaseSide() {
        return baseSide;
    }

    public void setBaseSide(int baseSide) {
        this.baseSide = baseSide;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String getArea() {
        return baseSide + "*" + height + "/2 = " + (double)(baseSide * height) / 2;
    }

    @Override
    public void setValues() {
        System.out.println("Base Side: ");
        baseSide = scanner.nextInt();

        System.out.println("Height: ");
        height = scanner.nextInt();
    }
}
