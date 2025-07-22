package July22Homework.ECommerce;

public class Grocery extends Product {

    private static final int DISCOUNT = 20;
    public Grocery(int id, String name, double price) {
        super(id, name, price, DISCOUNT);
    }
}
