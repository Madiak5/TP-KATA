package re.forestier.edu.rpg;
import java.util.ArrayList;

public class Dwarf extends Player {
    public Dwarf(String playerName, String avatarName, int money, ArrayList<Item> inventory) {
        super(playerName, avatarName, "DWARF", money, inventory);
    }

    @Override
    public void majFinDeTour() {
        this.currenthealthpoints += 1;
        
        for (Item i : this.inventory) {
            if (i.name.equals("Holy Elixir")) {
                this.currenthealthpoints += 1;
            }
        }
    }
}