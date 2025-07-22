package July22.PenAndShapes;

/**
 * usage of overriding with Circle and Rectangle
 */
public class Pen {

    public static final double PI = 3.14;

    public double drawShape(Shape shape) {
        if(shape instanceof Rectangle) {
            Rectangle rect = (Rectangle) shape;
            return rect.draw();
        }else if(shape instanceof Circle) {
            Circle circle = (Circle) shape;
            return circle.draw();
        }
        else return 0;

    }

    public String changeColor(Shape shape,String color) {
        shape.setColor(color);
        return shape.toString();
    }

}
