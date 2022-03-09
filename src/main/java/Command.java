import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class Command {
    private String currentParam;

    public abstract String getBaseName();
    public abstract void runCommand(String[] commandWords, MessageReceivedEvent event);

    protected String getCurrentParam() {
        return currentParam;
    }

    protected void updateCurrentParam(String[] commandWords, MessageReceivedEvent event) throws NoParamException {
        currentParam = commandWords.length > 0 ? commandWords[0] : null;
        if (currentParam == null) {
            throw new NoParamException();
        }
    }

    protected void sendNoSuchCommandMessage(MessageReceivedEvent event) {
        event.getChannel().sendMessage("No such command: " + currentParam + ".").queue();
    }

    protected void sendIncorrectParamMessage(MessageReceivedEvent event) {
        event.getChannel().sendMessage("Incorrect parameter: " + currentParam + ".").queue();
    }

    protected void sendGeneralErrorMessage(Exception e, MessageReceivedEvent event) {
        System.out.println(e.getMessage());
        event.getChannel().sendMessage("Error: Please try again.").queue();
    }

    protected abstract void sendHelpMessage(MessageReceivedEvent event);
}
