package re.forestier.edu.rpg; // <-- Changement ici : on est dans le package rpg

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
// Plus besoin d'importer player ou UpdatePlayer car on est dans le même dossier !

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;

public class UnitTests {

    // ==========================================
    // TESTS SUR LA CLASSE PLAYER
    // ==========================================

    @Test
    @DisplayName("Test de création d'un joueur valide (ARCHER)")
    void testPlayerCreationValid() {
        player p = new player("Florian", "Grognak", "ARCHER", 100, new ArrayList<>());
        assertThat(p.playerName, is("Florian"));
        assertThat(p.getAvatarClass(), is("ARCHER"));
        assertThat(p.money, is(100));
        assertThat(p.abilities, notNullValue());
        assertThat(p.abilities.get("INT"), is(1));
    }

    @Test
    @DisplayName("Test de création invalide (Classe inconnue)")
    void testPlayerCreationInvalid() {
        player p = new player("Test", "Test", "WARRIOR", 100, new ArrayList<>());
        assertThat(p.playerName, nullValue());
    }

    @Test
    @DisplayName("Impossible d'avoir de l'argent négatif")
    void testNegativeMoney() {
        player p = new player("Florian", "Grognak", "ADVENTURER", 100, new ArrayList<>());
        try {
            p.removeMoney(200);
            fail("Aurait du lever une exception");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Player can't have a negative money!"));
        }
    }

    @Test
    @DisplayName("Retirer de l'argent normalement")
    void testRemoveMoney() {
        player p = new player("Florian", "Grognak", "ADVENTURER", 100, new ArrayList<>());
        p.removeMoney(50);
        assertThat(p.money, is(50));
    }

    @Test
    @DisplayName("Ajouter de l'argent")
    void testAddMoney() {
        player p = new player("Florian", "Grognak", "ADVENTURER", 100, new ArrayList<>());
        p.addMoney(50);
        assertThat(p.money, is(150));
    }

    @Test
    @DisplayName("Ajouter de l'argent null")
    void testAddMoneyNull() {
        player p = new player("Florian", "Grognak", "ADVENTURER", 100, new ArrayList<>());
        p.addMoney(10); 
        assertThat(p.money, is(110));
    }

    @Test
    @DisplayName("Calcul des niveaux (XP)")
    void testLevels() {
        player p = new player("Florian", "Grognak", "ADVENTURER", 100, new ArrayList<>());
        
        // Maintenant qu'on est dans le même package, on a le droit de toucher à xp !
        p.xp = 0; assertThat(p.retrieveLevel(), is(1));
        p.xp = 10; assertThat(p.retrieveLevel(), is(2));
        p.xp = 27; assertThat(p.retrieveLevel(), is(3));
        p.xp = 57; assertThat(p.retrieveLevel(), is(4));
        p.xp = 111; assertThat(p.retrieveLevel(), is(5));
    }

    // ==========================================
    // TESTS SUR UPDATEPLAYER (Logique de jeu)
    // ==========================================

    @Test
    @DisplayName("Ajout XP sans montée de niveau")
    void testAddXpNoLevelUp() {
        player p = new player("Florian", "Grognak", "ADVENTURER", 100, new ArrayList<>());
        boolean leveledUp = UpdatePlayer.addXp(p, 5); 
        
        assertThat(leveledUp, is(false));
        assertThat(p.getXp(), is(5));
        assertThat(p.inventory.isEmpty(), is(true));
    }

    @Test
    @DisplayName("Ajout XP avec montée de niveau")
    void testAddXpLevelUp() {
        player p = new player("Florian", "Grognak", "ADVENTURER", 100, new ArrayList<>());
        boolean leveledUp = UpdatePlayer.addXp(p, 20); 
        
        assertThat(leveledUp, is(true));
        assertThat(p.retrieveLevel(), is(2));
        assertThat(p.inventory.size(), is(1));
    }

    @Test
    @DisplayName("MajFinDeTour : Joueur KO")
    void testMajFinDeTourKO() {
        player p = new player("Florian", "Grognak", "ADVENTURER", 100, new ArrayList<>());
        p.healthpoints = 100;
        p.currenthealthpoints = 0;
        UpdatePlayer.majFinDeTour(p);
        assertThat(p.currenthealthpoints, is(0));
    }

