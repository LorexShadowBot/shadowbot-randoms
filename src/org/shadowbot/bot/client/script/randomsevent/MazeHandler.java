package org.shadowbot.bot.client.script.randomsevent;

import org.shadowbot.bot.api.RandomEvent;
import org.shadowbot.bot.api.listeners.PaintListener;
import org.shadowbot.bot.api.methods.data.Game;
import org.shadowbot.bot.api.methods.data.movement.Camera;
import org.shadowbot.bot.api.methods.data.movement.Walking;
import org.shadowbot.bot.api.methods.interactive.GameEntities;
import org.shadowbot.bot.api.methods.interactive.Players;
import org.shadowbot.bot.api.methods.interactive.Widgets;
import org.shadowbot.bot.api.util.Filter;
import org.shadowbot.bot.api.util.Log;
import org.shadowbot.bot.api.util.Random;
import org.shadowbot.bot.api.util.Time;
import org.shadowbot.bot.api.wrapper.*;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Lorex
 * Date: 10/20/13
 * Time: 6:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class MazeHandler extends RandomEvent implements PaintListener {

    private final ArrayList<Tile> wrongDoors = new ArrayList<>();
    private ArrayList<Tile> alreadyUsedDoors = new ArrayList<>();

    public static final int OPEN_DOOR_ID = 83;
    public static final String FINAL_CHEST = "Strange shrine";
    private GameObject currentDoor = null;

    @Override
    public int solve() {
        if (Camera.getPitch() < 80) {
            Camera.setPitch(Random.nextInt(80, 90));
        }
        if (currentDoor == null) {
            final GameObject chest = GameEntities.getNearest(new Filter<GameObject>() {

                @Override
                public boolean accept(GameObject o) {
                    if (o != null) {
                        final GameObjectComposite oc = o.getComposite();
                        if (oc != null) {
                            final String name = oc.getName();
                            if (name != null) {
                                return name.equals(FINAL_CHEST);
                            }
                        }
                    }
                    return false;
                }

            });
            if (chest != null) {
                if (chest.distanceTo() < 2) {
                    if (chest.interact("Touch")) {
                    }

                }
            }
            currentDoor = getNextDoor();
            if (currentDoor == null) {
                Log.info("No door detected");
                if (alreadyUsedDoors != null) {
                    if (alreadyUsedDoors.size() > 0) {
                        for (int i = 0; i < alreadyUsedDoors.size(); i++) {
                            if (alreadyUsedDoors.get(i) != null) {
                                alreadyUsedDoors.remove(i);
                            }
                        }
                    }
                }
            }
        } else {
            if (notpassable()) {
                wrongDoors.add(currentDoor.getLocation());
                currentDoor = null;
                for (int j = 0; j < 3 && Widgets.canContinue(); j++) {
                    Widgets.clickContinue();
                    for (int i = 0; i < 40 && Widgets.canContinue(); i++, Time.sleep(40, 60))
                        ;
                }
            } else if (GameEntities.getNearest(OPEN_DOOR_ID) != null) {
                for (int i = 0; i < 30 && GameEntities.getNearest(OPEN_DOOR_ID) != null; i++, Time
                        .sleep(40, 60))
                    ;
                alreadyUsedDoors.add(currentDoor.getLocation());
                currentDoor = null;
            } else if (Widgets.canContinue()) {
                Widgets.clickContinue();
                for (int i = 0; i < 30 && Widgets.canContinue(); i++, Time.sleep(40, 60))
                    ;
            } else {
                if (!Players.getLocal().isMoving() && Players.getLocal().getAnimation() == -1) {
                    if (currentDoor.distanceTo() > 4) {
                        Walking.walkTo(currentDoor);
                        for (int i = 0; i < 20 && !Players.getLocal().isMoving(); i++, Time.sleep(
                                40, 60))
                            ;
                        for (int i = 0; i < 50 && Players.getLocal().isMoving(); i++, Time.sleep(
                                50, 150))
                            ;
                    } else {
                        turnCameraToDoor();
                        if (currentDoor.interact("Open")) {
                            for (int i = 0; i < 40 && GameEntities.getNearest(OPEN_DOOR_ID) == null; i++, Time
                                    .sleep(70, 130))
                                ;
                            if (GameEntities.getNearest(OPEN_DOOR_ID) != null) {
                                for (int i = 0; i < 50
                                        && GameEntities.getNearest(OPEN_DOOR_ID) != null; i++, Time
                                        .sleep(100, 150))
                                    ;
                                    alreadyUsedDoors.add(currentDoor.getLocation());
                                    currentDoor = null;
                            }
                        }
                    }
                }
            }
        }
        return Random.nextInt(80, 130);
    }

    @Override
    public boolean isActive() {
        if (Game.isLoggedIn()) {
            final Widget parent = Widgets.getWidget(209);
            if (parent.isValid()) {
                final WidgetChild child = parent.getChild(0);
                if (child != null) {
                    final String text = child.getText();
                    if (text != null) {
                        return text.toLowerCase().contains("complete the maze");
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "Maze";
    }

    @Override
    public String getAuthor() {
        return "Lorex";
    }

    private void turnCameraToDoor() {
        final GameObject border = GameEntities.getNearest(1489);
        if (border != null) {
            final Tile borderLocation = border.getLocation();
            final Tile playerLocation = Players.getLocal().getLocation();
            if (Math.abs(borderLocation.getX() - playerLocation.getX()) > Math.abs(borderLocation
                    .getY() - playerLocation.getY())) {
                if (borderLocation.getX() > playerLocation.getX()) {
                    Camera.setAngel(Random.nextInt(260, 280));
                } else {
                    Camera.setAngel(Random.nextInt(80, 100));
                }
            } else {
                if (borderLocation.getY() > playerLocation.getY()) {
                    Camera.setAngel(Random.nextInt(170, 190));
                } else {
                    int i = Random.nextInt(-10, 10);
                    if (i < 0) {
                        i += 360;
                    }
                    if (i >= 360) {
                        i -= 360;
                    }
                    Camera.setAngel(i);
                }
            }
        }
    }

    private boolean notpassable() {
        Widget parent = Widgets.getWidget(210);
        if (parent.isValid()) {
            WidgetChild child = parent.getChild(0);
            if (child != null) {
                final String text = child.getText();
                return text.toLowerCase().contains("don't think that's the right way");
            }
        }
        return false;
    }

    private GameObject getNextDoor() {
        final ArrayList<Tile> walkableTiles = getReachableAllTiles(Players.getLocal().getLocation());
        for (final Tile t : walkableTiles) {
            if (t != null) {
                for (GameObject obj : GameEntities.getAll(new Filter<GameObject>() {

                    @Override
                    public boolean accept(GameObject o) {
                        if (o != null) {
                            return Math.abs(o.getLocation().getX() - t.getX())
                                    + Math.abs(o.getLocation().getY() - t.getY()) == 1;
                        }
                        return false;
                    }

                })) {
                    if (isRightDoor(obj)) {
                        return obj;
                    }
                }
            }
        }
        return null;
    }

    private boolean isRightDoor(GameObject o) {
        if (o != null) {
            if (o.getId() == 16677 || o.getId() == 16678 || o.getId() == 16679 || o.getId() == 16680) {
                if (!wrongDoors.contains(o.getLocation())
                        && !alreadyUsedDoors.contains(o.getLocation())) {
                    return true;
                }
            }
        }
        return false;
    }

    private ArrayList<Tile> getReachableAllTiles(final Tile start) {
        final ArrayList<Tile> result = new ArrayList<>();
        int j = 0;
        int turned = 0;
        Tile last = null;
        final GameObject border = GameEntities.getNearest(1489);
        if (!isWalkable(start)) {
            if (border != null) {
                int index = 0;
                final ArrayList<Tile> cache = getNearestWalkableTiles(start);
                if (cache != null && cache.size() > 0) {
                    for (int i = 0; i < cache.size(); i++) {
                        if (Math.sqrt(Math.pow(
                                Math.abs(cache.get(i).getX() - border.getLocation().getX()), 2)
                                + Math.pow(
                                Math.abs(cache.get(i).getY() - border.getLocation().getY()),
                                2)) < Math.sqrt(Math.pow(
                                Math.abs(cache.get(index).getX() - border.getLocation().getX()), 2)
                                + Math.pow(
                                Math.abs(cache.get(index).getY()
                                        - border.getLocation().getY()), 2))) {
                            index = i;
                        }
                    }
                }
                last = cache.get(index);
            }
        } else {
            last = start;
        }
        if (last == null) {
            Log.info("Couldn't find valid start");
            return null;
        }
        Tile current = getNearestWalkableTiles(last).get(0);

        while (j < 100 && turned < 3) {
            j++;
            ArrayList<Tile> tiles = getNearestWalkableTiles(current);
            if (tiles.size() > 1) {
                for (Tile tile : tiles) {
                    if (tile.getX() != last.getX() || tile.getY() != last.getY()) {
                        if (!result.contains(current)) {
                            result.add(current);
                        }
                        last = current;
                        current = tile;
                        break;
                    }
                }
            } else {
                turned++;
                if (!result.contains(current)) {
                    result.add(current);
                }
                last = current;
                current = tiles.get(0);
            }
        }
        return result;
    }

    private ArrayList<Tile> getNearestWalkableTiles(final Tile tile) {
        final ArrayList<Tile> tiles = new ArrayList<Tile>();
        if (isWalkable(new Tile(tile.getX() + 1, tile.getY()))) {
            tiles.add(new Tile(tile.getX() + 1, tile.getY()));
        }
        if (isWalkable(new Tile(tile.getX() - 1, tile.getY()))) {
            tiles.add(new Tile(tile.getX() - 1, tile.getY()));
        }
        if (isWalkable(new Tile(tile.getX(), tile.getY() + 1))) {
            tiles.add(new Tile(tile.getX(), tile.getY() + 1));
        }
        if (isWalkable(new Tile(tile.getX(), tile.getY() - 1))) {
            tiles.add(new Tile(tile.getX(), tile.getY() - 1));
        }
        return tiles;

    }

    private boolean isWalkable(final Tile t) {
        GameObject[] objs = GameEntities.getAll(new Filter<GameObject>() {

            @Override
            public boolean accept(GameObject arg0) {
                return arg0.getLocation().equals(t);
            }

        });
        if (objs != null) {
            for (GameObject ob : objs) {
                if (ob != null) {
                    if (ob.getType() == GameObject.Type.WALL_DECORATION) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        if (currentDoor != null) {
            currentDoor.getLocation().draw(g);
        }
    }
}
