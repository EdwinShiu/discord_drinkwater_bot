import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SettingCommand extends Command {
    @Override
    public String getBaseName() {
        return null;
    }

    @Override
    public void runCommand(String[] commandWords, MessageReceivedEvent event) {

    }

    @Override
    protected void sendHelpMessage(MessageReceivedEvent event) {
        event.getChannel().sendMessage("Setting Help Message").queue();
    }
}
