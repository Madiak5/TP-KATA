package re.forestier.edu.rpg;

import java.util.ArrayList;
import java.util.HashMap;

public class Archer extends Player {

    public Archer(String playerName, String avatarName, int money, ArrayList<Item> inventory) {
        super(playerName, avatarName, "ARCHER", money, inventory);
    }

    @Override
    protected void applyClassSpecificEndTurnEffects() {
        // Soin de base de l'archer
        this.currenthealthpoints += 1;

        // Vérification spécifique (Arc Magique)
        if (this.inventory != null) {
            for (Item i : this.inventory) {
                if (i.name.equals("Magic Bow")) {
                    this.currenthealthpoints += (this.currenthealthpoints / 8) - 1;
                }
            }
        }
    }

    @Override
    protected HashMap<String, Integer> getLevelBonuses(int level) {
        HashMap<String, Integer> bonuses = new HashMap<>();

        // Données récupérées de l'ancien UpdatePlayer.java
        switch (level) {
            case 1:
                bonuses.put("INT", 1); bonuses.put("ATK", 3);
                bonuses.put("CHA", 1); bonuses.put("VIS", 3);
                break;
            case 2:
                bonuses.put("DEF", 1); bonuses.put("CHA", 2);
                break;
            case 3:
                bonuses.put("ATK", 3);
                break;
            case 4:
                bonuses.put("DEF", 2);
                break;
            case 5:
                bonuses.put("ATK", 4);
                break;
            default:
                return null;
        }
        return bonuses;
    }
}