// Implemented by Sukhman

import java.util.*;

public class SetLocation extends Location {
    private Scene scene;
    private ArrayList<Role> roles = new ArrayList<>();
    private int shotsRemaining;
    private int initialShots;

    public SetLocation(String name, int shotsRemaining) {
        super(name);
        this.shotsRemaining = shotsRemaining;
        this.initialShots = shotsRemaining;
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

    public void setShotsRemaining(int shots) { // used for testing
        shotsRemaining = shots;
    }

    public void revealScene() {
        if(scene != null) {
            scene.reveal();
        }
    }
    public void removeShot() {
        if(shotsRemaining > 0) {
            shotsRemaining--;
            if(shotsRemaining == 0) {
                wrapScene();
            }
        }
    }
    public boolean isSceneComplete() { 
        return shotsRemaining <= 0; // returns a boolean (true if <= 0) 
    }

    public void wrapScene() {
        if(scene == null) {
            return; // needed for day 1 due to how board distributes scenes
        }

        bonuses();

        for(Role role : roles) {
            role.removePlayer();
        }

        for(Role role : scene.getRoles()) {
            role.removePlayer();
        }

    }

    public void bonuses() {
        if (scene == null) {
            return;
        }

        ArrayList<Role> sceneRoles = new ArrayList<>();
        for(Role role : scene.getRoles()) {
            sceneRoles.add(role);
        }

        boolean onCardPlayer = false;
        for(Role role : sceneRoles) { // if no on-card player, no bonus
            if(role.getOccupiedBy() != null) {
                onCardPlayer = true;
                break;
            }
        }

        if(!onCardPlayer) {
            System.out.println("No player on-card therefore no bonuses paid out!");
            return;
        }

        int budget = scene.getBudget();
        ArrayList<Integer> diceRolls = new ArrayList<>();
        sceneRoles.sort((r1, r2) -> Integer.compare(r2.getRequiredRank(), r1.getRequiredRank())); // sorts the roles by rank
        for(int i = 0; i < budget; i++) {
            diceRolls.add((int)(Math.random() * 6) + 1);
        }
        Collections.sort(diceRolls, Collections.reverseOrder()); // sort in descending order

        int index = 0;
        for(int die : diceRolls) {
            Role currentRole = sceneRoles.get(index);
            Player player = currentRole.getOccupiedBy();
            if(player != null) {
                player.addMoney(die);
            }
            index++;

            if(index >= sceneRoles.size()) {
                index = 0; // wrap back around as in accordance with the rules
            }
        }

        for(Role role : roles) {
            Player player = role.getOccupiedBy();
            if(player != null) {
                    player.addMoney(role.getRequiredRank());
            }
        }
        System.out.println("Bonuses have been paid out!");
    }

    public void resetShots() {
        this.shotsRemaining = initialShots;
    }

    public List<Role> getAvailableRoles() { // returns list of available roles
        List<Role> available = new ArrayList<>();

        for (Role ro : roles) {
            if (ro.isAvailable()){
                available.add(ro);
            }
        }
        for (Role rol : scene.getRoles()) {
            if(rol.isAvailable()) {
                available.add(rol);
            }
        }
        return available;
    }
}