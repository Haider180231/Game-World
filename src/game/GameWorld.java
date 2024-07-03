package game;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game world, including its rooms, players, and the target.
 */
public class GameWorld implements Igameworld {
  private int rows;
  private int columns;
  private Itarget target;
  private List<Iroom> rooms;
  private List<Iplayer> players;

  /**
   * Constructs a new GameWorld.
   *
   * @param rows the number of rows in the game world
   * @param columns the number of columns in the game world
   * @param name the name of the game world
   * @param target the target character in the game world
   * @param rooms the list of rooms in the game world
   */
  public GameWorld(int rows, int columns, String name, Itarget target, List<Iroom> rooms) {
    this.rows = rows;
    this.columns = columns;
    this.target = target;
    this.rooms = rooms;
    this.players = new ArrayList<>();
  }

  @Override
  public void addRoom(Iroom room) {
    rooms.add(room);
  }

  @Override
  public Iroom getRoomByIndex(int index) {
    if (index < 0 || index >= rooms.size()) {
      throw new IndexOutOfBoundsException("Invalid room index: " + index);
    }
    return rooms.get(index);
  }

  @Override
  public List<Iroom> getRooms() {
    return rooms;
  }

  @Override
  public Itarget getTarget() {
    return target;
  }

  @Override
  public List<Iroom> getNeighbors(Iroom room) {
    List<Iroom> neighbors = new ArrayList<>();
    for (Iroom r : rooms) {
      if (r != room && ((Room) room).isNeighbor((Room) r)) {
        neighbors.add(r);
      }
    }
    return neighbors;
  }

  @Override
  public void displayRoomInfo(int index) {
    if (index < 0 || index >= rooms.size()) {
      System.out.println("Invalid room index.");
      return;
    }
    Iroom room = rooms.get(index);
    System.out.println("Room Name: " + room.getName());
    System.out.println("Items in the Room:");
    for (Iitem item : room.getItems()) {
      System.out.println(" - " + item.getName() + " (Damage: " + item.getDamage() + ")");
    }
    List<Iroom> neighbors = getNeighbors(room);
    System.out.println("Visible Rooms:");
    for (Iroom neighbor : neighbors) {
      System.out.println(" - " + neighbor.getName());
    }
    System.out.println("Players in the Room:");
    for (Iplayer player : players) {
      if (player.getCoordinates().equals(room.getCoordinates())) {
        System.out.println(" - " + player.getName());
      }
    }
  }

  @Override
  public void moveTarget() {
    int currentRoomIndex = 0;
    if (target.getCoordinates() != null) {
      for (int i = 0; i < rooms.size(); i++) {
        if (rooms.get(i).getCoordinates().equals(target.getCoordinates())) {
          currentRoomIndex = i;
          break;
        }
      }
    }
    int nextRoomIndex = (currentRoomIndex + 1) % rooms.size();
    target.moveTarget(rooms.get(nextRoomIndex).getCoordinates());
  }

  @Override
  public void addPlayer(Iplayer player) {
    players.add(player);
  }

  @Override
  public List<Iplayer> getPlayers() {
    return players;
  }

  @Override
  public void movePlayer(Iplayer player, Tuple<Integer, Integer> newCoordinates) {
    Iroom currentRoom = getRoomByCoordinates(player.getCoordinates());
    Iroom targetRoom = getRoomByCoordinates(newCoordinates);

    if (currentRoom != null && targetRoom != null 
        && getNeighbors(currentRoom).contains(targetRoom)) {
      player.move(newCoordinates);
    }
  }

  @Override
  public int getColumns() {
    return columns;
  }

  @Override
  public int getRows() {
    return rows;
  }

  @Override
  public Iroom getRoomByCoordinates(Tuple<Integer, Integer> coordinates) {
    for (Iroom room : rooms) {
      if (room.getCoordinates().equals(coordinates)) {
        return room;
      }
    }
    return null;
  }
}
