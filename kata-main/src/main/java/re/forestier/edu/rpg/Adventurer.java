package re.forestier.edu.rpg;
import java.util.ArrayList;

public class Adventurer extends Player {
    public Adventurer(String playerName, String avatarName, int money, ArrayList<Item> inventory) {
        super(playerName, avatarName, "ADVENTURER", money, inventory);
    }

    @Override
    public void majFinDeTour() {
        this.currenthealthpoints += 2;
        if (this.retrieveLevel() < 3) {
            this.currenthealthpoints -= 1;
        }
    }
}