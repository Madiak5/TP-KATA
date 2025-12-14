package re.forestier.edu.rpg;
import java.util.ArrayList;

public class Archer extends Player {
    public Archer(String playerName, String avatarName, int money, ArrayList<Item> inventory) {
        // On passe automatiquement "ARCHER" au parent
        super(playerName, avatarName, "ARCHER", money, inventory);
    }

    @Override
    public void majFinDeTour() {
        this.currenthealthpoints += 1;
        
        // Vérification spécifique à l'Archer
        for (Item i : this.inventory) {
            if (i.name.equals("Magic Bow")) {
                this.currenthealthpoints += (this.currenthealthpoints / 8) - 1;
            }
        }
    }
}