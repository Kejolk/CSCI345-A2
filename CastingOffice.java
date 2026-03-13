// Implemented by Arvind and Sukhman
public class CastingOffice extends Location {
    private int[][] upgradeCosts; // [rank][money/credits]

    public CastingOffice(String name, int x, int y, int h, int w) {
        super(name, x, y, h, w);

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
    public String upgradePlayer(Player player, int rank, boolean useCredits) { 
        // validates player
        if (player == null) {
            return "";
        }
        Location location = player.getLocation();
        if(!(location instanceof CastingOffice)) {
            System.out.println(player.getName() + " is not at the Casting Office.");
            return "";
        }

        if(rank <= player.getRank()) { // to avoid player upgrading to rank equal or lower than current
            return "You may only upgrade to a rank higher than your current one.";
        }
        
        int cost;
        if(useCredits) { // cost is determined based on user choice of credits or money
            cost = upgradeCosts[rank][1];
        } else {
            cost = upgradeCosts[rank][0];
        }

        if((useCredits && player.getCredits() >= cost)) { // if player chooses to use credits & can afford upgrade
            player.addCredits(-cost);
            player.setRank(rank);
            return player.getName() + " upgraded to rank " + rank + " using credits.";
        } 
        if(!useCredits && player.getMoney() >= cost) { // if player chooses money & can afford upgrade
            player.addMoney(-cost);
            player.setRank(rank);
            return player.getName() + " upgraded to rank " + rank + " using money.";
        }

        return "Upgrade failed due to insufficient amount of selected resource.";
    }
}
