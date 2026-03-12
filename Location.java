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

    protected int x, y, h, w;

    public Location(String name, int x, int y, int h, int w) {
        this.name = name;
        this.adjacentLocations = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
    }

    public int getX() { 
        return x; 
    }
    public int getY() {
         return y; 
    }
    public int getH() { 
        return h; 
    }
    public int getW() {
         return w;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public String getName() {
        return name;
    }

    public List<Location> getAdjacentLocations() {
        return adjacentLocations;

    }

    public Scene getScene() {
        return scene;
    }

    public void addAdjacentLocation(Location location) { // addsadjacent locations to this locations list
        if(location != null && !adjacentLocations.contains(location)) {
            adjacentLocations.add(location);
        }
    }
}
