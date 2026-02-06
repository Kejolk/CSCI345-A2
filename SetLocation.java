// Created by Sukhman Lally

public class SetLocation extends Location {
    private Scene scene;
    private Role[] roles;
    private int shotsRemaining;

    public SetLocation(String name) {
        super(name);
    }

    public int getShotsRemaining() {
        return shotsRemaining;
    }

    public void setRole(Role[] roles) {
        this.roles = roles;
    }

    public void revealScene() {}
    public void removeShot() {}
    public boolean isSceneComplete() { 
    return false; 
    }
    public void wrapScene() {}
}  