package re.forestier.edu.rpg;

import java.util.ArrayList;
import java.util.HashMap;

public class Adventurer extends Player {

    public Adventurer(String playerName, String avatarName, int money, ArrayList<String> inventory) {
        super(playerName, avatarName, money, inventory);
        setAbilities();
    }

    @Override
    public void setAbilities() {
        this.abilities = abilitiesPerTypeAndLevel().get(1);
    }

    @Override
    public String getAvatarClass() {
        return "ADVENTURER";
    }

    public HashMap<String, Integer> getAvatarLevel(int level) {
        return abilitiesPerTypeAndLevel().get(level);
    }

    public HashMap<Integer, HashMap<String, Integer>> abilitiesPerTypeAndLevel() {
        HashMap<Integer, HashMap<String, Integer>> adventurerMap = new HashMap<>();

        Object[][] levels = {
                { 1, new String[] { "INT", "DEF", "ATK", "CHA" }, new int[] { 1, 1, 3, 2 } },
                { 2, new String[] { "INT", "CHA" }, new int[] { 2, 3 } },
                { 3, new String[] { "ATK", "ALC" }, new int[] { 5, 1 } },
                { 4, new String[] { "DEF" }, new int[] { 3 } },
                { 5, new String[] { "VIS", "DEF" }, new int[] { 1, 4 } }
        };

        for (Object[] levelData : levels) {
            int level = (int) levelData[0];
            String[] abilities = (String[]) levelData[1];
            int[] values = (int[]) levelData[2];

            HashMap<String, Integer> levelAbilities = new HashMap<>();
            for (int i = 0; i < abilities.length; i++) {
                levelAbilities.put(abilities[i], values[i]);
            }
            adventurerMap.put(level, levelAbilities);
        }

        return adventurerMap;
    }

}
