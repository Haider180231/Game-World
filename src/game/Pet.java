package game;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * Represents a pet in the game world.
 */
public class Pet implements Ipet {
  private String name;
  private Tuple<Integer, Integer> coordinates;
  private Stack<Iroom> dfsStack;
  private Set<Iroom> visitedRooms;

  /**
   * Constructs a Pet with the specified name and coordinates.
   *
   * @param name the name of the pet
   * @param coordinates the initial coordinates of the pet
   */
  public Pet(String name, Tuple<Integer, Integer> coordinates) {
    this.name = name;
    this.coordinates = coordinates;
    this.dfsStack = new Stack<>();
    this.visitedRooms = new HashSet<>();
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
    // Reset DFS traversal when manually moved
    dfsStack.clear();
    visitedRooms.clear();
  }

  @Override
  public void moveDfs(Igameworld world) {
    if (dfsStack.isEmpty()) {
      // Start DFS traversal from the current room
      Iroom currentRoom = world.getRoomByCoordinates(coordinates);
      dfsStack.push(currentRoom);
      visitedRooms.add(currentRoom);
    }

    while (!dfsStack.isEmpty()) {
      Iroom room = dfsStack.pop();
      List<Iroom> neighbors = world.getNeighbors(room);
      for (Iroom neighbor : neighbors) {
        if (!visitedRooms.contains(neighbor)) {
          dfsStack.push(neighbor);
          visitedRooms.add(neighbor);
          move(neighbor.getCoordinates());
          return; // Ensure the pet moves only once per call
        }
      }
    }
  }
}
