package FirstWeek.July22.PenAndShapes;

public class Shape {
    protected String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Shape(String color) {
        this.color = color;
    }
    public double draw() {
        return 0;
    }
}
