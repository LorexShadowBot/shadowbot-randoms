package org.shadowbot.bot.client.script.randomsevent;

import org.shadowbot.bot.api.RandomEvent;
import org.shadowbot.bot.api.methods.data.Game;
import org.shadowbot.bot.api.methods.data.Settings;
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
import org.shadowbot.bot.api.wrapper.GameObject;
import org.shadowbot.bot.api.wrapper.NPC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Magorium
 * Date: 8/14/13
 * Time: 10:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class DrillDemonHandler extends RandomEvent {

    private Exercise currentExercise = null;
    private int sign1 = 1, sign2 = 2, sign3 = 3, sign4 = 4;
    List<Integer> mats = new ArrayList<>();

    @Override
    public String getAuthor() {
        return "Magorium, Lorex";
    }

    @Override
    public String getName() {
        return "Drill Demon";
    }

    @Override
    public boolean isActive() {
        return Game.isLoggedIn() && NPCs.getNearest("Sergeant Damien") != null && NPCs.getNearest("Sergeant Damien").distanceTo() < 10;
    }

    @Override
    public int solve() {
        NPC sergeant = NPCs.getNearest("Sergeant Damien");
        if (Camera.getPitch() < 80) {
            Camera.setPitch(Random.nextInt(81, 90));
        } else if (currentExercise != null) {
            update();
            int id = 0;
            if (sign1 == currentExercise.id) {
                id = getMatIds().get(0);
            } else if (sign2 == currentExercise.id) {
                id = getMatIds().get(1);
            } else if (sign3 == currentExercise.id) {
                id = getMatIds().get(2);
            } else if (sign4 == currentExercise.id) {
                id = getMatIds().get(3);
            }
            GameObject exercise = GameEntities.getNearest(id);
            if (exercise != null) {
                if (exercise.isOnScreen()) {
                    if (exercise.interact("Use")) {
                        for (int i = 0; i < 50 && Players.getLocal().getAnimation() == -1; i++, Time.sleep(100, 150)) ;
                        for (int i = 0; i < 80 && Players.getLocal().getAnimation() != -1; i++, Time.sleep(100, 150)) ;
                        for (int i = 0; i < 20 && !Widgets.canContinue(); i++, Time.sleep(100, 150)) ;
                        currentExercise = null;
                    }
                } else {
                    Walking.walkTo(exercise.getLocation());
                }
            }
        } else if (Widgets.canContinue()) {
            if (Widgets.getWidget(519, 1) != null && Widgets.getWidget(519, 1).isVisible()) {
                currentExercise = getExercise(Widgets.getWidget(519, 1).getText());
                Log.info("Exercise: " + currentExercise.getName());
            }
            Widgets.clickContinue();
        } else if (sergeant.isOnScreen()) {
            sergeant.click();
            for (int i = 0; i < 30 && !Widgets.canContinue(); i++, Time.sleep(100, 150)) ;
        } else {
            Camera.turnTo(sergeant.getLocation());
        }
        return Random.nextInt(150, 200);
    }

    private List<Integer> getMatIds(){
        if(mats.size() == 0){
        final List<Integer> ids = new ArrayList<>();
        GameObject[] objs = GameEntities.getAll(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject o) {

                return o.getType() == GameObject.Type.FLOOR_DECORATION && o.distanceTo() < 10;
            }
        });
        for (GameObject o : objs) {
            int count = 0;
            int id = o.getId();
            for (GameObject o1 : objs) {
                if (id == o1.getId()) {
                    count++;
                }
            }
            if (count == 2) {
                if(!ids.contains(id)){
                    ids.add(id);
                }
            }
        }
        Collections.sort(ids);
        if(Math.abs(ids.get(0) - ids.get(1)) > 3){
            ids.remove(0);
        }
        for(int i = 1; i < ids.size() - 1; i++){
            if(Math.abs(ids.get(i) - ids.get(i - 1)) > 3 && Math.abs(ids.get(i) - ids.get(i + 1)) > 3){
                ids.remove(i);
            }
        }
        if(Math.abs(ids.get(ids.size() - 1) - ids.get(ids.size() - 2)) > 3){
            ids.remove(ids.size() - 1);
        }
            mats = ids;
        }
        return mats;
    }

    private enum Exercise {
        JUMPING_JACKS(1, "Star jumps"),
        PUSH_UPS(2, "Push ups"),
        JOGGING(4, "Jog"),
        SIT_UPS(3, "Sit ups");

        int id;
        String name;

        Exercise(int id, String name) {
            this.id = id;
            this.name = name;
        }

        private int getObjectID() {
            return id;
        }

        private String getName() {
            return this.name;
        }
    }

    private Exercise getExercise(String text) {
        for (Exercise e : Exercise.values()) {
            if (text.toLowerCase().contains(e.getName().toLowerCase())) return e;
        }
        return null;
    }

    private void update() {
        switch (Settings.getSetting(531)) {
            case 668:
                sign1 = 1;
                sign2 = 2;
                sign3 = 3;
                sign4 = 4;
                break;
            case 675:
                sign1 = 2;
                sign2 = 1;
                sign3 = 3;
                sign4 = 4;
                break;
            case 724:    //
                sign1 = 1;
                sign2 = 3;
                sign3 = 2;
                sign4 = 4;
                break;
            case 738:    //
                sign1 = 3;
                sign2 = 1;
                sign3 = 2;
                sign4 = 4;
                break;
            case 787:   //
                sign1 = 2;
                sign2 = 3;
                sign3 = 1;
                sign4 = 4;
                break;
            case 794:
                sign1 = 3;
                sign2 = 2;
                sign3 = 1;
                sign4 = 4;
                break;
            case 1116:    //
                sign1 = 1;
                sign2 = 2;
                sign3 = 4;
                sign4 = 3;
                break;
            case 1123:
                sign1 = 2;
                sign2 = 1;
                sign3 = 4;
                sign4 = 3;
                break;
            case 1228:      //
                sign1 = 1;
                sign2 = 4;
                sign3 = 2;
                sign4 = 3;
                break;
            case 1249:     //
                sign1 = 4;
                sign2 = 1;
                sign3 = 2;
                sign4 = 3;
                break;
            case 1291:
                sign1 = 2;
                sign2 = 4;
                sign3 = 1;
                sign4 = 3;
                break;
            case 1305:
                sign1 = 4;
                sign2 = 2;
                sign3 = 1;
                sign4 = 3;
                break;
            case 1620:  //
                sign1 = 1;
                sign2 = 3;
                sign3 = 4;
                sign4 = 2;
                break;
            case 1634:  //
                sign1 = 3;
                sign2 = 1;
                sign3 = 4;
                sign4 = 2;
                break;
            case 1676:
                sign1 = 1;
                sign2 = 4;
                sign3 = 3;
                sign4 = 2;
                break;
            case 1697:  //
                sign1 = 4;
                sign2 = 1;
                sign3 = 3;
                sign4 = 2;
                break;
            case 1802:  //
                sign1 = 3;
                sign2 = 4;
                sign3 = 1;
                sign4 = 2;
                break;
            case 1809:   //
                sign1 = 4;
                sign2 = 3;
                sign3 = 1;
                sign4 = 2;
                break;
            case 2131:     //
                sign1 = 2;
                sign2 = 3;
                sign3 = 4;
                sign4 = 1;
                break;
            case 2138:    //
                sign1 = 3;
                sign2 = 2;
                sign3 = 4;
                sign4 = 1;
                break;
            case 2187:        //
                sign1 = 2;
                sign2 = 4;
                sign3 = 3;
                sign4 = 1;
                break;
            case 2201:      //
                sign1 = 4;
                sign2 = 2;
                sign3 = 3;
                sign4 = 1;
                break;
            case 2250:        //
                sign1 = 3;
                sign2 = 4;
                sign3 = 2;
                sign4 = 1;
                break;
            case 2257:     //
                sign1 = 4;
                sign2 = 3;
                sign3 = 2;
                sign4 = 1;
                break;
        }
    }
}
