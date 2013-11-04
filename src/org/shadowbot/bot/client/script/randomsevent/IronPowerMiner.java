package org.shadowbot.bot.client.script.randomsevent;

import org.shadowbot.bot.api.Manifest;
import org.shadowbot.bot.api.RandomEventHandler;
import org.shadowbot.bot.api.ShadowScript;
import org.shadowbot.bot.api.SkillCategory;
import org.shadowbot.bot.api.methods.data.Inventory;
import org.shadowbot.bot.api.methods.interactive.GameEntities;
import org.shadowbot.bot.api.methods.interactive.GroundItems;
import org.shadowbot.bot.api.methods.interactive.Players;
import org.shadowbot.bot.api.util.*;
import org.shadowbot.bot.api.wrapper.*;

@Manifest(author = "Lorex", category = SkillCategory.MINING, description = "powermines iron ore at alkarit", scriptname = "ironorepowerminer")
public class IronPowerMiner extends ShadowScript {

    private boolean check = false;
    private int[] ids = {14978, 14979, 10798, 10796};

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
        final Player local = Players.getLocal();
        if (!local.isMoving()) {
            final Item handle = Inventory.getItem(466);
            if (handle == null) {
                if (isExploding()) {
                    Log.info("exploding rock detected");
                    if (!check) {
                        Log.info("Clicking on other rock");
                        final GameObject rock = GameEntities.getNearest(ids);
                        if (rock != null) {
                            if (rock.interact("Mine")) {
                                for (int i = 0; i < 40 && Players.getLocal().getAnimation() == -1; i++, Time.sleep(40, 60))
                                    ;
                                if (Players.getLocal().getAnimation() != -1) {
                                    check = true;
                                }
                            }
                        }
                    }
                } else {
                    check = false;
                }
                if (Inventory.getCount(440) < 3) {
                    final GameObject rock = GameEntities.getNearest(new Filter<GameObject>() {
                        @Override
                        public boolean accept(GameObject gameObject) {
                            if (gameObject.distanceTo() < 2) {
                                return gameObject.getId() == 14978 || gameObject.getId() == 14979;
                            }
                            return false;
                        }
                    });
                    if (rock != null) {
                        rock.click();
                        for (int i = 0; i < 45 && Players.getLocal().getAnimation() == -1; i++) {
                            Time.sleep(40, 60);
                        }
                        for (int i = 0; i < 45 && Players.getLocal().getAnimation() != -1; i++) {
                            sleep(40, 60);
                        }
                    }
                } else {
                    for (int i = 0; i < 3; i++) {
                        int count = Inventory.getCount(440);
                        Inventory.dropItem(440);
                        for (int j = 0; j < 40 && count == Inventory.getCount(440); j++) {
                            Time.sleep(40, 60);
                        }
                    }
                }
            } else {
                final GroundItem head = GroundItems.getNearest(490);
                if (head != null) {
                    head.interact("Take");
                    for (int i = 0; i < 60 && !Inventory.contains(490); i++) {
                        Time.sleep(40, 60);
                    }
                } else {
                    final Item headInv = Inventory.getItem(490);
                    if (headInv != null) {
                        headInv.interact("Use");
                        Time.sleep(2000, 2500);
                        handle.interact("Use");
                        for (int i = 0; i < 40 && Inventory.contains(490); i++) {
                            Time.sleep(40, 60);
                        }
                    } else {
                        Log.info("Lost pickaxe head, sorry :(");
                    }
                }
            }
        }
        return Random.nextInt(150, 250);
    }

    private boolean isExploding() {
        return GameEntities.getNearest(new Filter<GameObject>() {

            @Override
            public boolean accept(GameObject arg0) {
                return arg0.getId() > 1000 && arg0.getId() < 3000 && arg0.distanceTo() < 2;
            }

        }) != null;
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
