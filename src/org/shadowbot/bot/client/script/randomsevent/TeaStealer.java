package org.shadowbot.bot.client.script.randomsevent;

import org.shadowbot.bot.api.Manifest;
import org.shadowbot.bot.api.RandomEventHandler;
import org.shadowbot.bot.api.ShadowScript;
import org.shadowbot.bot.api.SkillCategory;
import org.shadowbot.bot.api.methods.data.Inventory;
import org.shadowbot.bot.api.methods.interactive.GameEntities;
import org.shadowbot.bot.api.util.Random;
import org.shadowbot.bot.api.util.Time;
import org.shadowbot.bot.api.wrapper.GameObject;

@Manifest(author = "Lorex", category = SkillCategory.THEIVING, description = "steals Tea at Varrock", scriptname = "TeaStealer")
public class TeaStealer extends ShadowScript {

    private final AvoidCombatHandler avoidCombatHandler = new AvoidCombatHandler();
    private final DrillDemonHandler drillDemonHandler = new DrillDemonHandler();
    private final FreakyFosterHandler freakyFosterHandler = new FreakyFosterHandler();
    private final FrogCaveHandler frogCaveHandler = new FrogCaveHandler();
    private final LostAndFoundHandler lostAndFoundHandler = new LostAndFoundHandler();
    private final MazeHandler mazeHandler = new MazeHandler();
    private final MimeHandler mimeHandler = new MimeHandler();
    private final MollyHandler mollyHandler = new MollyHandler();
    private final PilloryHandler pilloryHandler = new PilloryHandler();
    private final PrisonHandler prisonHandler = new PrisonHandler();
    private final QuizMasterHandler quizMasterHandler = new QuizMasterHandler();
    private final SandwichLadyHandler sandwichLadyHandler = new SandwichLadyHandler();
    private final ScapeIslandHandler scapeIslandHandler = new ScapeIslandHandler();
    private final StrangeBoxHandler strangeBoxHandler = new StrangeBoxHandler();
    private final StrangePlantHandler strangePlantHandler = new StrangePlantHandler();
    private final SurpriseExamHandler surpriseExamHandler = new SurpriseExamHandler();
    private final TalkerHandler talkerHandler = new TalkerHandler();
    private final PinballHandler pinballHandler = new PinballHandler();

    @Override
    public int loop() {
        final GameObject tea = GameEntities.getNearest(635);
        if (tea != null) {
            tea.click();
            for (int i = 0; i < 40 && GameEntities.getNearest(635) != null; i++) {
                Time.sleep(50);
            }
        }
        if (Inventory.contains(1978)) {
            Inventory.dropItem(1978);
            for (int i = 0; i < 40 && Inventory.contains(1978); i++) {
                Time.sleep(50);
            }
        }
        return Random.nextInt(150, 250);
    }

    @Override
    public void onStart() {
        RandomEventHandler.addRandomEvent(avoidCombatHandler);
        RandomEventHandler.addRandomEvent(drillDemonHandler);
        RandomEventHandler.addRandomEvent(freakyFosterHandler);
        RandomEventHandler.addRandomEvent(frogCaveHandler);
        RandomEventHandler.addRandomEvent(lostAndFoundHandler);
        RandomEventHandler.addRandomEvent(mazeHandler);
        RandomEventHandler.addRandomEvent(mimeHandler);
        RandomEventHandler.addRandomEvent(mollyHandler);
        RandomEventHandler.addRandomEvent(pilloryHandler);
        RandomEventHandler.addRandomEvent(prisonHandler);
        RandomEventHandler.addRandomEvent(quizMasterHandler);
        RandomEventHandler.addRandomEvent(sandwichLadyHandler);
        RandomEventHandler.addRandomEvent(scapeIslandHandler);
        RandomEventHandler.addRandomEvent(strangeBoxHandler);
        RandomEventHandler.addRandomEvent(strangePlantHandler);
        RandomEventHandler.addRandomEvent(surpriseExamHandler);
        RandomEventHandler.addRandomEvent(talkerHandler);
        RandomEventHandler.addRandomEvent(pinballHandler);
    }

    @Override
    public void onStop() {
        RandomEventHandler.removeRandomEvent(avoidCombatHandler);
        RandomEventHandler.removeRandomEvent(drillDemonHandler);
        RandomEventHandler.removeRandomEvent(freakyFosterHandler);
        RandomEventHandler.removeRandomEvent(frogCaveHandler);
        RandomEventHandler.removeRandomEvent(lostAndFoundHandler);
        RandomEventHandler.removeRandomEvent(mazeHandler);
        RandomEventHandler.removeRandomEvent(mimeHandler);
        RandomEventHandler.removeRandomEvent(mollyHandler);
        RandomEventHandler.removeRandomEvent(pilloryHandler);
        RandomEventHandler.removeRandomEvent(prisonHandler);
        RandomEventHandler.removeRandomEvent(quizMasterHandler);
        RandomEventHandler.removeRandomEvent(sandwichLadyHandler);
        RandomEventHandler.removeRandomEvent(scapeIslandHandler);
        RandomEventHandler.removeRandomEvent(strangeBoxHandler);
        RandomEventHandler.removeRandomEvent(strangePlantHandler);
        RandomEventHandler.removeRandomEvent(surpriseExamHandler);
        RandomEventHandler.removeRandomEvent(talkerHandler);
        RandomEventHandler.removeRandomEvent(pinballHandler);
    }

}
