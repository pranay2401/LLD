package businessModel;

/**
 * Business object to store Flight information.
 */
public class Flight {
    String id;
    String source;
    String destination;
    int departureTime;
    int arrivalTime;

    public Flight(String id, String source, String destination, int departureTime, int arrivalTime) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public String getId() {
        return id;
    }

    public String getSource() {
        return source;
    }


    public String getDestination() {
        return destination;
    }

    public int getDepartureTime() {
        return departureTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }
}