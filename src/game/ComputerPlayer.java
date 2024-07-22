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
   * Constructs a new ComputerPlayer with the specified name, coordinates, and maximum items.
   *
   * @param name        the name of the computer player
   * @param coordinates the initial coordinates of the computer player
   * @param maxItems    the maximum number of items the computer player can carry
   */
  public ComputerPlayer(String name, Tuple<Integer, Integer> coordinates, int maxItems) {
    this.name = name;
    this.coordinates = coordinates;
    this.maxItems = maxItems;
    this.items = new ArrayList<>();
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
        output.append(" - ").append(item.getName()).append(
            " (Damage: ").append(item.getDamage()).append(")\n");
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
          continue; // 如果宠物在邻近房间，不显示该房间的任何信息
        }

        output.append("Items in ").append(neighbor.getName()).append(":\n");
        if (neighbor.getItems().isEmpty()) {
          output.append("  - No items\n");
        } else {
          for (Iitem item : neighbor.getItems()) {
            output.append("  - ").append(item.getName()).append(" (Damage: ").append(item.getDamage()).append(")\n");
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

    for (Iplayer player : world.getPlayers()) {
      if (!player.equals(this) && player.canSee(this, world)) {
        return "Attack seen by " + player.getName() + ". Attack stopped.";
      }
    }

    // 找到伤害最大的物品
    Iitem maxDamageItem = items.stream()
        .max((item1, item2) -> Integer.compare(item1.getDamage(), item2.getDamage()))
        .orElse(null);

    int totalDamage = (maxDamageItem != null)
        ? maxDamageItem.getDamage() : 1; // Poking in the eye does 1 damage

    target.takeDamage(totalDamage);

    if (maxDamageItem != null) {
      items.remove(maxDamageItem); // Remove the used item
    }

    return "Computer Player " + name + " attacked "
        + target.getName() + " with " + totalDamage + " damage.";
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
    Random random = new Random();
    Itarget target = world.getTarget();
    if (target != null && target.getCoordinates().equals(coordinates)) {
      System.out.println(attack(target, world));
      return; // End the turn immediately after attacking
    }

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
        System.out.println(lookAround(world));
        break;

      default:
        // Default case to handle unexpected values
        System.out.println("Unexpected action value: " + action);
        break;
    }
  }
}
