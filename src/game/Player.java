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
   * Constructs a new Player with the specified name, coordinates, and maximum items.
   *
   * @param name        the name of the player
   * @param coordinates the initial coordinates of the player
   * @param maxItems    the maximum number of items the player can carry
   */
  public Player(String name, Tuple<Integer, Integer> coordinates, int maxItems) {
    this.name = name;
    this.coordinates = coordinates;
    this.items = new ArrayList<>();
    this.maxItems = maxItems;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Tuple<Integer, Integer> getCoordinates() {
    return coordinates;
  }

  @Override
  public void move(Tuple<Integer, Integer> newCoordinates) {
    this.coordinates = newCoordinates;
  }

  @Override
  public void addItem(Iitem item) {
    if (items.size() < maxItems) {
      items.add(item);
    }
  }

  @Override
  public List<Iitem> getItems() {
    return items;
  }

  @Override
  public int getMaxItems() {
    return maxItems;
  }

  @Override
  public String lookAround(Igameworld world) {
    Iroom currentRoom = world.getRoomByCoordinates(coordinates);
    StringBuilder output = new StringBuilder();
    output.append("Player ").append(name).append(
        " is in: ").append(currentRoom.getName()).append(".\n");

    output.append("Items in the room:\n");
    if (currentRoom.getItems().isEmpty()) {
      output.append(" - No items\n");
    } else {
      for (Iitem item : currentRoom.getItems()) {
        output.append(" - ").append(
            item.getName()).append(" (Damage: ").append(item.getDamage()).append(")\n");
      }
    }

    output.append("Players in the room:\n");
    boolean playersFound = false;
    for (Iplayer player : world.getPlayers()) {
      if (!player.equals(this) && player.getCoordinates().equals(coordinates)) {
        output.append(" - ").append(player.getName()).append("\n");
        playersFound = true;
      }
    }
    if (!playersFound) {
      output.append(" - No other players\n");
    }

    Ipet pet = world.getPet();
    if (pet != null && pet.getCoordinates().equals(coordinates)) {
      output.append("Pet: ").append(pet.getName()).append(" is here.\n");
    }

    Itarget target = world.getTarget();
    if (target != null && target.getCoordinates().equals(coordinates)) {
      output.append("Target: ").append(target.getName()).append(" is here.\n");
    }

    output.append("Visible rooms:\n");
    List<Iroom> neighbors = world.getNeighbors(currentRoom);
    if (neighbors.isEmpty()) {
      output.append(" - No visible rooms\n");
    } else {
      for (Iroom neighbor : neighbors) {
        output.append(" - ").append(neighbor.getName()).append("\n");

        if (pet != null && pet.getCoordinates().equals(neighbor.getCoordinates())) {
          continue; 
        }

        output.append("Items in ").append(neighbor.getName()).append(":\n");
        if (neighbor.getItems().isEmpty()) {
          output.append("  - No items\n");
        } else {
          for (Iitem item : neighbor.getItems()) {
            output.append("  - ").append(item.getName()).append(
                " (Damage: ").append(item.getDamage()).append(")\n");
          }
        }

        output.append("Players in ").append(neighbor.getName()).append(":\n");
        boolean neighborPlayersFound = false;
        for (Iplayer player : world.getPlayers()) {
          if (!player.equals(this) && player.getCoordinates().equals(neighbor.getCoordinates())) {
            output.append("  - ").append(player.getName()).append("\n");
            neighborPlayersFound = true;
          }
        }
        if (!neighborPlayersFound) {
          output.append("  - No other players\n");
        }

        if (target != null && target.getCoordinates().equals(neighbor.getCoordinates())) {
          output.append("Target: ").append(target.getName()).append(" is here.\n");
        }
      }
    }

    return output.toString();
  }

  @Override
  public String attack(Itarget target, Igameworld world) {
    if (target == null) {
      return "There is no target to attack.";
    }

    // Check if the target is in the same room
    if (!target.getCoordinates().equals(this.coordinates)) {
      return "The target is not in the same room. Attack not possible.";
    }

    for (Iplayer player : world.getPlayers()) {
      if (!player.equals(this) && player.canSee(this, world)) {
        return "Attack seen by " + player.getName() + ". Attack stopped.";
      }
    }

    Iitem maxDamageItem = items.stream()
        .max((item1, item2) -> Integer.compare(item1.getDamage(), item2.getDamage()))
        .orElse(null);

    int totalDamage = (maxDamageItem != null) ? maxDamageItem.getDamage() : 1; 

    target.takeDamage(totalDamage);

    if (maxDamageItem != null) {
      items.remove(maxDamageItem); // Remove the used item
    }

    return "Player " + name + " attacked " + target.getName() + " with " + totalDamage + " damage.";
  }

  @Override
  public boolean canSee(Iplayer other, Igameworld world) {
    // Check if the players are in the same coordinates
    if (this.coordinates.equals(other.getCoordinates())) {
      return true;
    }

    // Get the current room and neighboring rooms
    Iroom currentRoom = world.getRoomByCoordinates(this.coordinates);
    List<Iroom> neighbors = world.getNeighbors(currentRoom);

    // Check if the other player is in one of the neighboring rooms
    for (Iroom neighbor : neighbors) {
      if (neighbor.getCoordinates().equals(other.getCoordinates())) {
        // If pet is in the same neighboring room as the other player, the player cannot see
        Ipet pet = world.getPet();
        if (pet != null && pet.getCoordinates().equals(other.getCoordinates())) {
          return false;
        }
        return true;
      }
    }

    return false;
  }

  @Override
  public void takeTurn(Igameworld world) {
    // Player turn logic can be implemented here
  }
}
