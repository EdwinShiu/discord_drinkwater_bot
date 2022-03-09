import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;

public class TimeCommand extends Command {

    @Override
    public String getBaseName() {
        return "time";
    }

    @Override
    public void runCommand(String[] commandWords, MessageReceivedEvent event) {
        try {
            System.out.println("interval test: " + Arrays.toString(commandWords));
            verifyCommand(commandWords);

            updateCurrentParam(commandWords, event);

            event.getChannel().sendMessage("TimeCommand").queue();

            // TODO: run command

        } catch (NoParamException e) {
            sendHelpMessage(event);
        } catch (IncorrectCommandException e) {
            sendIncorrectParamMessage(event);
        } catch (Exception e) {
            sendGeneralErrorMessage(e, event);
        }
    }

    @Override
    protected void sendHelpMessage(MessageReceivedEvent event) {
        event.getChannel().sendMessage("Time Help Message").queue();
    }

    private void verifyCommand(String[] commandWords) throws IncorrectCommandException {
        if (commandWords.length != 1) {
            throw new IncorrectCommandException();
        }
    }
}
