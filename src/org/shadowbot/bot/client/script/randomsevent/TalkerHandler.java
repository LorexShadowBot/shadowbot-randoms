package org.shadowbot.bot.client.script.randomsevent;

import org.shadowbot.bot.api.RandomEvent;
import org.shadowbot.bot.api.methods.data.Game;
import org.shadowbot.bot.api.methods.interactive.NPCs;
import org.shadowbot.bot.api.methods.interactive.Players;
import org.shadowbot.bot.api.methods.interactive.Widgets;
import org.shadowbot.bot.api.util.Filter;
import org.shadowbot.bot.api.util.Random;
import org.shadowbot.bot.api.util.Time;
import org.shadowbot.bot.api.wrapper.NPC;

public class TalkerHandler extends RandomEvent {

    public static final String DRUNKEN_DWARF = "Drunken Dwarf";
    public static final String GENIE = "Genie";
    public static final String SECURITY_GUARD = "Security Guard";
    public static final String DR_JEKYLL = "Dr Jekyll";
    public static final String RICK_TURPENTINE = "Rick Turpentine";
    public static final String CAPN_HAND = "Cap'n Hand";
    public static final String MYSTERIOUS_OLD_MAN = "Mysterious Old Man";

    private NPC npc;

    private final String[] names = {CAPN_HAND, DRUNKEN_DWARF, GENIE, SECURITY_GUARD, DR_JEKYLL,
            RICK_TURPENTINE, MYSTERIOUS_OLD_MAN };

    @Override
    public String getAuthor() {
        return "Magorium,Lorex";
    }

    @Override
    public String getName() {
        return "Talk Handler";
    }

    @Override
    public boolean isActive() {
        if (Game.isLoggedIn()) {
            for (final String name : names) {
                npc = NPCs.getNearest(new Filter<NPC>() {

                    @Override
                    public boolean accept(NPC n) {
                        if (n != null) {
                            final String nameOfNPC = n.getName();
                            if (nameOfNPC != null) {
                                if (nameOfNPC.equals(name) && n.distanceTo() < 3) {
                                    final String message = n.getSpokenMessage();
                                    if (message != null) {
                                        if (message.toLowerCase().contains(
                                                Players.getLocal().getName().toLowerCase())) {
                                            return true;
                                        }
                                    }
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                });
                if (npc != null)
                    return true;
            }
        }
        return false;
    }

    @Override
    public int solve() {
        if (npc != null) {
            if (Widgets.canContinue()) {
                Widgets.clickContinue();
                for (int i = 0; i < 40 && npc != null; i++, Time.sleep(100, 150))
                    ;
            } else {
                npc.interact("Talk-to");
                for (int i = 0; i < 20 && npc != null && !Widgets.canContinue(); i++, Time.sleep(
                        100, 150))
                    ;
            }
        }
        return Random.nextInt(150, 200);
    }

}
