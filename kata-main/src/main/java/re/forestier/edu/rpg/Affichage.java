package re.forestier.edu.rpg;

public class Affichage {

    public static String afficherJoueur(Player player) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Joueur ").append(player.avatarName).append(" joué par ").append(player.playerName).append("\n");
        sb.append("Niveau : ").append(player.retrieveLevel()).append(" (XP : ").append(player.getXp()).append(")\n\n");

        sb.append("Capacités :\n");
        if (player.abilities != null) {
            player.abilities.forEach((name, level) -> {
                sb.append(name).append(" : ").append(level).append("\n");
            });
        }

        sb.append("\nInventaire :\n");
        //  L'inventaire contient maintenant des objets Item
        if (player.inventory != null) {
            for (Item item : player.inventory) {
                // On utilise la méthode toString() de l'Item automatiquement
                sb.append(item.toString()).append("\n");
            }
        }

        return sb.toString();
    }

    public static String afficherJoueurMarkdown(Player player) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("# Joueur ").append(player.avatarName).append("\n");
        
        sb.append("*Joué par ").append(player.playerName).append("*\n\n");
        
        sb.append("## Caractéristiques\n");
        
        sb.append("* **Niveau** : ").append(player.retrieveLevel()).append("\n");
        sb.append("* **XP** : ").append(player.getXp()).append("\n");
        sb.append("* **Argent** : ").append(player.money).append("\n\n");

        sb.append("## Inventaire\n");
        
        // Liste des objets
        if (player.inventory != null) {
            for (Item item : player.inventory) {
                sb.append("* ").append(item.toString()).append("\n");
            }
        }
        
        return sb.toString();
    }
    
}