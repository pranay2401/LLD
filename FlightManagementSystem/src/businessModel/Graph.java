package businessModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Business Object to store flights information departing from a city.
 */
public class Graph {
    private Map<String, List<Flight>> adj;

    public Graph() {
        adj = new HashMap<>();
    }

    private void addFlight(String id, String source, String destination, int departureTime, int arrivalTime) {
        final Flight flight = new Flight(id, source, destination, departureTime, arrivalTime);
        if (!adj.containsKey(source)) {
            adj.put(source, new ArrayList<>());
        }
        adj.get(source).add(flight);
    }

    /**
     * Getter method to access adjacency list of the graph.
     * @return Map of source to list of flights.
     */
    public Map<String, List<Flight>> getAdj() {
        return adj;
    }

    /**
     * Method to create a graph storing information of all flights for a city.
     *
     * @param filePath path of file containing the information of all flights
     * @return Graph object.
     */
    public static Graph initializeGraph(String filePath) {
        final Graph graph = new Graph();

        String line = "";
        final String splitBy = ",";
        try {
            final BufferedReader br = new BufferedReader(new FileReader(filePath));
            while ((line = br.readLine()) != null)
            {
                final String[] flight = line.split(splitBy);
                graph.addFlight(flight[0], flight[1], flight[2], Integer.parseInt(flight[3]), Integer.parseInt(flight[4]));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return graph;
    }
}
