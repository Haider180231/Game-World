package game;

/**
 * Represents a pet in the game world.
 */
public interface Ipet {
  /**
   * Retrieves the name of the pet.
   *
   * @return the name of the pet
   */
  String getName();

  /**
   * Retrieves the current coordinates of the pet.
   *
   * @return the current coordinates of the pet
   */
  Tuple<Integer, Integer> getCoordinates();

  /**
   * Moves the pet to new coordinates.
   *
   * @param newCoordinates the new coordinates to move the pet to
   */
  void move(Tuple<Integer, Integer> newCoordinates);

  /**
   * Moves the pet using a depth-first search (DFS) algorithm to navigate through the game world.
   *
   * @param world the game world in which the pet moves
   */
  void moveDfs(Igameworld world);
}
