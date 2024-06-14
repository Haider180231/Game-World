package game;

import java.util.List;

/**
 * Represents a room in the game world.
 */
public interface Iroom {

  /**
   * Retrieves the name of the room.
   *
   * @return the name of the room
   */
  String getName();

  /**
   * Retrieves the starting coordinates of the room.
   *
   * @return the starting coordinates of the room
   */
  Tuple<Integer, Integer> getCoordinates();

  /**
   * Retrieves the ending coordinates of the room.
   *
   * @return the ending coordinates of the room
   */
  Tuple<Integer, Integer> getEndingCoordinates();

  /**
   * Adds an item to the room.
   *
   * @param item the item to add
   */
  void addItem(Iitem item);

  /**
   * Determines if the specified room is a neighbor of this room.
   *
   * @param other the other room to check
   * @return true if the other room is a neighbor, false otherwise
   */
  boolean isNeighbor(Room other);

  /**
   * Retrieves the list of items in the room.
   *
   * @return the list of items in the room
   */
  List<Iitem> getItems();
}
