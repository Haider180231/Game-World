package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a computer-controlled player in the game.
 */
public class ComputerPlayer implements Iplayer {
  private String name;
  private Tuple<Integer, Integer> coordinates;
  private int maxItems;
  private List<Iitem> items;

  /**
   * Constructs a new ComputerPlayer.
   *
   * @param name the name of the player
   * @param coordinates the initial coordinates of the player
   * @param maxItems the maximum number of items the player can carry
   */
  public ComputerPlayer(String name, Tuple<Integer, Integer> coordinates, int maxItems) {
    this.name = name;
    this.coordinates = coordinates;
    this.maxItems = maxItems;
    this.items = new ArrayList<>();
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
   * Gets the maximum number of items the player can carry.
   *
   * @return the maximum number of items
   */
  @Override
  public int getMaxItems() {
    return maxItems;
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
   * Moves the player to new coordinates.
   *
   * @param newCoordinates the new coordinates to move to
   */
  @Override
  public void move(Tuple<Integer, Integer> newCoordinates) {
    coordinates = newCoordinates;
  }

  /**
   * Executes a turn for the computer player, choosing randomly between moving,
   * picking up an item, or looking around.
   *
   * @param world the game world in which the player is situated
   */
  public void takeTurn(Igameworld world) {
    Random random = new Random();
    int action = random.nextInt(3);

    switch (action) {
      case 0:
        // move
        List<Iroom> neighbors = world.getNeighbors(world.getRoomByCoordinates(coordinates));
        if (!neighbors.isEmpty()) {
          Iroom newRoom = neighbors.get(random.nextInt(neighbors.size()));
          move(newRoom.getCoordinates());
        }
        break;

      case 1:
        // pick item
        Iroom currentRoom = world.getRoomByCoordinates(coordinates);
        if (currentRoom != null && !currentRoom.getItems().isEmpty()) {
          Iitem item = currentRoom.getItems().get(0);
          addItem(item);
          currentRoom.getItems().remove(item);
        }
        break;

      case 2:
        // look around
        break;

      default:
        // Default case to handle unexpected values
        System.out.println("Unexpected action value: " + action);
        break;
    }
  }
}
