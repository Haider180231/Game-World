package game;

/**
 * Represents a target in the game world.
 */
public class Target implements Itarget {
  private int health;
  private String name;
  private Tuple<Integer, Integer> coordinates;

  /**
   * Constructs a new Target with the specified health, name, and coordinates.
   *
   * @param health      the health of the target
   * @param name        the name of the target
   * @param coordinates the initial coordinates of the target
   */
  public Target(int health, String name, Tuple<Integer, Integer> coordinates) {
    this.health = health;
    this.name = name;
    this.coordinates = coordinates;
  }

  @Override
  public int getHealth() {
    return health;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void moveTarget(Tuple<Integer, Integer> newCoordinates) {
    this.coordinates = newCoordinates;
  }

  @Override
  public Tuple<Integer, Integer> getCoordinates() {
    return coordinates;
  }

  @Override
  public void takeDamage(int damage) {
    health -= damage;
    if (health < 0) {
      health = 0;
    }
  }
}
