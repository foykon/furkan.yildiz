package FirstWeek.July22.PenAndShapes;

public class Circle extends Shape {
    private static final double PI = 3.14;
    private double radius;


    public double getRadius() {
        return radius;
    }

    public Circle(double radius, String color) {
        super(color);
        this.radius = radius;
        this.color = color;
    }

    @Override
    public double draw() {
        System.out.println("Drawing Circle");
        return radius*radius*PI;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
