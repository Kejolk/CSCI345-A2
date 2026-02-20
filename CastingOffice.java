// Created by Sukhman Lally
// Implemented by Arvind Ramesh

public class CastingOffice extends Location {
    private int[][] upgradeCosts; // [rank][money/credits]


    public CastingOffice(String name) {
        super(name);

        upgradeCosts = new int[][] {
            // ranks 0-6
            {0, 0}, 
            {0, 0},
            {4, 5},
            {10, 10},
            {18, 15},
            {28, 20},
            {40, 25}
        };
    }

    public int[][] getUpgradeCosts() {
        return upgradeCosts;
    }

    /**
     * if player location is at casting office and and has sufficent resources to upgrade, upgrade player
     * Can only upgrade once 
     */
    public boolean upgradePlayer(Player player, int rank) { 
        if (player == null) {
            return false;
            
        }

        if (rank <= 0 || rank >= upgradeCosts.length) {
            return false;
            
        }

        int moneyCost = upgradeCosts[rank][0];
        int creditCost = upgradeCosts[rank][1];

        if (player.upgradeRank(rank, creditCost, true)) {
            System.out.println(player.getName() + " upgraded to rank " + rank + " using credits.");
            return true;
            
        }

        if (player.upgradeRank(rank, moneyCost, false)) {
            System.out.println(player.getName() + " upgraded to rank " + rank + " using money.");
            return true;
            
        }


        System.out.println("Upgrade failed.");
        return false; 
    }
}