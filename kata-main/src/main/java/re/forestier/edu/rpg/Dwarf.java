package re.forestier.edu.rpg;

import java.util.ArrayList;
import java.util.HashMap;

public class Dwarf extends Player {

    public Dwarf(String playerName, String avatarName, int money, ArrayList<String> inventory) {
        super(playerName, avatarName, money, inventory);
        setAbilities();
    }

    @Override
    public void setAbilities() {
        this.abilities = abilitiesPerTypeAndLevel().get(1);
    }

    @Override
    public String getAvatarClass() {
        return "DWARF";
    }

    public HashMap<String, Integer> getAvatarLevel(int level) {
        return abilitiesPerTypeAndLevel().get(level);
    }

    public static HashMap<Integer, HashMap<String, Integer>> abilitiesPerTypeAndLevel() {
        HashMap<Integer, HashMap<String, Integer>> dwarfMap = new HashMap<>();

        Object[][] levels = {
                { 1, new String[] { "ALC", "INT", "ATK" }, new int[] { 4, 1, 3 } },
                { 2, new String[] { "DEF", "ALC" }, new int[] { 1, 5 } },
                { 3, new String[] { "ATK" }, new int[] { 4 } },
                { 4, new String[] { "DEF" }, new int[] { 2 } },
                { 5, new String[] { "CHA" }, new int[] { 1 } }
        };

        for (Object[] levelData : levels) {
            int level = (int) levelData[0];
            String[] abilities = (String[]) levelData[1];
            int[] values = (int[]) levelData[2];

            HashMap<String, Integer> levelAbilities = new HashMap<>();
            for (int i = 0; i < abilities.length; i++) {
                levelAbilities.put(abilities[i], values[i]);
            }
            dwarfMap.put(level, levelAbilities);
        }

        return dwarfMap;
    }

}
