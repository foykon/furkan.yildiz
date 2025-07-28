package FirstWeek.July22.BusProblem;

public class Bus {
    private Passenger[] passengers;
    private Destination destination;
    private int maxCapacity;

    public Bus(Passenger[] passengers, Destination destination, int maxCapacity) {
        this.passengers = passengers;
        this.destination = destination;
        this.maxCapacity = maxCapacity;
    }

    public Bus() {
        this.passengers = new Passenger[0];
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Passenger[] getPassengers() {
        return passengers;
    }

    public void setPassengers(Passenger[] passengers) {
        this.passengers = passengers;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public void insertPassenger(Passenger passenger) {
        if(passenger == null) {
            System.out.println("Passenger is null");
        }

        if (passenger.getDestination().equals(destination)) {
            if(passengers.length == maxCapacity) {
                System.out.println("Bus is already full");
                return;
            }
            Passenger[] newPassengers = new Passenger[passengers.length + 1];
            System.arraycopy(passengers, 0, newPassengers, 0, passengers.length);
            newPassengers[newPassengers.length - 1] = passenger;
            passengers = newPassengers;

            System.out.println("Passenger inserted.");
        }else {
            System.out.println("destination do not match");
        }

    }
}
