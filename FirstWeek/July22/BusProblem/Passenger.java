package FirstWeek.July22.BusProblem;

public class Passenger {
    private String passengerName;
    private Destination destination;

    public Passenger(String passengerName, Destination destination) {
        this.passengerName = passengerName;
        this.destination = destination;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }
}
