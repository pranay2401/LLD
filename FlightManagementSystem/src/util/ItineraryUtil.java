package util;

import businessModel.Flight;
import businessModel.Graph;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Util class to store all operations on an Route.
 */
public class ItineraryUtil {

    /**
     * Method to find the shortest paths based on the travel time.
     * @param routes - List of routes from one city to another.
     * @return List of 5 shortest routes in the format {city1_city2_city3: {Flight1_Flight2: <travelTime in minutes></travelTime>}}
     */
    public static List<String> getShortestRoutes(List<List<Flight>> routes) {
        final PriorityQueue<List<Flight>> pq = new PriorityQueue<>(5, (p1, p2) -> comparePaths(p1, p2));
        for (List<Flight> path : routes) {
            pq.offer(path);
        }

        final List<List<Flight>> res = new ArrayList<>();
        final Stack<List<Flight>> stack = new Stack<>();
        while(!pq.isEmpty()) {
            stack.push(pq.poll());
        }
        while(!stack.isEmpty() && res.size() < 5) {
            res.add(stack.pop());
        }
        return ItineraryUtil.getPathsList(res);
    }

    /**
     * Method to compute all routes of length less than 5 from source to destination.
     * @param sourceToFlightsGraph graph containing information of all flights from a given source.
     * @param source Source city
     * @param destination Destination city
     * @return Routes from source to destination represented as List of List of Flights.
     */
    public static List<List<Flight>> findRoutes(@NotNull Graph sourceToFlightsGraph,
                                                @NotNull String source,
                                                @NotNull String destination) {
        final List<List<Flight>> resultPaths = new ArrayList<>();
        final Set<String> visited = new HashSet<>();
        dfs(sourceToFlightsGraph, source, destination, visited, new ArrayList<>(), resultPaths, 0);
        return resultPaths;
    }

    // Method to compute the total travel time from departure of first flight to arrival of last flight
    private static int calculateTotalTravelTime(@NotNull List<Flight> route) {
        if (route.isEmpty()){
            return 0;
        }

        route.sort(Comparator.comparingInt(f -> f.getDepartureTime()));
        final Flight firstFlight = route.get(0);
        final Flight lastFlight = route.get(route.size()-1);
        int routeStartTime = firstFlight.getDepartureTime();
        int routeEndTime = lastFlight.getArrivalTime();

        if (routeEndTime < routeStartTime) {
            // if departure is 1500 and arrival is 0115 next day
            routeEndTime += 2400;
        }
        int hourDiff = (routeEndTime / 100) - (routeStartTime / 100 )- 1;
        int minDiff = (routeEndTime % 100) + (60 - routeStartTime % 100);
        if (minDiff >= 60) {
            hourDiff++;
            minDiff = minDiff - 60;
        }
        return hourDiff*60 + minDiff;
    }

    // Handle given constraints to select a flight only if the departure time of current flight
    // is at least 2 hours later than the arrival time of previous flight
    private static boolean isNextFlightValid(@NotNull List<Flight> route, @NotNull Flight nextFlight) {
        if (route.isEmpty()) {
            return true;
        }
        final Flight lastFlight = route.get(route.size() - 1);
        return nextFlight.getDepartureTime() > lastFlight.getArrivalTime() + 200;
    }

    private static List<String> getPathsList(@NotNull List<List<Flight>> paths) {
        final List<String> result = new ArrayList<>();

        for (List<Flight> path : paths) {
            StringBuilder city = new StringBuilder("");
            StringBuilder id = new StringBuilder("");
            int totalTime = ItineraryUtil.calculateTotalTravelTime(path);

            for (int index=0; index<path.size(); index++) {
                Flight flight = path.get(index);
                if (index==0) {
                    city.append(flight.getSource());
                    id.append(flight.getId());
                }

                city.append("_" + flight.getDestination());
                if (index > 0) id.append("_" + flight.getId());
            }
            StringBuilder sb = new StringBuilder("{");
            sb.append("'" + city + "': {'" + id + "': " + totalTime + "}");
            sb.append("}");
            result.add(sb.toString());
        }
        return result;
    }

    private static void dfs(@NotNull Graph graph,
                            @NotNull String current,
                            @NotNull String destination,
                            @NotNull Set<String> visited,
                            @NotNull List<Flight> route,
                            @NotNull List<List<Flight>> allRoutes,
                            int depth) {
        visited.add(current);
        final Map<String, List<Flight>> adj = graph.getAdj();
        if (current.equals(destination) && route.size() <5) {
            allRoutes.add(new ArrayList<>(route));
            visited.remove(current);
            return;
        }

        if (depth >=5) {
            visited.remove(current);
            return;
        }

        if (!adj.containsKey(current)) {
            visited.remove(current);
            return;
        }

        for (Flight nextFlight : adj.get(current)) {
            if (!visited.contains(nextFlight.getDestination()) && ItineraryUtil.isNextFlightValid(route, nextFlight)) {
                route.add(nextFlight);
                dfs(graph, nextFlight.getDestination(), destination, visited, route, allRoutes, depth + 1);
                route.remove(route.size() - 1);
            }
        }

        visited.remove(current);
    }

    private static int comparePaths(@NotNull List<Flight> path1, @NotNull List<Flight> path2) {
        if (path1.size() != path2.size()) {
            return path2.size()-path1.size();
        }
        return ItineraryUtil.calculateTotalTravelTime(path2)- ItineraryUtil.calculateTotalTravelTime(path1);
    }
}
