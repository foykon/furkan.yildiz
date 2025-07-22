package July22Homework.ECommerce;

public class Product {
    protected int id;
    protected String name;
    protected double price;
    protected int discount;

    public Product(int id, String name, double price, int discount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.discount = discount;
    }

    public double calculateDiscountedPrice(){
        return price * (100-discount) / 100;
    }
}
