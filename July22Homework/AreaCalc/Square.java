package July22Homework.AreaCalc;

import java.util.Scanner;

public class Square extends Shape {
    private double edge;

    public Square(Scanner input) {
        scanner = input;
    }

    public double getEdge() {
        return edge;
    }

    public void setEdge(double edge) {
        this.edge = edge;
    }

    @Override
    public String getArea() {
        return edge + "*" + edge + " = " + (edge * edge);
    }

    @Override
    public void setValues() {
        System.out.println("Edge: ");
        edge = scanner.nextInt();
    }
}
