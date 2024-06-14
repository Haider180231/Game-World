package GameWorld;

public interface ITarget {
    int getHealth();
    String getName();
    Tuple<Integer, Integer> getCoordinates();
    void moveTarget(Tuple<Integer, Integer> newCoordinates);
}
