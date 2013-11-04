package org.shadowbot.bot.client.script.randomsevent;

import org.shadowbot.bot.api.Manifest;
import org.shadowbot.bot.api.RandomEventHandler;
import org.shadowbot.bot.api.ShadowScript;
import org.shadowbot.bot.api.SkillCategory;
import org.shadowbot.bot.api.listeners.PaintListener;
import org.shadowbot.bot.api.methods.data.movement.Camera;
import org.shadowbot.bot.api.methods.data.movement.Walking;
import org.shadowbot.bot.api.methods.interactive.NPCs;
import org.shadowbot.bot.api.methods.interactive.Players;
import org.shadowbot.bot.api.util.Random;
import org.shadowbot.bot.api.util.Time;
import org.shadowbot.bot.api.wrapper.NPC;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Osama
 * Date: 10/18/13
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */

@Manifest(scriptname = "RandomFinder", category = SkillCategory.MISC, description = "Find randoms", author = "Lorex and Magorium")
public class ManThiever extends ShadowScript implements PaintListener {

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
    private final ReportHandler reportHandler = new ReportHandler();
    private final RewardHandler rewardHandler = new RewardHandler();
    private final SandwichLadyHandler sandwichLadyHandler = new SandwichLadyHandler();
    private final ScapeIslandHandler scapeIslandHandler = new ScapeIslandHandler();
    private final StrangeBoxHandler strangeBoxHandler = new StrangeBoxHandler();
    private final StrangePlantHandler strangePlantHandler = new StrangePlantHandler();
    private final SurpriseExamHandler surpriseExamHandler = new SurpriseExamHandler();
    private final TalkerHandler talkerHandler = new TalkerHandler();
    private final PinballHandler pinballHandler = new PinballHandler();

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
        RandomEventHandler.addRandomEvent(reportHandler);
        RandomEventHandler.addRandomEvent(rewardHandler);
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
        RandomEventHandler.removeRandomEvent(reportHandler);
        RandomEventHandler.removeRandomEvent(rewardHandler);
        RandomEventHandler.removeRandomEvent(sandwichLadyHandler);
        RandomEventHandler.removeRandomEvent(scapeIslandHandler);
        RandomEventHandler.removeRandomEvent(strangeBoxHandler);
        RandomEventHandler.removeRandomEvent(strangePlantHandler);
        RandomEventHandler.removeRandomEvent(surpriseExamHandler);
        RandomEventHandler.removeRandomEvent(talkerHandler);
        RandomEventHandler.removeRandomEvent(pinballHandler);
    }

    @Override
    public int loop() {
        final NPC npc = NPCs.getNearest("Man");
        if (npc != null) {
            if (npc.isOnScreen()) {
                npc.interact("Pickpocket");
                for (int i = 0; i < 10 && Players.getLocal().getAnimation() == -1; i++, Time.sleep(100, 150)) ;
                for (int i = 0; i < 10 && Players.getLocal().getAnimation() != -1; i++, Time.sleep(100, 150)) ;
                Time.sleep(100, 150);
            } else {
                if (npc.distanceTo() < 8) {
                    Camera.turnTo(npc);
                } else {
                    Walking.walkTo(npc);
                }
            }
        }
        return Random.nextInt(200, 300);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void paint(Graphics arg0) {

    }
}
