package pro.sky.telegrambot.utils;

public enum Command {

   COMMAND_START ("/start", ""),
   COMMAND_HELP("/help", ""),
   COMMAND_SETTINGS( "/settings" , ""),
   COMMAND_REGISTRATION ("/registration", "");
    public final String commandText;
    public final String description;
    Command(String commandText, String description) {
        this.commandText = commandText;
        this.description = description;
    }
}
