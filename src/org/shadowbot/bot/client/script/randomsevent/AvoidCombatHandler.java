package org.shadowbot.bot.client.script.randomsevent;

import org.shadowbot.bot.api.RandomEvent;
import org.shadowbot.bot.api.methods.data.Calculations;
import org.shadowbot.bot.api.methods.data.Game;
import org.shadowbot.bot.api.methods.data.movement.Walking;
import org.shadowbot.bot.api.methods.interactive.NPCs;
import org.shadowbot.bot.api.methods.interactive.Players;
import org.shadowbot.bot.api.util.Filter;
import org.shadowbot.bot.api.util.Random;
import org.shadowbot.bot.api.util.Time;
import org.shadowbot.bot.api.wrapper.NPC;
import org.shadowbot.bot.api.wrapper.Tile;

public class AvoidCombatHandler extends RandomEvent {

    private static final String ROCK_GOLEM = "Rock golem";
    private static final String SWARM = "Swarm";
    private static final String EVIL_CHICKEN = "Evil chicken";
    private static final String[] COMBAT_NPC = {ROCK_GOLEM, SWARM, EVIL_CHICKEN};

    private final Filter<NPC> filter = new Filter<NPC>() {
        @Override
        public boolean accept(final NPC npc) {
            if (COMBAT_NPC != null) {
                final String name = npc.getName();
                if (name != null) {
                    for (final String combatName : COMBAT_NPC) {
                        return name.toLowerCase().equals(combatName.toLowerCase());
                    }
                }
            }
            return false;
        }
    };

    private Tile runAwayTile = null;

    @Override
    public String getAuthor() {
        return "Magorium";
    }

    @Override
    public String getName() {
        return "Avoid Combat";
    }

    @Override
    public boolean isActive() {
        return Game.isLoggedIn() && NPCs.getNearest(filter) != null && Players.getLocal().isInCombat() && Calculations.distanceTo(NPCs.getNearest(filter).getLocation()) < 5;
    }

    @Override
    public int solve() {
        if(runAwayTile != null){

        } else {

        }
        Tile loc = Players.getLocal().getLocation();
        Walking.walkTo(new Tile(loc.getX() + Random.nextInt(-7, 7), loc.getY() + Random.nextInt(-7, 7)));
        for (int i = 0; i < 30 && Players.getLocal().isMoving(); i++, Time.sleep(100, 150)) ;
        for (int i = 0; i < 30 && Players.getLocal().isInCombat(); i++, Time.sleep(100, 150)) ;
        return Random.nextInt(150, 200);
    }


}
