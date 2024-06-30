package game;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a room in the game world.
 */
public class Room implements Iroom {
  private String name;
  private Tuple<Integer, Integer> coordinates;
  private Tuple<Integer, Integer> endingCoordinates;
  private List<Iitem> items;

  /**
   * Constructs a Room with the specified name, starting coordinates, and ending coordinates.
   *
   * @param name              the name of the room
   * @param coordinates       the starting coordinates of the room
   * @param endingCoordinates the ending coordinates of the room
   */
  public Room(String name, Tuple<Integer, Integer> coordinates, 
      Tuple<Integer, Integer> endingCoordinates) {
    this.name = name;
    this.coordinates = coordinates;
    this.endingCoordinates = endingCoordinates;
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
  public Tuple<Integer, Integer> getEndingCoordinates() {
    return endingCoordinates;
  }

  @Override
  public void addItem(Iitem item) {
    items.add(item);
  }

  @Override
  public List<Iitem> getItems() {
    return items;
  }

  @Override
  public boolean isNeighbor(Room other) {
    return (this.coordinates.getFirst() == other.endingCoordinates.getFirst() + 1 
        && this.coordinates.getSecond() <= other.endingCoordinates.getSecond() 
        && this.endingCoordinates.getSecond() >= other.coordinates.getSecond()) 
        || (this.endingCoordinates.getFirst() == other.coordinates.getFirst() - 1 
           && this.coordinates.getSecond() <= other.endingCoordinates.getSecond() 
           && this.endingCoordinates.getSecond() >= other.coordinates.getSecond()) 
        || (this.coordinates.getSecond() == other.endingCoordinates.getSecond() + 1 
           && this.coordinates.getFirst() <= other.endingCoordinates.getFirst() 
           && this.endingCoordinates.getFirst() >= other.coordinates.getFirst()) 
        || (this.endingCoordinates.getSecond() == other.coordinates.getSecond() - 1 
           && this.coordinates.getFirst() <= other.endingCoordinates.getFirst() 
           && this.endingCoordinates.getFirst() >= other.coordinates.getFirst());
  }
}
