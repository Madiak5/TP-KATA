package re.forestier.edu.rpg;

import java.util.HashMap;
import java.util.Random;

public class UpdatePlayer {

    private final static String[] objectList = { "Lookout Ring : Prevents surprise attacks",
            "Scroll of Stupidity : INT-2 when applied to an enemy", "Draupnir : Increases XP gained by 100%",
            "Magic Charm : Magic +10 for 5 rounds",
            "Rune Staff of Curse : May burn your ennemies... Or yourself. Who knows?",
            "Combat Edge : Well, that's an edge", "Holy Elixir : Recover your HP"
    };

    public static HashMap<String, HashMap<Integer, HashMap<String, Integer>>> abilitiesPerTypeAndLevel() {
        HashMap<String, HashMap<Integer, HashMap<String, Integer>>> abilitiesPerTypeAndLevel = new HashMap<>();

        HashMap<Integer, HashMap<String, Integer>> adventurerMap = new HashMap<>();
        HashMap<String, Integer> adventurerLevel1 = new HashMap<>();
        adventurerLevel1.put("INT", 1);
        adventurerLevel1.put("DEF", 1);
        adventurerLevel1.put("ATK", 3);
        adventurerLevel1.put("CHA", 2);
        adventurerMap.put(1, adventurerLevel1);

        HashMap<String, Integer> adventurerLevel2 = new HashMap<>();
        adventurerLevel1.put("INT", 2);
        adventurerLevel1.put("CHA", 3);
        adventurerMap.put(2, adventurerLevel2);

        HashMap<String, Integer> adventurerLevel3 = new HashMap<>();
        adventurerLevel3.put("ATK", 5);
        adventurerLevel3.put("ALC", 1);
        adventurerMap.put(3, adventurerLevel3);

        HashMap<String, Integer> adventurerLevel4 = new HashMap<>();
        adventurerLevel4.put("DEF", 3);
        adventurerMap.put(4, adventurerLevel4);

        HashMap<String, Integer> adventurerLevel5 = new HashMap<>();
        adventurerLevel5.put("VIS", 1);
        adventurerLevel5.put("DEF", 4);
        adventurerMap.put(5, adventurerLevel5);

        abilitiesPerTypeAndLevel.put("ADVENTURER", adventurerMap);

        HashMap<Integer, HashMap<String, Integer>> archerMap = new HashMap<>();
        HashMap<String, Integer> archerLevel1 = new HashMap<>();
        archerLevel1.put("INT", 1);
        archerLevel1.put("ATK", 3);
        archerLevel1.put("CHA", 1);
        archerLevel1.put("VIS", 3);
        archerMap.put(1, archerLevel1);

        HashMap<String, Integer> archerLevel2 = new HashMap<>();
        archerLevel2.put("DEF", 1);
        archerLevel2.put("CHA", 2);
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

        HashMap<Integer, HashMap<String, Integer>> dwarf = new HashMap<>();
        HashMap<String, Integer> dwarfLevel1 = new HashMap<>();
        dwarfLevel1.put("ALC", 4);
        dwarfLevel1.put("INT", 1);
        dwarfLevel1.put("ATK", 3);
        dwarf.put(1, dwarfLevel1);

        HashMap<String, Integer> dwarfLevel2 = new HashMap<>();
        dwarfLevel2.put("DEF", 1);
        dwarfLevel2.put("ALC", 5);
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

        HashMap<Integer, HashMap<String, Integer>> goblinMap = new HashMap<>();

        HashMap<String, Integer> goblinLevel1 = new HashMap<>();
        goblinLevel1.put("INT", 2);
        goblinLevel1.put("ATK", 2);
        goblinLevel1.put("ALC", 1);
        goblinMap.put(1, goblinLevel1);

        HashMap<String, Integer> goblinLevel2 = new HashMap<>();
        goblinLevel2.put("ATK", 3);
        goblinLevel2.put("ALC", 4);
        goblinMap.put(2, goblinLevel2);

        HashMap<String, Integer> goblinLevel3 = new HashMap<>();
        goblinLevel3.put("VIS", 1);
        goblinMap.put(3, goblinLevel3);

        HashMap<String, Integer> goblinLevel4 = new HashMap<>();
        goblinLevel4.put("DEF", 1);
        goblinMap.put(4, goblinLevel4);

        HashMap<String, Integer> goblinLevel5 = new HashMap<>();
        goblinLevel5.put("DEF", 2);
        goblinLevel5.put("ATK", 4);
        goblinMap.put(5, goblinLevel5);

        abilitiesPerTypeAndLevel.put("GOBLIN", goblinMap);

        return abilitiesPerTypeAndLevel;
    }

    public static boolean addXp(Player Player, int xp) {
        int currentLevel = Player.retrieveLevel();
        Player.xp += xp;
        int newLevel = Player.retrieveLevel();

        if (newLevel != currentLevel) {
            // Player leveled-up!
            // Give a random object
            ;
            Random random = new Random();
            Player.inventory.add(objectList[random.nextInt(objectList.length - 0) + 0]);

            // Add/upgrade abilities to Player
            HashMap<String, Integer> abilities = abilitiesPerTypeAndLevel().get(Player.getAvatarClass()).get(newLevel);
            abilities.forEach((ability, level) -> {
                Player.abilities.put(ability, abilities.get(ability));
            });
            return true;
        }
        return false;
    }

    // majFinDeTour met à jour les points de vie
    public static void majFinDeTour(Player player) {
        // Si le joueur est KO, on ne fait rien
        if (player.currenthealthpoints <= 0) {
            System.out.println("Le joueur est KO !");
            return;
        }

        // Gestion de la borne max (si déjà au max)
        if (player.currenthealthpoints >= player.healthpoints) {
            player.currenthealthpoints = player.healthpoints;
            return;
        }

        // Si le joueur a plus de 50% de vie, pas de soin (règle actuelle du jeu)
        if (player.currenthealthpoints >= player.healthpoints / 2) {
            return;
        }

        // Application des soins selon la classe
        switch (player.getAvatarClass()) {
            case "DWARF":
                player.currenthealthpoints += 1;
                if (player.inventory.contains("Holy Elixir")) {
                    player.currenthealthpoints += 1;
                }
                break;

            case "ARCHER":
                player.currenthealthpoints += 1;
                if (player.inventory.contains("Magic Bow")) {
                    // Attention : calcul basé sur les PVs courants (déjà augmentés de 1 juste
                    // avant)
                    player.currenthealthpoints += (player.currenthealthpoints / 8) - 1;
                }
                break;

            case "ADVENTURER":
                player.currenthealthpoints += 2;
                if (player.retrieveLevel() < 3) {
                    player.currenthealthpoints -= 1;
                }
                break;

            default:
                // Si c'est une autre classe qu'on ne connait pas, pas de soin spécifique défini
                break;
        }

        // Vérification finale pour ne pas dépasser la vie max
        if (player.currenthealthpoints > player.healthpoints) {
            player.currenthealthpoints = player.healthpoints;
        }
    }
}