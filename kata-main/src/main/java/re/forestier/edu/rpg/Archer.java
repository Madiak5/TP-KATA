package re.forestier.edu.rpg;

import java.util.ArrayList;
import java.util.HashMap;

public class Archer extends Player {

    public Archer(String playerName, String avatarName, int money, ArrayList<String> inventory) {
        super(playerName, avatarName, money, inventory);
        setAbilities();
    }

    @Override
    public void setAbilities() {
        this.abilities = abilitiesPerTypeAndLevel().get(1);
    }

    @Override
    public String getAvatarClass() {
        return "ARCHER";
    }

    public HashMap<String, Integer> getAvatarLevel(int level) {
        return abilitiesPerTypeAndLevel().get(level);
    }

    public static HashMap<Integer, HashMap<String, Integer>> abilitiesPerTypeAndLevel() {
        HashMap<Integer, HashMap<String, Integer>> archerMap = new HashMap<>();

        Object[][] levels = {
                { 1, new String[] { "INT", "ATK", "CHA", "VIS" }, new int[] { 1, 3, 1, 3 } },
                { 2, new String[] { "DEF", "CHA" }, new int[] { 1, 2 } },
                { 3, new String[] { "ATK" }, new int[] { 3 } },
                { 4, new String[] { "DEF" }, new int[] { 2 } },
                { 5, new String[] { "ATK" }, new int[] { 4 } }
        };

        for (Object[] levelData : levels) {
            int level = (int) levelData[0];
            String[] abilities = (String[]) levelData[1];
            int[] values = (int[]) levelData[2];

            HashMap<String, Integer> levelAbilities = new HashMap<>();
            for (int i = 0; i < abilities.length; i++) {
                levelAbilities.put(abilities[i], values[i]);
            }
            archerMap.put(level, levelAbilities);
        }

        return archerMap;
    }

}
