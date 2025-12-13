package re.forestier.edu.rpg;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import re.forestier.edu.rpg.Adventurer;
import re.forestier.edu.rpg.Affichage;
import re.forestier.edu.rpg.Archer;
import re.forestier.edu.rpg.Dwarf;
import re.forestier.edu.rpg.Goblin;
import re.forestier.edu.rpg.Player;
import re.forestier.edu.rpg.UpdatePlayer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.ArrayList;

public class UnitTests {

    // ==========================================
    // TESTS SUR LES SOUS-CLASSES DE PLAYER
    // ==========================================

    @Test
    @DisplayName("Test de création d'un joueur valide (ARCHER)")
    void testPlayerCreationValid() {
        Archer p = new Archer("Florian", "Grognak", 100, new ArrayList<>());
        assertThat(p.playerName, is("Florian"));
        assertThat(p.getAvatarClass(), is("ARCHER"));
        assertThat(p.money, is(100));
        assertThat(p.abilities, notNullValue());
        assertThat(p.abilities.get("INT"), is(1));
    }

    @Test
    @DisplayName("Impossible d'avoir de l'argent négatif")
    void testNegativeMoney() {
        Adventurer p = new Adventurer("Florian", "Grognak", 100, new ArrayList<>());
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
        Adventurer p = new Adventurer("Florian", "Grognak", 100, new ArrayList<>());
        p.removeMoney(50);
        assertThat(p.money, is(50));
    }

    @Test
    @DisplayName("Ajouter de l'argent")
    void testAddMoney() {
        Adventurer p = new Adventurer("Florian", "Grognak", 100, new ArrayList<>());
        p.addMoney(50);
        assertThat(p.money, is(150));
    }

    @Test
    @DisplayName("Ajouter de l'argent null (Test non pertinent après correction de Player.addMoney)")
    void testAddMoneyNull() {
        // 💡 Ce test est désormais redondant ou obsolète après la simplification de
        // addMoney.
        Adventurer p = new Adventurer("Florian", "Grognak", 100, new ArrayList<>());
        p.addMoney(10);
        assertThat(p.money, is(110));
    }

    @Test
    @DisplayName("Calcul des niveaux (XP)")
    void testLevels() {
        Adventurer p = new Adventurer("Florian", "Grognak", 100, new ArrayList<>());
        p.setXp(0);
        assertThat(p.retrieveLevel(), is(1));
        p.setXp(10);
        assertThat(p.retrieveLevel(), is(2));
        p.setXp(27);
        assertThat(p.retrieveLevel(), is(3));
        p.setXp(57);
        assertThat(p.retrieveLevel(), is(4));
        p.setXp(111);
        assertThat(p.retrieveLevel(), is(5));
    }

    // ==========================================
    // TESTS SUR UPDATEPlayer (Logique de jeu)
    // ==========================================

    @Test
    @DisplayName("Ajout XP sans montée de niveau")
    void testAddXpNoLevelUp() {
        Adventurer p = new Adventurer("Florian", "Grognak", 100, new ArrayList<>());
        boolean leveledUp = UpdatePlayer.addXp(p, 5);

        assertThat(leveledUp, is(false));
        assertThat(p.getXp(), is(5));
        assertThat(p.inventory.isEmpty(), is(true));
    }

    @Test
    @DisplayName("Ajout XP avec montée de niveau")
    void testAddXpLevelUp() {
        Adventurer p = new Adventurer("Florian", "Grognak", 100, new ArrayList<>());
        boolean leveledUp = UpdatePlayer.addXp(p, 20);

        assertThat(leveledUp, is(true));
        assertThat(p.retrieveLevel(), is(2));
        assertThat(p.inventory.size(), is(1));

        assertThat(p.abilities.get("INT"), is(2));
        assertThat(p.abilities.get("CHA"), is(3));
    }

    @Test
    @DisplayName("MajFinDeTour : Joueur KO")
    void testMajFinDeTourKO() {
        Adventurer p = new Adventurer("Florian", "Grognak", 100, new ArrayList<>());
        p.healthpoints = 100;
        p.currenthealthpoints = 0;
        UpdatePlayer.majFinDeTour(p);
        assertThat(p.currenthealthpoints, is(0));
    }

    @Test
    @DisplayName("MajFinDeTour : Joueur Full Vie")
    void testMajFinDeTourFullLife() {
        Adventurer p = new Adventurer("Florian", "Grognak", 100, new ArrayList<>());
        p.healthpoints = 100;
        p.currenthealthpoints = 100;
        UpdatePlayer.majFinDeTour(p);
        assertThat(p.currenthealthpoints, is(100));
    }

