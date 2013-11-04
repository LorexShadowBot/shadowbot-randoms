package org.shadowbot.bot.client.script.randomsevent;

import org.shadowbot.bot.api.RandomEvent;
import org.shadowbot.bot.api.methods.data.Game;
import org.shadowbot.bot.api.methods.data.Inventory;
import org.shadowbot.bot.api.methods.data.movement.Walking;
import org.shadowbot.bot.api.methods.interactive.GameEntities;
import org.shadowbot.bot.api.methods.interactive.GroundItems;
import org.shadowbot.bot.api.methods.interactive.NPCs;
import org.shadowbot.bot.api.methods.interactive.Widgets;
import org.shadowbot.bot.api.util.Filter;
import org.shadowbot.bot.api.util.Log;
import org.shadowbot.bot.api.util.Random;
import org.shadowbot.bot.api.util.Time;
import org.shadowbot.bot.api.wrapper.*;


import java.util.ArrayList;
import java.util.Collections;

/*
Worked on 23.10.2013 1 of 1 times
  */
public class FreakyFosterHandler extends RandomEvent {

    public static final int WIDGET_WARNING = 566, MEAT_ID = 11704, WIDGET_CHAT = 242, WIDGET_CHAT_LEAVE = 241;
    private final String exitPortal = "Exit portal";
    private final String freakyForster = "Freaky Forester";
    private int peasantId = -1;
    private int droppedItem = -1;
    private boolean leave = false;

    private enum Foster {

        ONE_TAIL(310, "one"),
        TWO_TAIL(348, "two"),
        THREE_TAIL(378, "three"),
        FOUR_TAIL(408, "four");

        int length;
        String name;

        Foster(final int length, final String name) {
            this.length = length;
            this.name = name;
        }
    }

    @Override
    public String getAuthor() {
        return "Magorium, Lorex";
    }

    @Override
    public String getName() {
        return "Freaky Foster";
    }

    @Override
    public boolean isActive() {
        return Game.isLoggedIn() && (NPCs.getNearest(freakyForster) != null || (Widgets.getWidget(241).isValid() && Widgets.getWidget(241, 1).getText() != null && Widgets.getWidget(241, 1).getText().equals("Freaky Forester")));
    }

