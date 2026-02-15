// Created by Sukhman Lally
// Implemented by Arvind Ramesh

public class Scene {
    private String title;
    private int budget;
    private Role[] roles;
    private boolean isRevealed;
    private boolean isComplete;

    public Scene(String title, int budget, Role[] roles) {
        this.title = title;
        this.budget = budget;
        this.roles = roles;
        this.isRevealed = false;
        this.isComplete = false;
    }


    public int getBudget() {
        return budget; 
    }

    public String getTitle() {
        return title;
    }

    public Role[] getRoles() {
        return roles;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    /**
     * Resets scene for a new day
     */
    public void reset() {
        isRevealed = false;
        isComplete = false;

        if (roles != null) {
            for (Role role : roles) {
                if (role != null) {
                    role.removePlayer();
            
                }
            }
            
        }

    }

    public void reveal() {
        isRevealed = true;
        System.out.println("Scene revealed: " + title);
    }

    public boolean isComplete() {
        return isComplete; 
    }
}