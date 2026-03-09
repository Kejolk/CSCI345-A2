/*

Deadwood GUI helper file
Author: Moushumi Sharmin
This file shows how to create a simple GUI using Java Swing and Awt Library
Classes Used: JFrame, JLabel, JButton, JLayeredPane

*/

import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.event.*;

public class BoardLayersListener extends JFrame {
// Game manager object
GameManager game;

// JLabels
JLabel boardlabel;
JLabel cardlabel;
JLabel playerlabel;
JLabel mLabel;
JLabel messageLabel;

//JButtons
JButton bAct;
JButton bRehearse;
JButton bMove;
JButton bUpgrade;
JButton bEndTurn;

// JLayered Pane
JLayeredPane bPane;

// Constructor
public BoardLayersListener(GameManager game) {

      // Set the title of the JFrame
      super("Deadwood");
      // Set the exit option for the JFrame
      setDefaultCloseOperation(EXIT_ON_CLOSE);

      this.game = game;

      // Create the JLayeredPane to hold the display, cards, dice and buttons
      bPane = getLayeredPane();
   
      // Create the deadwood board
      boardlabel = new JLabel();
      ImageIcon icon =  new ImageIcon("images/board.jpg");
      boardlabel.setIcon(icon); 
      boardlabel.setBounds(0,0,icon.getIconWidth(),icon.getIconHeight());
   
      // Add the board to the lowest layer
      bPane.add(boardlabel, 0);
   
      // Set the size of the GUI
      setSize(icon.getIconWidth()+200,icon.getIconHeight());
      
      // Add a scene card to this room
      cardlabel = new JLabel();
      ImageIcon cIcon =  new ImageIcon("images/01.png");
      cardlabel.setIcon(cIcon); 
      cardlabel.setBounds(20,65,cIcon.getIconWidth()+2,cIcon.getIconHeight());
      cardlabel.setOpaque(true);
   
      // Add the card to the lower layer
      bPane.add(cardlabel, 1);
      
   
      // Add a dice to represent a player. 
      // Role for Crusty the prospector. The x and y co-ordiantes are taken from Board.xml file
      playerlabel = new JLabel();
      ImageIcon pIcon = new ImageIcon("images/r2.png");
      playerlabel.setIcon(pIcon);
      //playerlabel.setBounds(114,227,pIcon.getIconWidth(),pIcon.getIconHeight());  
      playerlabel.setBounds(114,227,46,46);
      playerlabel.setVisible(false);
      bPane.add(playerlabel, 3);
   
      // Create the Menu for action buttons
      mLabel = new JLabel("MENU");
      mLabel.setBounds(icon.getIconWidth()+40,0,100,20);
      bPane.add(mLabel,2);

      // Create output to show on the board instead of console
      messageLabel = new JLabel("Welcome to Deadwood!");
      messageLabel.setBounds(20, icon.getIconHeight() - 70, 800, 30);
      messageLabel.setForeground(Color.BLACK);

      bPane.add(messageLabel, 2);

      // Create Action buttons
      bAct = new JButton("ACT");
      bAct.setBackground(Color.white);
      bAct.setBounds(icon.getIconWidth()+10, 30,100, 100);
      bAct.addMouseListener(new boardMouseListener());
      
      bRehearse = new JButton("REHEARSE");
      bRehearse.setBackground(Color.white);
      bRehearse.setBounds(icon.getIconWidth()+10,130,100, 100);
      bRehearse.addMouseListener(new boardMouseListener());
      
      bMove = new JButton("MOVE");
      bMove.setBackground(Color.white);
      bMove.setBounds(icon.getIconWidth()+10,230,100, 100);
      bMove.addMouseListener(new boardMouseListener());
      
      bUpgrade = new JButton("UPGRADE");
      bUpgrade.setBackground(Color.white);
      bUpgrade.setBounds(icon.getIconWidth()+10,330,100, 100);
      bUpgrade.addMouseListener(new boardMouseListener());

      bEndTurn = new JButton("END TURN");
      bEndTurn.setBackground(Color.white);
      bEndTurn.setBounds(icon.getIconWidth()+10,430,100, 100);
      bEndTurn.addMouseListener(new boardMouseListener());

      

      // Place the action buttons in the top layer
      bPane.add(bAct, 2);
      bPane.add(bRehearse, 2);
      bPane.add(bMove, 2);
      bPane.add(bUpgrade, 2);
      bPane.add(bEndTurn, 2);
}

// This class implements Mouse Events

class boardMouseListener implements MouseListener{

   // Code for the different button clicks
   public void mouseClicked(MouseEvent e) {
      
      if (e.getSource()== bAct){
         playerlabel.setVisible(true);
         game.actCurrentPlayer();
      }
      else if (e.getSource()== bRehearse){
         game.rehearseCurrentPlayer();
      }
      else if (e.getSource()== bMove){
         game.moveCurrentPlayer();
      }
      else if (e.getSource()== bUpgrade){
         game.upgradeCurrentPlayer();
      }
      else if (e.getSource()== bEndTurn){
         game.endTurn();
      }

   }
   public void mousePressed(MouseEvent e) {
   }
   public void mouseReleased(MouseEvent e) {
   }
   public void mouseEntered(MouseEvent e) {
   }
   public void mouseExited(MouseEvent e) {
   }
}

// Method to display text
public void displayMessage(String text) {
   messageLabel.setText(text);
}


public static void main(String[] args) {

   String input = JOptionPane.showInputDialog("How many players?");
   int numPlayers = Integer.parseInt(input);

   BoardLayersListener board = new BoardLayersListener(null);
   GameManager game = new GameManager(numPlayers, board);

   board.game = game;

   board.setVisible(true);
   game.startGame();
}
}