package re.forestier.edu.rpg;

import java.util.ArrayList;
import java.util.HashMap;

public class Adventurer extends Player {

    public Adventurer(String playerName, String avatarName, int money, ArrayList<Item> inventory) {
        super(playerName, avatarName, "ADVENTURER", money, inventory);
    }

    @Override
    protected void applyClassSpecificEndTurnEffects() {
        // Logique spécifique de l'aventurier (Soin de +2, réduit de 1 si bas niveau)
        this.currenthealthpoints += 2;
        if (this.retrieveLevel() < 3) {
            this.currenthealthpoints -= 1;
        }
    }

    @Override
    protected HashMap<String, Integer> getLevelBonuses(int level) {
        HashMap<String, Integer> bonuses = new HashMap<>();
        
        // Ces données viennent de l'ancien UpdatePlayer.java
        switch (level) {
            case 1:
                bonuses.put("INT", 1); bonuses.put("DEF", 1);
                bonuses.put("ATK", 3); bonuses.put("CHA", 2);
                break;
            case 2:
                bonuses.put("INT", 2); bonuses.put("CHA", 3);
                break;
            case 3:
                bonuses.put("ATK", 5); bonuses.put("ALC", 1);
                break;
            case 4:
                bonuses.put("DEF", 3);
                break;
            case 5:
                bonuses.put("VIS", 1); bonuses.put("DEF", 4);
                break;
            default:
                return null;
        }
        return bonuses;
    }
}