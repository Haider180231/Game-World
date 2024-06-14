package GameWorld;

public class Target implements ITarget {
    private int health;
    private String name;

    public Target(int health, String name) {
        this.health = health;
        this.name = name;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public String getName() {
        return name;
    }
}
