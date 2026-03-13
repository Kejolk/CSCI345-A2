public class CreatePlayer {
    public static Player createPlayer(String name, int numPlayers) { // player creator, factory pattern
        Player player = new Player(name);
            
        if (numPlayers == 5) { // adjust resources based on number of players
            player.addCredits(2);
        }                           
        if (numPlayers == 6) {
            player.addCredits(4); 
        }
        if (numPlayers == 7 || numPlayers == 8) {
            player.setRank(2);
        }

        return player;

    }
}