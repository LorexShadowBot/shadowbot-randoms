package org.shadowbot.bot.client.script.randomsevent;

import org.shadowbot.bot.api.RandomEvent;
import org.shadowbot.bot.api.methods.data.Calculations;
import org.shadowbot.bot.api.methods.data.Game;
import org.shadowbot.bot.api.methods.data.Inventory;
import org.shadowbot.bot.api.methods.interactive.Widgets;
import org.shadowbot.bot.api.util.Log;
import org.shadowbot.bot.api.util.Random;
import org.shadowbot.bot.api.util.Time;
import org.shadowbot.bot.api.wrapper.WidgetChild;


/**
 * Created with IntelliJ IDEA.
 * User: Magorium
 * Date: 8/6/13
 * Time: 2:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class StrangeBoxHandler extends RandomEvent {

    private final int BOX_ID = 3062;
    private final int WIDGET_BOX = 190;

    @Override
    public String getAuthor() {
        return "Magorium, Lorex";
    }

    @Override
    public String getName() {
        return "Strange Box";
    }

    @Override
    public boolean isActive() {
        return Game.isLoggedIn() && Inventory.contains(BOX_ID);
    }

    @Override
    public int solve() {

        if (Widgets.getWidget(WIDGET_BOX).isValid()) {
            WidgetChild wid = getWidgetWith(getAnswer(getQuestion()));
            if (wid != null) {
                wid.interact("Ok");
                for (int i = 0; i < 50 && Widgets.getWidget(WIDGET_BOX).isValid(); i++, Time.sleep(100, 150)) ;
            }else{
                Log.info("Error : No Solve Found");
            }
        } else {
            Inventory.getItem(BOX_ID).interact("Open");
            for (int i = 0; i < 50 && !Widgets.getWidget(WIDGET_BOX).isValid(); i++, Time.sleep(100, 150)) ;
        }
        return Random.nextInt(150, 200);
    }

    private String getQuestion() {
        return Widgets.getWidget(WIDGET_BOX, 6).getText();
    }

    private String getAnswer(String question) {
        question = question.toLowerCase();
        String[] shape = {"", "", ""};
        String[] number = {"", "", ""};

        int[] circle = {7005, 7020, 7035};
        int[] pentagon = {7006, 7021, 7036};
        int[] square = {7007, 7022, 7037};
        int[] star = {7008, 7023, 7038};
        int[] triangle = {7009, 7024, 7039};

        int[][] shape_id = {circle, pentagon, square, star, triangle};
        String[] shape_name = {"circle", "pentagon", "square", "star", "triangle"};

        int[] n0 = {7010, 7025, 7040};
        int[] n1 = {7011, 7026, 7041};
        int[] n2 = {7012, 7027, 7042};
        int[] n3 = {7013, 7028, 7043};
        int[] n4 = {7014, 7029, 7044};
        int[] n5 = {7015, 7030, 7045};
        int[] n6 = {7016, 7031, 7046};
        int[] n7 = {7017, 7032, 7047};
        int[] n8 = {7018, 7033, 7048};
        int[] n9 = {7019, 7034, 7049};

        int[][] number_id = {n1, n2, n3, n4, n5, n6, n7, n8, n9};
        String[] number_name = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};

        for (int i = 0; i < 3; i++) {
            for (int a = 0; a < shape_id.length; a++) {
                if (Calculations.stackContain(shape_id[a], Widgets.getWidget(WIDGET_BOX, i).getModelId())) {
                    shape[i] = shape_name[a];
                }
            }
        }
        for (int i = 3; i < 6; i++) {
            for (int a = 0; a < number_id.length; a++) {
                if (Calculations.stackContain(number_id[a], Widgets.getWidget(WIDGET_BOX, i).getModelId())) {
                    number[(i - 3)] = number_name[a];
                }
            }
        }
        if (question.contains("shape has")) {
            for (int i = 0; i < number.length; i++) {
                if (question.toLowerCase().contains(number[i].toLowerCase())) {
                    return shape[i];
                }
            }
        } else if (question.contains("number is")) {
            for (int i = 0; i < shape.length; i++) {
                if (question.toLowerCase().contains(shape[i].toLowerCase())) {
                    return number[i];
                }
            }
        }

        return "null";
    }

    private WidgetChild getWidgetWith(String answer) {
        for (int i = 10; i < 13; i++) {
            if (Widgets.getWidget(WIDGET_BOX, i).getText().toLowerCase().contains(answer.toLowerCase())) {
                return Widgets.getWidget(WIDGET_BOX, i - 3);
            }
        }
        return null;
    }
}
