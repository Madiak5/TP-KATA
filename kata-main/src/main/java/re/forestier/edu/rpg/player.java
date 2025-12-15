package re.forestier.edu.rpg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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

    // Liste des butins possibles (Déplacé depuis UpdatePlayer)
    private static final Item[] POSSIBLE_LOOT = {
        new Item("Lookout Ring", "Prevents surprise attacks", 1, 50),
        new Item("Scroll of Stupidity", "INT-2 when applied to an enemy", 1, 10),
        new Item("Draupnir", "Increases XP gained by 100%", 5, 100),
        new Item("Magic Charm", "Magic +10 for 5 rounds", 2, 20),
        new Item("Rune Staff of Curse", "May burn your ennemies... Or yourself.", 10, 200),
        new Item("Combat Edge", "Well, that's an edge", 8, 80),
        new Item("Holy Elixir", "Recover your HP", 2, 25)
    };

    public Player(String playerName, String avatarName, String avatarClass, int money, ArrayList<Item> inventory) {
        this.playerName = playerName;
        this.avatarName = avatarName;
        this.avatarClass = avatarClass;
        this.money = money;
        this.inventory = inventory;
        
        this.healthpoints = 100;
        this.currenthealthpoints = 100;
        this.level = 1;
        this.xp = 0;

        this.abilities = getLevelBonuses(1);
        if (this.abilities == null) {
            this.abilities = new HashMap<>();
        }
    }

    // LOGIQUE DÉPLACÉE DE UpdatePlayer (Maintenant qui est maintenant là ici)

    public boolean addXp(int xpAmount) {
        int currentLevel = retrieveLevel();
        this.xp += xpAmount;
        int newLevel = retrieveLevel();

        if (newLevel != currentLevel) {
            Random random = new Random();
            pickUp(POSSIBLE_LOOT[random.nextInt(POSSIBLE_LOOT.length)]);

            HashMap<String, Integer> newAbilities = getLevelBonuses(newLevel);
            if (newAbilities != null) {
                newAbilities.forEach((stat, val) -> this.abilities.put(stat, val));
            }
            return true;
        }
        return false;
    }

    public void updateEndTurn() {
        if (this.currenthealthpoints <= 0) {
            System.out.println("Le joueur est KO !");
            return;
        }

        if (this.currenthealthpoints >= this.healthpoints) {
            this.currenthealthpoints = this.healthpoints;
            return;
        }
        
        if (this.currenthealthpoints >= this.healthpoints / 2) {
            return;
        }

       
        applyClassSpecificEndTurnEffects();

        if (this.currenthealthpoints > this.healthpoints) {
            this.currenthealthpoints = this.healthpoints;
        }
    }


    protected abstract HashMap<String, Integer> getLevelBonuses(int level);

    protected abstract void applyClassSpecificEndTurnEffects();


    public String getAvatarClass() {
        return avatarClass;
    }

    public int getCurrentWeight() {
        int total = 0;
        if (inventory != null) {
            for (Item item : inventory) total += item.weight;
        }
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