    @Test
    @DisplayName("MajFinDeTour : DWARF < 50% HP (Sans Elixir)")
    void testDwarfLowHpNoItem() {
        Dwarf p = new Dwarf("Florian", "Grognak", 100, new ArrayList<>());
        p.healthpoints = 10;
        p.currenthealthpoints = 2;
        UpdatePlayer.majFinDeTour(p);
        assertThat(p.currenthealthpoints, is(3));
    }

    @Test
    @DisplayName("MajFinDeTour : DWARF < 50% HP (Avec Elixir)")
    void testDwarfLowHpWithItem() {
        Dwarf p = new Dwarf("Florian", "Grognak", 100, new ArrayList<>());
        p.inventory.add("Holy Elixir");
        p.healthpoints = 10;
        p.currenthealthpoints = 2;
        UpdatePlayer.majFinDeTour(p);
        assertThat(p.currenthealthpoints, is(4)); // 2 + 1 (base) + 1 (Elixir)
    }

    @Test
    @DisplayName("MajFinDeTour : ARCHER < 50% HP (Sans Arc)")
    void testArcherLowHpNoItem() {
        Archer p = new Archer("Florian", "Grognak", 100, new ArrayList<>());
        p.healthpoints = 20;
        p.currenthealthpoints = 5;
        UpdatePlayer.majFinDeTour(p);
        assertThat(p.currenthealthpoints, is(6)); // 5 + 1 (soin de base Archer)
    }

    @Test
    @DisplayName("MajFinDeTour : ARCHER < 50% HP (Avec Arc Magique)")
    void testArcherLowHpWithItem() {
        Archer p = new Archer("Florian", "Grognak", 100, new ArrayList<>());
        p.healthpoints = 20;
        p.currenthealthpoints = 8;
        UpdatePlayer.majFinDeTour(p);
        assertThat(p.currenthealthpoints, is(9));
    }

    @Test
    @DisplayName("MajFinDeTour : ADVENTURER < 50% HP (Niveau faible)")
    void testAdventurerLowHpLowLevel() {
        Adventurer p = new Adventurer("Florian", "Grognak", 100, new ArrayList<>());
        p.healthpoints = 20;
        p.currenthealthpoints = 5;
        p.setXp(0);
        UpdatePlayer.majFinDeTour(p);
        assertThat(p.currenthealthpoints, is(6));
    }

    @Test
    @DisplayName("MajFinDeTour : ADVENTURER < 50% HP (Niveau élevé)")
    void testAdventurerLowHpHighLevel() {
        Adventurer p = new Adventurer("Florian", "Grognak", 100, new ArrayList<>());
        p.healthpoints = 20;
        p.currenthealthpoints = 5;
        p.setXp(60);
        UpdatePlayer.majFinDeTour(p);
        assertThat(p.currenthealthpoints, is(7)); // 5 + 2 = 7
    }

    @Test
    @DisplayName("MajFinDeTour : Soin ne dépasse pas max HP")
    void testMajFinDeTourCapMax() {
        Adventurer p = new Adventurer("Florian", "Grognak", 100, new ArrayList<>());
        p.setXp(60);
        p.healthpoints = 20;
        p.currenthealthpoints = 19; // > 50% (10), le soin ne s'applique pas

        UpdatePlayer.majFinDeTour(p);

        assertThat(p.currenthealthpoints, is(19));
    }

    @Test
    @DisplayName("Création et évolution d'un Gobelin")
    void testGoblin() {
        Goblin p = new Goblin("Keke", "Keke le Gobelin", 100, new ArrayList<>());
        assertThat(p.getAvatarClass(), is("GOBLIN"));
        assertThat(p.abilities.get("INT"), is(2));
        assertThat(p.abilities.get("ATK"), is(2));
        assertThat(p.abilities.get("ALC"), is(1));
        UpdatePlayer.addXp(p, 20);
        assertThat(p.retrieveLevel(), is(2));
        assertThat(p.abilities.get("ATK"), is(3));
        assertThat(p.abilities.get("ALC"), is(4));
    }

    // ==========================================
    // TESTS SUR L'AFFICHAGE
    // ==========================================

    @Test
    @DisplayName("Test de l'affichage textuel")
    void testAffichageJoueur() {
        Archer p = new Archer("Florian", "Grognak", 100, new ArrayList<>());
        p.inventory.add("Potion");

        String result = Affichage.afficherJoueur(p);

        assertThat(result, containsString("Joueur Grognak joué par Florian"));
        assertThat(result, containsString("Niveau : 1"));
        assertThat(result, containsString("INT : 1"));
        assertThat(result, containsString("Potion"));
    }

}