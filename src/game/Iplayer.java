package game;

import java.util.List;

public interface Iplayer {
    String getName();
    Tuple<Integer, Integer> getCoordinates();
    void move(Tuple<Integer, Integer> newCoordinates);
    List<Iitem> getItems();
    void addItem(Iitem item);
    int getMaxItems();
}
