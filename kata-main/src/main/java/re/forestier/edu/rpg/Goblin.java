package re.forestier.edu.rpg;

import java.util.ArrayList;
import java.util.HashMap;

public class Goblin extends Player {

    public Goblin(String playerName, String avatarName, int money, ArrayList<Item> inventory) {
        super(playerName, avatarName, "GOBLIN", money, inventory);
    }

    @Override
    protected void applyClassSpecificEndTurnEffects() {
        // Le sujet n'indique aucun soin spécifique pour le Gobelin..
    }

    @Override
    protected HashMap<String, Integer> getLevelBonuses(int level) {
        HashMap<String, Integer> bonuses = new HashMap<>();

        // Stats définies dans la Partie 4 
        switch (level) {
            case 1:
                bonuses.put("INT", 2); bonuses.put("ATK", 2); bonuses.put("ALC", 1);
                break;
            case 2:
                bonuses.put("ATK", 3); bonuses.put("ALC", 4);
                break;
            case 3:
                bonuses.put("VIS", 1);
                break;
            case 4:
                bonuses.put("DEF", 1);
                break;
            case 5:
                bonuses.put("DEF", 2); bonuses.put("ATK", 4);
                break;
            default:
                return null;
        }
        return bonuses;
    }
}