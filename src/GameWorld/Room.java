package GameWorld;

import java.util.ArrayList;
import java.util.List;

public class Room implements IRoom {
    private String name;
    private Tuple<Integer, Integer> coordinates;
    private List<IItem> items;
    private int index;

    public Room(String name, Tuple<Integer, Integer> coordinates, int index) {
        this.name = name;
        this.coordinates = coordinates;
        this.items = new ArrayList<>();
        this.index = index;
    }

    @Override
    public void addItem(IItem item) {
        items.add(item);
    }

    @Override
    public List<IItem> getItems() {
        return items;
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
    public int getIndex() {
        return index;
    }

    @Override
    public List<IRoom> getNeighbors(List<IRoom> rooms) {
        // 根据需要实现获取邻居房间的逻辑
        return new ArrayList<>();
    }
}
