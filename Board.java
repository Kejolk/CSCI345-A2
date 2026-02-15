// Created by Sukhman Lally
// Implemented by Arvind Ramesh


public class Board {
    private Location[] locations;
    private Scene[] scenes;


    // Getters
    public Location[] getLocations() {
        return locations;
    }

    public Scene[] getScenes() {
        return scenes;
    }

    /**
     * Creates new board with current test locations for implementation purposes
     */
    public void setupBoard() {
        // Create locations (add more when game sets up)
        Location start = new Location("Start");
        Location town = new Location("Town");
        Location testLocation = new Location("Test");

        // Connect locations
        start.addAdjacentLocation(town);
        town.addAdjacentLocation(start);

        town.addAdjacentLocation(testLocation);
        testLocation.addAdjacentLocation(town);

        // Initialize locations and scenes list
        locations = new Location[] {start, town, testLocation};
        scenes = new Scene[locations.length];


    }

    /**
     * Returns a location by name
     * @param name
     * @return
     */
    public Location getLocation(String name) {
        if (name == null || locations == null) {
            return null;
            
        }

        // if the input is in the locations list, return the location
        for (Location location : locations) {
            if (location.getName().equalsIgnoreCase(name)) {
                return location;
                
            }
        }

        return null;
         
    }


    /**
     * Resets all scenes for next/new day
     */
    public void resetScene() {
        if (scenes == null) {
            return;
            
        }

        for (Scene scene : scenes) {
            if (scene != null) {
                scene.reset(); // added reset() method to scene class
                
            }
            
        }

    }

    /**
     * Checks if two locations are adjacent
     * @param location1
     * @param location2
     * @return
     */
    public boolean isAdjacent(Location location1, Location location2) {
        if (location1 == null || location2 == null) {
            return false;
            
        }
        return location1.getAdjacentLocations().contains(location2); 
        }
}