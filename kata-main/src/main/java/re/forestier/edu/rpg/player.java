package re.forestier.edu.rpg;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    public String PlayerName;
    public String Avatar_name;
    private String AvatarClass;

    public Integer money;
    private Float __real_money__;

    public int level;
    public int healthpoints;
    public int currenthealthpoints;
    protected int xp;

    public HashMap<String, Integer> abilities;
    public ArrayList<String> inventory;

    public Player(String PlayerName, String avatar_name, String avatarClass, int money, ArrayList<String> inventory) {
        if (!avatarClass.equals("ARCHER") && !avatarClass.equals("ADVENTURER") && !avatarClass.equals("DWARF")
                && !avatarClass.equals("GOBLIN")) {
            return;
        }

        this.PlayerName = PlayerName;
        Avatar_name = avatar_name;
        AvatarClass = avatarClass;
        this.money = Integer.valueOf(money);
        this.inventory = inventory;
        this.abilities = UpdatePlayer.abilitiesPerTypeAndLevel().get(AvatarClass).get(1);
    }

    public String getAvatarClass() {
        return AvatarClass;
    }

    public void removeMoney(int amount) throws IllegalArgumentException {
        if (money - amount < 0) {
            throw new IllegalArgumentException("Player can't have a negative money!");
        }

        money = Integer.parseInt(money.toString()) - amount;
    }

    public void addMoney(int amount) {
        var value = Integer.valueOf(amount);
        money = money + (value != null ? value : 0);
    }

    public int retrieveLevel() {
        if (xp < 10)
            return 1;
        if (xp < 27)
            return 2;
        if (xp < 57)
            return 3;
        if (xp < 111)
            return 4;
        return 5;
    }

    public int getXp() {
        return this.xp;
    }

    /*
     * Ингредиенты:
     * Для теста:
     * 
     * 250 г муки
     * 125 г сливочного масла (холодное)
     * 70 г сахара
     * 1 яйцо
     * 1 щепотка соли
     */

}
