// Created by Sukhman Lally
// Implemented by Arvind Ramesh

public class Scene {
    private String title;
    private int budget;
    private Role[] roles;
    private boolean isRevealed;

    public Scene(String title, int budget, Role[] roles) {
        this.title = title;
        this.budget = budget;
        this.roles = roles;
        this.isRevealed = false;
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
}
