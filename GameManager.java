// Created by Sukhman Lally
// Implemented by Arvind Ramesh

import java.util.*;

public class GameManager {
    
    private List<Player> players = new ArrayList<>();
    private Board board = new Board();
    private int currentPlayerIndex = 0;
    private int currentDay = 1;
    private int maxDays;
    private boolean gameRunning = true;
    private Scanner scan = new Scanner(System.in);
    private int numPlayers;

    public GameManager(int numPlayers) {
        this.numPlayers = numPlayers;
        if(numPlayers <= 3) { // setting up game rules based on number of players
            maxDays = 3;
        } else {
            maxDays = 4;
        }   
    }

    /**
     * Starts the game
     */
    public void startGame() {
        board.setupBoard();
        setupPlayers();
        startDay();
        gameLoop();
    }

    public void setupPlayers() {
        for (int i = 1; i <= numPlayers; i++) {
            System.out.println("Enter name for player " + i + ":");
            String name = scan.nextLine();
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
            players.add(player);
        }
    }

    public void startDay() {
        System.out.println("Day " + currentDay + " begins.");

        board.resetScene();
        board.assignScenesForDay(currentDay);
        Location trailer = board.getLocation("Trailer");
        for (Player player : players) {
            player.resetNewDay();
            player.setLocation(trailer);
        }

    }

    public void gameLoop() {
        while (gameRunning) {
            Player currentPlayer = players.get(currentPlayerIndex);
            System.out.println("It is " + currentPlayer.getName() + "'s turn.");
            boolean turnComplete = false;
            while (!turnComplete) {
                System.out.println("Choose action (type 'help' to get a list of actions):");
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
                        System.out.println(currentPlayer.getName() + " is at " + currentPlayer.getLocation().getName() + ".");
                        break;
                    case "endgame":
                        System.out.println("Ending the game.");
                        turnComplete = true;
                        gameRunning = false;
                        break;
                    default:
                        System.out.println("Invalid input. Type 'help' for a list of valid commands."); 
                        break;
                }

                if (board.isDayOver()) {
                    System.out.println("All scenes are now complete. Day " + currentDay + " is over!");
                    endDay();
                    return;
                }
            }

            nextTurn();
        }
    }

    private void manageMove(Player player) { // needs a manager to handle inputs
        Location loc = player.getLocation();
        List<Location> adj = loc.getAdjacentLocations();

        System.out.println("Here are the valid adjacent locations:");    
        for (int i = 0; i < adj.size(); i++) {
            System.out.println("Location " + i + ": " + adj.get(i).getName());
        }

        int locationChoice = isValidIntInput(0, adj.size() - 1, "Enter number of location to move to:");
        if (locationChoice < 0 || locationChoice >= adj.size()) {
            System.out.println("Invaid location selected");
            return;
        }

        Location target = adj.get(locationChoice);
        player.move(target);
    }

    public void manageTakeRole(Player player) {
        Location loc = player.getLocation();

        if (!(loc instanceof SetLocation)) {
            System.out.println("You must be on a set to take a role.");
            return;
        }

        SetLocation set = (SetLocation) loc;
        List<Role> availableRoles = set.getAvailableRoles();
        

        if(availableRoles.isEmpty()) {
            System.out.println("There is no available role on this set.");
        } else {
            String roleType;
            System.out.println("These are the available roles");
            for (int i = 0; i < availableRoles.size(); i++) {
                Role r = availableRoles.get(i);
                if(r.isOnCard()) {
                    roleType = "On Card";
                } else {
                    roleType = "Off Card";
                }
                System.out.println("Role " + i + ": " + r.getName() + " (Rank " + r.getRequiredRank() + " - " + roleType + ")");
            }
        }
        
        int roleChoice = isValidIntInput(0, availableRoles.size() - 1, "Enter number of role to take:");
        if(roleChoice >= 0 && roleChoice < availableRoles.size()) {
            player.takeRole(availableRoles.get(roleChoice));
        }
    }

    private void manageUpgrade(Player player) {
        Location loc = board.getLocation("Casting Office");
    
        CastingOffice office = (CastingOffice) loc;
        System.out.println("Upgrade Costs:");
        System.out.println("Rank   |   Money    |    Credits");
        System.out.println("2           04             05\n3           10             18\n4           18             15");
        System.out.println("5           28             20\n6           40             25");

        int targetRank = isValidIntInput(2, 6, "Enter target rank:");


        if (targetRank < 2 || targetRank > 6) {
            System.out.println("Invalid rank. You can only upgrade to ranks 2-6.");
            return;
        }

        System.out.println("Do you wish to use credits (type 'yes' if you wish to use credits as any another input will select money).");
        boolean useCredits = scan.nextLine().equalsIgnoreCase("yes");

        office.upgradePlayer(player, targetRank, useCredits); 
    }

    public void endDay() {
        System.out.println("Day " + currentDay + " ends.");

        currentDay++;

        if (currentDay > maxDays) {
            gameRunning = false;
            calcFinalScore();
            
        } else {
            startDay();
        }
    }

    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size(); // the modulus will cause it to go from last player -> first player
    }

    public void calcFinalScore() {
        System.out.println("Game over! Calculating final scores:");

        Player winner = null;
        int highestScore = -1;

        for (Player player : players) {
            int score = player.getMoney() + player.getCredits() + (player.getRank() * 5);

            if (score > highestScore) {
                highestScore = score;
                winner = player;

                
            }
            
        }

        if (winner != null) {
            System.out.println("Winner is: " + winner.getName() + " with score: " + highestScore);
            
        }
    }

    private int isValidIntInput(int min, int max, String prompt) { // catches any non-valid inputs
    while (true) {
        System.out.println(prompt);
        String input = scan.nextLine();

        try {
            int value = Integer.parseInt(input);

            if (value >= min && value <= max) {
                return value;
            } else {
                System.out.println("Please enter a number between " + min + " and " + max + ".");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        }
    }
}

    public void playerInfo(Player currentPlayer) {
        System.out.print(currentPlayer.getName() + " | Rank: " + currentPlayer.getRank()
            + " | Money: " + currentPlayer.getMoney() + " | Credits: " + currentPlayer.getCredits());
        if (currentPlayer.getRole() != null) { //prints role only if player has one
            System.out.print(" | Role: " + currentPlayer.getRole().getName());
        }
        System.out.println();
    }

    public void helpPrint() {
        System.out.println("These are a list of valid commands:");
        System.out.println("move  - Move to an adjacent location");
        System.out.println("role  - Take a role if on a set");
        System.out.println("act  - Act out your role");
        System.out.println("rehearse  - Increase rehearsal chips for your role");
        System.out.println("upgrade  - Upgrade rank at the Casting Office");
        System.out.println("end  - End your turn");
        System.out.println("who   - Show your player info");
        System.out.println("where  - Show your current location");
        System.out.println("help  - Show this list of commands");
        System.out.println("endgame  - End the game immediately");
    }
}
