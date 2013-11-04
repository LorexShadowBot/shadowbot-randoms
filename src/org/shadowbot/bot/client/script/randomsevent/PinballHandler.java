package org.shadowbot.bot.client.script.randomsevent;

import org.shadowbot.bot.api.RandomEvent;
import org.shadowbot.bot.api.RandomEventHandler;
import org.shadowbot.bot.api.methods.data.Game;
import org.shadowbot.bot.api.methods.data.Menu;
import org.shadowbot.bot.api.methods.data.Settings;
import org.shadowbot.bot.api.methods.data.movement.Camera;
import org.shadowbot.bot.api.methods.data.movement.Walking;
import org.shadowbot.bot.api.methods.input.Mouse;
import org.shadowbot.bot.api.methods.interactive.GameEntities;
import org.shadowbot.bot.api.methods.interactive.NPCs;
import org.shadowbot.bot.api.methods.interactive.Players;
import org.shadowbot.bot.api.methods.interactive.Widgets;
import org.shadowbot.bot.api.util.Filter;
import org.shadowbot.bot.api.util.Log;
import org.shadowbot.bot.api.util.Random;
import org.shadowbot.bot.api.util.Time;
import org.shadowbot.bot.api.wrapper.GameObject;
import org.shadowbot.bot.api.wrapper.GameObjectComposite;
import org.shadowbot.bot.api.wrapper.NPC;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class PinballHandler extends RandomEvent {

    private final int WIDGET_SCORE = 263;
    private static int[] TOWER_SETTINGS_BASE = {512, 546, 580, 614, 648};
    private static ArrayList<Integer> TOWER_SETTING = new ArrayList<>();
    private static final int CONSTANT_VAR = 512;

    @Override
    public String getAuthor() {
        return "Magorium and Lorex";
    }

    @Override
    public String getName() {
        return "Pinball";
    }

    @Override
    public boolean isActive() {
        NPC tilt = NPCs.getNearest("tilt");
        return Game.isLoggedIn() && tilt != null && tilt.distanceTo() < 15;
    }

    @Override
    public int solve() {
        if (Widgets.canContinue()) {
            RandomEventHandler.setStatus("Continuing Widgets");
            Widgets.clickContinue();
        } else if (getScore() < 10) {
            if (Players.getLocal().getAnimation() == -1) {
                RandomEventHandler.setStatus("Determine Target");
                getTowerSettings();
                int ID = determineTarget(Settings.getSetting(727));
                TOWER_SETTING.clear();
                GameObject object = GameEntities.getNearest(ID);
                if (object != null) {
                    if (object.isOnScreen()) {
                        if (interact(object, "Tag")) {
                            for (int i = 0; i < 60 && Players.getLocal().getAnimation() == -1; i++, Time.sleep(100, 150))
                                ;
                            for (int i = 0; i < 60 && Players.getLocal().getAnimation() != -1; i++, Time.sleep(100, 150))
                                ;
                            Time.sleep(1300, 1800);
                        }
                    } else {
                        Camera.turnTo(object);
                    }
                }
            }
        } else {
            RandomEventHandler.setStatus("Exiting");
            final GameObject exit = GameEntities.getNearest(new Filter<GameObject>() {
                @Override
                public boolean accept(GameObject gameObject) {
                    if (gameObject != null) {
                        GameObjectComposite gameObjectComposite = gameObject.getComposite();
                        if (gameObjectComposite != null) {
                            final String name = gameObjectComposite.getName();
                            if (name != null) {
                                return name.equals("Cave Exit");
                            }
                        }
                    }
                    return false;
                }
            });
            if (exit != null) {
                Camera.turnTo(exit);
                if (exit.distanceTo() > 6) {
                    exit.interact("Exit");
                    for (int i = 0; i < 70 && isActive(); i++, Time.sleep(100, 150)) ;
                } else {
                    Walking.walkTo(exit);
                    for (int i = 0; i < 50 && Players.getLocal().isMoving(); i++) {
                        Time.sleep(100, 150);
                    }
                }
            }
        }
        return Random.nextInt(150, 200);
    }

    private int getScore() {
        if (!Widgets.getWidget(WIDGET_SCORE).isValid())
            return 0;
        return Integer.parseInt(Widgets.getWidget(WIDGET_SCORE, 1).getText().replace("Score: ", ""));
    }

    private int determineTarget(int gameSetting) {
        Iterator<Integer> itr = TOWER_SETTING.iterator();
        while (itr.hasNext()) {
            if (TOWER_SETTING.contains(gameSetting)) {
                return getIds()[TOWER_SETTING.indexOf(gameSetting)];
            } else {
                Log.info("Target not found..");
                break;
            }
        }
        return -1;
    }

    private void getTowerSettings() {
        int ourScore = 0;
        if (Widgets.getWidget(WIDGET_SCORE) != null) {
            switch (Widgets.getWidget(WIDGET_SCORE,
                    1).getText()) {
                case "Score: 0":
                    ourScore = 0;
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[0] - CONSTANT_VAR);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[1] - CONSTANT_VAR);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[2] - CONSTANT_VAR);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[3] - CONSTANT_VAR);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[4] - CONSTANT_VAR);
                    break;
                case "Score: 1":
                    ourScore = 1;
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[0]);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[1]);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[2]);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[3]);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[4]);
                    break;
                case "Score: 2":
                    ourScore = 2;
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[0] + CONSTANT_VAR);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[1] + CONSTANT_VAR);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[2] + CONSTANT_VAR);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[3] + CONSTANT_VAR);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[4] + CONSTANT_VAR);
                    break;
                case "Score: 3":
                    ourScore = 3 - 1;
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[0] + CONSTANT_VAR * ourScore);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[1] + CONSTANT_VAR * ourScore);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[2] + CONSTANT_VAR * ourScore);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[3] + CONSTANT_VAR * ourScore);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[4] + CONSTANT_VAR * ourScore);
                    break;
                case "Score: 4":
                    ourScore = 4 - 1;
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[0] + CONSTANT_VAR * ourScore);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[1] + CONSTANT_VAR * ourScore);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[2] + CONSTANT_VAR * ourScore);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[3] + CONSTANT_VAR * ourScore);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[4] + CONSTANT_VAR * ourScore);
                    break;
                case "Score: 5":
                    ourScore = 5 - 1;
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[0] + CONSTANT_VAR * ourScore);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[1] + CONSTANT_VAR * ourScore);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[2] + CONSTANT_VAR * ourScore);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[3] + CONSTANT_VAR * ourScore);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[4] + CONSTANT_VAR * ourScore);
                    break;
                case "Score: 6":
                    ourScore = 6 - 1;
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[0] + CONSTANT_VAR * ourScore);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[1] + CONSTANT_VAR * ourScore);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[2] + CONSTANT_VAR * ourScore);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[3] + CONSTANT_VAR * ourScore);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[4] + CONSTANT_VAR * ourScore);
                    break;
                case "Score: 7":
                    ourScore = 7 - 1;
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[0] + CONSTANT_VAR * ourScore);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[1] + CONSTANT_VAR * ourScore);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[2] + CONSTANT_VAR * ourScore);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[3] + CONSTANT_VAR * ourScore);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[4] + CONSTANT_VAR * ourScore);
                    break;
                case "Score: 8":
                    ourScore = 8 - 1;
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[0] + CONSTANT_VAR * ourScore);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[1] + CONSTANT_VAR * ourScore);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[2] + CONSTANT_VAR * ourScore);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[3] + CONSTANT_VAR * ourScore);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[4] + CONSTANT_VAR * ourScore);
                    break;
                case "Score: 9":
                    ourScore = 9 - 1;
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[0] + CONSTANT_VAR * ourScore);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[1] + CONSTANT_VAR * ourScore);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[2] + CONSTANT_VAR * ourScore);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[3] + CONSTANT_VAR * ourScore);
                    TOWER_SETTING.add(TOWER_SETTINGS_BASE[4] + CONSTANT_VAR * ourScore);
                    break;
                case "Score: 10":
                    break;
            }
        }
    }

    private int[] getIds() {
        ArrayList<Integer> ids = new ArrayList<>();
        for (GameObject o : GameEntities.getAll(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject arg0) {
                final GameObjectComposite goc = arg0.getComposite();
                if (goc != null) {
                    final String name = goc.getName();
                    if (name != null) {
                        return name.equals("Pinball Post");
                    }
                }
                return false;
            }
        })) {
            ids.add(o.getId());
        }
        Collections.sort(ids);
        int[] is = new int[5];
        for (int i = 0; i < 5; i++) {
            is[i] = ids.get(i);
        }
        return is;
    }

    public boolean interact(GameObject gameObject, String action) {
        setStatus("Interacting with Pinball Tag...");
        if (!Menu.contains(action, "")) {
            for (int i = 0; i < 15; i++) {
                Point p = gameObject.getLocation().getPointOnScreen();
                Mouse.move(p);
                if (Menu.contains(action, "")) break;
            }
        }
        Time.sleep(75, 100);
        return Menu.interact(action, "");
    }
}
