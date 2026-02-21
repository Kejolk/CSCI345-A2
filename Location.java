// Defines the Location Class
// Created by Arvind Ramesh (Skeleton)
// Implemented by Sukhman

// Imports
import java.util.*;

// Main class
public class Location {
    private String name;
    private List<Location> adjacentLocations; 
    private Scene scene;
    
    public Location(String name) {
        this.name = name;
        this.adjacentLocations = new ArrayList<>();
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public String getName() {
        return name;
    }

    public List<Location> getAdjacentLocations() {;
        return adjacentLocations;

    }

    public Scene getScene() {
        return scene;
    }

    public void addAdjacentLocation(Location location) {
        if(location != null && !adjacentLocations.contains(location)) {
            adjacentLocations.add(location);
        }
    }
}
