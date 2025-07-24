import July23SelfLearning.LibraryBookManagementSystem.BookManager;
import July23SelfLearning.LibraryBookManagementSystem.LibraryManager;
import July23SelfLearning.LibraryBookManagementSystem.SetValues;
import July23SelfLearning.LibraryBookManagementSystem.UserManager;
import July23SelfLearning.ListSetMap;
import July24.Book;
import July24.LambdaExpression;
import July24.Library;
import July24Homework.SimpleDatabase.DatabaseUI;
import July24Homework.StreamApiExample.FindPrimeNumber;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // System.out.println("Hello World by furkan.yildiz");

        FindPrimeNumber findPrimeNumber = new FindPrimeNumber(50);
        findPrimeNumber.printPrimeNumbers();


        DatabaseUI databaseUI = new DatabaseUI();
        databaseUI.run();




        // LambdaExpression.run();

        /* July23 SelfLearning
        LibraryManager libraryManager = new LibraryManager(
                                                new UserManager(SetValues.setUserValues()),
                                                new BookManager(SetValues.setBookValues()),
                                                new Scanner(System.in));
        libraryManager.runManager();

        ListSetMap listSetMapStackQueue = new ListSetMap();
        listSetMapStackQueue.run();
        */

        /* July23
        OccurrencesOfWord occ = new OccurrencesOfWord();
        occ.run();

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
