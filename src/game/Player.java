package game;

import java.util.ArrayList;
import java.util.List;

public class Player implements Iplayer {
    private String name;
    private Tuple<Integer, Integer> coordinates;
    private List<Iitem> items;
    private int maxItems;

    public Player(String name, Tuple<Integer, Integer> coordinates, int maxItems) {
        this.name = name;
        this.coordinates = coordinates;
        this.items = new ArrayList<>();
        this.maxItems = maxItems;
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
    public void move(Tuple<Integer, Integer> newCoordinates) {
        this.coordinates = newCoordinates;
    }

    @Override
    public void addItem(Iitem item) {
        if (items.size() < maxItems) {
            items.add(item);
        }
    }

    @Override
    public List<Iitem> getItems() {
        return items;
    }

    @Override
    public int getMaxItems() {
        return maxItems;
    }
}