    @Override
    public int solve() {
        if (Widgets.getWidget(WIDGET_WARNING).isValid()) {
            Widgets.getWidget(WIDGET_WARNING, 18).click(true);
            for (int i = 0; i < 50 && Widgets.getWidget(WIDGET_WARNING).isValid(); i++, Time.sleep(100, 150)) ;
            if (!isActive()) {
                leave = false;
                peasantId = -1;
                droppedItem = -1;
            }
        } else if (leave) {
            GroundItem item = GroundItems.getNearest(droppedItem);
            if (item != null && droppedItem > 0) {
                if (item.isOnScreen()) {
                    if (Inventory.isFull()) {
                        if (Inventory.contains(MEAT_ID)) {
                            Inventory.dropAll(MEAT_ID);
                        }
                    } else {
                        int Count = Inventory.getCount();
                        item.interact("Take");
                        for (int i = 0; i < 40 && Inventory.getCount() == Count; i++, Time.sleep(100, 150)) ;
                    }
                } else {
                    Walking.walkTo(item.getLocation());
                    for (int i = 0; i < 40 && !item.isOnScreen(); i++, Time.sleep(100, 150)) ;
                }
            } else {
                GameObject portal = GameEntities.getNearest(new Filter<GameObject>() {
                    @Override
                    public boolean accept(GameObject gameObject) {
                        if (gameObject != null) {
                            final GameObjectComposite gameObjectComposite = gameObject.getComposite();
                            if (gameObjectComposite != null) {
                                final String name = gameObjectComposite.getName();
                                if (name != null) {
                                    return name.equals(exitPortal);
                                }
                            }
                        }
                        return false;  //To change body of implemented methods use File | Settings | File Templates.
                    }
                });
                if (portal != null) {
                    if (portal.isOnScreen()) {
                        portal.interact("Use");
                        for (int i = 0; i < 50 && isActive() && !Widgets.getWidget(WIDGET_WARNING).isValid(); i++, Time.sleep(100, 150))
                            ;
                        if (!isActive()) {
                            leave = false;
                            peasantId = -1;
                            droppedItem = -1;
                        }
                    } else {
                        Walking.walkTo(portal.getLocation());
                        for (int i = 0; i < 40 && !portal.isOnScreen(); i++, Time.sleep(100, 150)) ;
                    }
                }
            }
        } else if (Inventory.contains(MEAT_ID)) {
            NPC foster = NPCs.getNearest(freakyForster);
            if (foster != null) {
                if (foster.isOnScreen()) {
                    foster.interact("Talk-to");
                    for (int i = 0; i < 70 && Inventory.contains(MEAT_ID); i++, Time.sleep(100, 150)) ;
                    if (!Inventory.contains(MEAT_ID)) {
                        leave = true;
                    }
                } else {
                    Walking.walkTo(foster.getLocation());
                    for (int i = 0; i < 40 && !foster.isOnScreen(); i++, Time.sleep(100, 150)) ;
                }
            }
        } else if (peasantId > 0) {
            GroundItem meat = GroundItems.getNearest(MEAT_ID);
            if (meat != null) {
                if (meat.isOnScreen()) {
                    if (Inventory.isFull()) {
                        Item item = getLowestItem();
                        droppedItem = item.getId();
                        item.interact("Drop");
                        for (int i = 0; i < 20 && Inventory.isFull(); i++, Time.sleep(100, 150)) ;
                    } else {
                        meat.interact("Take");
                        for (int i = 0; i < 40 && !Widgets.canContinue(); i++, Time.sleep(100, 150)) ;
                    }
                } else {
                    Walking.walkTo(meat.getLocation());
                    for (int i = 0; i < 40 && !meat.isOnScreen(); i++, Time.sleep(100, 150)) ;
                }
            } else {
                NPC chicken = NPCs.getNearest(peasantId);
                if (chicken != null) {
                    if (chicken.isOnScreen()) {
                        chicken.interact("Attack");
                        for (int i = 0; i < 40 && meat == null; i++, Time.sleep(100, 150)) ;
                    } else {
                        Walking.walkTo(chicken.getLocation());
                        for (int i = 0; i < 40 && !chicken.isOnScreen(); i++, Time.sleep(100, 150)) ;
                    }
                }
            }
        } else {
            if (Widgets.getWidget(WIDGET_CHAT_LEAVE).isValid() && Widgets.getWidget(WIDGET_CHAT_LEAVE, 2).getText().contains("leave")) {
                Log.info("leave");
                leave = true;
                Widgets.clickContinue();
            } else if (Widgets.getWidget(WIDGET_CHAT).isValid()) {
                for (int i = 2; i < 5; i++) {
                    if (getId(Widgets.getWidget(WIDGET_CHAT, i).getText()) != -1) {
                        peasantId = getId(Widgets.getWidget(WIDGET_CHAT, i).getText());
                        System.out.println(peasantId);
                        break;
                    }
                }
                Widgets.clickContinue();
            } else if (Widgets.canContinue()) {
                Widgets.clickContinue();
            } else {
                NPC foster = NPCs.getNearest(freakyForster);
                if (foster != null) {
                    if (foster.isOnScreen()) {
                        foster.interact("Talk-to");
                        for (int i = 0; i < 40 && !Widgets.canContinue(); i++, Time.sleep(100, 150)) ;
                    } else {
                        Walking.walkTo(foster.getLocation());
                        for (int i = 0; i < 40 && !foster.isOnScreen(); i++, Time.sleep(100, 150)) ;
                    }
                }
            }
        }
        return Random.nextInt(150, 200);
    }

    private int getId(String message) {
        if (message.toLowerCase().contains("one") || message.toLowerCase().contains("1 tails")) {
            return getId(Foster.ONE_TAIL.length);
        } else if (message.toLowerCase().contains("two") || message.toLowerCase().contains("2 tails")) {
            return getId(Foster.TWO_TAIL.length);
        } else if (message.toLowerCase().contains("three") || message.toLowerCase().contains("3 tails")) {
            return getId(Foster.THREE_TAIL.length);
        } else if (message.toLowerCase().contains("four") || message.toLowerCase().contains("4 tails")) {
            return getId(Foster.FOUR_TAIL.length);
        }
        return -1;
    }

    private int getId(int model) {
        for (NPC npc : NPCs.getAll()) {
            if (npc != null && npc.getModel() != null && npc.getModel().getXTriangles().length == model) {
                return npc.getId();
            }
        }
        return -1;
    }

    private Item getLowestItem() {
        ArrayList<Integer> count = new ArrayList<Integer>();
        for (Item item : Inventory.getItems()) {
            if (item != null && item.getId() != 995) {
                count.add(Inventory.getCount(item.getId()));
            }
        }
        return Inventory.getItems()[count.indexOf(Collections.min(count))];
    }
}
