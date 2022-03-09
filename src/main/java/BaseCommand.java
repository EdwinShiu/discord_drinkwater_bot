import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;

public class BaseCommand extends Command {
    private final Command[] childCommands = {
        new IntervalCommand(),
        new TimeCommand(),
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
    protected void sendHelpMessage(MessageReceivedEvent event) {
        event.getChannel().sendMessage("Help Message").queue();
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
