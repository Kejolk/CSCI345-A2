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
        // Player Moves
        // Player chooses to either take role, act, or rehearse
        // Player can rank upgrade if requirements are met



    }


    // Methods
    /**
     * Moves player between locations
     */
    public static void move(Location newLocation) {
        // Should be called with a specific player in mind
        // Changes that players location state to the new inserted state
        

        

    }

    /**
     * Takes role 
     */
    public static boolean takeRole(Role role) {
        boolean temp = false;

        return temp;

    }

    /**
     * Performs role for Player, given the Player has a role
     */
    public static boolean act() {
        boolean temp = false;

        return temp;

    }

    /**
     * Rehearses role
     */
    public static void rehearse() {

    }

    /**
     * Requests a rank upgrade
     */
    public static boolean upgradeRank() {
        boolean temp = false;

        return temp;

    }

    /**
     * Resets the day cycle 
     */
    public static void resetNewDay() {

    }



}
