package game;

/**
 * Represents a target in the game world.
 */
public interface Itarget {

  /**
   * Retrieves the health of the target.
   *
   * @return the health of the target
   */
  int getHealth();

  /**
   * Retrieves the name of the target.
   *
   * @return the name of the target
   */
  String getName();

  /**
   * Retrieves the current coordinates of the target.
   *
   * @return the current coordinates of the target
   */
  Tuple<Integer, Integer> getCoordinates();

  /**
   * Moves the target to new coordinates.
   *
   * @param newCoordinates the new coordinates to move the target to
   */
  void moveTarget(Tuple<Integer, Integer> newCoordinates);
}
