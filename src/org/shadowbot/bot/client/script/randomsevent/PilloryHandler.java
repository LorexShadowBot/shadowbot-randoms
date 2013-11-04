package org.shadowbot.bot.client.script.randomsevent;

import org.shadowbot.bot.api.RandomEvent;
import org.shadowbot.bot.api.methods.data.Game;
import org.shadowbot.bot.api.methods.data.movement.Camera;
import org.shadowbot.bot.api.methods.data.movement.Walking;
import org.shadowbot.bot.api.methods.interactive.GameEntities;
import org.shadowbot.bot.api.methods.interactive.Players;
import org.shadowbot.bot.api.methods.interactive.Widgets;
import org.shadowbot.bot.api.util.Filter;
import org.shadowbot.bot.api.util.Random;
import org.shadowbot.bot.api.util.Time;
import org.shadowbot.bot.api.wrapper.GameObject;
import org.shadowbot.bot.api.wrapper.GameObjectComposite;
import org.shadowbot.bot.api.wrapper.Tile;

public class PilloryHandler extends RandomEvent {

    private static final Tile PILLORY_TILE = new Tile(2606, 3105);
    private static final int TRIANGLE_MODEL_ID = 9752;
    private static final int SQUARE_MODEL_ID = 9750;
    private static final int CIRCLE_MODEL_ID = 9751;
    private static final int INTERFACE = 189;


    @Override
    public String getAuthor() {
        return "Magorium and Lorex";
    }

    @Override
    public String getName() {
        return "Pillory";
    }

    @Override
    public boolean isActive() {
        return Game.isLoggedIn() && Players.getLocal().getLocation().equals(PILLORY_TILE);
    }

    @Override
    public int solve() {
        if (Widgets.getWidget(INTERFACE).isValid()) {
            int MODEL_ID = Widgets.getWidget(INTERFACE, 2).getModelId();
            for (int i = 3; i < 6; i++) {
                if (Widgets.getWidget(INTERFACE, i).getModelId() == (MODEL_ID - 4)) {
                    Widgets.getWidget(INTERFACE, i).click(true);
                    for (int a = 0; a < 20 && Widgets.getWidget(INTERFACE) != null && Widgets.getWidget(INTERFACE, 2).getModelId() == MODEL_ID; a++, Time.sleep(100, 150))
                        ;
                    break;
                }
            }
        } else {
            GameObject object = GameEntities.getNearest(new Filter<GameObject>() {

                @Override
                public boolean accept(final GameObject o) {
                    if (o != null) {
                        GameObjectComposite oc = o.getComposite();
                        if (oc != null) {
                            final String name = oc.getName();
                            if (name != null) {
                                return name.toLowerCase().equals("cage");
                            }
                        }
                    }
                    return false;
                }
            });
            if (object.isOnScreen()) {
                object.interact("Unlock");
                for (int i = 0; i < 30 && Players.getLocal().getAnimation() == -1; i++, Time.sleep(100, 150)) ;
            } else {
                if (object.distanceTo() < 7) {
                    Camera.turnTo(object);
                } else {
                    Walking.walkTo(object.getLocation());
                }
                for (int i = 0; i < 15 && !Players.getLocal().isMoving(); i++, Time.sleep(100, 150)) ;
            }
        }
        return Random.nextInt(150, 200);
    }
}