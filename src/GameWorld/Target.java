package GameWorld;

public class Target implements ITarget {
    private int health;
    private String name;
    private Tuple<Integer, Integer> coordinates;

    public Target(int health, String name, Tuple<Integer, Integer> coordinates) {
        this.health = health;
        this.name = name;
        this.coordinates = coordinates;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void moveTarget(Tuple<Integer, Integer> newCoordinates) {
        this.coordinates = newCoordinates;
    }

    @Override
    public Tuple<Integer, Integer> getCoordinates() {
        return coordinates;
    }
}
