package org.shadowbot.bot.client.script.randomsevent;

import org.shadowbot.bot.api.RandomEvent;
import org.shadowbot.bot.api.methods.interactive.Widgets;
import org.shadowbot.bot.api.util.Random;
import org.shadowbot.bot.api.util.Time;
import org.shadowbot.bot.api.wrapper.WidgetChild;

/**
 * Created with IntelliJ IDEA.
 * User: Lorex
 * Date: 11/3/13
 * Time: 6:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportHandler extends RandomEvent {
    @Override
    public boolean isActive() {
        final String text = Widgets.getWidget(553, 15).getText();
        if (text != null) {
            return !text.equals("<col=ffff00>|");
        }
        return false;
    }

    @Override
    public String getAuthor() {
        return "Lorex";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getName() {
        return "Report";
    }

    @Override
    public int solve() {
        this.setStatus("We don't report ;)");
        WidgetChild close = Widgets.getWidget(553, 10);
        if (close != null && close.isVisible()) {
            close.click(true);
            for (int i = 0; i < 60 && this.isActive(); i++) {
                Time.sleep(40, 60);
            }
        }
        return Random.nextInt(150, 250);
    }
}
