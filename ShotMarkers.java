public class ShotMarkers {

    private int shotNumber;
    private int x;
    private int y;
    private int h;
    private int w;

    public ShotMarkers(int number, int x, int y, int h, int w) {
        this.shotNumber = number;
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
    }
    public int getShotNumber() { 
        return shotNumber; 
    }
    public int getX() { 
        return x; 
    }
    public int getY() {
        return y; 
    }
    public int getH() {
         return h; 
    }
    public int getW() {
         return w; 
    }
}