package org.shadowbot.bot.client.script.randomsevent;

import org.shadowbot.bot.api.RandomEvent;
import org.shadowbot.bot.api.methods.data.Calculations;
import org.shadowbot.bot.api.methods.data.Game;
import org.shadowbot.bot.api.methods.interactive.NPCs;
import org.shadowbot.bot.api.methods.interactive.Widgets;
import org.shadowbot.bot.api.util.Random;
import org.shadowbot.bot.api.wrapper.NPC;
import org.shadowbot.bot.api.wrapper.WidgetChild;


/**
 * Created with IntelliJ IDEA.
 * User: Magorium
 * Date: 8/15/13
 * Time: 9:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class MimeHandler extends RandomEvent {

    final int BOW_ANIMATION = 858, CHATBOX_EMOTE = 188;
    final String MIME_ID = "Mime";
    Emote currentEmote = null;

    @Override
    public String getAuthor() {
        return "Magorium";
    }

    @Override
    public String getName() {
        return "Mime";
    }

    @Override
    public boolean isActive() {
        return Game.isLoggedIn() && NPCs.getNearest(MIME_ID) != null && Calculations.distanceTo(NPCs.getNearest(MIME_ID).getLocation()) < 15;
    }

    @Override
    public int solve() {
        NPC mime = NPCs.getNearest(MIME_ID);
        if (mime != null) {
            if (mime.getAnimation() > 0 && mime.getAnimation() != BOW_ANIMATION && currentEmote == null) {
                currentEmote = getEmote(mime.getAnimation());
            } else if (currentEmote != null && Widgets.getWidget(CHATBOX_EMOTE).isValid()) {
                if (getWidgetWith(currentEmote) != null) {
                    getWidgetWith(currentEmote).click(true);
                }
                currentEmote = null;
            }
        }
        return Random.nextInt(150, 200);
    }

    enum Emote {
        THINK(857, "Think"),
        CRY(860, "Cry"),
        LAUGH(861, "Laugh"),
        Dance(866, "Dance"),
        GLASS_WALL(1128, "Glass Wall"),
        LEAN(1129, "Lean"),
        ROPE(1130, "Climb Rope"),
        GLASS_BOX(1131, "Glass Box");

        int animation;
        String emote;

        Emote(int animation, String emote) {
            this.animation = animation;
            this.emote = emote;
        }
    }

    private Emote getEmote(int animation) {
        for (Emote emote : Emote.values()) {
            if (emote.animation == animation) {
                return emote;
            }
        }
        return null;
    }

    private WidgetChild getWidgetWith(Emote emote) {
        for (WidgetChild child : Widgets.getWidget(CHATBOX_EMOTE).getChildren()) {
            if (child.getText() != null && child.getText().contains(emote.emote)) {
                return child;
            }
        }
        return null;
    }
}
