// Implemented by Sukhman Lally

import java.awt.JobAttributes;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class GameManager {
    
    private List<Player> players = new ArrayList<>();
    private Board board = new Board();
    private int currentPlayerIndex = 0;
    private int currentDay = 1;
    private int maxDays;
    private boolean gameRunning = true;
    private Scanner scan = new Scanner(System.in);
    private int numPlayers;
    private BoardLayersListener gui;


    public GameManager(int numPlayers, BoardLayersListener gui) {
        this.numPlayers = numPlayers;
        if(numPlayers <= 3) { // setting up game rules based on number of players
            maxDays = 3;
        } else {
            maxDays = 4;
        }
        this.gui = gui;
    }

    /**
     * Starts the game
     */
    public void startGame() {
        board.setupBoard();
        setupPlayers();
        startDay();
        Player currentPlayer = players.get(currentPlayerIndex);
        gui.displayMessage("It is " + currentPlayer.getName() + "'s turn.");
        gui.updateUpgradeButton(currentPlayer.getLocation());
    }

    public void setupPlayers() {
        for (int i = 1; i <= numPlayers; i++) {

            // Changed to use UI to ask for players
            String name = JOptionPane.showInputDialog(
                null,
                "Enter name for Player " + i + ":"
            );

            // in case user leaves blank name
            if (name == null || name.trim().isEmpty()) {
                name = "Player " + i;
                
            }

            Player player = CreatePlayer.createPlayer(name, numPlayers);
            players.add(player);
        }

        gui.updatePlayerInfo(players);
        gui.createPlayerDice(players);
    }

// DAY MANAGEMENT -----------------------------------------------------------------------------------------------------

    public void startDay() { // starts each day, resets player location to trailer, clears board, and puts new scenes
        gui.displayMessage("Day " + currentDay + " begins.");

        gui.clearSceneCards();
        gui.clearShotMarkers();

        board.resetScene();
        board.assignScenesForDay(currentDay);
        for (Location loc : board.getLocations()) {
            if (loc instanceof SetLocation) {
                SetLocation set = (SetLocation) loc;

                gui.placeSceneCard(set);
                gui.placeShotMarkers(set);
            }
        }
        Location trailer = board.getLocation("Trailer");
        for (Player player : players) {
            player.resetNewDay();
            player.setLocation(trailer);
            int index = players.indexOf(player);
            gui.movePlayerDice(index, trailer.getX(), trailer.getY()); // move players dice to trailer
        }

    }

    /* 
    public void gameLoop() { // game loop, manages player actions
        while (gameRunning) {
            Player currentPlayer = players.get(currentPlayerIndex);
            gui.displayMessage("It is " + currentPlayer.getName() + "'s turn.");
            boolean turnComplete = false;
            while (!turnComplete) {
                gui.displayMessage("Choose action (type 'help' to get a list of actions):");
                String action = scan.nextLine().toLowerCase();

                switch (action) {
                    case "move":
                        manageMove(currentPlayer); 
                        break;
                    case "role":
                        manageTakeRole(currentPlayer);
                        break;
                    case "act":
                        currentPlayer.act();
                        break;
                    case "rehearse":
                        currentPlayer.rehearse();
                        break;
                    case "upgrade":
                        manageUpgrade(currentPlayer);
                        break;
                    case "end":
                        turnComplete = true;
                        currentPlayer.setActionTaken(false);
                        break;
                    case "help":
                        helpPrint();
                        break;
                    case "who":
                        playerInfo(currentPlayer);
                        break;
                    case "where":
                        gui.displayMessage(currentPlayer.getName() + " is at " + currentPlayer.getLocation().getName() + ".");
                        break;
                    case "endgame":
                        gui.displayMessage("Ending the game.");
                        calcFinalScore();
                        turnComplete = true;
                        gameRunning = false;
                        break;
                    default:
                        gui.displayMessage("Invalid input. Type 'help' for a list of valid commands."); 
                        break;
                }

                if (board.isDayOver()) { // checks if the current day is over
                    gui.displayMessage("All scenes are now complete. Day " + currentDay + " is over!");
                    endDay();
                }
            }

            nextTurn();
        }
    }*/


    // prints adjacent locations and lets player pick one, then calls Player class move to that location
    private void manageMove(Player player) { // needs a manager to handle inputs
        Location loc = player.getLocation();
        List<Location> adj = loc.getAdjacentLocations();

        gui.displayMessage("Here are the valid adjacent locations:");    
        for (int i = 0; i < adj.size(); i++) {
            gui.displayMessage("Location " + i + ": " + adj.get(i).getName());
        }

        gui.showMoveOptions(adj, player);
    }



    // Prints out rank upgrade costs, manages player inputs, and calls CastingOffice upgrade player class
    private void manageUpgrade(Player player) {
        Location loc = player.getLocation();

        if (!(loc instanceof CastingOffice)) {
            gui.displayMessage("You must be at the Casting Office to upgrade.");
            return;
            
        }

        CastingOffice office = (CastingOffice) loc;

        /*gui.displayMessage("Upgrade Costs:");
        gui.displayMessage("Rank   |   Money   |   Credits");
        gui.displayMessage("2   |  4  |  5");
        gui.displayMessage("3   |  10  |  10");
        gui.displayMessage("4   |  18  |  15");
        gui.displayMessage("5   |  28  |  20");
        gui.displayMessage("6   |  40  |  25");*/

        // Asks rank
        String rankInput = JOptionPane.showInputDialog(
            null,
            "Enter target rank (2-6)"
        );

        if (rankInput == null) return;

        int targetRank;

        try {
            targetRank = Integer.parseInt(rankInput);
            
        } catch (NumberFormatException e) {
            gui.displayMessage("Invalid number.");
            return;
        }

        if (targetRank < 2 || targetRank > 6) {
            gui.displayMessage("Invalid rank.");
            return;
            
        }

        String[] options = {"Money", "Credits"};

        int choice = JOptionPane.showOptionDialog(
            null,
            "How would you like to pay?",
            "Upgrade",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[0]
        );

        boolean useCredits = (choice == 1);

        String message = office.upgradePlayer(player, targetRank, useCredits);
        gui.displayMessage(message);
        gui.updateDiceRank(players.indexOf(player), player.getRank());

        gui.updatePlayerInfo(players);

    }

    public void endDay() {
        gui.displayMessage("Day " + currentDay + " ends.");

        currentDay++;

        if (currentDay > maxDays) {
            gameRunning = false; // end the game, call to calculate final scores
            calcFinalScore();
            
        } else {
            startDay(); // else start a new day
        }
    }

// TURN MANAGEMENT -------------------------------------------------------------------------------------------

    public void nextTurn() { // next players turn
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size(); // the modulus will cause it to go from last player -> first player
        Player pl = players.get(currentPlayerIndex);
        gui.clearRoleDropdown();
        gui.enableActionButtons();
        gui.updateRoleButtons(pl);
        gui.updateUpgradeButton(pl.getLocation());
        gui.displayMessage("It is now " + pl.getName() + "'s turn.");

        Location loc = pl.getLocation();

        if (pl.getRole() == null && loc instanceof SetLocation) {
            SetLocation set = (SetLocation) loc;

            if (!set.isSceneComplete()) {
                List<Role> roles = set.getAvailableRoles();

                if (!roles.isEmpty()) {
                    gui.showRoleOptions(roles, pl);
                }
            }
        }
    }

    public void endTurn() {
        Player p = players.get(currentPlayerIndex);
        p.setActionTaken(false);
        nextTurn();
    }

// PLAYER ACTIONS --------------------------------------------------------------------------------------------
    public void moveCurrentPlayer() {
        Player p = players.get(currentPlayerIndex);
        if(p.getActionTaken()) {
            gui.displayMessage(p.getName() + " has already taken their action this turn!");
            return;
        }

        if (p.getRole() != null) {
        gui.displayMessage(p.getName() + " cannot move while on a role!");
        return;
        }
        Location loc = p.getLocation();
        List<Location> adj = loc.getAdjacentLocations();
        System.out.println("Player: " + p.getName());
        System.out.println("Location: " + loc.getName());
        System.out.println("Adjacent count: " + adj.size());
        gui.showMoveOptions(adj, p);
    }

    public void moveCurrentPlayerTo(Location loc) {
        Player p = players.get(currentPlayerIndex);
        
        p.move(loc);

        if(loc instanceof SetLocation) { // reveal scene 
            SetLocation set = (SetLocation) loc;
            Scene scene = set.getScene();
            if(!scene.isRevealed()) {
                scene.reveal();
                gui.revealSceneCard(set);
                System.out.println(scene.getImage());
            }
        }

        gui.playerMoved();
        p.setActionTaken(true);

        int index = players.indexOf(p);
        gui.movePlayerDice(index, loc.getX(), loc.getY());

        gui.displayMessage(p.getName() + " moved to " + loc.getName());
        gui.updatePlayerInfo(players);

        gui.updateUpgradeButton(loc);

        // If location is a set ask about role
        if (loc instanceof SetLocation) {
            SetLocation set = (SetLocation) loc;

            if (!set.isSceneComplete()) {
                List<Role> roles = set.getAvailableRoles();

                if (!roles.isEmpty()) {
                    gui.showRoleOptions(roles, p);
                    
                }
                
            }
            
        }
    }


    public void actCurrentPlayer() {
        Player p = players.get(currentPlayerIndex);
        if(p.getActionTaken()) {
            gui.displayMessage(p.getName() + " has already taken their action this turn!");
            return;
        }

        if (p.getRole() == null) {
            gui.displayMessage("You must take a role before acting.");
            return;
            
        }

        SetLocation set = (SetLocation) p.getLocation();

        if (set.isSceneComplete()) {
            gui.displayMessage("Scene already wrapped!");
            return;
            
        }
        int beforeShots = set.getShotsRemaining();
        ActionMessage message;
        message = p.act();

        gui.displayMessage(message.getMainMessage());
        gui.displayBonusMessage(message.getBonusMessage());

        int afterShots = set.getShotsRemaining();

        if (beforeShots > afterShots) {
            gui.removeShotMarker(set);
            
        }

        if (beforeShots > 0 && afterShots == 0) {
            gui.removeSceneCard(set);
            
        }

        gui.playerActed();
        p.setActionTaken(true);
        gui.updatePlayerInfo(players);

        if(board.isDayOver()) {
            gui.displayMessage("Only one scene remains. Day is over!");
            endDay();
        }
    }

    public void rehearseCurrentPlayer() {
        Player p = players.get(currentPlayerIndex);
        if(p.getActionTaken()) {
            gui.displayMessage(p.getName() + " has already taken their action this turn!");
            return;
        }

        if (p.getRole() == null) {
            gui.displayMessage("You must take a role before rehearsing.");
            return;
            
        }

        p.rehearse();
        p.setActionTaken(true);
        gui.playerRehearsed();
        gui.updatePlayerInfo(players);
    }

    public void upgradeCurrentPlayer() {
        Player p = players.get(currentPlayerIndex);

        manageUpgrade(p);

        gui.updatePlayerInfo(players);
    }

// ROLE MANAGEMENT ------------------------------------------------------------------------------------------
    // Prints out available roles at location, handles player input, and calls Player class take role
    public void manageTakeRole(Player player) {
        Location loc = player.getLocation();

        if (!(loc instanceof SetLocation)) {
            gui.displayMessage("You must be on a set to take a role.");
            return;
        }

        SetLocation set = (SetLocation) loc;
        List<Role> availableRoles = set.getAvailableRoles();
        

        if(availableRoles.isEmpty()) {
            gui.displayMessage("There is no available role on this set.");
        } else {
            String roleType;
            gui.displayMessage("These are the available roles");
            for (int i = 0; i < availableRoles.size(); i++) {
                Role r = availableRoles.get(i);
                if(r.isOnCard()) {
                    roleType = "On Card";
                } else {
                    roleType = "Off Card";
                }
                gui.displayMessage("Role " + i + ": " + r.getName() + " (Rank " + r.getRequiredRank() + " - " + roleType + ")");
            }
        }
        
        int roleChoice = isValidIntInput(0, availableRoles.size() - 1, "Enter number of role to take:");
        if(roleChoice >= 0 && roleChoice < availableRoles.size()) {
            player.takeRole(availableRoles.get(roleChoice));
        }
    }

// Utility --------------------------------------------------------------------------------------------------
    // Determines winner and prints who they are and their score
    public void calcFinalScore() {

        gui.displayMessage("Game over! Calculating final scores:");

        List<Player> sortedPlayers = new ArrayList<>(players);

        sortedPlayers.sort((p1, p2) ->
            Integer.compare(p2.getFinalScore(), p1.getFinalScore())


        );

        gui.showGameOverScreen(sortedPlayers);
    }

    private int isValidIntInput(int min, int max, String prompt) { // catches any non-valid inputs
    while (true) {
        gui.displayMessage(prompt);
        String input = scan.nextLine();

        try {
            int value = Integer.parseInt(input);

            if (value >= min && value <= max) {
                return value;
            } else {
                gui.displayMessage("Please enter a number between " + min + " and " + max + ".");
            }

        } catch (NumberFormatException e) {
            gui.displayMessage("Invalid input. Please enter a valid number.");
        }
    }
}

    public void playerInfo(Player currentPlayer) { // prints all of current player's info
        String info = currentPlayer.getName()
        + " | Rank: " + currentPlayer.getRank()
        + " | Money: " + currentPlayer.getMoney() 
        + " | Credits: " + currentPlayer.getCredits();

        if (currentPlayer.getRole() != null) { //prints role only if player has one
            info += " | Role: " + currentPlayer.getRole().getName();
        }

        gui.displayMessage(info);
    }

    public void helpPrint() { // list of valid commands
        gui.displayMessage("These are a list of valid commands:");
        gui.displayMessage("move  - Move to an adjacent location");
        gui.displayMessage("role  - Take a role if on a set");
        gui.displayMessage("act  - Act out your role");
        gui.displayMessage("rehearse  - Increase rehearsal chips for your role");
        gui.displayMessage("upgrade  - Upgrade rank at the Casting Office");
        gui.displayMessage("end  - End your turn");
        gui.displayMessage("who   - Show your player info");
        gui.displayMessage("where  - Show your current location");
        gui.displayMessage("help  - Show this list of commands");
        gui.displayMessage("endgame  - End the game immediately");
    }
    
    public void showCurrentPlayerInfo() {
        Player p = players.get(currentPlayerIndex);
        playerInfo(p);
    }


    public int getPlayerIndex(Player player) {
        return players.indexOf(player);
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
}