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

    public static boolean addXp(Player player, int xp) {
        int currentLevel = player.retrieveLevel();
        player.xp += xp;
        int newLevel = player.retrieveLevel();

        if (newLevel != currentLevel) {

            Random random = new Random();
            player.inventory.add(objectList[random.nextInt(objectList.length)]);

            for (int level = currentLevel + 1; level <= newLevel; level++) {
                HashMap<String, Integer> newAbilities = player.getAvatarLevel(level);

                if (newAbilities != null) {
                    newAbilities.forEach((ability, abilityLevel) -> {
                        player.abilities.put(ability, abilityLevel);
                    });
                }
            }

            return true;
        }
        return false;
    }

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
