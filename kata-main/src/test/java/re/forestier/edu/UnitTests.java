package re.forestier.edu.rpg;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;

public class UnitTests {

    @Test
    @DisplayName("Test de création d'un joueur valide")
    void testPlayerCreationValid() {
        Player p = new Archer("Florian", "Grognak", 100, new ArrayList<>());
        
        assertThat(p.playerName, is("Florian"));
        assertThat(p.getAvatarClass(), is("ARCHER")); // Le constructeur Archer a bien mis "ARCHER"
        assertThat(p.money, is(100));
        assertThat(p.abilities, notNullValue());
        assertThat(p.abilities.get("INT"), is(1));
    }

    @Test
    @DisplayName("Impossible d'avoir de l'argent négatif")
    void testNegativeMoney() {
        Player p = new Adventurer("Florian", "Grognak", 100, new ArrayList<>());
        try {
            p.removeMoney(200);
            fail("Aurait du lever une exception");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Player can't have a negative money!"));
        }
    }

    @Test
    @DisplayName("Retirer de l'argent")
    void testRemoveMoney() {
        Player p = new Adventurer("Florian", "Grognak", 100, new ArrayList<>());
        p.removeMoney(50);
        assertThat(p.money, is(50));
    }

    @Test
    @DisplayName("Ajouter de l'argent")
    void testAddMoney() {
        Player p = new Adventurer("Florian", "Grognak", 100, new ArrayList<>());
        p.addMoney(50);
        assertThat(p.money, is(150));
    }

    @Test
    @DisplayName("Calcul des niveaux (XP)")
    void testLevels() {
        Player p = new Adventurer("Florian", "Grognak", 100, new ArrayList<>());
        p.xp = 0; assertThat(p.retrieveLevel(), is(1));
        p.xp = 10; assertThat(p.retrieveLevel(), is(2));
        p.xp = 27; assertThat(p.retrieveLevel(), is(3));
        p.xp = 57; assertThat(p.retrieveLevel(), is(4));
        p.xp = 111; assertThat(p.retrieveLevel(), is(5));
    }

    @Test
    @DisplayName("UpdatePlayer : Ajout XP et montée de niveau")
    void testAddXpLevelUp() {
        Player p = new Adventurer("Florian", "Grognak", 100, new ArrayList<>());
        boolean leveledUp = UpdatePlayer.addXp(p, 20); 
        assertThat(leveledUp, is(true));
        assertThat(p.retrieveLevel(), is(2));
        assertThat(p.inventory.size(), is(1)); 
        assertThat(p.inventory.get(0), instanceOf(Item.class));
    }

    @Test
    @DisplayName("MajFinDeTour : Soins de base DWARF")
    void testMajFinDeTourDwarf() {
        Player p = new Dwarf("Florian", "Grognak", 100, new ArrayList<>());
        p.healthpoints = 100;
        p.currenthealthpoints = 10;
        p.inventory.add(new Item("Holy Elixir", "Heals", 1, 10));
        
        UpdatePlayer.majFinDeTour(p);
        // La méthode majFinDeTour() appelée est celle de Dwarf.java !
        assertThat(p.currenthealthpoints, is(12));
    }

    @Test
    @DisplayName("MajFinDeTour : Soins de base ARCHER")
    void testMajFinDeTourArcher() {
        Player p = new Archer("Florian", "Grognak", 100, new ArrayList<>());
        p.healthpoints = 100;
        p.currenthealthpoints = 20;
        p.inventory.add(new Item("Magic Bow", "Cool bow", 2, 50));

        UpdatePlayer.majFinDeTour(p);
        assertThat(p.currenthealthpoints, is(22)); 
    }

    @Test
    @DisplayName("MajFinDeTour : ADVENTURER")
    void testMajFinDeTourAdventurer() {
        Player p = new Adventurer("Florian", "Grognak", 100, new ArrayList<>());
        p.healthpoints = 100;
        p.currenthealthpoints = 10;
        UpdatePlayer.majFinDeTour(p);
        assertThat(p.currenthealthpoints, is(11));
    }
    
