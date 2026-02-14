// Defines the Role class 
// Created by Arvind Ramesh (skeleton)
// Implemented by Sukhman

public class Role {
    private String name;
    private int requiredRank;
    private Player occupiedBy;
    private boolean onCard;

    public Role(String name, int requiredRank, boolean onCard) {
        this.name = name;
        this.requiredRank = requiredRank;
        this.onCard = onCard;
        this.occupiedBy = null; // not intially occupied by any player
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

    public void assignPlayer(Player player) {
        if(player != null && isAvailable()) {
            occupiedBy = player;
            player.setRole(this); // passes this object data to player
            System.out.println(player.getName() + " got the role " + name + "!");
        } else {
            System.out.println("This role is not available. " + player.getName() + " currently has it.");
        }
    }

    public void removePlayer() {
        occupiedBy = null;
    }

    public boolean isAvailable() {
       return occupiedBy == null;
    }

    public static void main(String[] args) {
        System.out.println("Testing for compile: Role");
    }

}
