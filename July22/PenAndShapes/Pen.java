package July22.PenAndShapes;

/**
 * usage of overriding with Circle and Rectangle
 */
public class Pen {

    public static final double PI = 3.14;

    public double draw(Rectangle rect){
        return rect.getHeight()*rect.getWidth();
    }

    public double draw(Circle circle){
        return circle.getRadius() * circle.getRadius() * PI;
    }

    public String changeColor(Rectangle rect, String color){
         rect.setColor(color);
         return rect.getColor();
    }

    public String changeColor(Circle circ, String color){
        circ.setColor(color);
        return circ.getColor();
    }
}
