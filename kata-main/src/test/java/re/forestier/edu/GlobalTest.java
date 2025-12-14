package re.forestier.edu;

import org.junit.jupiter.api.Test;
import re.forestier.edu.rpg.Adventurer;
import re.forestier.edu.rpg.Affichage;
import re.forestier.edu.rpg.Item;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class GlobalTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testMainClass() {
        // On lance la méthode main
        Main.main(new String[]{});
        String output = outContent.toString();
        if (!output.contains("Joueur Ruzberg de Rivehaute")) {
             fail("Le Main n'a pas affiché le joueur attendu");
        }
    }

    @Test
    void testToString() throws IOException {
        ArrayList<Item> inventory = new ArrayList<>();
        inventory.add(new Item("Excalibur", "Epée légendaire", 10, 500));
        Adventurer p = new Adventurer("Arthur", "RoiArthur", 200, inventory);

        String resultatActuel = Affichage.afficherJoueur(p);
        verifierContenuFichier("GlobalTest.testToString", resultatActuel);
    }

    private void verifierContenuFichier(String nomTest, String contenuActuel) throws IOException {
        String contenuNormalise = contenuActuel.replace("\r\n", "\n").trim();
        
        String nomFichierApprouve = nomTest + ".approved.txt";
        String nomFichierRecu = nomTest + ".received.txt";
        
        Path cheminApprouve = Paths.get("src/test/java/re/forestier/edu/" + nomFichierApprouve);
        Path cheminRecu = Paths.get("src/test/java/re/forestier/edu/" + nomFichierRecu);

        // Si le fichier approuvé n'existe pas
        if (!Files.exists(cheminApprouve)) {
            Files.writeString(cheminRecu, contenuNormalise);
            fail("Le fichier approuvé (" + nomFichierApprouve + ") n'existe pas.\n" +
                 "Un fichier reçu a été généré : " + cheminRecu.toAbsolutePath() + "\n" +
                 "Vérifiez son contenu et renommez-le en .approved.txt pour valider.");
            return;
        }

        // Si le fichier existe, on compare
        String contenuAttendu = Files.readString(cheminApprouve).replace("\r\n", "\n").trim();

        if (!contenuNormalise.equals(contenuAttendu)) {
            Files.writeString(cheminRecu, contenuNormalise);
            // On utilise assertEquals pour voir la différence dans le terminal
            assertEquals(contenuAttendu, contenuNormalise, 
                "Le contenu ne correspond pas ! Voir le fichier généré : " + cheminRecu.getFileName());
        } else {
            // Si c'est bon, on supprime le fichier .received s'il traînait
            Files.deleteIfExists(cheminRecu);
        }
    }
}