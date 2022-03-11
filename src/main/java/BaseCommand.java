import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class BaseCommand extends Command {
    private final Command[] childCommands = {
        new IntervalCommand(),
    };

    @Override
    public void runCommand(String[] commandWords, MessageReceivedEvent event) {
        try {
            System.out.println("test: " + Arrays.toString(commandWords));

            updateCurrentParam(commandWords, event);

            Command command = decodeCommand();

            commandWords = commandWords.length > 1 ? Arrays.copyOfRange(commandWords, 1, commandWords.length) : new String[0];
            command.runCommand(commandWords, event);
        } catch (NoParamException e) {
            sendHelpMessage(event);
        } catch (IncorrectCommandException e) {
            sendNoSuchCommandMessage(event);
        } catch (Exception e) {
            sendGeneralErrorMessage(e, event);
        }
    }

    @Override
    public String getBaseName() {
        return "!dw";
    }

    @Override
    public String[] getCommand() {
        ArrayList<String> result = new ArrayList<>();
        String[] thisCommands = new String[]{"!dw"};
        for (String thisCommand : thisCommands) {
            for (Command childCommand : childCommands) {
                for (String command : childCommand.getCommand()) {
                    result.add(String.format("%s %s", thisCommand, command));
                }
            }
        }
        return result.toArray(new String[0]);
    }

    @Override
    protected void sendHelpMessage(MessageReceivedEvent event) {
        EmbedBuilder messageBuilder = new EmbedBuilder(Main.defaultEmbedMessageBuilder);
        messageBuilder.setDescription(getText());
        event.getChannel().sendMessageEmbeds(messageBuilder.build()).queue();
    }

    private String getText() {
        StringBuilder result = new StringBuilder();
        String[] thisCommands = getCommand();
        for (String thisCommand : thisCommands) {
            result.append(String.format("%s\n", thisCommand));
        }
        return result.toString();
    }


    private Command decodeCommand() throws IncorrectCommandException {
        var commandFound =  Arrays.stream(childCommands)
                .filter((command) -> command.getBaseName().equals(getCurrentParam()))
                .findFirst();
        if (commandFound.isEmpty()) {
            throw new IncorrectCommandException();
        }
        return commandFound.get();
    }

}
