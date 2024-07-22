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
   * Gets the coordinates of the player.
   *
   * @return the coordinates of the player
   */
  Tuple<Integer, Integer> getCoordinates();

  /**
   * Moves the player to new coordinates.
   *
   * @param newCoordinates the new coordinates to move the player to
   */
  void move(Tuple<Integer, Integer> newCoordinates);

  /**
   * Gets the list of items the player currently has.
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

  /**
   * Allows the player to look around in the game world.
   *
   * @param world the game world
   * @return a string describing the surroundings
   */
  String lookAround(Igameworld world);

  /**
   * Attacks the target character.
   *
   * @param target the target character to attack
   * @param world the game world to check visibility
   * @return the result of the attack as a string
   */
  String attack(Itarget target, Igameworld world);

  /**
   * Determines if this player can see another player.
   *
   * @param other the other player to check
   * @param world the game world
   * @return true if this player can see the other player, false otherwise
   */
  boolean canSee(Iplayer other, Igameworld world);

  /**
   * Takes the player's turn in the game.
   *
   * @param world the game world
   */
  void takeTurn(Igameworld world);
}
