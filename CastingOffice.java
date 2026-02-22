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
    public void upgradePlayer(Player player, int rank, boolean useCredits) { 
        // validates player
        if (player == null) {
            return;
            
        }

        if(rank <= player.getRank()) {
            System.out.println("You may only upgrade to a rank higher than your current one.");
            return;
        }
        
        int cost;
        if(useCredits) {
            cost = upgradeCosts[rank][1];
        } else {
            cost = upgradeCosts[rank][0];
        }

        if((useCredits && player.getCredits() >= cost)) {
            player.addCredits(-cost);
            player.setRank(rank);
            System.out.println(player.getName() + " upgraded to rank " + rank + " using credits.");
            return;
        } 
        if(!useCredits && player.getMoney() >= cost) {
            player.addMoney(-cost);
            player.setRank(rank);
            System.out.println(player.getName() + " upgraded to rank " + rank + " using money.");
            return;
        }

        System.out.println("Upgrade failed due to insufficient amount of selected resource.");
    }
}
