package FirstWeek.July22Homework.ECommerce;

public class Electronic extends Product {
    private static final int DISCOUNT = 30;
    public Electronic(int id, String name, double price) {
        super(id, name, price , DISCOUNT);
    }
}
