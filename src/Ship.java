import java.util.ArrayList;

// class used for creating instances of individual ships
public class Ship {

    public String name;
    public int size;
    ArrayList<Integer> position; // stores positions of points creating particular ship

    Ship (String name, int size) {
        this.name = name;
        this.size = size;
        this.position = new ArrayList<>();
    }
}
