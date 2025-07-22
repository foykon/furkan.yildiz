import July22.BusProblem.Bus;
import July22.BusProblem.Destination;
import July22.BusProblem.Passenger;

public class Main {
    public static void main(String[] args) {

        // System.out.println("Hello World by furkan.yildiz");


        Bus bus1 = new Bus();
        bus1.setDestination(Destination.ANKARA);

        bus1.insertPassenger(new Passenger("Ali",Destination.ANKARA));
        bus1.insertPassenger(new Passenger("Murat",Destination.ISTANBUL));
        bus1.insertPassenger(new Passenger("Furkan",Destination.ANKARA));







        /* july22
        System.out.println(Arrays.toString(July22Questions.reverseArray(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10})));
        Circle circle = new Circle(3,"blue");
        Rectangle rectangle = new Rectangle(3,3, "yellow");
        Pen pen = new Pen();
        */

        /* july 21
        July21Questions.playGuessingGame(0, 100);
        System.out.println(July21Questions.gradeCalculator());
        */

    }
}
