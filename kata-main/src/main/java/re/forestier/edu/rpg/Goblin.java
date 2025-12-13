package re.forestier.edu.rpg;

import java.util.ArrayList;
import java.util.HashMap;

public class Goblin extends Player {

    public Goblin(String playerName, String avatarName, int money, ArrayList<String> inventory) {
        super(playerName, avatarName, money, inventory);
        setAbilities();
    }

    @Override
    public void setAbilities() {
        // Initialisation des capacités spécifiques au Gobelin
        this.abilities = abilitiesPerTypeAndLevel().get(1);
    }

    @Override
    public String getAvatarClass() {
        return "GOBLIN";
    }

    @Override
    public HashMap<String, Integer> getAvatarLevel(int level) {
        return abilitiesPerTypeAndLevel().get(level);
    }

    public HashMap<Integer, HashMap<String, Integer>> abilitiesPerTypeAndLevel() {
        HashMap<Integer, HashMap<String, Integer>> goblinMap = new HashMap<>();

        Object[][] levels = {
                { 1, new String[] { "INT", "ATK", "ALC" }, new int[] { 2, 2, 1 } },
                { 2, new String[] { "ATK", "ALC" }, new int[] { 3, 4 } },
                { 3, new String[] { "VIS" }, new int[] { 1 } },
                { 4, new String[] { "DEF" }, new int[] { 1 } },
                { 5, new String[] { "DEF", "ATK" }, new int[] { 2, 4 } }
        };

        for (Object[] levelData : levels) {
            int level = (int) levelData[0];
            String[] abilities = (String[]) levelData[1];
            int[] values = (int[]) levelData[2];

            HashMap<String, Integer> levelAbilities = new HashMap<>();
            for (int i = 0; i < abilities.length; i++) {
                levelAbilities.put(abilities[i], values[i]);
            }
            goblinMap.put(level, levelAbilities);
        }

        return goblinMap;
    }

}
