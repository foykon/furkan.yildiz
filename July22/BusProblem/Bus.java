package July22.BusProblem;

public class Bus {
    private Passenger[] passengers;
    private Destination destination;

    public Bus(Passenger[] passengers, Destination destination) {
        this.passengers = passengers;
        this.destination = destination;
    }

    public Bus() {
        this.passengers = new Passenger[0];
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
            throw new IllegalArgumentException("Passenger cannot be null");
        }

        if (passenger.getDestination() == destination ) {
            Passenger[] newPassengers = new Passenger[passengers.length + 1];
            System.arraycopy(passengers, 0, newPassengers, 0, passengers.length);
            newPassengers[newPassengers.length - 1] = passenger;
            passengers = newPassengers;

            System.out.println("Passenger inserted.");
        }

        else System.out.println("destination do not match");

    }
}
