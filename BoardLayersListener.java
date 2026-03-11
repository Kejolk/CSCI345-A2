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
import java.util.ArrayList;
import java.util.List;

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

// Movement dynamic buttons
List<JButton> moveButtons = new ArrayList<>();

// JLayered Pane
JLayeredPane bPane;

// Player Info things
JPanel playerPanel;
JLabel[] playerInfoLabels;
List<JLabel> playerDice = new ArrayList<>();

private static final Color ENABLED_COLOR = Color.white;
private static final Color DISABLED_COLOR = new Color(200,200,200);
private static final Color DISABLED_TEXT = Color.darkGray;
private static final String[] PLAYER_DICE_COLORS = {"b", "c", "g", "o", "p", "v", "w", "y"};
private static final Integer UI_LAYER = 2;
private static final Integer DICE_LAYER = 3;

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
      setSize(icon.getIconWidth()+500,icon.getIconHeight()+300);
      
      // Add a scene card to this room
      cardlabel = new JLabel();
      ImageIcon cIcon =  new ImageIcon("images/Card/01.png");
      cardlabel.setIcon(cIcon); 
      cardlabel.setBounds(20,65,cIcon.getIconWidth()+2,cIcon.getIconHeight());
      cardlabel.setOpaque(true);
   
      // Add the card to the lower layer
      bPane.add(cardlabel, 1);
      
   
      // Add a dice to represent a player. 
      // Role for Crusty the prospector. The x and y co-ordiantes are taken from Board.xml file
      playerlabel = new JLabel();
      ImageIcon pIcon = new ImageIcon("images/Dice/r2.png");
      playerlabel.setIcon(pIcon);
      //playerlabel.setBounds(114,227,pIcon.getIconWidth(),pIcon.getIconHeight());  
      playerlabel.setBounds(114,227,46,46);
      playerlabel.setVisible(false);
      bPane.add(playerlabel, DICE_LAYER);
   
      // Create the Menu for action buttons
      mLabel = new JLabel("MENU");
      mLabel.setBounds(icon.getIconWidth()+40,0,100,20);
      bPane.add(mLabel,UI_LAYER);

      // Create output to show on the board instead of console
      messageLabel = new JLabel("Welcome to Deadwood!");
      messageLabel.setBounds(icon.getIconWidth() + 10, 525, 500, 40);
      messageLabel.setForeground(Color.BLACK);
      messageLabel.setFont(new Font("Arial", Font.BOLD, 18));

      bPane.add(messageLabel, UI_LAYER);

      // Create Action buttons
      bAct = new JButton("ACT");
      bAct.setBackground(Color.white);
      bAct.setBounds(icon.getIconWidth()+10,230,100, 100);
      bAct.addMouseListener(new boardMouseListener());

      bMove = new JButton("MOVE");
      bMove.setBackground(Color.white);
      bMove.setBounds(icon.getIconWidth()+10, 30,100, 100);
      bMove.addMouseListener(new boardMouseListener());
      
      bRehearse = new JButton("REHEARSE");
      bRehearse.setBackground(Color.white);
      bRehearse.setBounds(icon.getIconWidth()+10,130,100, 100);
      bRehearse.addMouseListener(new boardMouseListener());
      
      bUpgrade = new JButton("UPGRADE");
      bUpgrade.setEnabled(false);
      bUpgrade.setToolTipText("Upgrade rank at Casting Office");
      bUpgrade.setBackground(new Color(200,200,200));
      bUpgrade.setBounds(icon.getIconWidth()+10,330,100, 100);
      bUpgrade.addMouseListener(new boardMouseListener());

      bEndTurn = new JButton("END TURN");
      bEndTurn.setBackground(Color.white);
      bEndTurn.setBounds(icon.getIconWidth()+10,430,100, 100);
      bEndTurn.addMouseListener(new boardMouseListener());

      // Create Player info area
      playerPanel = new JPanel();
      playerPanel.setLayout(new GridLayout(8,1));
      playerPanel.setBounds(icon.getIconWidth()+150, 50, 300, 300);
      playerPanel.setBorder(BorderFactory.createTitledBorder("Players"));

      playerInfoLabels = new JLabel[8];

      for (int i = 0; i < 8; i++) {
         playerInfoLabels[i] = new JLabel("");
         playerPanel.add(playerInfoLabels[i]);
         
      }
      bPane.add(playerPanel, UI_LAYER);

      // Place the action buttons in the top layer
      bPane.add(bMove, UI_LAYER);
      bPane.add(bAct, UI_LAYER);
      bPane.add(bRehearse, UI_LAYER);
      bPane.add(bUpgrade, UI_LAYER);
      bPane.add(bEndTurn, UI_LAYER);

      setVisible(true);


   }

   // This class implements Mouse Events
   class boardMouseListener implements MouseListener{
      // Code for the different button clicks
      public void mouseClicked(MouseEvent e) {
         if (!((JButton)e.getSource()).isEnabled()) return;
         
         if (e.getSource() == bAct){
            //playerlabel.setVisible(true);
            game.actCurrentPlayer();
         }
         else if (e.getSource() == bRehearse){
            game.rehearseCurrentPlayer();
         }
         else if (e.getSource() == bMove){
            game.moveCurrentPlayer();
         }
         else if (e.getSource() == bUpgrade){
            game.upgradeCurrentPlayer();
         }
         else if (e.getSource() == bEndTurn){
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

   // Method to update player info
   public void updatePlayerInfo(List<Player> players) { 
   for (int i = 0; i < players.size(); i++) {

      Player p = players.get(i);

      String info = p.getName()
         + " | Rank: " + p.getRank()
         + " | Money: " + p.getMoney() 
         + " | Credits: " + p.getCredits();


      if (p.getRole() != null) { //prints role only if player has one
         info += " | Role: " + p.getRole().getName();
      }

      playerInfoLabels[i].setText(info);
      
   }

   for (int i = players.size(); i <playerInfoLabels.length; i++) {
      playerInfoLabels[i].setText("");
      
   }

   }

   // Method to show location options
   public void showMoveOptions(List<Location> locations, Player p) {
      clearMoveButtons();
      displayMessage("Where would you like to move " + p.getName() + "?");
      //System.out.println("Locations available: " + locations.size());

      int startX = boardlabel.getWidth() + 50;
      int startY = 570;

      for (int i = 0; i < locations.size(); i++) {
         Location loc = locations.get(i);

         JButton btn = createButton(loc.getName(), startX, startY + (i*50), 150, 40);

         btn.addActionListener(e -> {
            game.moveCurrentPlayerTo(loc);
            clearMoveButtons();

         });

         moveButtons.add(btn);
         bPane.add(btn, UI_LAYER);
         
      }

      refreshUI();
   }

   public void clearMoveButtons() {
      for (JButton b : moveButtons) {
         bPane.remove(b);
         
      }

      moveButtons.clear();
      refreshUI();
   }

   // Method to take/show role options
   public void showRoleOptions(List<Role> roles, Player player) {
      displayMessage("Would you like to take a role?");

      int startX = boardlabel.getWidth() + 50;
      int startY = 420 + (moveButtons.size() * 50) + 10;

      String[] roleNames = new String[roles.size()];
      for (int i = 0; i < roles.size(); i++) {
         Role r = roles.get(i);
         roleNames[i] = r.getName() + " (Rank " + r.getRequiredRank() + ")";
         
      }

      JComboBox<String> roleDropdown = new JComboBox<>(roleNames);
      roleDropdown.setBounds(startX, startY, 150, 30);

      JButton takeRoleButton = createButton("Take Role", startX, startY + 40, 150, 30);
      JButton denyRoleButton = createButton("Deny Role", startX, startY + 80, 150, 30);

      takeRoleButton.addActionListener(e -> {
         int index = roleDropdown.getSelectedIndex();
         Role chosenRole = roles.get(index);

         player.takeRole(chosenRole);
         displayMessage(player.getName() + " took role " + chosenRole.getName());

         updateRoleButtons(player);

         bPane.remove(roleDropdown);
         bPane.remove(takeRoleButton);
         bPane.remove(denyRoleButton);

         refreshUI();

      });

      denyRoleButton.addActionListener(e -> {
         displayMessage(player.getName() + " declined to take a role");

         bPane.remove(roleDropdown);
         bPane.remove(takeRoleButton);
         bPane.remove(denyRoleButton);

         refreshUI();
      });

      bPane.add(roleDropdown, UI_LAYER);
      bPane.add(takeRoleButton, UI_LAYER);
      bPane.add(denyRoleButton, UI_LAYER);

      refreshUI();
   }

   // updates upgrade button
   public void updateUpgradeButton(Location playerLocation) {
      if (playerLocation instanceof CastingOffice) {
         enableButton(bUpgrade);
         
      } else {
         disableButton(bUpgrade);
      }
   }


   private JLabel createDice(int index) {
      String diceFile = "images/Dice/" + PLAYER_DICE_COLORS[index] + "1.png";
      JLabel dice = new JLabel(new ImageIcon(diceFile));

      dice.setBounds(

         1000 + (index*30), 
         280, 
         100, 
         100
      );

      bPane.add(dice, JLayeredPane.PALETTE_LAYER);

      return dice;


   }

   public void createPlayerDice(List<Player> players) {
      playerDice.clear();

      for (int i = 0; i < players.size(); i++) {
         JLabel dice = createDice(i);
         playerDice.add(dice);

      }

      refreshUI();
   }

   public void movePlayerDice(int playerIndex, int x, int y) {
      JLabel dice = playerDice.get(playerIndex);
      dice.setLocation(x, y);

      refreshUI();
   }

   public void disableButton(JButton b) {
      b.setEnabled(false);
      b.setOpaque(true);
      b.setBackground(DISABLED_COLOR);
      b.setForeground(DISABLED_TEXT);
   }

   public void enableButton(JButton b) {
      b.setEnabled(true);
      b.setOpaque(true);
      b.setBackground(ENABLED_COLOR);
      b.setForeground(Color.black);
   }

   public void resetActionButtons() {
      enableButton(bMove);
   }

   public void playerMoved() {
      disableButton(bMove);
   }

   public void playerActed() {
      disableButton(bAct);
      disableButton(bRehearse);
   }

   public void playerRehearsed() {
      disableButton(bRehearse);
      disableButton(bAct);
   }

   private JButton createButton(String text, int x, int y, int w, int h) {
      JButton b = new JButton(text);
      b.setBounds(x, y, w, h);
      return b;
   }

   private void refreshUI() {
      bPane.revalidate();
      bPane.repaint();
   }

   public void updateRoleButtons(Player p) {
      if (p.getRole() == null) {
         disableButton(bAct);
         disableButton(bRehearse);
         
      }
      else {
         enableButton(bAct);
         enableButton(bRehearse);
      }

   }



}