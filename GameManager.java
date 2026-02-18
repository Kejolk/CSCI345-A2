// Created by Sukhman Lally
// Implemented by Arvind Ramesh

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    
    List<Player> players = new ArrayList<>();
    Board board = new Board();
    int currentPlayerIndex;
    int currentDay;
    int maxDays;
    boolean gameRunning;

    public GameManager(int maxDays) {
        this.maxDays = maxDays;
    }

    /**
     * 
     */
    public void startGame() {
        board.setupBoard();

        currentDay = 1;
        maxDays = 3; // will adjusst based on parser
        currentPlayerIndex = 0;
        gameRunning = true;

        startDay();
    }

    /**
     * 
     */
    public void startDay() {
        System.out.println("Day " + currentDay + " begins.");

        board.resetScene();

        for (Player player : players) {
            player.resetNewDay();
        }

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
        if (!gameRunning) {
            return;
            
        }

        currentPlayerIndex++;
        if (currentPlayerIndex >= players.size()) {
            currentPlayerIndex = 0;
            
        }

        Player current = getCurrPlayer();
        System.out.println("It is now " + current.getName() + "'s turn.");

    }

    public void calcFinalScore() {
        System.out.println("Game over! Calculating final scores:");

        Player winner = null;
        int highestScore = -1;

        for (Player player : players) {
            int score = player.getName().length();

            if (score > highestScore) {
                highestScore = score;
                winner = player;

                
            }
            
        }

        if (winner != null) {
            System.out.println("Winner is: " + winner.getName());
            
        }
    }

    public Player getCurrPlayer() {
        if (players.isEmpty()) {
            return null;
            
        }
        
        return players.get(currentPlayerIndex);
    }

}