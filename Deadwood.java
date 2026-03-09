// CSCI 345 Assignment 2
// Authors: Sukhman Lally, Arvind Ramesh
// Purpose: Main starting method for game 
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Deadwood {
    private static int numPlayers = 0;
    // Main class - get number of players and starts game loop
    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);
        while(numPlayers < 2 || numPlayers > 8) {
            String input = JOptionPane.showInputDialog("Enter number of players (2-8):");

            if (input == null) {
                System.exit(0);
            }

            numPlayers = Integer.parseInt(input);

            if(numPlayers < 2 || numPlayers > 8) {
                JOptionPane.showMessageDialog(null, "Invalid number of players");
            }
        }

        // Added Connection to GUI 
        BoardLayersListener board = new BoardLayersListener(null);
        GameManager game = new GameManager(numPlayers, board);
        board.game = game;

        board.setVisible(true);

        game.startGame();
    }
}
