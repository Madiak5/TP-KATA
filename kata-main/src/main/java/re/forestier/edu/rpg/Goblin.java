package re.forestier.edu.rpg;
import java.util.ArrayList;

public class Goblin extends Player {
    public Goblin(String playerName, String avatarName, int money, ArrayList<Item> inventory) {
        super(playerName, avatarName, "GOBLIN", money, inventory);
    }

    @Override
    public void majFinDeTour() {
        // dans le sujet rien n'indique qu'il reçoit des soins spécifiques donc on laisse vide pour le moment
    }
}