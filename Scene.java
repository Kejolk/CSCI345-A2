// Created by Sukhman Lally
// Implemented by Arvind Ramesh
import java.util.*;
public class Scene {
    private String title;
    private int budget;
    private ArrayList<Role> roles;
    private boolean isRevealed;
    private String image;

    public Scene(String title, int budget, ArrayList<Role> roles, String image) {
        this.title = title;
        this.budget = budget;
        this.roles = roles;
        this.image = image;
        this.isRevealed = false;
    }

    public int getBudget() {
        return budget; 
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public String getImage() {
        return image;
    }
    public boolean isRevealed() {
        return isRevealed;
    }


    /**
     * Resets scene for a new day
     */
    public void reset() { 
        isRevealed = false;

        if (roles != null) {
            for (Role role : roles) {
                if (role != null) {
                    role.removePlayer();
            
                }
            }
            
        }

    }

    public void reveal() { // reveals scene 
        isRevealed = true;
        //System.out.println("Scene revealed: " + title);
    }
}
