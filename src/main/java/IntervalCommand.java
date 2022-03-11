import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.regex.PatternSyntaxException;

public class IntervalCommand extends Command {
    private final IntervalBehavior intervalBehavior = new IntervalBehavior();

    @Override
    public String getBaseName() {
        return "interval";
    }

    @Override
    public String[] getCommand() {
        return new String[]{"interval <hh:mm> <interval>", "interval stop"};
    }

    private String[] getDescription() {
        return new String[]{"Send alert every <interval> minutes from <hh:mm>", "Terminate alarm"};
    }

    private String getText() {
        StringBuilder result = new StringBuilder();
        String[] thisCommands = getCommand();
        String[] thisDescriptions = getDescription();
        for (int i = 0; i < thisCommands.length; i++) {
            result.append(String.format("%s - %s\n", thisCommands[i], thisDescriptions[i]));
        }
        return result.toString();
    }

    @Override
    public void runCommand(String[] commandWords, MessageReceivedEvent event) {
        try {
            System.out.println("interval test: " + Arrays.toString(commandWords));
            updateCurrentParam(commandWords, event);
            verifyCommand(commandWords);

            action(commandWords, event);

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
        EmbedBuilder messageBuilder = new EmbedBuilder(Main.defaultEmbedMessageBuilder);
        messageBuilder.setDescription(getText());
        event.getChannel().sendMessageEmbeds(messageBuilder.build()).queue();
    }


    private void verifyCommand(String[] commandWords) throws IncorrectCommandException {
        if (commandWords.length < 1 || commandWords.length > 2) {
            throw new IncorrectCommandException();
        }
    }

    private void action(String[] commandWords, MessageReceivedEvent event) {
        try {
            if (commandWords[0].equals("stop")) {
                intervalBehavior.stopTimer(event);
                return;
            }
            String[] hourMinString = commandWords[0].split(":");
            if (hourMinString.length != 2) {
                throw new IncorrectCommandException();
            }

            final int hour = Integer.parseInt(hourMinString[0]);
            final int minute = Integer.parseInt(hourMinString[1]);

            if (commandWords.length == 2) {
                final int intervalInMinute = Integer.parseInt(commandWords[1]);
                intervalBehavior.setTimer(hour, minute, intervalInMinute, event);
            } else {
                intervalBehavior.setTimer(hour, minute, event);
            }

        } catch (NumberFormatException | PatternSyntaxException | IncorrectCommandException e) {
            sendIncorrectParamMessage(event);
        }
    }

}

