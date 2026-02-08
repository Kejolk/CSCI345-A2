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
        // Purely for testing
        // Create locations to test move
        Location start = new Location("Start");
        Location town = new Location("Town");

        // Create test player
        Player test = new Player("Arvind", 1, 25, 10, start, null, 0, false);

        // Link locations 
        start.addAdjacentLocation(town);
        town.addAdjacentLocation(start);

        // Player Moves using move()
        test.move(town);

    }


    // Methods
    /**
     * Moves player if they have not moved already and if they do not have a role 
     */
    public void move(Location newLocation) {
        // If the player has moved already
        if (hasMoved) {
            // Print error message
            System.out.println(name + " already moved this turn.");
            return;
            
        }
        
        // If player currently has a role
        if (currentRole != null) {
            // Remove the player from the role
            currentRole.removePlayer();
            // Set role to null
            currentRole = null;
        }

        // If location is null then change nothing
        if (location == null) {
            return;
        }

        // If the location chosen is adjacent to the current location
        if (location.getAdjacentLocations().contains(newLocation)) {
            // Set player location to new location
            location = newLocation;
            // Set movement status to true
            hasMoved = true;
            // Print out update message
            System.out.println(name + " moved to " + newLocation.name);
        } else {
            // If location chosen is not adjacent, print out invalid message
            System.out.println("Invalid move.");
        }

    }

    /**
     * Takes role 
     */
    public boolean takeRole(Role role) {
        // If the players current role is filled
        if (currentRole != null) {
            // return false
            return false;
        }

        // if the players rank is lower than the recquired rank the role needs return false
        if (rank < role.requiredRank) {
            return false;
        }

        // If the role is not available return false
        if (!role.isAvailable()) {
            return false;
        }

        // Assign the role the the player
        role.assignPlayer(this);
        currentRole = role;

        return true;

    }

    /**
     * Performs role for Player, given the Player has a role
     */
    public boolean act() {
        // If the player has no role then return false
        if (currentRole == null) {
            return false;
        }

        // Dice roll value calc
        int diceRoll = (int)(Math.random() * 6) + 1;

        // If the dice roll and players rehearsalchips are high enough give credits to player
        if (diceRoll + rehearsalChips >= currentRole.requiredRank) {
            credits += 2;                                                       // CHECK HOW MANY CREDITS YOU NEED
            return true;
        }

        // else return nothing
        return false;
    }

    /**
     * Rehearses role
     */
    public void rehearse() {
        // if player has no role return nothing
        if (currentRole == null) {
            return;
        }

        // Increase rehearsal chip count
        rehearsalChips++;

    }

    /**
     * Requests a rank upgrade
     */
    public boolean upgradeRank(int newRank, int cost, boolean useCredits) {
        // If players new rank is lower or the same as current rank return false
        if (newRank <= rank) {
            return false;
        }

        // If player has enough credits for the rank
        if (useCredits && credits >= cost) {
            // Deduct appropriate amount of credits and reassign the new rank
            credits -= cost;
            rank = newRank;
            return true;
        }

        // If the player does not have enough credits but has enough money
        if (!useCredits && money >= cost) {
            // Deduct appropriate amount of money and reassign the new rank
            money -= cost;
            rank = newRank;
            return true;

        }

        // else return false
        return false;

    }

    /**
     * Resets the day cycle 
     */
    public void resetNewDay() {
        // set current values to base after day ends
        currentRole = null;
        rehearsalChips = 0;
        hasMoved = false;

    }



}