    @Test
    @DisplayName("Création et évolution d'un Gobelin")
    void testGoblin() {
        Player p = new Goblin("Keke", "Keke le Gobelin", 100, new ArrayList<>());
        assertThat(p.getAvatarClass(), is("GOBLIN"));
        assertThat(p.abilities.get("INT"), is(2));
        UpdatePlayer.addXp(p, 20); 
        assertThat(p.retrieveLevel(), is(2));
        assertThat(p.abilities.get("ATK"), is(3));
    }

    @Test
    @DisplayName("Gestion Inventaire : Poids max et Vente")
    void testInventoryAdvanced() {
        Player p = new Adventurer("Marchand", "GrosSac", 100, new ArrayList<>());
        Item potion = new Item("Potion", "Soin", 2, 10);
        Item heavyRock = new Item("Rocher", "Trop lourd", 1000, 0); 

        boolean success = p.pickUp(potion);
        assertThat(success, is(true));
        boolean fail = p.pickUp(heavyRock);
        assertThat(fail, is(false));

        int moneyBefore = p.money;
        p.sell(potion);
        assertThat(p.money, is(moneyBefore + 10));
    }

    @Test
    @DisplayName("Affichage format Markdown")
    void testAffichageMarkdown() {
        Player p = new Archer("Florian", "Grognak", 100, new ArrayList<>());
        p.inventory.add(new Item("Epee", "Coupe fort", 5, 50));
        
        String resultat = Affichage.afficherJoueurMarkdown(p);
        
        assertThat(resultat, containsString("# Joueur Grognak"));
        assertThat(resultat, containsString("*Joué par Florian*"));
        assertThat(resultat, containsString("## Caractéristiques"));
        assertThat(resultat, containsString("**Niveau** : 1"));
        assertThat(resultat, containsString("* Epee (5kg, 50$)"));
    }
    @Test
    @DisplayName("Branch Coverage : Joueur déjà KO ne se soigne pas")
    void testMajFinDeTourKO() {
        // Cas : PV <= 0
        Player p = new Adventurer("Test", "Test", 100, new ArrayList<>());
        p.currenthealthpoints = 0; 
        
        UpdatePlayer.majFinDeTour(p);
        
        // Il ne doit pas gagner de vie s'il est mort
        assertThat(p.currenthealthpoints, is(0));
    }

    @Test
    @DisplayName("Branch Coverage : Joueur Full Vie ne dépasse pas le max")
    void testMajFinDeTourFullLife() {
        // Cas : PV >= Max
        Player p = new Adventurer("Test", "Test", 100, new ArrayList<>());
        p.currenthealthpoints = 100;
        
        UpdatePlayer.majFinDeTour(p);
        
        assertThat(p.currenthealthpoints, is(100));
    }

    @Test
    @DisplayName("Branch Coverage : Règle des 50% (Pas de soin si > 50% PV)")
    void testMajFinDeTourHalfLifeRule() {
        // Cas : PV > 50 (ex: 60/100) -> Ne doit pas soigner
        Player p = new Adventurer("Test", "Test", 100, new ArrayList<>());
        p.currenthealthpoints = 60;
        
        UpdatePlayer.majFinDeTour(p);
        
        assertThat(p.currenthealthpoints, is(60));
    }

    @Test
    @DisplayName("Branch Coverage : Archer sans arc")
    void testArcherNoBow() {
        Player p = new Archer("Legolas", "Elf", 100, new ArrayList<>());
        p.currenthealthpoints = 20;
        
        UpdatePlayer.majFinDeTour(p);
        assertThat(p.currenthealthpoints, is(21));
    }

    @Test
    @DisplayName("Branch Coverage : Nain sans Elixir")
    void testDwarfNoElixir() {
        Player p = new Dwarf("Gimli", "Dwarf", 100, new ArrayList<>());
        p.currenthealthpoints = 10;
        UpdatePlayer.majFinDeTour(p);
        assertThat(p.currenthealthpoints, is(11));
    }
    
