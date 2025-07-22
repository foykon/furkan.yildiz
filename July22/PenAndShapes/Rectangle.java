package July22.PenAndShapes;

public class Rectangle extends Shape {
    private int width;
    private int height;

    public String getColor() {
        return color;
    }

    public Rectangle(int width, int height, String color) {
        super(color);
        this.width = width;
        this.height = height;
    }

    @Override
    public double draw() {
        System.out.println("Drawing Rectangle");
        return width * height;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
