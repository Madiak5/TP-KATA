package re.forestier.edu.rpg;

import java.util.HashMap;
import java.util.Random;

public class UpdatePlayer {

    private final static Item[] objectList = {
        new Item("Lookout Ring", "Prevents surprise attacks", 1, 50),
        new Item("Scroll of Stupidity", "INT-2 when applied to an enemy", 1, 10),
        new Item("Draupnir", "Increases XP gained by 100%", 5, 100),
        new Item("Magic Charm", "Magic +10 for 5 rounds", 2, 20),
        new Item("Rune Staff of Curse", "May burn your ennemies... Or yourself.", 10, 200),
        new Item("Combat Edge", "Well, that's an edge", 8, 80),
        new Item("Holy Elixir", "Recover your HP", 2, 25)
    };

    public static HashMap<String, HashMap<Integer, HashMap<String, Integer>>> abilitiesPerTypeAndLevel() {
        HashMap<String, HashMap<Integer, HashMap<String, Integer>>> abilitiesPerTypeAndLevel = new HashMap<>();

        // ADVENTURER 
        HashMap<Integer, HashMap<String, Integer>> adventurerMap = new HashMap<>();
        HashMap<String, Integer> adventurerLevel1 = new HashMap<>();
        adventurerLevel1.put("INT", 1); adventurerLevel1.put("DEF", 1); adventurerLevel1.put("ATK", 3); adventurerLevel1.put("CHA", 2);
        adventurerMap.put(1, adventurerLevel1);
        
        HashMap<String, Integer> adventurerLevel2 = new HashMap<>();
        adventurerLevel2.put("INT", 2); adventurerLevel2.put("CHA", 3);
        adventurerMap.put(2, adventurerLevel2); 

        HashMap<String, Integer> adventurerLevel3 = new HashMap<>();
        adventurerLevel3.put("ATK", 5); adventurerLevel3.put("ALC", 1);
        adventurerMap.put(3, adventurerLevel3);

        HashMap<String, Integer> adventurerLevel4 = new HashMap<>();
        adventurerLevel4.put("DEF", 3);
        adventurerMap.put(4, adventurerLevel4);

        HashMap<String, Integer> adventurerLevel5 = new HashMap<>();
        adventurerLevel5.put("VIS", 1); adventurerLevel5.put("DEF", 4);
        adventurerMap.put(5, adventurerLevel5);
        abilitiesPerTypeAndLevel.put("ADVENTURER", adventurerMap);

        // ARCHER 
        HashMap<Integer, HashMap<String, Integer>> archerMap = new HashMap<>();
        HashMap<String, Integer> archerLevel1 = new HashMap<>();
        archerLevel1.put("INT", 1); archerLevel1.put("ATK", 3); archerLevel1.put("CHA", 1); archerLevel1.put("VIS", 3);
        archerMap.put(1, archerLevel1);

        HashMap<String, Integer> archerLevel2 = new HashMap<>();
        archerLevel2.put("DEF", 1); archerLevel2.put("CHA", 2);
        archerMap.put(2, archerLevel2);

        HashMap<String, Integer> archerLevel3 = new HashMap<>();
        archerLevel3.put("ATK", 3);
        archerMap.put(3, archerLevel3);

        HashMap<String, Integer> archerLevel4 = new HashMap<>();
        archerLevel4.put("DEF", 2);
        archerMap.put(4, archerLevel4);

        HashMap<String, Integer> archerLevel5 = new HashMap<>();
        archerLevel5.put("ATK", 4);
        archerMap.put(5, archerLevel5);
        abilitiesPerTypeAndLevel.put("ARCHER", archerMap);

        // DWARF
        HashMap<Integer, HashMap<String, Integer>> dwarf = new HashMap<>();
        HashMap<String, Integer> dwarfLevel1 = new HashMap<>();
        dwarfLevel1.put("ALC", 4); dwarfLevel1.put("INT", 1); dwarfLevel1.put("ATK", 3);
        dwarf.put(1, dwarfLevel1);

        HashMap<String, Integer> dwarfLevel2 = new HashMap<>();
        dwarfLevel2.put("DEF", 1); dwarfLevel2.put("ALC", 5);
        dwarf.put(2, dwarfLevel2);

        HashMap<String, Integer> dwarfLevel3 = new HashMap<>();
        dwarfLevel3.put("ATK", 4);
        dwarf.put(3, dwarfLevel3);

        HashMap<String, Integer> dwarfLevel4 = new HashMap<>();
        dwarfLevel4.put("DEF", 2);
        dwarf.put(4, dwarfLevel4);

        HashMap<String, Integer> dwarfLevel5 = new HashMap<>();
        dwarfLevel5.put("CHA", 1);
        dwarf.put(5, dwarfLevel5);
        abilitiesPerTypeAndLevel.put("DWARF", dwarf);

        // GOBLIN 
        HashMap<Integer, HashMap<String, Integer>> goblinMap = new HashMap<>();
        HashMap<String, Integer> goblinLevel1 = new HashMap<>();
        goblinLevel1.put("INT", 2); goblinLevel1.put("ATK", 2); goblinLevel1.put("ALC", 1);
        goblinMap.put(1, goblinLevel1);

        HashMap<String, Integer> goblinLevel2 = new HashMap<>();
        goblinLevel2.put("ATK", 3); goblinLevel2.put("ALC", 4);
        goblinMap.put(2, goblinLevel2);

        HashMap<String, Integer> goblinLevel3 = new HashMap<>();
        goblinLevel3.put("VIS", 1);
        goblinMap.put(3, goblinLevel3);

        HashMap<String, Integer> goblinLevel4 = new HashMap<>();
        goblinLevel4.put("DEF", 1);
        goblinMap.put(4, goblinLevel4);

        HashMap<String, Integer> goblinLevel5 = new HashMap<>();
        goblinLevel5.put("DEF", 2); goblinLevel5.put("ATK", 4);
        goblinMap.put(5, goblinLevel5);
        abilitiesPerTypeAndLevel.put("GOBLIN", goblinMap);

        return abilitiesPerTypeAndLevel;
    }

    public static boolean addXp(Player player, int xp) {
        int currentLevel = player.retrieveLevel();
        player.xp += xp;
        int newLevel = player.retrieveLevel();

        if (newLevel != currentLevel) {
            Random random = new Random();
            int randomIndex = random.nextInt(objectList.length);
            player.pickUp(objectList[randomIndex]);

            HashMap<String, Integer> abilities = abilitiesPerTypeAndLevel().get(player.getAvatarClass()).get(newLevel);
            if (abilities != null) {
                abilities.forEach((ability, level) -> {
                    player.abilities.put(ability, abilities.get(ability));
                });
            }
            return true;
        }
        return false;
    }
    public static void majFinDeTour(Player player) {
        if(player.currenthealthpoints <= 0) {
            System.out.println("Le joueur est KO !");
            return;
        }

        if(player.currenthealthpoints >= player.healthpoints) {
            player.currenthealthpoints = player.healthpoints;
            return;
        }
        
        if(player.currenthealthpoints >= player.healthpoints / 2) {
            return;
        }

        // C'est au joueur de se soigner lui meme 
        
        player.majFinDeTour();

        // Bornage final
        if(player.currenthealthpoints > player.healthpoints) {
            player.currenthealthpoints = player.healthpoints;
        }
    }
}