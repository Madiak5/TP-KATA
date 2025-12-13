package re.forestier.edu.rpg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public abstract class Player {

    public String playerName;
    public String avatarName;

    public Integer money;
    // private Float __real_money__;
    public int maxWeight;
    public int currentWeight;

    public int level;
    public int healthpoints;
    public int currenthealthpoints;
    protected int xp;

    public HashMap<String, Integer> abilities;
    public ArrayList<String> inventory;

    public Player(String playerName, String avatarName, int money, ArrayList<String> inventory) {
        this.playerName = playerName;
        this.avatarName = avatarName;
        this.money = money;
        this.inventory = inventory;
        this.abilities = new HashMap<>();
    }

    public void removeMoney(int amount) throws IllegalArgumentException {
        if (money - amount < 0) {
            throw new IllegalArgumentException("Player can't have a negative money!");
        }
        money = money - amount;
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

    public void setXp(int xp) {
        this.xp = xp;
    }

    // ==========================================
    // MÉTHODES ABSTRAITES (Le Contrat)
    // ==========================================

    public abstract void setAbilities();

    public abstract String getAvatarClass();

    public abstract HashMap<String, Integer> getAvatarLevel(int level);

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
