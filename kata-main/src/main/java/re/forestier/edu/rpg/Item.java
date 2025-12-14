package re.forestier.edu.rpg;

public class Item {
    public String name;
    public String description;
    public int weight;
    public int value;

    public Item(String name, String description, int weight, int value) {
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.value = value;
    }
    
    // Pour faciliter l'affichage plus tard
    @Override
    public String toString() {
        return name + " (" + weight + "kg, " + value + "$)";
    }
}