    @Test
    @DisplayName("Branch Coverage : Niveau Max (XP très haute)")
    void testMaxLevel() {
        // TEST Pour tester les derniers "else" de retrieveLevel
        Player p = new Adventurer("Test", "Test", 100, new ArrayList<>());
        p.xp = 200; 
        
        assertThat(p.retrieveLevel(), is(5));
    }
    @Test
    @DisplayName("Branch Coverage : Paliers intermédiaires de niveaux")
    void testLevelIntermediates() {
        Player p = new Adventurer("Test", "Test", 100, new ArrayList<>());
        
        // Test level 2 (entre 10 et 27)
        p.xp = 20; 
        assertThat(p.retrieveLevel(), is(2));
        
        // Test level 3 (entre 27 et 57)
        p.xp = 40;
        assertThat(p.retrieveLevel(), is(3));
        
        // Test level 4 (entre 57 et 111)
        p.xp = 80;
        assertThat(p.retrieveLevel(), is(4));
    }

    @Test
    @DisplayName("Branch Coverage : Gain d'XP sans monter de niveau")
    void testAddXpNoLevelUp() {
        Player p = new Adventurer("Test", "Test", 100, new ArrayList<>());
        
        boolean leveledUp = UpdatePlayer.addXp(p, 5);
        
        assertThat(leveledUp, is(false));
        assertThat(p.getXp(), is(5));
    }

    @Test
    @DisplayName("Branch Coverage : Vendre un objet qu'on n'a pas")
    void testSellItemNotOwned() {
        Player p = new Adventurer("Marchand", "Test", 100, new ArrayList<>());
        Item phantomItem = new Item("Fantome", "N'existe pas", 1, 1000);
        p.sell(phantomItem);
        assertThat(p.money, is(100));
    }
    @Test
    @DisplayName("Branch Coverage : Aventurier Haut Niveau (Pas de Malus de soin)")
    void testAdventurerHighLevelHeal() {
        Player p = new Adventurer("Conan", "Barbare", 100, new ArrayList<>());
        
        p.xp = 80; 
        p.healthpoints = 100;
        p.currenthealthpoints = 20;
        
        // Il se repose
        UpdatePlayer.majFinDeTour(p);
        assertThat(p.currenthealthpoints, is(22));
    }

    @Test
    @DisplayName("Branch Coverage : Affichage avec données nulles (Defensive Coding)")
    void testAffichageNullData() {
        Player p = new Adventurer("Bug", "Bug", 100, null);
        
        // On force les capacités à NULL
        p.abilities = null;
        String res = Affichage.afficherJoueur(p);
        String resMd = Affichage.afficherJoueurMarkdown(p);
        assertThat(res, notNullValue());
        assertThat(resMd, notNullValue());
    }
    @Test
    @DisplayName("Branch Coverage : Constructeur avec classe inconnue (Fallback)")
    void testUnknownPlayerClass() {
        Player p = new Player("Test", "Test", "UNKNOWN", 100, new ArrayList<>()) {
            @Override
            public void majFinDeTour() {
            }
        };
        assertThat(p.abilities, notNullValue());
        assertThat(p.abilities.isEmpty(), is(true));
    }
    @Test
    @DisplayName("Branch Coverage : Bornage Max (Soin excessif)")
    void testOverHealingClamp() {
        Player p = new Player("Hacker", "GodMode", "ADVENTURER", 100, new ArrayList<>()) {
            @Override
            public void majFinDeTour() {
                this.currenthealthpoints += 1000;
            }
        };

        p.healthpoints = 100;
        p.currenthealthpoints = 1;

        // On lance la mise à jour
        UpdatePlayer.majFinDeTour(p);
        assertThat(p.currenthealthpoints, is(100));
    }
    @Test
    @DisplayName("Branch Coverage : Montée au niveau 6 (Hors limites des stats)")
    void testLevelUpToUnknownLevel() {
        // On crée un joueur niveau 5 (XP > 111)
        Player p = new Adventurer("God", "God", 100, new ArrayList<>());
        p.xp = 150; 
        Player superPlayer = new Adventurer("Super", "Man", 100, new ArrayList<>()) {
            @Override
            public int retrieveLevel() {
                return 6; // On force le niveau 6
            }
        };
        
        boolean res = UpdatePlayer.addXp(superPlayer, 10);
        
        assertThat(res, is(false)); 
    }
}