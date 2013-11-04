package org.shadowbot.bot.client.script.randomsevent;

import org.shadowbot.bot.api.RandomEvent;
import org.shadowbot.bot.api.methods.data.Game;
import org.shadowbot.bot.api.methods.interactive.NPCs;
import org.shadowbot.bot.api.util.Filter;
import org.shadowbot.bot.api.util.Random;
import org.shadowbot.bot.api.util.Time;
import org.shadowbot.bot.api.wrapper.NPC;

/**
 * Created with IntelliJ IDEA.
 * User: Lorex
 * Date: 10/19/13
 * Time: 11:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class StrangePlantHandler extends RandomEvent {

    private final String strangePlantName = "Strange plant";

    @Override
    public int solve() {
        final NPC plant = getStangePlant();
        if (plant != null) {
            if (plant.interact("Pick")) {
                for (int i = 0; i < 100 && getStangePlant() != null; i++, Time.sleep(40, 60)) ;
            }
        }
        return Random.nextInt(80, 110);
    }

    @Override
    public boolean isActive() {
        final NPC plant = getStangePlant();
        return Game.isLoggedIn() && plant != null && !plant.isInCombat() && plant.distanceTo() < 5 && plant.getAnimation() == -1;
    }

    @Override
    public String getName() {
        return "Strange Plant";
    }

    @Override
    public String getAuthor() {
        return "Lorex";
    }

    private NPC getStangePlant() {
        return NPCs.getNearest(new Filter<NPC>() {
            @Override
            public boolean accept(NPC npc) {
                if (npc != null) {
                    final String name = npc.getName();
                    if (name != null) {
                        if (name.equals(strangePlantName)) {
                            final String[] actions = npc.getActions();
                            if (actions != null) {
                                for (final String action : actions) {
                                    if (action != null) {
                                        if (action.equals("Pick")) {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return false;
            }
        });
    }
}
