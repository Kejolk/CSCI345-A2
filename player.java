// Defines the Player class
// Created by Arvind Ramesh
// Implement fully by Wednesday night


public class Player {
    String name;
    int rank;
    int money;
    int credits;
    Location location;
    Role currentRole;
    int rehearsalChips;
    boolean hasMoved; 

    public Player(String name, int rank, int money, int credits, Location location, Role currentRole, int rehearsalChips, boolean hasMoved) {
        this.name = name;
        this.rank = rank;
        this.money = money;
        this.credits = credits;
        this.location = location;
        this.currentRole = currentRole;
        this.rehearsalChips = rehearsalChips;
        this.hasMoved = hasMoved; 

    }





    public static void main(String[] args) {
        // Define Player processes
        // Create test locations 
        Location start = new Location("Start");
        Location town = new Location("Town");

        // Link locations 
        start.addAdjacentLocation(town);

        Player test = new Player("Arvind", 1, 25, 10, start, null, 0, false);

        town.addAdjacentLocation(start);

        // Player Moves
        test.move(town);

        // Player chooses to either take role, act, or rehearse
        // Player can rank upgrade if requirements are met



    }


    // Methods
    /**
     * Moves player between locations
     */
    public void move(Location newLocation) {
        // Should be called with a specific player in mind
        // Changes that players location state to the new inserted state

        if (hasMoved) {
            System.out.println(name + " already moved this turn.");
            return;
            
        }
        

        if (currentRole != null) {
            currentRole.removePlayer();
            currentRole = null;
        }

        if (location == null) {
            return;
        }


        if (location.getAdjacentLocations().contains(newLocation)) {
            location = newLocation;
            hasMoved = true;
            System.out.println(name + " moved to " + newLocation.name);
        } else {
            System.out.println("Invalid move.");
        }

    }

    /**
     * Takes role 
     */
    public boolean takeRole(Role role) {
        if (currentRole != null) {
            return false;
        }

        if (rank < role.recquiredRank) {
            return false;
        }

        if (!role.isAvailable()) {
            return false;
        }

        role.assignPlayer(this);
        currentRole = role;

        return true;

    }

    /**
     * Performs role for Player, given the Player has a role
     */
    public boolean act() {
        if (currentRole == null) {
            return false;
        }

        int diceRoll = (int)(Math.random() * 6) + 1;

        if (diceRoll + rehearsalChips >= currentRole.recquiredRank) {
            credits += 2;                                                       // CHECK HOW MANY CREDITS YOU NEED
            return true;
        }

        return false;
    }

    /**
     * Rehearses role
     */
    public void rehearse() {
        if (currentRole == null) {
            return;
        }

        rehearsalChips++;

    }

    /**
     * Requests a rank upgrade
     */
    public boolean upgradeRank(int newRank, int cost, boolean useCredits) {
        if (newRank <= rank) {
            return false;
        }

        if (useCredits && credits >= cost) {
            credits -= cost;
            rank = newRank;
            return true;
        }

        if (!useCredits && money >= cost) {
            money -= cost;
            rank = newRank;
            return true;

        }

        return false;

    }

    /**
     * Resets the day cycle 
     */
    public void resetNewDay() {
        currentRole = null;
        rehearsalChips = 0;
        hasMoved = false;

    }



}
