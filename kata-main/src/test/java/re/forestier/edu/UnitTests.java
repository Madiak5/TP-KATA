package re.forestier.edu.rpg;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UnitTests {

    @Test
    @DisplayName("Test de création d'un joueur valide")
    void testPlayerCreationValid() {
        Player p = new Archer("Florian", "Grognak", 100, new ArrayList<>());
        
        assertThat(p.playerName, is("Florian"));
        assertThat(p.getAvatarClass(), is("ARCHER"));
        assertThat(p.money, is(100));
        assertThat(p.abilities, notNullValue());
        assertThat(p.abilities.get("INT"), is(1));
    }

    @Test
    @DisplayName("Impossible d'avoir de l'argent négatif")
    void testNegativeMoney() {
        Player p = new Adventurer("Florian", "Grognak", 100, new ArrayList<>());
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            p.removeMoney(200);
        });

        assertThat(exception.getMessage(), is("Player can't have a negative money!"));
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
        // MODIFIÉ : Appel direct sur l'objet p
        boolean leveledUp = p.addXp(20); 
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
        
        // MODIFIÉ : updateEndTurn au lieu de UpdatePlayer.majFinDeTour
        p.updateEndTurn();
        assertThat(p.currenthealthpoints, is(12));
    }

    @Test
    @DisplayName("MajFinDeTour : Soins de base ARCHER")
    void testMajFinDeTourArcher() {
        Player p = new Archer("Florian", "Grognak", 100, new ArrayList<>());
        p.healthpoints = 100;
        p.currenthealthpoints = 20;
        p.inventory.add(new Item("Magic Bow", "Cool bow", 2, 50));

        p.updateEndTurn();
        assertThat(p.currenthealthpoints, is(22)); 
    }

    @Test
    @DisplayName("MajFinDeTour : ADVENTURER")
    void testMajFinDeTourAdventurer() {
        Player p = new Adventurer("Florian", "Grognak", 100, new ArrayList<>());
        p.healthpoints = 100;
        p.currenthealthpoints = 10;
        
        p.updateEndTurn();
        assertThat(p.currenthealthpoints, is(11));
    }
    
    @Test
    @DisplayName("Création et évolution d'un Gobelin")
    void testGoblin() {
        Player p = new Goblin("Keke", "Keke le Gobelin", 100, new ArrayList<>());
        assertThat(p.getAvatarClass(), is("GOBLIN"));
        assertThat(p.abilities.get("INT"), is(2));
        
        // MODIFIÉ : Appel direct
        p.addXp(20); 
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
        Player p = new Adventurer("Test", "Test", 100, new ArrayList<>());
        p.currenthealthpoints = 0; 
        
        p.updateEndTurn();
        
        assertThat(p.currenthealthpoints, is(0));
    }

    @Test
    @DisplayName("Branch Coverage : Joueur Full Vie ne dépasse pas le max")
    void testMajFinDeTourFullLife() {
        Player p = new Adventurer("Test", "Test", 100, new ArrayList<>());
        p.currenthealthpoints = 100;
        
        p.updateEndTurn();
        
        assertThat(p.currenthealthpoints, is(100));
    }

    @Test
    @DisplayName("Branch Coverage : Règle des 50% (Pas de soin si > 50% PV)")
    void testMajFinDeTourHalfLifeRule() {
        Player p = new Adventurer("Test", "Test", 100, new ArrayList<>());
        p.currenthealthpoints = 60;
        
        p.updateEndTurn();
        
        assertThat(p.currenthealthpoints, is(60));
    }

    @Test
    @DisplayName("Branch Coverage : Archer sans arc")
    void testArcherNoBow() {
        Player p = new Archer("Legolas", "Elf", 100, new ArrayList<>());
        p.currenthealthpoints = 20;
        
        p.updateEndTurn();
        assertThat(p.currenthealthpoints, is(21));
    }

    @Test
    @DisplayName("Branch Coverage : Nain sans Elixir")
    void testDwarfNoElixir() {
        Player p = new Dwarf("Gimli", "Dwarf", 100, new ArrayList<>());
        p.currenthealthpoints = 10;
        
        p.updateEndTurn();
        assertThat(p.currenthealthpoints, is(11));
    }
    
    @Test
    @DisplayName("Branch Coverage : Niveau Max (XP très haute)")
    void testMaxLevel() {
        Player p = new Adventurer("Test", "Test", 100, new ArrayList<>());
        p.xp = 200; 
        
        assertThat(p.retrieveLevel(), is(5));
    }

    @Test
    @DisplayName("Branch Coverage : Paliers intermédiaires de niveaux")
    void testLevelIntermediates() {
        Player p = new Adventurer("Test", "Test", 100, new ArrayList<>());
        
        p.xp = 20; 
        assertThat(p.retrieveLevel(), is(2));
        
        p.xp = 40;
        assertThat(p.retrieveLevel(), is(3));
        
        p.xp = 80;
        assertThat(p.retrieveLevel(), is(4));
    }

    @Test
    @DisplayName("Branch Coverage : Gain d'XP sans monter de niveau")
    void testAddXpNoLevelUp() {
        Player p = new Adventurer("Test", "Test", 100, new ArrayList<>());
        
        boolean leveledUp = p.addXp(5);
        
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
        
        p.updateEndTurn();
        assertThat(p.currenthealthpoints, is(22));
    }

    @Test
    @DisplayName("Branch Coverage : Affichage avec données nulles (Defensive Coding)")
    void testAffichageNullData() {
        Player p = new Adventurer("Bug", "Bug", 100, null);
        
        p.abilities = null;
        String res = Affichage.afficherJoueur(p);
        assertThat(res, notNullValue());
    }

    @Test
    @DisplayName("Adventurer Niveau 3+ : Bonus de soin complet (+2 PV)")
    void testAdventurerHealHighLevel() {
        Player p = new Adventurer("Conan", "Le Barbare", 100, new ArrayList<>());
        
        p.xp = 30; 
        p.currenthealthpoints = 10;

        p.updateEndTurn(); 
        assertThat(p.currenthealthpoints, is(12));
    }

    @Test
    @DisplayName("Coverage : Monter à un niveau inconnu (ex: Niveau 6)")
    void testLevelUpToNonExistentLevel() {
        // Astuce : On surcharge retrieveLevel mais on hérite de Adventurer
        // pour ne pas avoir à réimplémenter les méthodes abstraites.
        Player p = new Adventurer("Test", "Test", 100, new ArrayList<>()) {
            @Override
            public int retrieveLevel() {
                return this.xp < 100 ? 5 : 6;
            }
        };
        
        p.xp = 0; 
        
        p.addXp(200); 
        assertThat(p.retrieveLevel(), is(6));
    }

    @Test
    @DisplayName("Coverage : Limite exacte des 50% PV (Ne doit pas soigner)")
    void testMajFinDeTourExactHalfLife() {
        Player p = new Adventurer("Test", "Test", 100, new ArrayList<>());
        
        p.currenthealthpoints = 50;
        
        p.updateEndTurn();
        
        assertThat(p.currenthealthpoints, is(50));
    }

    @Test
    @DisplayName("Coverage : Limite juste en dessous des 50% PV (Doit soigner)")
    void testMajFinDeTourJustBelowHalfLife() {
        Player p = new Adventurer("Test", "Test", 100, new ArrayList<>());
        
        p.currenthealthpoints = 49;
        
        p.updateEndTurn();
        assertThat(p.currenthealthpoints, is(50)); 
    }

    @Test
    @DisplayName("Coverage : Affichage d'un joueur sans inventaire (Branche boucle vide)")
    void testAffichageEmptyInventory() {
        Player p = new Adventurer("Pauvre", "Hobo", 0, new ArrayList<>());
        
        String res = Affichage.afficherJoueurMarkdown(p);
        
        assertThat(res, containsString("# Joueur Hobo"));
        assertThat(res, not(containsString("* Potion")));
    }

    @Test
    @DisplayName("Coverage : Poids d'un inventaire vide (Boucle For)")
    void testWeightEmptyInventory() {
        Player p = new Adventurer("Leger", "Plume", 100, new ArrayList<>());
        
        int weight = p.getCurrentWeight();
        
        assertThat(weight, is(0));
    }

    @Test
    @DisplayName("Coverage : Ramasser objet limite Poids (Exactement Max)")
    void testPickUpMaxWeight() {
        Player p = new Adventurer("Costaud", "Hercule", 100, new ArrayList<>());
        Item heavyItem = new Item("Enclume", "Lourd", 25, 0);
        
        boolean result = p.pickUp(heavyItem);
        
        assertThat(result, is(true));
        assertThat(p.getCurrentWeight(), is(25));
    }

    @Test
    @DisplayName("Coverage : Ramasser objet Trop Lourd (Refus)")
    void testPickUpTooHeavy() {
        Player p = new Adventurer("Faible", "PasMuscle", 100, new ArrayList<>());
        
        Item tooHeavy = new Item("Montagne", "Trop Lourd", 26, 0);
        
        boolean result = p.pickUp(tooHeavy);
        
        assertThat(result, is(false));
        assertThat(p.inventory.isEmpty(), is(true));
    }

    @Test
    @DisplayName("Coverage : Constructeur avec classe inconnue (Branche Else)")
    void testConstructorUnknownClass() {
        // Ici, on doit implémenter les méthodes abstraites du nouveau Player
        Player p = new Player("Naruto", "Konoha", "NINJA", 100, new ArrayList<>()) {
            @Override
            protected void applyClassSpecificEndTurnEffects() {
                // Rien
            }
            @Override
            protected HashMap<String, Integer> getLevelBonuses(int level) {
                // Pas de bonus pour les ninjas
                return new HashMap<>();
            }
        };
        
        assertThat(p.abilities, notNullValue());
        assertThat(p.abilities.isEmpty(), is(true));
    }

    @Test
    @DisplayName("Coverage : Affichage d'un joueur SANS compétences (Boucle abilities vide)")
    void testAffichageNoAbilities() {
        Player p = new Adventurer("NoSkill", "Null", 100, new ArrayList<>());
        
        p.abilities.clear(); 
        
        String res = Affichage.afficherJoueur(p);
        String resMd = Affichage.afficherJoueurMarkdown(p);
        
        assertThat(res, notNullValue());
        assertThat(resMd, containsString("Null"));
        assertThat(resMd, not(containsString("INT :")));
    }

    @Test
    @DisplayName("Coverage : Affichage avec listes VIDES (Branche For Loop Skipped)")
    void testAffichageEmptyLists() {
        Player p = new Adventurer("Vide", "MrVide", 100, new ArrayList<>());
        
        if (p.abilities != null) {
            p.abilities.clear();
        }

        String text = Affichage.afficherJoueur(p);
        String md = Affichage.afficherJoueurMarkdown(p);

        assertThat(text, containsString("Inventaire :"));
        assertThat(md, containsString("## Inventaire"));
        assertThat(md, not(containsString("* Potion"))); 
        assertThat(text, not(containsString("ATK :")));
    }
}