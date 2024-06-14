package GameWorld;

public interface IRoom {
    String getName();
    Tuple<Integer, Integer> getCoordinates();
    Tuple<Integer, Integer> getEndingCoordinates();
    void addItem(IItem item);
}
