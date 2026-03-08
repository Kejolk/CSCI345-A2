// Implemented by Arvind and Sukhman
import java.util.*;
public class Board {
    private Location[] locations;
    private List<Scene> sceneList;

    // Getters
    public Location[] getLocations() {
        return locations;
    }

    /**
     * Creates new board with current test locations for implementation purposes
     * Work on: 
     */
    public void setupBoard() {
        // create parser object
        ParseXML parser = new ParseXML();

        // acquire board and card data
        parser.readBoardData("XML/board.xml");
        parser.readCardData("XML/cards.xml");

        // create a map to store locations
        Map<String, Location> locationMap = new HashMap<>();

        // Creates location objects 
        for (ParseXML.LocationData data : parser.getLocations()) {
            Location loc;

            // if location is CastingOffice create special subclass
            if (data.isCastingOffice) {
                loc = new CastingOffice(data.name, data.coords.x, data.coords.y, data.coords.h, data.coords.w);
            } else if(data.isTrailer) { //or is trailer
                loc = new Location(data.name, data.coords.x, data.coords.y, data.coords.h, data.coords.w);
            } else {
                SetLocation set = new SetLocation(data.name, data.shotsRemaining, data.coords.x, data.coords.y, data.coords.h, data.coords.w);

                ArrayList<Role> roleList = new ArrayList<>();
                for (ParseXML.RoleData r : data.roles) {
                    roleList.add(new Role(r.name, r.rank, false, r.coords.x, r.coords.y, r.coords.h, r.coords.w));
                }

                set.setRole(roleList);

                List<ShotMarkers> shots = new ArrayList<>();
                for (ParseXML.ShotsData s : data.shots) {
                    shots.add(new ShotMarkers(s.shotNumber, s.coords.x, s.coords.y, s.coords.h, s.coords.w));
                }
                set.setShots(shots);
                loc = set;
            }

            // store location in map
            locationMap.put(data.name, loc);

        }

        // Connects adjacent locations
        for (ParseXML.LocationData data : parser.getLocations()) {
            Location curr = locationMap.get(data.name);

            // add each neighbor to adjacency list
            for (String neighborName : data.neighbors) {
                Location neighbor = locationMap.get(neighborName);
                if (neighbor != null) {
                    curr.addAdjacentLocation(neighbor);
                    
                    if(!neighbor.getAdjacentLocations().contains(curr)) { // fixes issue of Trailer & CastingOffice not added as neighbors
                        neighbor.addAdjacentLocation(curr);
                    }
                }

                
            }
            
        }

        // convert map into array for easier use
        locations = locationMap.values().toArray(new Location[0]);

        // copy card list and shuffle
        List<ParseXML.CardData> cardList = new ArrayList<>(parser.getCards());
        Collections.shuffle(cardList);

        // converts carddata into scene object
        sceneList = new ArrayList<>();
        for (ParseXML.CardData card : cardList) {
            // creates Role objects for the scene
            ArrayList<Role> roles = new ArrayList<>();
            for (int i = 0; i < card.roles.size(); i++) {
                ParseXML.RoleData r = card.roles.get(i);
                roles.add(new Role(r.name, r.rank, r.onCard, r.coords.x, r.coords.y, r.coords.h, r.coords.w));
                
            }

            // create scene and add to scene list
            Scene scene = new Scene(card.name, card.budget, roles, card.image);
            sceneList.add(scene);
            
        }

        Collections.shuffle(sceneList); // shuffles the scenes
        

    }

    public void assignScenesForDay(int day) { // places scene cards on each set
        int startIndex = (day - 1) * 10; // 10 sets, keeps count of how many scenes have been used

        List<Scene> dayScenes = sceneList.subList(startIndex, startIndex + 10);

        int counter = 0;
        for (Location loc : locations) {
            if (loc instanceof SetLocation) {
                SetLocation set = (SetLocation) loc;

                // Remove players and reset shots
                set.wrapScene();
                set.resetShots();

            set.setScene(dayScenes.get(counter));
            counter++;
            }
        }
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
        if (locations == null) {
            return;
            
        }
 
        for (Location loc : locations) {
            if (loc.getScene() != null) {
                loc.getScene().reset();
                if (loc instanceof SetLocation) {
                    ((SetLocation) loc).resetShots();
                }
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

    public boolean isDayOver() { // checks if day is over (conditioned on only 1 scene active)
        int remainingScenes = 0;
        for (Location loc : locations) {
            if (loc instanceof SetLocation) {
                SetLocation set = (SetLocation) loc;
                if (!set.isSceneComplete()) {
                    remainingScenes++;
                }
            }
        }
        return remainingScenes <= 1; // if 1 scene remains, day is over
    }   
}

