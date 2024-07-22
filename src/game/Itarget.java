package game;

/**
 * Represents a target in the game world.
 */
public interface Itarget {

  int getHealth();

  String getName();

  Tuple<Integer, Integer> getCoordinates();

  void moveTarget(Tuple<Integer, Integer> newCoordinates);

  /**
   * Reduces the target's health by the specified damage.
   *
   * @param damage the amount of damage to take
   */
  void takeDamage(int damage);
}
