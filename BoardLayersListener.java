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
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class BoardLayersListener extends JFrame {
// Game manager object
GameManager game;

// JLabels
JLabel boardlabel;
JLabel cardlabel;
JLabel playerlabel;
JLabel mLabel;
JLabel messageLabel;
JLabel bonusMessageLabel;

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
JTable playerTable;
DefaultTableModel playerTableModel;
List<JLabel> playerDice = new ArrayList<>();

Map<SetLocation, JLabel> sceneCardLabels = new HashMap<>();

// Role drop box
JComboBox<String> roleDropdown;
JButton takeRoleButton;
JButton denyRoleButton;

private double scaleX = 0.75; // how we've scaled everything to fit
private double scaleY = 0.75;

private static final Color ENABLED_COLOR = Color.white;
private static final Color DISABLED_COLOR = new Color(200,200,200);
private static final Color DISABLED_TEXT = Color.darkGray;
private static final String[] PLAYER_DICE_COLORS = {"b", "c", "g", "o", "p", "v", "w", "y"};
private static final Integer UI_LAYER = 2;
private static final Integer DICE_LAYER = 3;
private Map<ShotMarkers, JLabel> shotMarkerLabels = new HashMap<>();

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
      Image img = icon.getImage();
      int scaledWidth = icon.getIconWidth() * 3 / 4; // scaling board
      int scaledHeight = icon.getIconHeight() * 3 / 4;

      Image scaledImg = img.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
      icon = new ImageIcon(scaledImg);
      boardlabel.setIcon(icon); 
      boardlabel.setBounds(0,0,scaledWidth,scaledHeight);
   
      // Add the board to the lowest layer
      bPane.add(boardlabel, 0);
   
      
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
      playerlabel.setBounds(114,227,46,46);// Set the size of the GUI
      setPreferredSize(new Dimension(icon.getIconWidth() + 250, icon.getIconHeight() + 120));
      pack();
      playerlabel.setVisible(false);
      bPane.add(playerlabel, DICE_LAYER);
   
      // Create the Menu for action buttons
      mLabel = new JLabel("MENU");
      mLabel.setBounds(scaledWidth+40,0,100,20);
      bPane.add(mLabel,UI_LAYER);

      // Create output to show on the board instead of console
      messageLabel = new JLabel("Welcome to Deadwood!");
      messageLabel.setBounds(scaledWidth + 10, 525, 500, 40);
      messageLabel.setForeground(Color.BLACK);
      messageLabel.setFont(new Font("Arial", Font.BOLD, 18));

      bPane.add(messageLabel, UI_LAYER);

      // Create Action buttons
      bAct = new JButton("ACT");
      bAct.setBackground(Color.white);
      bAct.setBounds(scaledWidth+10,230,100, 100);
      bAct.addMouseListener(new boardMouseListener());

      bMove = new JButton("MOVE");
      bMove.setBackground(Color.white);
      bMove.setBounds(scaledWidth+10, 30,100, 100);
      bMove.addMouseListener(new boardMouseListener());
      
      bRehearse = new JButton("REHEARSE");
      bRehearse.setBackground(Color.white);
      bRehearse.setBounds(scaledWidth+10,130,100, 100);
      bRehearse.addMouseListener(new boardMouseListener());
      
      bUpgrade = new JButton("UPGRADE");
      bUpgrade.setEnabled(false);
      bUpgrade.setToolTipText("Upgrade rank at Casting Office");
      bUpgrade.setBackground(new Color(200,200,200));
      bUpgrade.setBounds(scaledWidth+10,330,100, 100);
      bUpgrade.addMouseListener(new boardMouseListener());

      bEndTurn = new JButton("END TURN");
      bEndTurn.setBackground(Color.white);
      bEndTurn.setBounds(scaledWidth+10,430,100, 100);
      bEndTurn.addMouseListener(new boardMouseListener());

      // Create Player info area
      playerPanel = new JPanel();
      playerPanel.setLayout(new BorderLayout());
      playerPanel.setBounds(20, scaledHeight + 10, scaledWidth - 40, 150);
      playerPanel.setBorder(BorderFactory.createTitledBorder("Players"));

      String[] columns = {"Player","Rank","Money","Credits","Chips","Score"};

      playerTableModel = new DefaultTableModel(columns,0);

      playerTable = new JTable(playerTableModel);
      playerTable.setRowHeight(22);
      playerTable.setEnabled(false);
      playerTable.getTableHeader().setReorderingAllowed(false);

      JScrollPane scrollPane = new JScrollPane(playerTable);

      playerPanel.add(scrollPane, BorderLayout.CENTER);
      bPane.add(playerPanel, UI_LAYER);

      // Place the action buttons in the top layer
      bPane.add(bMove, UI_LAYER);
      bPane.add(bAct, UI_LAYER);
      bPane.add(bRehearse, UI_LAYER);
      bPane.add(bUpgrade, UI_LAYER);
      bPane.add(bEndTurn, UI_LAYER);

      // Set the size of the GUI
      setPreferredSize(new Dimension(icon.getIconWidth() + 250, icon.getIconHeight() + 120));
      pack();
      setLocationRelativeTo(null);

      setVisible(true);


   }

   // This class implements Mouse Events
   class boardMouseListener implements MouseListener{
      // Code for the different button clicks
      public void mouseClicked(MouseEvent e) {
         if (!((JButton)e.getSource()).isEnabled()) return;
         
         if (e.getSource() == bAct){
            //playerlabel.setVisible(true);
            clearMoveButtons();
            game.actCurrentPlayer();
         }
         else if (e.getSource() == bRehearse){
            clearMoveButtons();
            game.rehearseCurrentPlayer();
         }
         else if (e.getSource() == bMove){
            disableButton(bAct);
            disableButton(bRehearse);
            disableButton(bUpgrade);
            disableButton(bEndTurn);
            game.moveCurrentPlayer();
         }
         else if (e.getSource() == bUpgrade){
            clearMoveButtons();
            game.upgradeCurrentPlayer();
         }
         else if (e.getSource() == bEndTurn){
            clearMoveButtons();
            clearRoleDropdown();
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
      if (bonusMessageLabel != null) { // to clearn the bonus messages if not needed here
         bonusMessageLabel.setVisible(false);
         bonusMessageLabel.setText("");
      }
   }

   // Method to update player info
   public void updatePlayerInfo(List<Player> players) { 
      playerTableModel.setRowCount(0);

      for(Player p : players) {
         String role = ""; // placeholder empty
         if(p.getRole() != null) {
            role = p.getRole().getName();
         }

         Object[] row = {
            p.getName(),
            p.getRank(),
            p.getMoney(),
            p.getCredits(),
            p.getChips(),
            p.getFinalScore()
         };

         playerTableModel.addRow(row);
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
            enableButton(bEndTurn);
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
      clearRoleDropdown();
      displayMessage("Would you like to take a role?");

      int startX = boardlabel.getWidth() + 50;
      int startY = 570;

      String[] roleNames = new String[roles.size()];
      for (int i = 0; i < roles.size(); i++) {
         Role r = roles.get(i);
         roleNames[i] = r.getName() + " (Rank " + r.getRequiredRank() + ")";
         
      }

      roleDropdown = new JComboBox<>(roleNames);
      roleDropdown.setBounds(startX, startY, 200, 30);

      takeRoleButton = createButton("Take Role", startX, startY + 40, 200, 30);
      denyRoleButton = createButton("Deny Role", startX, startY + 80, 200, 30);

      takeRoleButton.addActionListener(e -> {
         int index = roleDropdown.getSelectedIndex();
         Role chosenRole = roles.get(index);

         if(player.getRank() < chosenRole.getRequiredRank()) {
            displayMessage(player.getName() + "'s rank is too low for this role!");
            return;
         }
         player.takeRole(chosenRole);
         int playerIndex = game.getPlayerIndex(player);
         movePlayerToRole(playerIndex, player, chosenRole);
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
         46, 
         46
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
      int newX = (int)(x * scaleX);
      int newY = (int)(y * scaleY);

      int offSetX = (playerIndex % 3) * 20; // added offset on player die location to avoid stacking if in same location
      int offSetY = (playerIndex / 3) * 20;
      dice.setLocation(newX + offSetX, newY + offSetY);

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

   public void enableActionButtons() {
      enableButton(bMove);
      enableButton(bAct);
      enableButton(bRehearse);
      enableButton(bUpgrade);
      enableButton(bEndTurn);
   }

   public void resetActionButtons() {
      enableButton(bMove);
      enableButton(bEndTurn);
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
         enableButton(bMove);
         disableButton(bAct);
         disableButton(bRehearse);
      }
      else {
         enableButton(bAct);
         enableButton(bRehearse);
         disableButton(bMove);
      }

   }

   public void clearRoleDropdown() {
      if (roleDropdown != null) {
        bPane.remove(roleDropdown);
        roleDropdown = null;
      }

      if (takeRoleButton != null) {
         bPane.remove(takeRoleButton);
         takeRoleButton = null;
      }

      if (denyRoleButton != null) {
         bPane.remove(denyRoleButton);
         denyRoleButton = null;
      }

      refreshUI();
   }

   public void placeSceneCard(SetLocation set) {
      Scene scene = set.getScene();
      if (scene == null) return;
      int cardWidth = 160; 
      int cardHeight = 90;

      ImageIcon back = new ImageIcon("images/Cardback.png");  //UPDATE WHEN NEW back.png ADDED
      Image scaledImg = back.getImage().getScaledInstance(cardWidth, cardHeight, Image.SCALE_SMOOTH);
      ImageIcon scaledBack = new ImageIcon(scaledImg);

      JLabel card = new JLabel(scaledBack);

      int scaledX = (int)(set.getX() * scaleX);
      int scaledY = (int)(set.getY() * scaleY);

      card.setBounds(scaledX, scaledY, cardWidth, cardHeight);

      sceneCardLabels.put(set, card);

      bPane.add(card, JLayeredPane.PALETTE_LAYER);

      refreshUI();
   }
   
   public void revealSceneCard(SetLocation set) {
      JLabel card = sceneCardLabels.get(set);

      Scene scene = set.getScene();
      ImageIcon face = new ImageIcon("images/Card/" + scene.getImage());
      Image img = face.getImage();
      int scaledWidth = 160; // We can scale this to fit the board
      int scaledHeight = 90; 
      Image scaledImg = img.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
      
      card.setIcon(new ImageIcon(scaledImg));

      card.setSize(scaledWidth, scaledHeight);
      card.setOpaque(false);

      refreshUI();
   }

   // Removes scene card after wrapup
   public void removeSceneCard(SetLocation set) {
      JLabel card = sceneCardLabels.get(set);

      if (card != null) {
         bPane.remove(card);
         sceneCardLabels.remove(set);

      }
      refreshUI();
         
   }
   

   public void movePlayerToRole(int playerIndex, Player player, Role role) {

    JLabel dice = playerDice.get(playerIndex);

    int x = role.getX();
    int y = role.getY();

    if (role.isOnCard()) {
      Location loc = player.getLocation();

      if (loc instanceof SetLocation) {
         x += loc.getX();
         y += loc.getY();
         
      }
    }

    int scaledX = (int)(x * scaleX);
    int scaledY = (int)(y * scaleY);

    dice.setLocation(scaledX, scaledY);

    refreshUI();
   }

   public void updateDiceRank(int playerIndex, int rank) {
      JLabel dice = playerDice.get(playerIndex);

      String color = PLAYER_DICE_COLORS[playerIndex];

      String file = "images/Dice/" + color + rank + ".png";

      dice.setIcon(new ImageIcon(file));

      refreshUI();
   }

   public void clearSceneCards() {
    for (Map.Entry<SetLocation, JLabel> entry : sceneCardLabels.entrySet()) {
        JLabel cardLabel = entry.getValue();
        bPane.remove(cardLabel);
        cardLabel.setOpaque(false);
        cardLabel.repaint();
    }

    sceneCardLabels.clear();
    refreshUI();


   }

   public void placeShotMarkers(SetLocation set) {
      ImageIcon shotIcon = new ImageIcon("images/shot.png");

      int index = 0;

      for (ShotMarkers shot : set.getShots()) {

         JLabel marker = new JLabel(shotIcon);

        int scaledX = (int)(shot.getX() * scaleX);
        int scaledY = (int)(shot.getY() * scaleY);

        int scaledW = (int)(shotIcon.getIconWidth() * scaleX);
        int scaledH = (int)(shotIcon.getIconHeight() * scaleY);

         marker.setBounds(scaledX, scaledY, scaledW, scaledH);

         shotMarkerLabels.put(shot, marker);

         bPane.add(marker, JLayeredPane.PALETTE_LAYER);

         index++;

      }

      refreshUI();
      
   }

   public void removeShotMarker(SetLocation set) {
      List<ShotMarkers> shots = set.getShots();

      int remaining = set.getShotsRemaining();

      if (remaining < shots.size()) {
         ShotMarkers shot = shots.get(remaining);

         JLabel marker = shotMarkerLabels.get(shot);

         if (marker != null) {
            bPane.remove(marker);
            shotMarkerLabels.remove(shot);
            
         }
         
      }

      refreshUI();
   }

   public void clearShotMarkers() {
      for (JLabel marker : shotMarkerLabels.values()) {
         bPane.remove(marker);
         
      }

      shotMarkerLabels.clear();

      refreshUI();
   }

   public void showGameOverScreen(List<Player> sortedPlayers) {
      disableButton(bMove);
      disableButton(bAct);
      disableButton(bRehearse);
      disableButton(bUpgrade);
      disableButton(bEndTurn);

      clearShotMarkers();
      clearSceneCards();

      displayMessage("GAME OVER! - Final Scores");

      playerTableModel.setRowCount(0);

      for (Player p : sortedPlayers) {
         Object[] row = {
            p.getName(),
            p.getRank(),
            p.getMoney(),
            p.getCredits(),
            p.getChips(),
            p.getFinalScore()
         };
         playerTableModel.addRow(row);
         
      }

      playerTable.setRowSelectionInterval(0, 0);
      playerTable.setSelectionBackground(Color.YELLOW);

      playerTable.getColumnModel().getColumn(5).setHeaderValue("Final Score");

      refreshUI();
   }

   public void displayBonusMessage(String text) {
      if (bonusMessageLabel == null) {
         bonusMessageLabel = new JLabel();
         bonusMessageLabel.setForeground(Color.BLACK);
         bonusMessageLabel.setFont(new Font("Arial", Font.BOLD, 18));
         bPane.add(bonusMessageLabel, UI_LAYER);
      }

      int mainX = messageLabel.getX();
      int mainY = messageLabel.getY();
      int mainWidth = messageLabel.getWidth();

      bonusMessageLabel.setText(text);
      bonusMessageLabel.setBounds(mainX, mainY + 30, mainWidth, 30);
      bonusMessageLabel.setVisible(true);

      refreshUI();
   }
}