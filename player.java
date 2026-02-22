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
    private boolean actionTaken = false;

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

    public void setActionTaken(boolean action) {
        actionTaken = action;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    public void move(Location newLocation) {
        // If the player has moved already
        if (actionTaken) {
            // Print error message
            System.out.println(name + " may not move this turn. Wait until next turn to move, act or rehearse.");
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
        actionTaken = true;

        System.out.println(name + " has moved to " + newLocation.getName());
        actionTaken = true;

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

        SetLocation set = (SetLocation) location;

        if(set.isSceneComplete()) {
            System.out.println("This scene has already wrapped. " + name + " cannot take a role. Please move to a new set.");
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
        if (actionTaken) {
            System.out.println(name + " may not act this turn. Wait until next turn to move, act or rehearse");
            return;
        }

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

        
        SetLocation set = (SetLocation) location; // can convert the location to set due to inheritance 
        Scene scene = set.getScene();

        if(set.isSceneComplete()) {
            System.out.println("This scene has already wrapped. " + name + " cannot act.");
            return;
        }

        // If the dice roll and players rehearsal chips are high enough give credits to player
        if (diceRoll + rehearsalChips >= scene.getBudget()) {
            if(currentRole.isOnCard()) {
                addCredits(2);
                System.out.println(name + " has earned 2 credits!");
            } else {
                addCredits(1);
                addMoney(1);
                System.out.println(name + " has earned 1 credit and 1 dollar!");
            }
            set.removeShot();
            System.out.println(name + " succeeded in acting! Shots left on this set: " + set.getShotsRemaining());
            if(set.isSceneComplete()) {
                System.out.println("Scene has wrapped!");
                set.wrapScene();
            }
            actionTaken = true;
            return;
        }
        System.out.println(name + " failed in acting.");
        actionTaken = true;
        if(!currentRole.isOnCard()) {
            addMoney(1);
            System.out.println(name + " has earned 1 dollar!");
            return;
        }
    }

    /**
     * Rehearses role
     */
    public void rehearse() {
        if (actionTaken) {
            System.out.println(name + " may not rehearse this turn. Wait until next turn to move, act or rehearse.");
            return;
        }
        // if player has no role return nothing
        if (currentRole == null) {
            System.out.println("Cannot rehearse without a role.");
            return;
        }
        SetLocation set = (SetLocation) location;

        if(set.isSceneComplete()) {
            System.out.println("This scene has already wrapped. " + name + " cannot rehearse.");
            return;
        }
        // Increase rehearsal chip count, cannot have more rehearsal chips than budget
        if(rehearsalChips < set.getScene().getBudget() - 1) {
            rehearsalChips++;
            System.out.println(name + " rehearsed. Current rehearsal chips equal: " + rehearsalChips);
            actionTaken = true;
        }

    }

    /**
     * Resets the day cycle 
     */
    public void resetNewDay() {
        // set current values to base after day ends
        currentRole = null;
        rehearsalChips = 0;
        actionTaken = false;
    }
}
