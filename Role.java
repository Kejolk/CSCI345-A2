// Defines the Role class 
// Created by Arvind Ramesh (skeleton)
// Implemented by Sukhman

public class Role {
    private String name;
    private int requiredRank;
    private Player occupiedBy;
    private boolean onCard;
    private int x;
    private int y;
    private int h;
    private int w;

    public Role(String name, int requiredRank, boolean onCard, int x, int y, int h, int w) {
        this.name = name;
        this.requiredRank = requiredRank;
        this.onCard = onCard;
        this.occupiedBy = null; // not intially occupied by any player
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

    public String getName() {
        return name;
    }

    public Player getOccupiedBy() {
        return occupiedBy;
    }

    public int getRequiredRank() {
        return requiredRank;
    }

    public boolean isOnCard() {
        return onCard;
    }

    public void assignPlayer(Player player) { // attaches role to player
        if(player != null && isAvailable()) {
            occupiedBy = player;
            player.setRole(this); // passes this object data to player
            //System.out.println(player.getName() + " got the role " + name + "!");
        } else {
            //System.out.println("This role is not available. " + player.getName() + " currently has it.");
        }
    }

    public void removePlayer() { // removes plater from role
        if (occupiedBy != null) {
            occupiedBy.setRole(null);
            occupiedBy = null;
            
        }
    }

    public boolean isAvailable() { // returns whether the role is available
       return occupiedBy == null;
    }

}
