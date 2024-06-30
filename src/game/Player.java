package game;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the game.
 */
public class Player implements Iplayer {
  private String name;
  private Tuple<Integer, Integer> coordinates;
  private List<Iitem> items;
  private int maxItems;

  /**
   * Constructs a new Player.
   *
   * @param name the name of the player
   * @param coordinates the initial coordinates of the player
   * @param maxItems the maximum number of items the player can carry
   */
  public Player(String name, Tuple<Integer, Integer> coordinates, int maxItems) {
    this.name = name;
    this.coordinates = coordinates;
    this.items = new ArrayList<>();
    this.maxItems = maxItems;
  }

  /**
   * Gets the name of the player.
   *
   * @return the name of the player
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Gets the current coordinates of the player.
   *
   * @return the current coordinates of the player
   */
  @Override
  public Tuple<Integer, Integer> getCoordinates() {
    return coordinates;
  }

  /**
   * Moves the player to new coordinates.
   *
   * @param newCoordinates the new coordinates to move to
   */
  @Override
  public void move(Tuple<Integer, Integer> newCoordinates) {
    this.coordinates = newCoordinates;
  }

  /**
   * Adds an item to the player's inventory.
   *
   * @param item the item to add
   */
  @Override
  public void addItem(Iitem item) {
    if (items.size() < maxItems) {
      items.add(item);
    }
  }

  /**
   * Gets the list of items the player is carrying.
   *
   * @return the list of items
   */
  @Override
  public List<Iitem> getItems() {
    return items;
  }

  /**
   * Gets the maximum number of items the player can carry.
   *
   * @return the maximum number of items
   */
  @Override
  public int getMaxItems() {
    return maxItems;
  }
}
