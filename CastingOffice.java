// Created by Sukhman Lally
// Implemented by Arvind Ramesh

public class CastingOffice extends Location {
    private int[][] upgradeCosts; // [rank][money/credits]


    public CastingOffice(String name) {
        super(name);
    }

    public int[][] getUpgradeCosts() {
        return upgradeCosts;
    }

    /**
     * if player location is at casting office and and has sufficent resources to upgrade, upgrade player
     * Can only upgrade once 
     */
    public boolean upgradePlayer(Player player, int rank) { 
        // check if player is allowed to upgrade
        
        // Based on rank, charge appropriately


        
        return false; 
    }
}