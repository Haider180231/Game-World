package GameWorld;

public class Item implements IItem {
    private String name;
    private int damage;
    private int roomIndex;

    public Item(String name, int damage, int roomIndex) {
        this.name = name;
        this.damage = damage;
        this.roomIndex = roomIndex;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public int getRoomIndex() {
        return roomIndex;
    }
}
