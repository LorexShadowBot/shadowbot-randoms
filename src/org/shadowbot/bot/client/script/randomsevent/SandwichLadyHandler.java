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
import org.shadowbot.bot.api.wrapper.WidgetChild;


/**
 * Created with IntelliJ IDEA.
 * User: Magorium
 * Date: 8/29/13
 * Time: 2:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class SandwichLadyHandler extends RandomEvent {

    private final int WIDGET = 297;
    private NPC lady;

    @Override
    public String getAuthor() {
        return "Magorium";
    }

    @Override
    public String getName() {
        return "Sandwich Lady";
    }

    @Override
    public boolean isActive() {
        lady = NPCs.getNearest(new Filter<NPC>() {

            @Override
            public boolean accept(NPC Type) {
                return Type != null && Type.getName() !=null && Type.getName().equalsIgnoreCase("Sandwich lady")&& Type.getSpokenMessage().toLowerCase().contains(Players.getLocal().getName());
            }
        }) ;
        return Game.isLoggedIn() && lady !=null;
    }

    @Override
    public int solve() {
        while(lady !=null){
            if(Widgets.canContinue()){
                Widgets.clickContinue();
            }else if(Widgets.getWidget(WIDGET).isValid()){
                String whatSandwich = Widgets.getWidget(WIDGET, 8).getText();
                WidgetChild sandwich = getSandwichComponent(Sandwiches.getObject(whatSandwich).getId());
                if(sandwich !=null){
                    sandwich.click(true);
                    for(int i = 0 ; i < 20 && !Widgets.getWidget(WIDGET).isValid();i++, Time.sleep(100, 150));
                }
            }else{
                lady.interact("Talk-to");
                for(int i = 0 ; i < 20 && !Widgets.getWidget(WIDGET).isValid() && !Widgets.canContinue();i++, Time.sleep(100, 150));
            }

            Time.sleep(150, 250);
        }
        return Random.nextInt(150,200);
    }

    private enum Sandwiches {

        SQUARE(10731, "square"),
        ROLL(10727, "roll"),
        CHOCOLATE(10728, "chocolate"),
        BAGUETTE(10726, "baguette"),
        TRIANGLE(10732, "triangle"),
        KEBAB(10729, "kebab"),
        PIE(10730, "pie");
        private final int modelId;
        private final String name;

        Sandwiches(int m, String n) {
            this.modelId = m;
            this.name = n;
        }

        public int getId() {
            return modelId;
        }

        public String getMessage() {
            return name;
        }

        public static Sandwiches getObject(String nm) {
            for (Sandwiches e : Sandwiches.values()) {
                if (nm.equals(e.getMessage())) {
                    return e;
                }
            }
            return null;
        }
    }

    public WidgetChild getSandwichComponent(int mid) {
        WidgetChild sandwich;
        for (int i = 1; i < 8; i++) {
            sandwich = Widgets.getWidget(297, i);
            if (sandwich != null) {
                if (sandwich.getModelId() == mid) {
                    return sandwich;
                }
            }
        }
        return null;
    }
}
