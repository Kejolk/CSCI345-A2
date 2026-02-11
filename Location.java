// Defines the Location Class
// Created by Arvind Ramesh
// Implement Skeleton

// Imports
import java.util.List;

// Main class
public class Location {
    private String name;
    private List<Location> adjacentLocations; 
    
    public Location(String name) {
        this.name = name;
    }
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    public String getName() {
        return name;
    }
    // Methods

    /**
     * 
     * @return
     */
    public List<Location> getAdjacentLocations() {
        List<Location> temp = null;
        return temp;

    }

    /**
     * 
     * @param location
     */
    public void addAdjacentLocation(Location location) {

    }

}
