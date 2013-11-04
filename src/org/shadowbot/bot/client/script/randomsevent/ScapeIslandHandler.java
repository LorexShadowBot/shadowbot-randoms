package org.shadowbot.bot.client.script.randomsevent;

import org.shadowbot.bot.api.RandomEvent;
import org.shadowbot.bot.api.methods.data.Calculations;
import org.shadowbot.bot.api.methods.data.Game;
import org.shadowbot.bot.api.methods.data.Inventory;
import org.shadowbot.bot.api.methods.data.movement.Camera;
import org.shadowbot.bot.api.methods.interactive.*;
import org.shadowbot.bot.api.methods.data.movement.Walking;
import org.shadowbot.bot.api.util.Filter;
import org.shadowbot.bot.api.util.Random;
import org.shadowbot.bot.api.util.Time;
import org.shadowbot.bot.api.wrapper.GameObject;
import org.shadowbot.bot.api.wrapper.GroundItem;
import org.shadowbot.bot.api.wrapper.NPC;

/**
 * Created with IntelliJ IDEA.
 * User: Magorium
 * Date: 8/14/13
 * Time: 7:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScapeIslandHandler extends RandomEvent {

    private final int widget_state = 186;
    private final int FISH_SPOT = 1753;
    private final int ID_NET = 6209;
    private final int UNCOOKED_FISH = 6200;
    private final int COOKED_FISH = 6202;
    private final int UNCOOKED_POT = 1752;
    private final int LEAVE_PORTAL = 1754;
    private final int ID_EVIL_CAT = 5759;
    private final int id_servant = 5761;
    private int id_direction;
    private boolean LEAVE = false;
    private final int WEST_STATE = 1760, NORTH_STATE = 1759, EAST_STATE = 1758, SOUTH_STATE = 1757;
    private final int[] STATE = {NORTH_STATE, EAST_STATE, SOUTH_STATE, WEST_STATE};

    @Override
    public String getAuthor() {
        return "Magorium";
    }

    @Override
    public String getName() {
        return "ScapeIsland";
    }

    @Override
    public boolean isActive() {
        //GameEntities != null also activates in pinball randomevent therefore I disabled the random
        //return Game.isLoggedIn() && NPCs.getNearest(id_servant) != null || GameEntities.getNearest(STATE) != null;
        return false;
    }

    @Override
    public int solve() {
        try {
            NPC servant = NPCs.getNearest(id_servant);
            NPC cat = NPCs.getNearest(ID_EVIL_CAT);
            if (LEAVE) {
                GameObject portal = GameEntities.getNearest(LEAVE_PORTAL);
                if (portal.isOnScreen()) {
                    if (Players.getLocal().getAnimation() == -1 && !Players.getLocal().isMoving()) {
                        portal.interact("Enter");
                        for (int i = 0; i < 30 && isActive(); i++, Time.sleep(100, 150)) ;
                    }
                } else {
                    Walking.walkTo(portal.getLocation());
                }
            } else if (Inventory.contains(UNCOOKED_FISH)) {
                if (cat.isOnScreen()) {
                    if (Widgets.canContinue()) {
                        Widgets.clickContinue();
                    } else if (Players.getLocal().getAnimation() == -1 && !Players.getLocal().isMoving()) {
                        Inventory.getItem(UNCOOKED_FISH).interact("Use");
                        cat.interact("Use");
                        for (int i = 0; i < 30 && Inventory.contains(UNCOOKED_FISH); i++) {
                            if (cat.getSpokenMessage().contains("Z")) {
                                LEAVE = true;
                            }
                            Time.sleep(100, 150);
                        }
                        ;
                    }
                } else {
                    Walking.walkTo(cat.getLocation());
                }
            } else if (Inventory.contains(COOKED_FISH)) {
                GameObject pot = GameEntities.getNearest(UNCOOKED_POT);
                if (pot.isOnScreen()) {
                    if (Players.getLocal().getAnimation() == -1 && !Players.getLocal().isMoving()) {
                        Inventory.getItem(COOKED_FISH).interact("Use");
                        pot.interact("Use");
                        for (int i = 0; i < 30 && Inventory.contains(COOKED_FISH); i++, Time.sleep(100, 150)) ;
                    }
                } else {
                    Walking.walkTo(pot.getLocation());
                }
            } else if (servant != null && id_direction <= 0) {
                if (isWatching()) {
                    id_direction = STATE[Camera.getAngle() < 360 ? Camera.getAngle() / 90 : 0];
                    LEAVE = true;
                } else {
                    if (servant.isOnScreen()) {
                        if (Widgets.canContinue()) {
                            Widgets.clickContinue();
                        } else {
                            servant.interact("Talk-to");
                            for (int i = 0; i < 20 && !Widgets.canContinue(); i++, Time.sleep(100, 150)) ;
                        }
                    } else {
                        Walking.walkTo(servant.getLocation());
                    }
                }
            } else if (id_direction > 0 && Inventory.contains(ID_NET) && !isWatching()) {
                GameObject spot = getFishSpot();
                System.out.println(spot != null);
                if (spot.isOnScreen()) {
                    if (Players.getLocal().getAnimation() == -1) {
                        spot.interact("Net");
                        for (int i = 0; i < 30 && Players.getLocal().getAnimation() == -1; i++, Time.sleep(100, 150)) ;
                    }
                } else {
                    Walking.walkTo(spot.getLocation());
                }
            } else if (!isWatching() && !Inventory.contains(ID_NET)) {
                GroundItem item = GroundItems.getNearest(ID_NET);
                if (item.isOnScreen()) {
                    item.interact("Take");
                    for (int i = 0; i < 30 && !Inventory.contains(ID_NET); i++, Time.sleep(100, 150)) ;
                } else {
                    Walking.walkTo(item.getLocation());
                }
            }
        } catch (NullPointerException e) {

        }
        return Random.nextInt(150, 200);
    }

    private boolean isWatching() {
        return Widgets.getWidget(widget_state).isValid();
    }

    private GameObject getGameState() {
        return GameEntities.getNearest(id_direction);
    }

    private GameObject getFishSpot() {
        return GameEntities.getNearest(new Filter<GameObject>() {

            @Override
            public boolean accept(GameObject Type) {
                return Type != null && Type.getId() == FISH_SPOT && Calculations.distanceBetween(Type.getLocation(), getGameState().getLocation()) < 11;
            }
        });
    }
}
