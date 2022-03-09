import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.ResourceBundle;

public class Main extends ListenerAdapter {
    private final Command command = new BaseCommand();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        System.out.println("Message from " + event.getAuthor().getName() + ": " + event.getMessage().getContentDisplay());

        final String contentRaw = event.getMessage().getContentRaw();
        String[] commandWords = contentRaw.split("\\s+");
        System.out.println("Command words: " + Arrays.toString(commandWords));
        if (!commandWords[0].equals(command.getBaseName())) {
            return;
        }

        commandWords = commandWords.length > 1 ? Arrays.copyOfRange(commandWords, 1, commandWords.length) : new String[0];
        command.runCommand(commandWords, event);
    }

    public static void main(String[] args) {
        try {
            final ResourceBundle rd = ResourceBundle.getBundle("config");
            final String token = rd.getString("discord_bot_token");
            System.out.println(token);
            JDABuilder builder = JDABuilder.createDefault(token);
            builder.addEventListeners(new Main());
            builder.build();
        } catch (Exception exception) {
            System.out.format("Exception: %s\n", exception);
        }
    }
}
