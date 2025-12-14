package re.forestier.edu;

import re.forestier.edu.rpg.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        Player firstPlayer = new Dwarf("Florian", "Ruzberg de Rivehaute", 200, new ArrayList<>());

        // On affiche le résultat
        System.out.println(Affichage.afficherJoueur(firstPlayer));
        
        System.out.println("\n--- Version Markdown ---\n");
        System.out.println(Affichage.afficherJoueurMarkdown(firstPlayer));
    }
}