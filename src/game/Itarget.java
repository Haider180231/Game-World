package game;

/**
 * Represents a target in the game world.
 */
public interface Itarget {

  /**
   * Gets the current health of the target.
   *
   * @return the current health of the target
   */
  int getHealth();

  /**
   * Gets the name of the target.
   *
   * @return the name of the target
   */
  String getName();

  /**
   * Gets the current coordinates of the target.
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

  /**
   * Reduces the target's health by the specified damage.
   *
   * @param damage the amount of damage to take
   */
  void takeDamage(int damage);
}
