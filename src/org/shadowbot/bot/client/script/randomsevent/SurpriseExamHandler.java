package org.shadowbot.bot.client.script.randomsevent;

import org.shadowbot.bot.api.RandomEvent;
import org.shadowbot.bot.api.methods.data.Calculations;
import org.shadowbot.bot.api.methods.data.Game;
import org.shadowbot.bot.api.methods.data.movement.Camera;
import org.shadowbot.bot.api.methods.data.movement.Walking;
import org.shadowbot.bot.api.methods.interactive.GameEntities;
import org.shadowbot.bot.api.methods.interactive.NPCs;
import org.shadowbot.bot.api.methods.interactive.Widgets;
import org.shadowbot.bot.api.util.Filter;
import org.shadowbot.bot.api.util.Random;
import org.shadowbot.bot.api.util.Time;
import org.shadowbot.bot.api.wrapper.GameObject;
import org.shadowbot.bot.api.wrapper.GameObjectComposite;
import org.shadowbot.bot.api.wrapper.NPC;
import org.shadowbot.bot.api.wrapper.WidgetChild;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SurpriseExamHandler extends RandomEvent {

    private final int WIDGET_PATTREN = 103;
    private final int WIDGET_ITEM_BOARD = 559;
    
    private final int GREEN_DOOR = 2090;
    private final int PURPLE_DOOR = 1768;
    private final int BLUE_DOOR = 1767;
    private final int RED_DOOR = 1766;

    private int CURRENT_DOOR = 0;

    private static final int[] Ranged = {11539, 11540, 11541, 11614, 11615, 11633};

    private static final int[] Cooking = {11526, 11529, 11545, 11549, 11550, 11555, 11560,
            11563, 11564, 11607, 11608, 11616, 11620, 11621, 11622, 11623,
            11628, 11629, 11634, 11639, 11641, 11649, 11624};

    private static final int[] Fishing = {11527, 11574, 11578, 11580, 11599, 11600, 11601,
            11602, 11603, 11604, 11605, 11606, 11625};

    private static final int[] Combat = {11528, 11531, 11536, 11537, 11579, 11591, 11592,
            11593, 11597, 11627, 11631, 11635, 11636, 11638, 11642, 11648};

    private static final int[] Farming = {11530, 11532, 11547, 11548, 11554, 11556, 11571,
            11581, 11586, 11610, 11645};

    private static final int[] Magic = {11533, 11534, 11538, 11562, 11567, 11582};

    private static final int[] Firemaking = {11535, 11551, 11552, 11559, 11646};

    private static final int[] Hats = {11540, 11557, 11558, 11560, 11570, 11619, 11626,
            11630, 11632, 11637, 11654};
    private static final int[] Pirate = {11570, 11626, 11558};

    private static final int[] Jewellery = {11572, 11576, 11652};

    private static final int[] Drinks = {11542, 11543, 11544, 11644, 11647};

    private static final int[] Woodcutting = {11573, 11595};

    private static final int[] Boots = {11561, 11618, 11650, 11651};

    private static final int[] Crafting = {11546, 11553, 11565, 11566, 11568, 11569, 11572,
            11575, 11576, 11577, 11581, 11583, 11584, 11585, 11643, 11652,
            11653};

    private static final int[] Mining = {11587, 11588, 11594, 11596, 11598, 11609, 11610};

    private static final int[] Smithing = {11611, 11612, 11613};
    private static final int[][] items = {Ranged, Cooking, Fishing, Combat, Farming, Magic,
            Firemaking, Hats, Drinks, Woodcutting, Boots, Crafting, Mining,
            Smithing};
    public Question[] simQuestions = {
            new Question("I never leave the house without some sort of jewellery.", Jewellery),
            new Question("Pretty accessories made from silver and gold", Jewellery),
            new Question("There is no better feeling than", Jewellery),
            new Question("I'm feeling dehydrated", Drinks),
            new Question("All this work is making me thirsty", Drinks),
            new Question("quenched my thirst", Drinks),
            new Question("light my fire", Firemaking),
            new Question("fishy", Fishing),
            new Question("fishing for answers", Fishing),
            new Question("fish out of water", Drinks),
            new Question("strange headgear", Hats),
            new Question("tip my hat", Hats),
            new Question("thinking cap", Hats),
            new Question("wizardry here", Magic),
            new Question("rather mystical", Magic),
            new Question("abracada", Magic),
            new Question("hide one's face", Hats),
            new Question("shall unmask", Hats),
            new Question("hand-to-hand", Combat),
            new Question("hate melee and magic", Combat),
            new Question("hate ranging and magic", Combat),
            new Question("melee weapon", Combat),
            new Question("prefers melee", Combat),
            new Question("me hearties", Pirate),
            new Question("puzzle for landlubbers", Pirate),
            new Question("mighty pirate", Pirate),
            new Question("mighty archer", Ranged),
            new Question("as an arrow", Ranged),
            new Question("Ranged attack", Ranged),
            new Question("shiny things", Crafting),
            new Question("igniting", Firemaking),
            new Question("sparks from my synapses.", Firemaking),
            new Question("fire.", Firemaking),
            new Question("disguised", Hats),
            new Question("range", Ranged),
            new Question("arrow", Ranged),
            new Question("drink", Drinks),
            new Question("logs", Firemaking),
            new Question("light", Firemaking),
            new Question("headgear", Hats),
            new Question("hat", Hats),
            new Question("cap", Hats),
            new Question("mine", Mining),
            new Question("mining", Mining),
            new Question("ore", Mining),
            new Question("fish", Fishing),
            new Question("fishing", Fishing),
            new Question("thinking cap", Hats),
            new Question("cooking", Cooking),
            new Question("cook", Cooking),
            new Question("bake", Cooking),
            new Question("farm", Farming),
            new Question("farming", Farming),
            new Question("cast", Magic),
            new Question("magic", Magic),
            new Question("craft", Crafting),
            new Question("boot", Boots),
            new Question("chop", Woodcutting),
            new Question("cut", Woodcutting),
            new Question("tree", Woodcutting),
    };


    @Override
    public String getAuthor() {
        return "Magorium and Lorex";
    }

    @Override
    public String getName() {
        return "Surprise Exam";
    }

    @Override
    public boolean isActive() {
        boolean b = NPCs.getNearest("Mr. Mordaut") != null;
        CURRENT_DOOR = !b ? 0 : CURRENT_DOOR;
        return Game.isLoggedIn() && b;
    }

    @Override
    public int solve() {
        final NPC dragon = NPCs.getNearest("Mr. Mordaut");
        if (CURRENT_DOOR > 0) {
            final GameObject door = GameEntities.getNearest(CURRENT_DOOR);
            if (door != null) {
                if (door.isOnScreen()) {
                    //Camera set angle to....
                    Camera.setPitch(0);
                    door.interact("Open");
                    for (int i = 0; i < 25 && isActive(); i++, Time.sleep(100, 150)) ;
                } else {
                    Walking.walkTo(door.getLocation());
                    for (int i = 0; i < 25 && door.isOnScreen(); i++, Time.sleep(100, 150)) ;
                }
            }
        } else if (Widgets.canContinue()) {
            if (Widgets.getWidget(242).isValid()) {
                String text = Widgets.getWidget(242, 2).getText();
                if (text.toLowerCase().contains("purple")) {
                    CURRENT_DOOR = getDoorIds().get(2);
                } else if (text.toLowerCase().contains("red")) {
                    CURRENT_DOOR = getDoorIds().get(0);
                } else if (text.toLowerCase().contains("green")) {
                    CURRENT_DOOR = getDoorIds().get(3);
                } else if (text.toLowerCase().contains("blue")) {
                    CURRENT_DOOR = getDoorIds().get(1);
                }
            }
            Widgets.clickContinue();
        } else if (Widgets.getWidget(WIDGET_ITEM_BOARD).isValid()) {
            WidgetChild[] children = getWidgetWith(getSolve());
            System.out.println(children.length);
            for (WidgetChild child : children) {
                child.click(true);
                Time.sleep(1000, 2000);
            }
            if (Widgets.getWidget(WIDGET_ITEM_BOARD) != null) {
                Widgets.getWidget(WIDGET_ITEM_BOARD, 70).click(true);
                Time.sleep(1000, 2000);
            }
        } else if (Widgets.getWidget(WIDGET_PATTREN).isValid()) {
            if (getSolve()[0] > 0) {
                WidgetChild solve = getWidgetWith(getSolve()[0]);
                solve.click(true);
            } else {
                Widgets.getWidget(WIDGET_PATTREN, Random.nextInt(10, 13)).click(true);
            }
            for (int i = 0; i < 25 && Widgets.getWidget(WIDGET_PATTREN).isValid(); i++, Time.sleep(100, 150)) ;
        } else {
            if (dragon != null) {
                if (dragon.isOnScreen()) {
                    dragon.interact("Talk-to");
                    for (int i = 0; i < 25 && !Widgets.canContinue(); i++, Time.sleep(100, 150)) ;
                } else {
                    Walking.walkTo(dragon.getLocation());
                }
            }
        }
        return Random.nextInt(150,200);
    }

    private List<Integer> getDoorIds(){
        final List<Integer> doorIdList = new ArrayList<>();
        final GameObject[] doorObjects = GameEntities.getAll(new Filter<GameObject>() {

            @Override
            public boolean accept(final GameObject o) {
                final GameObjectComposite goc = o.getComposite();
                if (goc != null) {
                    return goc.getName().equals("Door");
                }
                return false;
            }
        });
        for (final GameObject o : doorObjects) {
            if (o != null) {
                doorIdList.add(o.getId());
            }
        }
        Collections.sort(doorIdList);
        return doorIdList;
    }


    private WidgetChild[] getWidgetWith(int[] id) {
        ArrayList<WidgetChild> widgetChildren = new ArrayList<WidgetChild>();
        for (int i = 24; i < 39; i++) {
            if (Calculations.stackContain(id, Widgets.getWidget(WIDGET_ITEM_BOARD, i).getModelId())) {
                widgetChildren.add(Widgets.getWidget(WIDGET_ITEM_BOARD, i));
            }
        }
        return widgetChildren.toArray(new WidgetChild[widgetChildren.size()]);
    }

    private WidgetChild getWidgetWith(int id) {
        for (int i = 12; i < 16; i++) {
            if (Widgets.getWidget(WIDGET_PATTREN, i).getModelId() == id) {
                return Widgets.getWidget(WIDGET_PATTREN, i);
            }
        }
        return null;
    }

    private int[] getSolve() {
        if (Widgets.getWidget(WIDGET_PATTREN).isValid()) {
            int[][] questions = {new int[]{11591, 11592, 11593}, new int[]{11596, 11588, 11553}, new int[]{11572, 11598, 11576},
                    new int[]{11533, 11562, 11534}, new int[]{11560, 11555, 11616}, new int[]{11561, 11650, 11651},
                    new int[]{11541, 11615, 11633}, new int[]{11597, 11536, 11537}, new int[]{11648, 11612, 11588}};
            int[] answers = {11597, 11612, 11543, 11538, 11620, 11618, 11539, 11642, 11596};
            for (int i = 0; i < questions.length; i++) {
                if (match(getCurrentPattern(), questions[i])) {
                    return new int[]{answers[i]};
                }
            }
            if (getCurrentPattern().length > 2) {
                int x = 0;
                int[] answer_items = null;
                for (int[] item : items) {
                    x = 0;
                    for (int answer : item) {
                        if (Calculations.stackContain(getCurrentPattern(), answer)) {
                            x++;
                        }
                        if (x == 3) {
                            answer_items = item;
                        }
                    }
                }
                if (answer_items != null) {
                    for (int i = 12; i < 16; i++) {
                        if (Calculations.stackContain(answer_items, Widgets.getWidget(WIDGET_PATTREN, i).getModelId())) {
                            return new int[]{Widgets.getWidget(WIDGET_PATTREN, i).getModelId()};
                        }
                    }
                }
            }
        } else if (Widgets.getWidget(WIDGET_ITEM_BOARD).isValid()) {
            String question = Widgets.getWidget(WIDGET_ITEM_BOARD, 72).getText();
            for (Question q : simQuestions) {
                if (question.toLowerCase().contains(q.getQuestion().toLowerCase())) {
                    return q.getAnswers();
                }
            }
        }
        return new int[1];
    }

    private int[] getCurrentPattern() {
        int[] pattern;
        if (Widgets.getWidget(WIDGET_PATTREN).isValid()) {
            pattern = new int[3];
            for (int i = 8; i < 11; i++) {
                pattern[i - 8] = Widgets.getWidget(WIDGET_PATTREN, i) != null ? Widgets.getWidget(WIDGET_PATTREN, i).getModelId() : 0;
            }
        } else {
            pattern = new int[15];
            for (int i = 24; i < 39; i++) {
                pattern[i - 24] = Widgets.getWidget(WIDGET_ITEM_BOARD, i).getModelId();
            }
        }
        return pattern;
    }

    private boolean match(int[] e, int[] j) {
        ArrayList<Integer> integer = new ArrayList<Integer>();
        for (int x : j) {
            integer.add(x);
        }
        if (e.length != j.length) {
            return false;
        }
        for (int a : e) {
            if (!integer.contains(a)) {
                return false;
            }
        }
        return true;
    }

    private class Question {

        String Question;
        int[] items;

        private Question(String Question, int[] items) {
            this.Question = Question;
            this.items = items;
        }

        public String getQuestion() {
            return this.Question;
        }

        public int[] getAnswers() {
            return this.items;
        }
    }

}
