package org.shadowbot.bot.client.script.randomsevent;

import org.shadowbot.bot.api.RandomEvent;
import org.shadowbot.bot.api.methods.data.Calculations;
import org.shadowbot.bot.api.methods.data.Game;
import org.shadowbot.bot.api.methods.interactive.NPCs;
import org.shadowbot.bot.api.methods.interactive.Widgets;
import org.shadowbot.bot.api.util.Random;
import org.shadowbot.bot.api.util.Time;
import org.shadowbot.bot.api.wrapper.WidgetChild;


/**
 * Created with IntelliJ IDEA.
 * User: Magorium
 * Date: 8/18/13
 * Time: 3:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class QuizMasterHandler extends RandomEvent {

    private final String QUIZ_MASTER = "Quiz Master";
    private final int WIDGET_ANSWER = 191, WIDGET_OPTION = 228;
    public int[] Fish = {6190, 6189};
    public int[] Jewelry = {6198, 6197};
    public int[] Weapons = {6191, 6192};
    public int[] Armors = {6193, 6194};
    public int[] Farming = {6195, 6196};
    private final int[][] ANSWERS = {Fish, Jewelry, Armors, Weapons, Farming};

    @Override
    public String getAuthor() {
        return "Magorium, Lorex";
    }

    @Override
    public String getName() {
        return "Quiz Master";
    }

    @Override
    public boolean isActive() {
        return Game.isLoggedIn() && NPCs.getNearest(QUIZ_MASTER) != null;
    }

    @Override
    public int solve() {
        if (Widgets.getWidget(WIDGET_OPTION).isValid()) {
            Widgets.getWidget(WIDGET_OPTION, 1).click(true);
            for (int i = 0; i < 40 && Widgets.getWidget(WIDGET_OPTION).isValid(); i++, Time.sleep(100, 150)) ;
        } else if (Widgets.getWidget(WIDGET_ANSWER).isValid()) {
            for (WidgetChild widgetchild : Widgets.getWidget(WIDGET_ANSWER).getChildren()) {
                int[] answer = getModelID();
                if (answer == null) {

                } else {
                    if (widgetchild != null && Calculations.stackContain(answer, widgetchild.getModelId())) {
                        widgetchild.click(true);
                        for (int i = 0; i < 40 && Widgets.getWidget(WIDGET_ANSWER).isValid(); i++, Time.sleep(100, 150))
                            ;
                    }
                }
            }
        }
        return Random.nextInt(150, 200);
    }

    private int[] getModelID() {
        for (int[] a : ANSWERS) {
            if (getCount(a) == 1) {
                return a;
            }
        }
        return null;
    }

    private int getCount(int[] Answers) {
        int count = 0;
        if (Widgets.getWidget(WIDGET_ANSWER).isValid()) {
            for (WidgetChild widgetchild : Widgets.getWidget(WIDGET_ANSWER).getChildren()) {
                if (widgetchild != null && Calculations.stackContain(Answers, widgetchild.getModelId())) {
                    count++;
                }
            }
        }
        return count;
    }
}

