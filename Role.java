// Defines the Role class 
// Created by Arvind Ramesh
// Implement skeleton

public class Role {
    String name;
    int requiredRank;
    Player occupiedBy;
    boolean onCard;

    // Methods
    /**
     * 
     * @return
     */
    public boolean assignPlayer(Player player) {
        if (occupiedBy == null) {
            occupiedBy = player;
            return true;

        }
        return false;


    }

    /**
     * 
     */
    public void removePlayer() {
        occupiedBy = null;

    }

    /**
     * 
     * @return
     */
    public boolean isAvailable() {
        return occupiedBy == null;
        
    }

}
