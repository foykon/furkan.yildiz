package July22Homework.ECommerce;

import July22Homework.AreaCalc.Circle;
import July22Homework.AreaCalc.Rectangle;
import July22Homework.AreaCalc.Square;
import July22Homework.AreaCalc.Triangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ShoppingCart {
    public Scanner scanner;
    public int input;
    public double totalPrice = 0;
    public Product[] products;
    public List<Product> chart;

    public ShoppingCart(Scanner scanner) {
        this.scanner = scanner;
        chart = new ArrayList<>();
    }

    private void setProducts(){
        products = new Product[]{
                new Electronic(1, "Telephone", 10000),
                new Electronic(2, "Laptop",  30000),
                new Electronic(3, "TV",  20000),
                new Clothing(4, "T-shirt", 1500),
                new Clothing(5, "Pants", 1000),
                new Clothing(6, "Dress", 2000),
                new Grocery(7, "Cucumber", 50),
                new Grocery(8, "Tomato", 40),
                new Grocery(9, "Potato",  30),
        };
    }

    public void shop(){

        setProducts();

        while(true) {
            System.out.println("Please enter the product you wish to add: ");
            System.out.println("1 - " + products[0].name);
            System.out.println("2 - " + products[1].name);
            System.out.println("3 - " + products[2].name);
            System.out.println("4 - " + products[3].name);
            System.out.println("5 - " + products[4].name);
            System.out.println("6 - " + products[5].name);
            System.out.println("7 - " + products[6].name);
            System.out.println("8 - " + products[7].name);
            System.out.println("9 - " + products[8].name);
            System.out.println("0 - End Shopping!" );
            input = scanner.nextInt();

            if(input == 0) { break; }

            chart.add(products[input]);

        }
        System.out.println("Discounted price : " + calculateTotalPrice());
    }

    public double calculateTotalPrice(){
        for(Product p : chart) {
            System.out.println("Name: " + p.name + "-/Price: " + p.price + "-/DiscountedPrice" + p.calculateDiscountedPrice());
            totalPrice += p.calculateDiscountedPrice();
        }

        return totalPrice;
    }
}
