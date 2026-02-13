// Defines the Location Class
// Created by Arvind Ramesh (Skeleton)
// Implemented by Sukhman

// Imports
import java.util.*;

// Main class
public class Location {
    private String name;
    private List<Location> adjacentLocations; 
    
    public Location(String name) {
        this.name = name;
        this.adjacentLocations = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Location> getAdjacentLocations() {;
        return adjacentLocations;

    }

    public void addAdjacentLocation(Location location) {
        if(location != null && !adjacentLocations.contains(location)) {
            adjacentLocations.add(location);
        }
    }

    public static void main(String[] args) {
        System.out.println("Testing for compile: Location");
    }
    
}
