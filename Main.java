import July22.BusProblem.Bus;
import July22.BusProblem.Destination;
import July22.BusProblem.Passenger;
import July22.PenAndShapes.Circle;
import July22.PenAndShapes.Pen;
import July22.PenAndShapes.Rectangle;
import July22Homework.AreaCalc.Calculator;
import July22Homework.ECommerce.ShoppingCart;
import July23.OccurrencesOfWord;
import July23.School.Course;
import July23.School.Student;
import July23.StringExample;

import java.io.*;
import java.util.Calendar;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // System.out.println("Hello World by furkan.yildiz");


        OccurrencesOfWord occ = new OccurrencesOfWord();
        occ.run();

        /* July23
        System.out.println(StringExample.sameFrequency("This is not not", "is" ,"not"));

        Course course = new Course(1, "Java Ignite", "Basics Of Java");
        Student student = new Student(1,"Furkan" , 22, course);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("student.txt"))) {
            oos.writeObject(student);
            System.out.println("Nesne başarıyla dosyaya yazıldı.");
        } catch (IOException e) {
            System.out.println("Dosya hatası.");
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("student.txt"))) {
            Student readStudent = (Student) ois.readObject();
            System.out.println("Dosyadan okunan nesne: " + readStudent.getCourse().getName());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Dosya hatası.");
        }
         */

        /* july22 homework
        Calculator calculator = new Calculator(new Scanner(System.in));
        calculator.run();
        ShoppingCart cart = new ShoppingCart(new Scanner(System.in));
        cart.shop();
        */

        /* july22 bus problem
        Bus bus1 = new Bus();
        bus1.setDestination(Destination.ANKARA);
        bus1.setMaxCapacity(1);
        bus1.insertPassenger(new Passenger("Ali",Destination.ANKARA));
        bus1.insertPassenger(new Passenger("Murat",Destination.ISTANBUL));
        bus1.insertPassenger(new Passenger("Furkan",Destination.ANKARA));
         */

        /* july22
        System.out.println(Arrays.toString(July22Questions.reverseArray(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10})));
        Circle circle = new Circle(3,"blue");
        Rectangle rectangle = new Rectangle(3,3, "yellow");
        Pen pen = new Pen();
        System.out.println(pen.drawShape(circle));
        System.out.println(pen.drawShape(rectangle));
         */

        /* july 21
        July21Questions.playGuessingGame(0, 100);
        System.out.println(July21Questions.gradeCalculator());
         */

    }
}
