import java.util.ArrayList;
import java.util.List;

public class GameManager {
    
    List<Player> players = new ArrayList<>();
    Board board = new Board();
    int currentPlayerIndex;
    int currentDay;
    int maxDays;
    boolean gameRunning;

    public static void startGame() {}

    public static void startDay() {}

    public static void endDay() {}

    public static void nextTurn() {}

    public static void calcFinalScore() {}

    public static Player getCurrPlayer() {
        return null;
    }

}
