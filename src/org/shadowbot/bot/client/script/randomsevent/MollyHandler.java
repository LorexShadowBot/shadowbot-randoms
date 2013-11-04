package org.shadowbot.bot.client.script.randomsevent;

import org.shadowbot.bot.api.RandomEvent;
import org.shadowbot.bot.api.RandomEventHandler;
import org.shadowbot.bot.api.methods.data.Calculations;
import org.shadowbot.bot.api.methods.data.Game;
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
 * Date: 8/14/13
 * Time: 5:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class MollyHandler extends RandomEvent {


    private int MOLLY_MODEL_TRAINGLE;

    private boolean TALK_TO_MOLLY = true;

    private final int WIDGET_OPTION = 228,
            NPC_WIDGET = 241,
            WIDGET_CLAW = 240, UP_BUTTON = 29, DOWN_BUTTON = 30, RIGHT_BUTTON = 21, LEFT_BUTTON = 16, BUTTON = 28;

    @Override
    public String getAuthor() {
        return "Magorium";
    }

    @Override
    public String getName() {
        return "Molly";
    }

    @Override
    public boolean isActive() {
        NPC molly = NPCs.getNearest("Molly");
        NPC suspect = NPCs.getNearest("Suspect");
        return Game.isLoggedIn() && (molly != null && molly.getSpokenMessage() == null) || (suspect != null && suspect.getSpokenMessage() == null);
    }

    @Override
    public int solve() {

        NPC molly = NPCs.getNearest("Molly");
        NPC suspect = NPCs.getNearest("Suspect");
        NPC[] all_suspect = NPCs.getAll("Suspect");

        if (Widgets.getWidget(NPC_WIDGET).isValid() && Widgets.getWidget(NPC_WIDGET, 2).getText().contains("Good luck")) {
            TALK_TO_MOLLY = false;
            Widgets.clickContinue();
        } else if (Widgets.getWidget(WIDGET_OPTION).isValid()) {
            final WidgetChild one = Widgets.getWidget(WIDGET_OPTION, 1);
            final WidgetChild two = Widgets.getWidget(WIDGET_OPTION, 2);
            if (one != null && two != null) {
                final String oneText = one.getText();
                final String twoText = two.getText();
                if (oneText != null && twoText != null) {
                    if (oneText.toLowerCase().contains("yes, i know.")) {
                        one.click(true);
                    } else {
                        two.click(true);
                    }
                }
            }
            for (int i = 0; i < 20 && Widgets.getWidget(WIDGET_OPTION).isValid(); i++, Time.sleep(100, 150))
                ;
        } else if (Widgets.canContinue()) {
            RandomEventHandler.setStatus("Continuing Widgets");
            Widgets.clickContinue();
        } else if (Camera.getPitch() < 80 && !Widgets.getWidget(WIDGET_CLAW).isValid()) {
            RandomEventHandler.setStatus("Adjusting Pitch");
            Camera.setPitch(Random.nextInt(80, 89));
        } else if (TALK_TO_MOLLY) {
            if (molly != null) {
                if (molly.isOnScreen()) {
                    MOLLY_MODEL_TRAINGLE = molly.getModel().getXTriangles().length;
                    molly.interact("Talk-to");
                    for (int i = 0; i < 20 && !Widgets.canContinue(); i++, Time.sleep(100, 150)) ;
                } else {
                    Camera.turnTo(molly);
                }
            }
        } else if (suspect != null) {
            if (all_suspect.length == 1 && molly != null) {
                if (Calculations.distanceTo(molly.getLocation()) > 5) {
                    GameObject door = GameEntities.getNearest(new Filter<GameObject>() {
                        @Override
                        public boolean accept(GameObject gameObject) {
                            final GameObjectComposite gameObjectComposite = gameObject.getComposite();
                            if (gameObjectComposite != null) {
                                return gameObjectComposite.getName().equals("Door");
                            }
                            return false;
                        }
                    });
                    if (door != null) {
                        if (door.isOnScreen()) {
                            door.interact("Open");
                            for (int i = 0; i < 40 && NPCs.getNearest("Molly") != null; i++, Time.sleep(100, 150)) ;
                        } else {
                            Walking.walkTo(door.getLocation());
                            for (int i = 0; i < 20 && !Players.getLocal().isMoving(); i++, Time.sleep(100, 150)) ;
                        }
                    }
                } else {
                    if (molly.isOnScreen()) {
                        molly.interact("Talk-to");
                        for (int i = 0; i < 40 && !Widgets.canContinue(); i++, Time.sleep(100, 150)) ;
                    } else {
                        Camera.turnTo(molly);
                    }
                }
            } else {
                if (Widgets.getWidget(WIDGET_CLAW).isValid()) {
                    RandomEventHandler.setStatus("Moving Claw");
                    WidgetChild up = Widgets.getWidget(WIDGET_CLAW, UP_BUTTON);
                    WidgetChild down = Widgets.getWidget(WIDGET_CLAW, DOWN_BUTTON);
                    WidgetChild left = Widgets.getWidget(WIDGET_CLAW, LEFT_BUTTON);
                    WidgetChild right = Widgets.getWidget(WIDGET_CLAW, RIGHT_BUTTON);
                    WidgetChild button = Widgets.getWidget(WIDGET_CLAW, BUTTON);
                    GameObject obj = GameEntities.getNearest(new Filter<GameObject>() {
                        @Override
                        public boolean accept(GameObject gameObject) {
                            final GameObjectComposite gameObjectComposite = gameObject.getComposite();
                            if (gameObjectComposite != null) {
                                return gameObjectComposite.getName().equals("Evil claw");
                            }
                            return false;
                        }
                    });
                    Tile objectLoc = obj.getLocation();
                    int clawX = objectLoc.getX(), clawY = objectLoc.getY();
                    NPC npc = NPCs.getNearest(getEvilTwin(MOLLY_MODEL_TRAINGLE));
                    Tile loc = npc.getLocation();
                    int locX = loc.getX(), locY = loc.getY();
                    int x = clawX - locX, y = clawY - locY;
                    if (x == 0 && y == 0) {
                        pressButton(button, obj, npc);
                        for (int i = 0; i < 60 && !Widgets.canContinue(); i++, Time.sleep(100, 150)) ;
                    } else if (x == 0) {
                        if (y > 0) {
                            pressButton(up, obj, npc);
                        } else {
                            pressButton(down, obj, npc);
                        }
                    } else {
                        if (x > 0) {
                            pressButton(right, obj, npc);
                        } else {
                            pressButton(left, obj, npc);
                        }
                    }
                } else {
                    GameObject control = GameEntities.getNearest(new Filter<GameObject>() {
                        @Override
                        public boolean accept(GameObject gameObject) {
                            final GameObjectComposite gameObjectComposite = gameObject.getComposite();
                            if (gameObjectComposite != null) {
                                return gameObjectComposite.getName().equals("Control panel");
                            }
                            return false;
                        }
                    });
                    if (control != null) {
                        if (control.isOnScreen()) {
                            if (control.interact("Use"))
                                for (int i = 0; i < 20 && !Widgets.getWidget(WIDGET_CLAW).isValid(); i++, Time.sleep(100, 150))
                                    ;
                        } else {
                            Walking.walkTo(control.getLocation());
                        }
                    }
                }
            }
        } else if (!TALK_TO_MOLLY) {
            GameObject door = GameEntities.getNearest(new Filter<GameObject>() {
                @Override
                public boolean accept(GameObject gameObject) {
                    final GameObjectComposite gameObjectComposite = gameObject.getComposite();
                    if (gameObjectComposite != null) {
                        return gameObjectComposite.getName().equals("Door");
                    }
                    return false;
                }
            });
            if (door != null) {
                if (door.isOnScreen()) {
                    RandomEventHandler.setStatus("Opening Door");
                    door.interact("Open");
                    for (int i = 0; i < 40 && molly != null; i++, Time.sleep(100, 150)) ;
                } else {
                    RandomEventHandler.setStatus("Turning Camera");
                    Camera.turnTo(door.getLocation());
                }
            }
        }
        return Random.nextInt(70, 150);
    }


    private int getEvilTwin(int id) {
        return NPCs.getNearest(new Filter<NPC>() {

            @Override
            public boolean accept(NPC Type) {
                return Type != null && Type.getName().equalsIgnoreCase("Suspect") && Type.getModel().getXTriangles().length == MOLLY_MODEL_TRAINGLE;
            }
        }).getId();
    }

    private void pressButton(WidgetChild button, GameObject obj, NPC npc) {
        int x = obj.getLocation().getX() - npc.getLocation().getX(), y = obj.getLocation().getY() - npc.getLocation().getY();
        button.click(true);
        for (int i = 0; i < 50 && ((obj.getLocation().getX() - npc.getLocation().getX()) == x && (obj.getLocation().getY() - npc.getLocation().getY()) == y); i++, Time.sleep(100, 150))
            ;
    }
}
