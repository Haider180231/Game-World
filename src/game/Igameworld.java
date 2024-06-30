package game;

import java.io.IOException;
import java.util.List;

/**
 * Represents the interface for the game world.
 */
public interface Igameworld {

  /**
   * Adds a room to the game world.
   *
   * @param room the room to add
   */
  void addRoom(Iroom room);

  /**
   * Gets a room by its index.
   *
   * @param index the index of the room
   * @return the room at the specified index
   */
  Iroom getRoomByIndex(int index);

  /**
   * Gets the list of all rooms in the game world.
   *
   * @return the list of rooms
   */
  List<Iroom> getRooms();

  /**
   * Gets the target character in the game world.
   *
   * @return the target character
   */
  Itarget getTarget();

  /**
   * Gets the neighboring rooms of a specified room.
   *
   * @param room the room for which to get the neighbors
   * @return the list of neighboring rooms
   */
  List<Iroom> getNeighbors(Iroom room);

  /**
   * Displays information about a specified room.
   *
   * @param index the index of the room to display information about
   */
  void displayRoomInfo(int index);

  /**
   * Moves the target character to the next room.
   */
  void moveTarget();

  /**
   * Adds a player to the game world.
   *
   * @param player the player to add
   */
  void addPlayer(Iplayer player);

  /**
   * Gets the list of all players in the game world.
   *
   * @return the list of players
   */
  List<Iplayer> getPlayers();

  /**
   * Moves a player to new coordinates.
   *
   * @param player the player to move
   * @param newCoordinates the new coordinates to move the player to
   */
  void movePlayer(Iplayer player, Tuple<Integer, Integer> newCoordinates);

  /**
   * Gets the number of columns in the game world.
   *
   * @return the number of columns
   */
  int getColumns();

  /**
   * Gets the number of rows in the game world.
   *
   * @return the number of rows
   */
  int getRows();

  /**
   * Gets a room by its coordinates.
   *
   * @param coordinates the coordinates of the room
   * @return the room at the specified coordinates, or null if no room is found
   */
  Iroom getRoomByCoordinates(Tuple<Integer, Integer> coordinates);
}
