import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class IntervalBehavior {
    private Timer timer;

    public void setTimer(int hour, int minute, int intervalInMinute, MessageReceivedEvent event) {
        stopTimer(event);
        timer = new Timer();
        final Calendar cal = Calendar.getInstance();
        final Date date = new Date(event.getMessage().getTimeCreated().toEpochSecond() * 1000);

        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        while (date.compareTo(cal.getTime()) > 0) {
            cal.add(Calendar.MINUTE, intervalInMinute);
        }

        final Date setDate = cal.getTime();
        System.out.printf("Now: %s\n", date);
        System.out.printf("Set: %s\n", setDate);

        timer.scheduleAtFixedRate(new MessageTimerTask(event), setDate, (long) intervalInMinute * 60 * 1000);
    }

    public void setTimer(int hour, int minute, MessageReceivedEvent event) {
        this.setTimer(hour, minute, 24 * 60, event);
    }

    public void stopTimer(MessageReceivedEvent event) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        event.getChannel().sendMessage("Alarm stopped!").queue();
    }

}

class MessageTimerTask extends TimerTask {
    private final MessageReceivedEvent event;

    MessageTimerTask(MessageReceivedEvent event) {
        this.event = event;
    }

    @Override
    public void run() {
        event.getChannel().sendMessage("Drink Water!").tts(true).queue();
    }

}
