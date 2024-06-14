package GameWorld;

import java.util.List;

public interface IRoom {
    String getName();
    Tuple<Integer, Integer> getCoordinates();
    Tuple<Integer, Integer> getEndingCoordinates();
    void addItem(IItem item);
    boolean isNeighbor(Room other);
    List<IItem> getItems();
}
