// CSCI 345 Assignment 2
// Authors: Sukhman Lally, Arvind Ramesh
// Purpose: Main starting method for game 
import java.util.Scanner;

public class Deadwood {
    private static int numPlayers = 0;
    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);
        while(numPlayers < 2 || numPlayers > 8) {
            System.out.println("Please input the number of players (2-8).");
            String numPlayersString = scan.nextLine();
            numPlayers = Integer.parseInt(numPlayersString);
            if(numPlayers < 2 || numPlayers > 8) {
                System.out.println("You have inputted an invalid number of players.");
            }
        }
        GameManager game = new GameManager(numPlayers);
        game.startGame();
    }
}
