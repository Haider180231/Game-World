package game;

import java.util.List;

/**
 * Represents the interface for the game world.
 */
public interface Igameworld {

  /**
   * Adds a room to the game world.
   *
   * @param room the room to be added
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
   * Gets a list of all rooms in the game world.
   *
   * @return a list of all rooms
   */
  List<Iroom> getRooms();

  /**
   * Gets the target in the game world.
   *
   * @return the target
   */
  Itarget getTarget();

  /**
   * Gets the pet in the game world.
   *
   * @return the pet
   */
  Ipet getPet();

  /**
   * Gets the neighboring rooms of a specified room.
   *
   * @param room the room for which to find neighbors
   * @return a list of neighboring rooms
   */
  List<Iroom> getNeighbors(Iroom room);

  /**
   * Displays information about a room.
   *
   * @param index the index of the room
   */
  void displayRoomInfo(int index);

  /**
   * Moves the target to a new location.
   */
  void moveTarget();

  /**
   * Adds a player to the game world.
   *
   * @param player the player to be added
   */
  void addPlayer(Iplayer player);

  /**
   * Gets a list of all players in the game world.
   *
   * @return a list of all players
   */
  List<Iplayer> getPlayers();

  /**
   * Moves a player to a new set of coordinates.
   *
   * @param player         the player to be moved
   * @param newCoordinates the new coordinates for the player
   */
  void movePlayer(Iplayer player, Tuple<Integer, Integer> newCoordinates);

  /**
   * Gets the number of columns in the game world grid.
   *
   * @return the number of columns
   */
  int getColumns();

  /**
   * Gets the number of rows in the game world grid.
   *
   * @return the number of rows
   */
  int getRows();

  /**
   * Gets a room by its coordinates.
   *
   * @param coordinates the coordinates of the room
   * @return the room at the specified coordinates
   */
  Iroom getRoomByCoordinates(Tuple<Integer, Integer> coordinates);

  /**
   * Moves the pet to a new set of coordinates.
   *
   * @param newCoordinates the new coordinates for the pet
   */
  void movePet(Tuple<Integer, Integer> newCoordinates);

  /**
   * Adds a pet to the game world.
   *
   * @param pet the pet to be added
   */
  void addPet(Ipet pet);

  /**
   * Advances the game world to the next turn.
   */
  void nextTurn();
}
