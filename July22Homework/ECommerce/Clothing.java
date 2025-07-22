package July22Homework.ECommerce;

public class Clothing extends Product{
    private static final int DISCOUNT = 25;
    public Clothing(int id, String name, double price) {
        super(id, name, price, DISCOUNT);
    }
}
