package game;

/**
 * Represents an item in the game world.
 */
public interface Iitem {

  /**
   * Retrieves the name of the item.
   *
   * @return the name of the item
   */
  String getName();

  /**
   * Retrieves the damage value of the item.
   *
   * @return the damage value of the item
   */
  int getDamage();

  /**
   * Retrieves the index of the room where the item is located.
   *
   * @return the room index of the item
   */
  int getRoomIndex();
}
