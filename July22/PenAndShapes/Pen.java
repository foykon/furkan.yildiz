package July22.PenAndShapes;

/**
 * usage of overriding with Circle and Rectangle
 */
public class Pen {

    public static final double PI = 3.14;

    public double drawShape(Shape shape) {
        return shape.draw();

    }

    public String changeColor(Shape shape,String color) {
        shape.setColor(color);
        return shape.toString();
    }

}
