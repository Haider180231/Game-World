package game;

import java.util.List;

/**
 * Represents a player in the game.
 */
public interface Iplayer {

  /**
   * Gets the name of the player.
   *
   * @return the name of the player
   */
  String getName();

  /**
   * Gets the current coordinates of the player.
   *
   * @return the current coordinates of the player
   */
  Tuple<Integer, Integer> getCoordinates();

  /**
   * Moves the player to new coordinates.
   *
   * @param newCoordinates the new coordinates to move to
   */
  void move(Tuple<Integer, Integer> newCoordinates);

  /**
   * Gets the list of items the player is carrying.
   *
   * @return the list of items
   */
  List<Iitem> getItems();

  /**
   * Adds an item to the player's inventory.
   *
   * @param item the item to add
   */
  void addItem(Iitem item);

  /**
   * Gets the maximum number of items the player can carry.
   *
   * @return the maximum number of items
   */
  int getMaxItems();
}