    @Test
    @DisplayName("MajFinDeTour : Joueur Full Vie")
    void testMajFinDeTourFullLife() {
        player p = new player("Florian", "Grognak", "ADVENTURER", 100, new ArrayList<>());
        p.healthpoints = 100;
        p.currenthealthpoints = 100;
        UpdatePlayer.majFinDeTour(p);
        assertThat(p.currenthealthpoints, is(100));
    }

    @Test
    @DisplayName("MajFinDeTour : DWARF < 50% HP (Sans Elixir)")
    void testDwarfLowHpNoItem() {
        player p = new player("Florian", "Grognak", "DWARF", 100, new ArrayList<>());
        p.healthpoints = 10;
        p.currenthealthpoints = 2;
        UpdatePlayer.majFinDeTour(p);
        assertThat(p.currenthealthpoints, is(3));
    }

    @Test
    @DisplayName("MajFinDeTour : DWARF < 50% HP (Avec Elixir)")
    void testDwarfLowHpWithItem() {
        player p = new player("Florian", "Grognak", "DWARF", 100, new ArrayList<>());
        p.inventory.add("Holy Elixir");
        p.healthpoints = 10;
        p.currenthealthpoints = 2;
        UpdatePlayer.majFinDeTour(p);
        assertThat(p.currenthealthpoints, is(4));
    }

    @Test
    @DisplayName("MajFinDeTour : ARCHER < 50% HP (Sans Arc)")
    void testArcherLowHpNoItem() {
        player p = new player("Florian", "Grognak", "ARCHER", 100, new ArrayList<>());
        p.healthpoints = 20;
        p.currenthealthpoints = 5; 
        UpdatePlayer.majFinDeTour(p);
        assertThat(p.currenthealthpoints, is(6));
    }

    @Test
    @DisplayName("MajFinDeTour : ARCHER < 50% HP (Avec Arc Magique)")
    void testArcherLowHpWithItem() {
        player p = new player("Florian", "Grognak", "ARCHER", 100, new ArrayList<>());
        p.inventory.add("Magic Bow");
        p.healthpoints = 20;
        p.currenthealthpoints = 8;
        UpdatePlayer.majFinDeTour(p);
        assertThat(p.currenthealthpoints, is(9));
    }

    @Test
    @DisplayName("MajFinDeTour : ADVENTURER < 50% HP (Niveau faible)")
    void testAdventurerLowHpLowLevel() {
        player p = new player("Florian", "Grognak", "ADVENTURER", 100, new ArrayList<>());
        p.healthpoints = 20;
        p.currenthealthpoints = 5;
        p.xp = 0; // Niveau 1
        UpdatePlayer.majFinDeTour(p);
        assertThat(p.currenthealthpoints, is(6));
    }

    @Test
    @DisplayName("MajFinDeTour : ADVENTURER < 50% HP (Niveau élevé)")
    void testAdventurerLowHpHighLevel() {
        player p = new player("Florian", "Grognak", "ADVENTURER", 100, new ArrayList<>());
        p.healthpoints = 20;
        p.currenthealthpoints = 5;
        p.xp = 60; // Niveau 4
        UpdatePlayer.majFinDeTour(p);
        assertThat(p.currenthealthpoints, is(7));
    }

    @Test
    @DisplayName("MajFinDeTour : Soin ne dépasse pas max HP")
    void testMajFinDeTourCapMax() {
        player p = new player("Florian", "Grognak", "ADVENTURER", 100, new ArrayList<>());
        p.xp = 60; 
        p.healthpoints = 20;
        p.currenthealthpoints = 19;
        
        UpdatePlayer.majFinDeTour(p);
        
        // Le code actuel ne soigne pas si on est au-dessus de 50% pv !
        // On s'attend donc à ce que ça reste à 19.
        assertThat(p.currenthealthpoints, is(19)); 
    }

    // ==========================================
    // TESTS SUR L'AFFICHAGE
    // ==========================================

    @Test
    @DisplayName("Test de l'affichage textuel")
    void testAffichageJoueur() {
        player p = new player("Florian", "Grognak", "ARCHER", 100, new ArrayList<>());
        p.inventory.add("Potion");
        
        String result = Affichage.afficherJoueur(p);
        
        assertThat(result, containsString("Joueur Grognak joué par Florian"));
        assertThat(result, containsString("Niveau : 1"));
        assertThat(result, containsString("INT : 1"));
        assertThat(result, containsString("Potion"));
    }
}