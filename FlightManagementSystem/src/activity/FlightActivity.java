package activity;

import businessModel.Flight;
import businessModel.Graph;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.ItineraryUtil;
import java.util.*;

/**
 * Activity class to access data for flights.
 */
public class FlightActivity {

    private static Graph graph = Graph.initializeGraph(Constants.FILE_URL);

    /**
     * @param source
     * @param destination
     * @return
     */
    public static List<String> getShortestFlights(@NotNull String source, @NotNull String destination) {
        final List<List<Flight>> paths = ItineraryUtil.findRoutes(graph, source, destination);
        return ItineraryUtil.getShortestRoutes(paths);
    }
}
