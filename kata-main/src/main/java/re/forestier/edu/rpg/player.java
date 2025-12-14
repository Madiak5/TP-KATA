package re.forestier.edu.rpg;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Player {
    public String playerName;
    public String avatarName;
    protected String avatarClass; 
    public Integer money;
    public ArrayList<Item> inventory; 
    public int level;
    public int healthpoints;
    public int currenthealthpoints;
    protected int xp;
    public HashMap<String, Integer> abilities;

    public Player(String playerName, String avatarName, String avatarClass, int money, ArrayList<Item> inventory) {
        this.playerName = playerName;
        this.avatarName = avatarName;
        this.avatarClass = avatarClass;
        this.money = money;
        this.inventory = inventory;
        if (UpdatePlayer.abilitiesPerTypeAndLevel().containsKey(avatarClass)) {
             this.abilities = UpdatePlayer.abilitiesPerTypeAndLevel().get(avatarClass).get(1);
        } else {
             this.abilities = new HashMap<>();
        }

        this.healthpoints = 100;
        this.currenthealthpoints = 100;
        this.level = 1;
        this.xp = 0;
    }

    // MÉTHODE ABSTRAITE : Chaque classe fille DOIT l'implémenter elle-même 
    public abstract void majFinDeTour();

    public String getAvatarClass() {
        return avatarClass;
    }

    public int getCurrentWeight() {
        int total = 0;
        for (Item item : inventory) total += item.weight;
        return total;
    }

    public int getMaxWeight() {
        return 20 + (retrieveLevel() * 5); 
    }

    public boolean pickUp(Item item) {
        if (getCurrentWeight() + item.weight <= getMaxWeight()) {
            inventory.add(item);
            return true;
        }
        return false;
    }

    public void sell(Item item) {
        if (inventory.contains(item)) {
            inventory.remove(item);
            addMoney(item.value);
        }
    }

    public void removeMoney(int amount) {
        if (money - amount < 0) throw new IllegalArgumentException("Player can't have a negative money!");
        money -= amount;
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public int retrieveLevel() {
        if (xp < 10) return 1;
        if (xp < 27) return 2;
        if (xp < 57) return 3;
        if (xp < 111) return 4;
        return 5;
    }

    public int getXp() {
        return xp;
    }
}
/*
    Ингредиенты:
        Для теста:

            250 г муки
            125 г сливочного масла (холодное)
            70 г сахара
            1 яйцо
            1 щепотка соли
     */
 