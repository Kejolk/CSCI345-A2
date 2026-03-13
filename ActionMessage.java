public class ActionMessage { // class for storing messages for gui (keeps model away from gui for messages)
    private String mainMessage;
    private String bonusMessage;

    public ActionMessage(String mainMessage, String bonusMessage) {
        this.mainMessage = mainMessage;
        this.bonusMessage = bonusMessage;
    }
    
    public String getMainMessage() {
        return mainMessage;
    }

    public String getBonusMessage() {
        return bonusMessage;
    }
}