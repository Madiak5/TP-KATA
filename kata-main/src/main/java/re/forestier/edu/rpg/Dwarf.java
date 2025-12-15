package re.forestier.edu.rpg;

import java.util.ArrayList;
import java.util.HashMap;

public class Dwarf extends Player {

    public Dwarf(String playerName, String avatarName, int money, ArrayList<Item> inventory) {
        super(playerName, avatarName, "DWARF", money, inventory);
    }

    @Override
    protected void applyClassSpecificEndTurnEffects() {
        // Soin de base du Nain (+1 PV)
        this.currenthealthpoints += 1;
        
        // Bonus supplémentaire pour chaque "Holy Elixir" dans l'inventaire
        if (this.inventory != null) {
            for (Item i : this.inventory) {
                if (i.name.equals("Holy Elixir")) {
                    this.currenthealthpoints += 1;
                }
            }
        }
    }

    @Override
    protected HashMap<String, Integer> getLevelBonuses(int level) {
        HashMap<String, Integer> bonuses = new HashMap<>();

        // Donnees recuperes de l'ancien UpdatePlayer.java 
        switch (level) {
            case 1:
                bonuses.put("ALC", 4); bonuses.put("INT", 1);
                bonuses.put("ATK", 3);
                break;
            case 2:
                bonuses.put("DEF", 1); bonuses.put("ALC", 5);
                break;
            case 3:
                bonuses.put("ATK", 4);
                break;
            case 4:
                bonuses.put("DEF", 2);
                break;
            case 5:
                bonuses.put("CHA", 1);
                break;
            default:
                return null;
        }
        return bonuses;
    }
}