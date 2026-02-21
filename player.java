// Defines the Player class
// Created by Arvind Ramesh

public class Player {
    private String name;
    private int rank = 1; // sets initial values, will be overwritten if needed based on player count
    private int money = 0;
    private int credits = 0;
    private Location location;
    private Role currentRole;
    private int rehearsalChips = 0;
    private boolean hasMoved = false; 

    public Player(String name) {
        this.name = name;
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

    public Role getRole() {
        return currentRole;
    }

    public Location getLocation() {
        return location;
    }

    public void addCredits(int credits) {
        this.credits += credits;
    }

    public void addMoney(int money) {
        this.money += money;
    }

    public void setRole(Role role) {
        currentRole = role;
    }

    public void setRank (int rank) {
        this.rank = rank;
    }

    public void setMoved(boolean moved) {
        hasMoved = moved;
    }

    public void setLocation(Location location) {
        this.location = location;
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

        // If new location is not adjacent, player can't move
        if (!location.getAdjacentLocations().contains(newLocation)) {
            System.out.println("Invalid move. Not an adjacent location.");
            return;
        }

        location = newLocation;
        hasMoved = true;

        System.out.println(name + " has moved to " + newLocation.getName());

        if(newLocation instanceof SetLocation) {
            SetLocation set = (SetLocation) newLocation;
            if(set.getScene() != null && !set.getScene().isRevealed()) {
                set.revealScene();
            }
        }
    }

    /**
     * Takes role 
     */
    public void takeRole(Role role) {

        if(!(location instanceof SetLocation)) {
            System.out.println("Must be on a set to take a role.");
        }
        // If the players current role is filled
        if (currentRole != null) {
            System.out.println(name + " already has a role.");
            return;
        }

        // if the players rank is lower than the recquired rank the role needs return false
        if (rank < role.getRequiredRank()) {
            System.out.println(name + "'s rank is too low for this role.");
            return;
        }

        // If the role is not available return false
        if (!role.isAvailable()) {
            System.out.println("This role is already taken.");
            return;
        }

        // Assign the role the the player
        role.assignPlayer(this);
        rehearsalChips = 0;
    }

    /**
     * Performs role for Player, given the Player has a role
     */
    public void act() {
        if (!(location instanceof SetLocation)) {
            System.out.println(name + " cannot act here!");
            return;
        }
        // If the player has no role then return false
        if (currentRole == null) {
            System.out.println(name + " does not currently have a role.");
            return;
        }

        // Dice roll value calc
        int diceRoll = (int)(Math.random() * 6) + 1;

        Scene scene = location.getScene();
        SetLocation set = (SetLocation) location; // can convert the location to set due to inheritance 

        // If the dice roll and players rehearsal chips are high enough give credits to player
        if (diceRoll + rehearsalChips >= scene.getBudget()) {
            if(currentRole.isOnCard()) {
                addCredits(2);
            } else {
                addCredits(1);
                addMoney(1);
            }
            set.removeShot();
            System.out.println(name + " succeeded in acting! Shots left on this set " + set.getShotsRemaining());
            return;
        }
        System.out.println(name + " failed in acting.");
        if(!currentRole.isOnCard()) {
            addMoney(1);
            return;
        }
    }

    /**
     * Rehearses role
     */
    public void rehearse() {
        // if player has no role return nothing
        if (currentRole == null) {
            System.out.println("Cannot rehearse without a role.");
            return;
        }
        SetLocation set = (SetLocation) location;
        // Increase rehearsal chip count, cannot have more rehearsal chips than budget
        if(rehearsalChips < set.getScene().getBudget() - 1) {
            rehearsalChips++;
            System.out.println(name + " rehearsed. Current rehearsal chips equal: " + rehearsalChips);
        }

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
