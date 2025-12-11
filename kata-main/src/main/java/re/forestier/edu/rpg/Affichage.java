package re.forestier.edu.rpg;

public class Affichage {

    public static String afficherJoueur(Player Player) {
        final String[] finalString = { "Joueur " + Player.Avatar_name + " joué par " + Player.PlayerName };
        finalString[0] += "\nNiveau : " + Player.retrieveLevel() + " (XP totale : " + Player.xp + ")";
        finalString[0] += "\n\nCapacités :";
        Player.abilities.forEach((name, level) -> {
            finalString[0] += "\n   " + name + " : " + level;
        });
        finalString[0] += "\n\nInventaire :";
        Player.inventory.forEach(item -> {
            finalString[0] += "\n   " + item;
        });

        return finalString[0];
    }
}
