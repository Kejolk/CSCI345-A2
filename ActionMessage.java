public class ActionMessage { // class for storing messages for gui (keeps model away from gui for messages)
    private String mainMessage;
    private String bonusMessage;

    public ActionMessage(String mainMessage, String bonusMessage) { // useful for when we have more than one message returning (ie acting/endDay + bonus)
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