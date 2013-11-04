package org.shadowbot.bot.client.script.randomsevent;

import org.shadowbot.bot.api.RandomEvent;
import org.shadowbot.bot.api.listeners.PaintListener;
import org.shadowbot.bot.api.methods.data.Game;
import org.shadowbot.bot.api.methods.data.movement.Camera;
import org.shadowbot.bot.api.methods.interactive.GameEntities;
import org.shadowbot.bot.api.methods.interactive.Widgets;
import org.shadowbot.bot.api.util.Filter;
import org.shadowbot.bot.api.util.Log;
import org.shadowbot.bot.api.util.Random;
import org.shadowbot.bot.api.util.Time;
import org.shadowbot.bot.api.wrapper.GameObject;
import org.shadowbot.bot.api.wrapper.GameObjectComposite;
import org.shadowbot.bot.api.wrapper.Model;

import java.awt.*;

public class LostAndFoundHandler extends RandomEvent implements PaintListener {

    public static final String appendageName = "Appendage";

    //QuickFix: ids:
    public static final int[] ids = {8974, 8734, 8973, 8972, 8975};


    @Override
    public String getAuthor() {
        return "Lorex";
    }

    @Override
    public String getName() {
        return "Lost And Found";
    }

    @Override
    public boolean isActive() {
   /*     final GameObject appendage = GameEntities.getNearest(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject gameObject) {
                final GameObjectComposite gameObjectComposite = gameObject.getComposite();
                if (gameObjectComposite != null) {
                    final String name = gameObjectComposite.getName();
                    if (name != null) {
                        return name.equals(appendageName);
                    }
                }
                return false;
            }
        });
        return Game.isLoggedIn() && appendage != null && appendage.distanceTo() < 6;
*/
        return false;
    }

    @Override
    public int solve() {
        if (Camera.getPitch() < 75) {
            Camera.setPitch(Random.nextInt(80, 90));
        }
        if (Widgets.canContinue()) {
            Widgets.clickContinue();
            for (int i = 0; i < 60 && Widgets.canContinue(); i++) {
                Time.sleep(40, 60);
            }
        } else {
            final GameObject[] gameObjects = GameEntities.getAll(new Filter<GameObject>() {
                @Override
                public boolean accept(GameObject gameObject) {
                    if (gameObject != null) {
                        final GameObjectComposite gameObjectComposite = gameObject.getComposite();
                        if (gameObjectComposite != null) {
                            final String name = gameObjectComposite.getName();
                            if (name != null) {
                                return name.equals(appendageName);
                            }
                        }
                    }
                    return false;
                }
            });
            int count = 0;
            int triangles;
            for (final GameObject gameObject : gameObjects) {
                if (gameObject != null) {
                    final Model model = gameObject.getModel();
                    if (model != null) {
                        triangles = model.getTriangles().length;
                        for (final GameObject gameObject1 : gameObjects) {
                            if (gameObject1 != null) {
                                final Model model1 = gameObject.getModel();
                                if (model1 != null) {
                                    if (model1.getTriangles().length == triangles) {
                                        count++;
                                    }
                                }
                            }
                        }
                        if (count == 2) {
                            if (gameObject.interact("Operate")) {
                                for (int i = 0; i < 40 && isActive(); i++) {
                                    Time.sleep(50, 150);
                                }
                                Log.info("Method 1");
                            }
                        }
                    }
                }
            }
            int id;
            count = 0;
            for (final GameObject gameObject : gameObjects) {
                if (gameObject != null) {
                    id = gameObject.getId();
                    for (final GameObject gameObject1 : gameObjects) {
                        if (gameObject1 != null) {
                            if (gameObject1.getId() == id) {
                                count++;
                            }
                        }
                    }
                    if (count == 2) {
                        if (gameObject.interact("Operate")) {
                            for (int i = 0; i < 40 && isActive(); i++) {
                                Time.sleep(50, 150);
                            }
                            Log.info("Method 2");
                        }
                    }
                }
            }
        }
        return Random.nextInt(150, 250);
    }


    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
    }
}
