package GameWorld;

import java.util.List;

public interface IRoom {
    void addItem(IItem item);
    List<IItem> getItems();
    String getName();
    Tuple<Integer, Integer> getCoordinates();
    int getIndex();
    List<IRoom> getNeighbors(List<IRoom> rooms);
}
