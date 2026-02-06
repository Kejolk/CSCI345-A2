// Defines the Location Class
// Created by Arvind Ramesh
// Implement Skeleton

// Imports
import java.util.ArrayList;
import java.util.List;

// Main class
public class Location {
    String name;
    List<Location> adjacentLocations; 
    
    public Location(String name) {
        this.name = name;
        adjacentLocations = new ArrayList<>();
    }

    // Methods

    /**
     * 
     * @return
     */
    public List<Location> getAdjacentLocations() {
        return adjacentLocations;

    }

    /**
     * 
     * @param location
     */
    public void addAdjacentLocation(Location location) {
        adjacentLocations.add(location);

    }

}
