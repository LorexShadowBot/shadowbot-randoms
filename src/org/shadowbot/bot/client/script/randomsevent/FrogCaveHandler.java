package org.shadowbot.bot.client.script.randomsevent;

import org.shadowbot.bot.api.RandomEvent;
import org.shadowbot.bot.api.methods.data.Game;
import org.shadowbot.bot.api.methods.data.movement.Camera;
import org.shadowbot.bot.api.methods.data.movement.Walking;
import org.shadowbot.bot.api.methods.interactive.GameEntities;
import org.shadowbot.bot.api.methods.interactive.NPCs;
import org.shadowbot.bot.api.methods.interactive.Players;
import org.shadowbot.bot.api.methods.interactive.Widgets;
import org.shadowbot.bot.api.util.Filter;
import org.shadowbot.bot.api.util.Log;
import org.shadowbot.bot.api.util.Random;
import org.shadowbot.bot.api.util.Time;
import org.shadowbot.bot.api.wrapper.*;

public class FrogCaveHandler extends RandomEvent {

    @Override
    public String getAuthor() {
        return "Lorex";
    }

    @Override
    public String getName() {
        return "Frog Cave";
    }

    @Override
    public boolean isActive() {
        return Game.isLoggedIn() && getPrincessFrog() != null && GameEntities.getNearest(new Filter<GameObject>() {
            @Override
            public boolean accept(final GameObject o) {
                if (o != null) {
                    final GameObjectComposite oc = o.getComposite();
                    if (oc != null) {
                        final String name = oc.getName();
                        if(name != null) {
                            return name.contains("Roots");
                        }
                    }
                }
                return false;
            }
        }) != null;
    }

    @Override
    public int solve() {
        final NPC frog = getPrincessFrog();
        if (frog != null) {
            if (Widgets.canContinue()) {
                Widgets.clickContinue();
                for (int i = 0; i < 40 && Widgets.canContinue(); i++) {
                    Time.sleep(40, 60);
                }
            } else if (continueSorry()) {
                for (int i = 0; i < 60 && getPrincessFrog() != null; i++) {
                    Time.sleep(40, 60);
                }
            } else {
                if (!Players.getLocal().isMoving()) {
                    Camera.turnTo(frog);
                    if (frog.distanceTo() > 8) {
                        Walking.walkTo(frog);
                        for (int i = 0; i < 30 && !Players.getLocal().isMoving(); i++) {
                            Time.sleep(40, 60);
                        }
                    } else {
                        frog.click(true);
                        for (int i = 0; i < 30 && !Widgets.canContinue(); i++) {
                            Time.sleep(40, 60);
                        }
                    }
                }
            }
        }
        return Random.nextInt(100, 130);
    }

    private NPC getPrincessFrog() {
        final NPC[] npcs = NPCs.getAll("Frog");
        int id;
        if (npcs.length > 0) {
            for (NPC npc : npcs) {
                int count = 0;
                id = npc.getId();
                for (NPC n : npcs) {
                    if (id == n.getId()) {
                        count++;
                    }
                }
                if (count == 1) {
                    return npc;
                }
            }
        }
        return null;
    }

    private boolean continueSorry() {
        final int[] childIds = {1, 2};
        final Widget parent = Widgets.getWidget(228);
        if (parent != null) {
            if (parent.isValid()) {
                for (final int childId : childIds) {
                    final WidgetChild child = parent.getChild(childId);
                    if (child != null) {
                        final String text = child.getText();
                        if (text != null) {
                            if (text.toLowerCase().contains("sorry")) {
                                return (child.interact("Continue"));
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

}

