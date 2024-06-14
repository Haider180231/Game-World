package GameWorld;

import java.util.List;
import java.util.ArrayList;

public class Room implements IRoom {
    private String name;
    private Tuple<Integer, Integer> coordinates;
    private Tuple<Integer, Integer> endingCoordinates;
    private List<IItem> items;

    public Room(String name, Tuple<Integer, Integer> coordinates, Tuple<Integer, Integer> endingCoordinates) {
        this.name = name;
        this.coordinates = coordinates;
        this.endingCoordinates = endingCoordinates;
        this.items = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Tuple<Integer, Integer> getCoordinates() {
        return coordinates;
    }

    @Override
    public Tuple<Integer, Integer> getEndingCoordinates() {
        return endingCoordinates;
    }

    @Override
    public void addItem(IItem item) {
        items.add(item);
    }
}
