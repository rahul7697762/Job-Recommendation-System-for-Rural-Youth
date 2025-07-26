package com.ruralyouth.dsa;

import java.util.*;

/**
 * Graph data structure for representing locations and finding nearby jobs
 * Uses adjacency list representation with BFS and Dijkstra's algorithm
 */
public class LocationGraph {
    private Map<String, LocationNode> locations;
    private Map<String, List<Edge>> adjacencyList;

    public LocationGraph() {
        this.locations = new HashMap<>();
        this.adjacencyList = new HashMap<>();
    }

    /**
     * Add a location to the graph
     */
    public void addLocation(String locationName, double latitude, double longitude) {
        LocationNode node = new LocationNode(locationName, latitude, longitude);
        locations.put(locationName, node);
        adjacencyList.putIfAbsent(locationName, new ArrayList<>());
    }

    /**
     * Add a road/connection between two locations
     */
    public void addRoad(String from, String to, double distance) {
        if (!locations.containsKey(from) || !locations.containsKey(to)) {
            throw new IllegalArgumentException("Location not found in graph");
        }
        
        Edge edge = new Edge(from, to, distance);
        adjacencyList.get(from).add(edge);
        
        // Add reverse edge for undirected graph
        Edge reverseEdge = new Edge(to, from, distance);
        adjacencyList.get(to).add(reverseEdge);
    }

    /**
     * Find nearby locations using BFS
     * Time Complexity: O(V + E) where V is vertices, E is edges
     */
    public List<String> findNearbyLocationsBFS(String startLocation, double maxDistance) {
        List<String> nearbyLocations = new ArrayList<>();
        
        if (!locations.containsKey(startLocation)) {
            return nearbyLocations;
        }

        Queue<BFSNode> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        
        queue.offer(new BFSNode(startLocation, 0.0));
        visited.add(startLocation);

        while (!queue.isEmpty()) {
            BFSNode current = queue.poll();
            
            if (current.distance <= maxDistance) {
                nearbyLocations.add(current.location);
            }

            for (Edge edge : adjacencyList.get(current.location)) {
                String neighbor = edge.to;
                double newDistance = current.distance + edge.distance;
                
                if (!visited.contains(neighbor) && newDistance <= maxDistance) {
                    visited.add(neighbor);
                    queue.offer(new BFSNode(neighbor, newDistance));
                }
            }
        }

        return nearbyLocations;
    }

    /**
     * Find shortest path to all locations using Dijkstra's algorithm
     * Time Complexity: O((V + E) log V) with priority queue
     */
    public Map<String, Double> findShortestDistances(String startLocation) {
        Map<String, Double> distances = new HashMap<>();
        PriorityQueue<DijkstraNode> pq = new PriorityQueue<>();
        Set<String> visited = new HashSet<>();

        // Initialize distances
        for (String location : locations.keySet()) {
            distances.put(location, Double.MAX_VALUE);
        }
        distances.put(startLocation, 0.0);

        pq.offer(new DijkstraNode(startLocation, 0.0));

        while (!pq.isEmpty()) {
            DijkstraNode current = pq.poll();
            
            if (visited.contains(current.location)) {
                continue;
            }
            
            visited.add(current.location);

            for (Edge edge : adjacencyList.get(current.location)) {
                String neighbor = edge.to;
                double newDistance = current.distance + edge.distance;
                
                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    pq.offer(new DijkstraNode(neighbor, newDistance));
                }
            }
        }

        return distances;
    }

    /**
     * Find shortest path between two locations
     */
    public List<String> findShortestPath(String start, String end) {
        if (!locations.containsKey(start) || !locations.containsKey(end)) {
            return new ArrayList<>();
        }

        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<DijkstraNode> pq = new PriorityQueue<>();
        Set<String> visited = new HashSet<>();

        // Initialize
        for (String location : locations.keySet()) {
            distances.put(location, Double.MAX_VALUE);
        }
        distances.put(start, 0.0);
        pq.offer(new DijkstraNode(start, 0.0));

        while (!pq.isEmpty()) {
            DijkstraNode current = pq.poll();
            
            if (current.location.equals(end)) {
                break;
            }
            
            if (visited.contains(current.location)) {
                continue;
            }
            
            visited.add(current.location);

            for (Edge edge : adjacencyList.get(current.location)) {
                String neighbor = edge.to;
                double newDistance = current.distance + edge.distance;
                
                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    previous.put(neighbor, current.location);
                    pq.offer(new DijkstraNode(neighbor, newDistance));
                }
            }
        }

        // Reconstruct path
        List<String> path = new ArrayList<>();
        String current = end;
        
        while (current != null) {
            path.add(0, current);
            current = previous.get(current);
        }

        return path.get(0).equals(start) ? path : new ArrayList<>();
    }

    /**
     * Calculate direct distance between two locations using coordinates
     */
    public double calculateDirectDistance(String location1, String location2) {
        LocationNode node1 = locations.get(location1);
        LocationNode node2 = locations.get(location2);
        
        if (node1 == null || node2 == null) {
            return Double.MAX_VALUE;
        }
        
        return node1.calculateDistance(node2.latitude, node2.longitude);
    }

    /**
     * Get all locations within a certain radius of a point
     */
    public List<String> getLocationsInRadius(double centerLat, double centerLon, double radius) {
        List<String> nearbyLocations = new ArrayList<>();
        
        for (LocationNode node : locations.values()) {
            double distance = node.calculateDistance(centerLat, centerLon);
            if (distance <= radius) {
                nearbyLocations.add(node.name);
            }
        }
        
        return nearbyLocations;
    }

    /**
     * Get all locations in the graph
     */
    public Set<String> getAllLocations() {
        return new HashSet<>(locations.keySet());
    }

    /**
     * Check if a location exists in the graph
     */
    public boolean hasLocation(String location) {
        return locations.containsKey(location);
    }

    /**
     * Get location coordinates
     */
    public LocationNode getLocation(String location) {
        return locations.get(location);
    }

    // Inner classes
    private static class LocationNode {
        String name;
        double latitude;
        double longitude;

        LocationNode(String name, double latitude, double longitude) {
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        double calculateDistance(double lat2, double lon2) {
            final int R = 6371; // Earth's radius in kilometers

            double latDistance = Math.toRadians(lat2 - this.latitude);
            double lonDistance = Math.toRadians(lon2 - this.longitude);
            
            double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(lat2))
                    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
            
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            
            return R * c;
        }
    }

    private static class Edge {
        String from;
        String to;
        double distance;

        Edge(String from, String to, double distance) {
            this.from = from;
            this.to = to;
            this.distance = distance;
        }
    }

    private static class BFSNode {
        String location;
        double distance;

        BFSNode(String location, double distance) {
            this.location = location;
            this.distance = distance;
        }
    }

    private static class DijkstraNode implements Comparable<DijkstraNode> {
        String location;
        double distance;

        DijkstraNode(String location, double distance) {
            this.location = location;
            this.distance = distance;
        }

        @Override
        public int compareTo(DijkstraNode other) {
            return Double.compare(this.distance, other.distance);
        }
    }
} 