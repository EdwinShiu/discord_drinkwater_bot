import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class Command {
    private String currentParam;

    public abstract String getBaseName();
    public abstract String[] getCommand();
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
        EmbedBuilder messageBuilder = new EmbedBuilder(Main.defaultEmbedMessageBuilder);
        messageBuilder.setDescription("No such command: " + currentParam + ".");
        event.getChannel().sendMessageEmbeds(messageBuilder.build()).queue();
    }

    protected void sendIncorrectParamMessage(MessageReceivedEvent event) {
        EmbedBuilder messageBuilder = new EmbedBuilder(Main.defaultEmbedMessageBuilder);
        messageBuilder.setDescription("Incorrect parameter: " + currentParam + ".");
        event.getChannel().sendMessageEmbeds(messageBuilder.build()).queue();
    }

    protected void sendGeneralErrorMessage(Exception e, MessageReceivedEvent event) {
        System.out.println(e.getMessage());
        EmbedBuilder messageBuilder = new EmbedBuilder(Main.defaultEmbedMessageBuilder);
        messageBuilder.setDescription("Error: Please try again.");
        event.getChannel().sendMessageEmbeds(messageBuilder.build()).queue();
    }

    protected abstract void sendHelpMessage(MessageReceivedEvent event);
}
