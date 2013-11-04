package org.shadowbot.bot.client.script.randomsevent;

import org.shadowbot.bot.api.RandomEvent;
import org.shadowbot.bot.api.methods.data.Game;
import org.shadowbot.bot.api.methods.data.Inventory;
import org.shadowbot.bot.api.methods.data.Settings;
import org.shadowbot.bot.api.methods.interactive.Widgets;
import org.shadowbot.bot.api.util.Random;
import org.shadowbot.bot.api.util.Time;
import org.shadowbot.bot.api.wrapper.Item;
import org.shadowbot.bot.api.wrapper.WidgetChild;

/**
 * Created with IntelliJ IDEA.
 * User: Lorex
 * Date: 10/30/13
 * Time: 10:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class RewardHandler extends RandomEvent {

    private int currentId = 0;

    public static final int REWARD_INTERFACE = 134;

    public static final int ATTACK_ID = 3;
    public static final int STRENGTH_ID = 4;
    public static final int RANGED_ID = 5;
    public static final int MAGIC_ID = 6;
    public static final int DEFENCE_ID = 7;
    public static final int HITPOINTS_ID = 8;
    public static final int PRAYER_ID = 9;
    public static final int AGILITY_ID = 10;
    public static final int HERBLORE_ID = 11;
    public static final int THIEVING_ID = 12;
    public static final int CRAFTING_ID = 13;
    public static final int RUNECRAFT_ID = 14;
    public static final int MINING_ID = 15;
    public static final int SMITHING_ID = 16;
    public static final int FISHING_ID = 17;
    public static final int COOKING_ID = 18;
    public static final int FIREMAKING_ID = 19;
    public static final int WOODCUTTING_ID = 20;
    public static final int FLETCHING_ID = 21;
    public static final int SLAYER_ID = 22;
    public static final int FARMING_ID = 23;
    public static final int CONSTRUCTION_ID = 24;
    public static final int HUNTER_ID = 25;

    public static final int CONFIRM_ID = 26;


    public static final String BOOK_OF_KNOWLEDGE = "Book of knowledge";
    public static final String LAMP = "Lamp";

    public static final String[] REWARD_NAMES = {BOOK_OF_KNOWLEDGE, LAMP};

    @Override
    public boolean isActive() {
        return Game.isLoggedIn() && getReward() != null;
    }

    @Override
    public int solve() {
        if (currentId == 0) {
            setSkillId();
        } else {
            if (Widgets.getWidget(REWARD_INTERFACE).isValid()) {
                if (currentId != 0) {
                    if (Settings.getSetting(261) == currentId - 2) {
                        final WidgetChild confirm = Widgets.getWidget(REWARD_INTERFACE, CONFIRM_ID);
                        if (confirm != null) {
                            confirm.click(true);
                            for (int i = 0; i < 40 && Widgets.getWidget(REWARD_INTERFACE).isValid(); i++) {
                                Time.sleep(40, 60);
                            }
                        }
                    } else {
                        final WidgetChild skillChild = Widgets.getWidget(REWARD_INTERFACE, currentId);
                        if (skillChild != null) {
                            skillChild.click(true);
                            for (int i = 0; i < 40 && Settings.getSetting(261) != currentId - 2; i++) {
                                Time.sleep(40, 60);
                            }
                        }
                    }
                }
            } else {
                Item reward = getReward();
                if (reward != null) {
                    if (reward.interact("Read") || reward.interact("Rub")) {
                        for (int i = 0; i < 40 && !Widgets.getWidget(REWARD_INTERFACE).isValid(); i++) {
                            Time.sleep(40, 60);
                        }
                    }
                }
            }
        }
        return Random.nextInt(100, 150);
    }

    @Override
    public String getName() {
        return "Reward";
    }

    @Override
    public String getAuthor() {
        return "Lorex";
    }

    private void setSkillId() {
        switch ("") {
            case "Agility":
                currentId = AGILITY_ID;
                break;
            case "Attack":
                currentId = ATTACK_ID;
                break;
            case "Construction":
                currentId = CONSTRUCTION_ID;
                break;
            case "Crafting":
                currentId = CRAFTING_ID;
                break;
            case "Cooking":
                currentId = COOKING_ID;
                break;
            case "Defence":
                currentId = DEFENCE_ID;
                break;
            case "Farming":
                currentId = FARMING_ID;
                break;
            case "Firemaking":
                currentId = FIREMAKING_ID;
                break;
            case "Fishing":
                currentId = FISHING_ID;
                break;
            case "Fletching":
                currentId = FLETCHING_ID;
                break;
            case "Herblore":
                currentId = HERBLORE_ID;
                break;
            case "Hitpoints":
                currentId = HITPOINTS_ID;
                break;
            case "Hunter":
                currentId = HUNTER_ID;
                break;
            case "Magic":
                currentId = MAGIC_ID;
                break;
            case "Mining":
                currentId = MINING_ID;
                break;
            case "Prayer":
                currentId = PRAYER_ID;
                break;
            case "Ranged":
                currentId = RANGED_ID;
                break;
            case "Runecraft":
                currentId = RUNECRAFT_ID;
                break;
            case "Slayer":
                currentId = SLAYER_ID;
                break;
            case "Smithing":
                currentId = SMITHING_ID;
                break;
            case "Strength":
                currentId = STRENGTH_ID;
                break;
            case "Thieving":
                currentId = THIEVING_ID;
                break;
            case "Woodcutting":
                currentId = WOODCUTTING_ID;
                break;
            default:
                final Item reward = getReward();
                if (reward != null) {
                    if (reward.interact("Destroy") || reward.interact("Drop")) {
                        for (int i = 0; i < 40 && getReward() == reward; i++) {
                            Time.sleep(40, 60);
                        }
                    }
                }
                break;
        }
    }

    private Item getReward() {
        Item[] items = Inventory.getItems();
        for (Item item : items) {
            String itemName = item.getName();
            if (itemName != null) {
                for (String name : REWARD_NAMES) {
                    if (itemName.toLowerCase().contains(name.toLowerCase())) {
                        return item;
                    }
                }
            }
        }
        return null;
    }
}
