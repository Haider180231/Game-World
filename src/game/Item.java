package game;

/**
 * Represents an item in the game world.
 */
public class Item implements Iitem {
  private String name;
  private int damage;
  private int roomIndex;

  /**
   * Constructs an Item with the specified name, damage, and room index.
   *
   * @param name      the name of the item
   * @param damage    the damage value of the item
   * @param roomIndex the index of the room where the item is located
   */
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
