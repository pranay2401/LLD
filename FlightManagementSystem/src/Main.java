import activity.FlightActivity;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        String source = "ATQ";
        String destination = "BLR";
//
//        String source = "IXC";
//        String destination = "COK";

//        String source = "IXC";
//        String destination = "GAU";

//        String source = "ATQ";
//        String destination = "COK";

        List<String> result = FlightActivity.getShortestFlights(source, destination);
        System.out.println("Displaying all routes from " + source + " to " + destination);
        for (String string : result) {
            System.out.println(string);
        }
    }
}
