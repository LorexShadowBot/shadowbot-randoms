package org.shadowbot.bot.client.script.randomsevent;

import org.shadowbot.bot.api.RandomEvent;
import org.shadowbot.bot.api.methods.data.Game;
import org.shadowbot.bot.api.methods.data.Inventory;
import org.shadowbot.bot.api.methods.data.movement.Camera;
import org.shadowbot.bot.api.methods.data.movement.Walking;
import org.shadowbot.bot.api.methods.interactive.GameEntities;
import org.shadowbot.bot.api.methods.interactive.NPCs;
import org.shadowbot.bot.api.methods.interactive.Players;
import org.shadowbot.bot.api.methods.interactive.Widgets;
import org.shadowbot.bot.api.util.Filter;
import org.shadowbot.bot.api.util.Random;
import org.shadowbot.bot.api.util.Time;
import org.shadowbot.bot.api.wrapper.*;

/**
 * Created with IntelliJ IDEA.
 * User: Magorium
 * Date: 8/23/13
 * Time: 3:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class PrisonHandler extends RandomEvent {

    private boolean leave = false;
    private final String didIt = "You did it, you got all the keys right!";
    private final String leaveString = "Come on, we should leave before Evil Bob comes back!";
    private final int leverId = 24296;
    private final int widgetId = 273;
    private final int keyId = 6966;

    private Balloon currentBalloon;

    private enum Balloon {

        ONE_STICK_TAIL(210, 10750), ONE_STICK(230, 10749), THREE_STICK(320, 10752), FOUR_STICK(282,
                10751);
        int id;
        int widget_id;

        Balloon(final int id, final int widget_id) {
            this.id = id;
            this.widget_id = widget_id;
        }
    }

    @Override
    public String getAuthor() {
        return "Magorium and Lorex";
    }

    @Override
    public String getName() {
        return "Prison Pete";
    }

    @Override
    public boolean isActive() {
        boolean speaking = false;
        final Widget parent = Widgets.getWidget(242);
        if (parent != null) {
            if (parent.isValid()) {
                final WidgetChild child = parent.getChild(1);
                if (child != null) {
                    final String text = child.getText();
                    if (text != null) {
                        if (text.toLowerCase().contains("prison pete")) {
                            speaking = true;
                        }
                    }
                }
            }
        }
        boolean b = NPCs.getNearest("Prison Pete") != null;
        if (!b) {
            currentBalloon = null;
        }
        return Game.isLoggedIn() && (b || speaking);
    }

    @Override
    public int solve() {
        if (!leave) {
            if (Camera.getPitch() < 80) {
                Camera.setPitch(Random.nextInt(80, 90));
            }
            if (Inventory.contains(keyId)) {
                final NPC pete = NPCs.getNearest("Prison Pete");
                if (pete != null) {
                    if (pete.distanceTo() > 3) {
                        if (!Players.getLocal().isMoving()) {
                            Walking.walkTo(pete);
                            for (int i = 0; i < 30 && !Players.getLocal().isMoving(); i++) {
                                Time.sleep(40, 60);
                            }
                        }
                    } else {
                        pete.interact("Talk-to");
                        for (int i = 0; i < 60 && Inventory.contains(keyId); i++) {
                            Time.sleep(40, 60);
                        }
                    }
                }
                currentBalloon = null;
            }
            if (currentBalloon != null && Widgets.getWidget(widgetId).isValid()) {
                Widgets.getWidget(widgetId, 6).click(true);
                for (int i = 0; i < 20 && Widgets.getWidget(widgetId).isValid(); i++, Time.sleep(
                        100, 150))
                    ;
            } else if (Widgets.canContinue()) {
                final Widget parent = Widgets.getWidget(242);
                if (parent != null) {
                    if (parent.isValid()) {
                        final WidgetChild wc = parent.getChild(2);
                        if (wc != null) {
                            final String text = wc.getText();
                            if (text != null) {
                                if (text.toLowerCase().equals(didIt.toLowerCase()) || text.toLowerCase().equals(leaveString.toLowerCase())) {
                                    leave = true;
                                }
                            }
                        }
                    }
                }
                Widgets.clickContinue();
            } else if (!Inventory.contains(keyId)) {
                if (currentBalloon == null) {
                    GameObject lever = GameEntities.getNearest(leverId);
                    if (Widgets.getWidget(widgetId).isValid()) {
                        currentBalloon = deterBalloon(Widgets.getWidget(widgetId, 3).getModelId());
                    } else if (lever != null) {
                        Camera.setAngel(Random.nextInt(80, 100));
                        if (lever.isOnScreen()) {
                            lever.interact("Pull");
                            for (int i = 0; i < 40 && !Widgets.getWidget(widgetId).isValid(); i++, Time
                                    .sleep(100, 150))
                                ;
                        } else {
                            Walking.walkTo(lever.getLocation());
                        }
                    }
                } else if (currentBalloon != null) {
                    NPC balloon = NPCs.getNearest(new Filter<NPC>() {
                        @Override
                        public boolean accept(NPC Type) {
                            return Type != null && Type.getModel() != null
                                    && Type.getModel().getXTriangles().length == currentBalloon.id;
                        }
                    });
                    if (balloon != null) {
                        if (balloon.isOnScreen()) {
                            balloon.interact("Pop");
                            for (int i = 0; i < 20 && !Widgets.canContinue(); i++, Time.sleep(100,
                                    150))
                                ;
                        } else {
                            Walking.walkTo(balloon.getLocation());
                        }
                    }
                }
            }
        } else {
            leave();
        }
        return Random.nextInt(150, 200);
    }

    private Balloon deterBalloon(int id) {
        for (Balloon balloon : Balloon.values()) {
            if (balloon.widget_id == id) {
                return balloon;
            }
        }
        return null;
    }

    private void leave() {
        GameObject lever = GameEntities.getNearest(leverId);
        if (!Widgets.canContinue()) {
            if (lever != null) {
                final Tile exit = new Tile(lever.getLocation().getX() + 10, lever.getLocation()
                        .getY() + 3);
                Walking.walkTo(exit);
                for (int i = 0; i < 100 && Players.getLocal().isMoving(); i++) {
                    Time.sleep(40, 60);
                }
            }
        } else {
            Widgets.clickContinue();

        }
    }
}
