// Created by Sukhman Lally

public class CastingOffice extends Location {
    private int[][] upgradeCosts; // [rank][money/credits]


    public CastingOffice(String name) {
        super(name);
    }
    public boolean upgradePlayer(Player player, int rank) {   
        return false; 
    }
}