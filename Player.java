// Defines the Player class
// Created by Arvind Ramesh


public class Player {
    private String name;
    private int rank;
    private int money;
    private int credits;
    private Location location;
    private Role currentRole;
    private int rehearsalChips;
    private boolean hasMoved; 

    public Player(String name, int rank, int money, int credits, Location location, Scene scene, Role currentRole, int rehearsalChips, boolean hasMoved) {
        this.name = name;
        this.rank = rank;
        this.money = money;
        this.credits = credits;
        this.location = location;
        this.currentRole = currentRole;
        this.rehearsalChips = rehearsalChips;
        this.hasMoved = hasMoved; 

    }

    // Methods

    public String getName() {
        return name;
    }

    public int getRank() {
        return rank;
    }

    public int getMoney() {
        return money;
    }

    public int getCredits() {
        return credits;
    }

    public void setRole(Role role) {
        currentRole = role;
    }

    public void move(Location newLocation) {
        // If the player has moved already
        if (hasMoved) {
            // Print error message
            System.out.println(name + " already moved this turn.");
            return;
            
        }
        
        // If player currently has a role
        if (currentRole != null) {
            // Print error message
            System.out.println(name + " cannot move while working on a role.");
            return;
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
            System.out.println(name + " moved to " + newLocation.getName());
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
        if (rank < role.getRequiredRank()) {
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

        // If the dice roll and players rehearsal chips are high enough give credits to player
        if (diceRoll + rehearsalChips >= location.getScene().getBudget()) {
            credits += 2;              //NOTE: add off/on card distinction        // CHECK HOW MANY CREDITS YOU NEED
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

        // Increase rehearsal chip count, cannot have more rehearsal chips than budget
        if(rehearsalChips < location.getScene().getBudget() - 1) {
            rehearsalChips++;
        }

    }

    /**
     * Requests a rank upgrade
     */
    public boolean upgradeRank(int newRank, int cost, boolean useCredits) { // NOTE: update this to remove cost, cost comes from CastingOffice
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
