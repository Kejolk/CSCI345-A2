// Implemented by Sukhman

import java.util.ArrayList;

public class SetLocation extends Location {
    private Scene scene;
    private ArrayList<Role> roles = new ArrayList<>();
    private int shotsRemaining;

    public SetLocation(String name, int shotsRemaining) {
        super(name);
        this.shotsRemaining = shotsRemaining;
    }

    public int getShotsRemaining() {
        return shotsRemaining;
    }

    public Scene getScene() {
        return scene;
    }
    
    public ArrayList<Role> getRoles() {
        return roles;
    }

    public void setScene(Scene scene) { // To assign a scene card to a location
        this.scene = scene;
    }

    public void setRole(ArrayList<Role> roles) {
        this.roles = roles;
    }

    public void revealScene() {
        if(scene != null) {
            scene.reveal();
        }
    }
    public void removeShot() {
        if(shotsRemaining > 0) {
            shotsRemaining--;
        }
    }
    public boolean isSceneComplete() { 
        return shotsRemaining <= 0; // returns a boolean (true if <= 0) 
    }

    public void wrapScene() {
        if(roles != null) {
            for(Role role : roles) {
                role.removePlayer();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Testing for compile: SetLocation");
    }
}  