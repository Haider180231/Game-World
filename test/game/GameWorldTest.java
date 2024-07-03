package game;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the GameWorld class.
 */
public class GameWorldTest {

  private Igameworld world;

  /**
   * Sets up the test environment.
   */
  @Before
  public void setUp() throws IOException {
    world = MansionParser.parseMansion("res/mansion.txt");
  }

  /**
   * Tests loading rooms and items.
   */
  @Test
  public void testLoadRoomsAndItems() {
    Assert.assertNotNull(world);
    Assert.assertEquals(21, world.getRooms().size());

    Iroom firstRoom = world.getRoomByIndex(0);
    Assert.assertEquals("Armory", firstRoom.getName());

    List<Iitem> items = firstRoom.getItems();
    Assert.assertNotNull(items);
    Assert.assertFalse(items.isEmpty());
  }

  /**
   * Tests getting neighbors of a room.
   */
  @Test
  public void testGetNeighbors() {
    Iroom firstRoom = world.getRoomByIndex(0);
    final List<Iroom> neighbors = world.getNeighbors(firstRoom);
    Assert.assertNotNull(neighbors);
    Assert.assertFalse(neighbors.isEmpty());

    Iroom neighbor = neighbors.get(0);
    Assert.assertEquals("Billiard Room", neighbor.getName());
  }

  /**
   * Tests moving the target.
   */
  @Test
  public void testMoveTarget() {
    Iroom initialRoom = world.getRoomByIndex(0);
    world.getTarget().moveTarget(initialRoom.getCoordinates());
    Assert.assertEquals(initialRoom.getCoordinates(), world.getTarget().getCoordinates());

    world.moveTarget();

    Iroom nextRoom = world.getRoomByIndex(1);
    Assert.assertEquals(nextRoom.getCoordinates(), world.getTarget().getCoordinates());
  }

  /**
   * Tests displaying room information.
   */
  @Test
  public void testDisplayRoomInfo() {
    Iroom firstRoom = world.getRoomByIndex(0);
    List<Iitem> items = firstRoom.getItems();
    final List<Iroom> neighbors = world.getNeighbors(firstRoom);

    // DisplayRoomInfo method prints the information to the console
    world.displayRoomInfo(0);

    Assert.assertNotNull(items);
    Assert.assertFalse(items.isEmpty());
    Assert.assertNotNull(neighbors);
    Assert.assertFalse(neighbors.isEmpty());
  }

  /**
   * Tests getting a room by its index.
   */
  @Test
  public void testGetRoomByIndex() {
    Iroom room = world.getRoomByIndex(0);
    Assert.assertNotNull(room);
    Assert.assertEquals("Armory", room.getName());
  }

  /**
   * Tests getting all rooms.
   */
  @Test
  public void testGetRooms() {
    List<Iroom> rooms = world.getRooms();
    Assert.assertNotNull(rooms);
    Assert.assertEquals(21, rooms.size());
  }

  /**
   * Tests getting the target.
   */
  @Test
  public void testGetTarget() {
    Itarget target = world.getTarget();
    Assert.assertNotNull(target);
    Assert.assertEquals("Doctor Lucky", target.getName());
  }

  /**
   * Tests invalid world description.
   */
  @Test
  public void testInvalidWorldDescription() {
    Assert.assertThrows(IOException.class, () -> {
      MansionParser.parseMansion("res/invalid_world.txt");
    });
  }

  /**
   * Tests invalid space description.
   */
  @Test
  public void testInvalidSpaceDescription() {
    Assert.assertThrows(IOException.class, () -> {
      MansionParser.parseMansion("res/invalid_space.txt");
    });
  }

  /**
   * Tests invalid item description.
   */
  @Test
  public void testInvalidItemDescription() {
    Assert.assertThrows(IOException.class, () -> {
      MansionParser.parseMansion("res/invalid_item.txt");
    });
  }

  /**
   * Tests no neighbors.
   */
  @Test
  public void testNoNeighbors() {
    Iroom isolatedRoom = new Room("Isolated Room", new Tuple<>(100, 100), new Tuple<>(101, 101));
    world.addRoom(isolatedRoom);
    List<Iroom> neighbors = world.getNeighbors(isolatedRoom);
    Assert.assertTrue(neighbors.isEmpty());
  }

  /**
   * Tests one neighbor.
   */
  @Test
  public void testOneNeighbor() {
    Iroom roomWithOneNeighbor = new Room("Room With One Neighbor", 
        new Tuple<>(5, 5), new Tuple<>(6, 6));
    Iroom neighborRoom = new Room("Neighbor Room", new Tuple<>(7, 5), new Tuple<>(8, 6));
    world.addRoom(roomWithOneNeighbor);
    world.addRoom(neighborRoom);
    List<Iroom> neighbors = world.getNeighbors(roomWithOneNeighbor);
    Assert.assertEquals(1, neighbors.size());
    Assert.assertEquals("Neighbor Room", neighbors.get(0).getName());
  }

  /**
   * Tests space with one item.
   */
  @Test
  public void testSpaceWithOneItem() {
    Iroom room = new Room("Room with Item", new Tuple<>(2, 2), new Tuple<>(2, 3));
    Iitem item = new Item("Sword", 10, 0);
    room.addItem(item);
    world.addRoom(room);

    List<Iitem> items = room.getItems();
    Assert.assertEquals(1, items.size());
    Assert.assertEquals("Sword", items.get(0).getName());
  }

  /**
   * Tests space with no items.
   */
  @Test
  public void testSpaceWithNoItems() {
    Iroom room = new Room("Empty Room", new Tuple<>(2, 2), new Tuple<>(2, 3));
    world.addRoom(room);

    List<Iitem> items = room.getItems();
    Assert.assertTrue(items.isEmpty());
  }

  /**
   * Tests target starting in room 0.
   */
  @Test
  public void testTargetStartsInRoom0() {
    Assert.assertEquals(new Tuple<>(0, 0), world.getTarget().getCoordinates());
  }

  /**
   * Tests moving the target from room 0 to room 1.
   */
  @Test
  public void testMoveTargetFromRoom0ToRoom1() {
    Iroom room0 = world.getRoomByIndex(0);
    Iroom room1 = world.getRoomByIndex(1);
    world.getTarget().moveTarget(room1.getCoordinates());
    Assert.assertEquals(room1.getCoordinates(), world.getTarget().getCoordinates());
  }

  /**
   * Tests moving the target from any room to the next room in the index list.
   */
  @Test
  public void testMoveTargetAnyRoom() {
    for (int i = 0; i < world.getRooms().size() - 1; i++) {
      Iroom currentRoom = world.getRoomByIndex(i);
      Iroom nextRoom = world.getRoomByIndex(i + 1);
      world.getTarget().moveTarget(nextRoom.getCoordinates());
      Assert.assertEquals(nextRoom.getCoordinates(), world.getTarget().getCoordinates());
    }
  }

  /**
   * Tests moving the target from the last room to room 0.
   */
  @Test
  public void testMoveTargetEndToStart() {
    Iroom lastRoom = world.getRoomByIndex(world.getRooms().size() - 1);
    Iroom firstRoom = world.getRoomByIndex(0);
    world.getTarget().moveTarget(lastRoom.getCoordinates());
    Assert.assertEquals(lastRoom.getCoordinates(), world.getTarget().getCoordinates());
    world.getTarget().moveTarget(firstRoom.getCoordinates());
    Assert.assertEquals(firstRoom.getCoordinates(), world.getTarget().getCoordinates());
  }

  /**
   * Tests adding a player to the game world.
   */
  @Test
  public void testAddPlayer() {
    Iroom initialRoom = world.getRoomByIndex(0);
    Iplayer player = new Player("Test Player", initialRoom.getCoordinates(), 5);
    world.addPlayer(player);
    List<Iplayer> players = world.getPlayers();
    Assert.assertEquals(1, players.size());
    Assert.assertEquals("Test Player", players.get(0).getName());
  }

  /**
   * Tests moving a player within the game world.
   */
  @Test
  public void testMovePlayer() {
    Iroom initialRoom = world.getRoomByIndex(0);
    Iroom targetRoom = world.getRoomByIndex(1);
    Iplayer player = new Player("Test Player", initialRoom.getCoordinates(), 5);
    world.addPlayer(player);
    world.movePlayer(player, targetRoom.getCoordinates());
    Assert.assertEquals(targetRoom.getCoordinates(), player.getCoordinates());
  }

  /**
   * Tests computer player taking a turn.
   */
  @Test
  public void testComputerPlayerTakeTurn() {
    Iroom initialRoom = world.getRoomByIndex(0);
    Iplayer computerPlayer = new ComputerPlayer("Computer", initialRoom.getCoordinates(), 5);
    world.addPlayer(computerPlayer);
    ((ComputerPlayer) computerPlayer).takeTurn(world);
  }

  /**
   * Tests looking around from a player's perspective.
   */
  @Test
  public void testLookAround() {
    Iroom initialRoom = world.getRoomByIndex(0);
    Iplayer player = new Player("Test Player", initialRoom.getCoordinates(), 5);
    world.addPlayer(player);
    List<Iroom> neighbors = world.getNeighbors(initialRoom);
    Assert.assertNotNull(neighbors);
    Assert.assertFalse(neighbors.isEmpty());
  }

  /**
   * Tests picking up an item in a room.
   */
  @Test
  public void testPickItem() {
    Iroom initialRoom = world.getRoomByIndex(0);
    Iitem item = new Item("Sword", 10, 0);
    initialRoom.addItem(item);
    Iplayer player = new Player("Test Player", initialRoom.getCoordinates(), 5);
    world.addPlayer(player);
    player.addItem(item);
    Assert.assertEquals(1, player.getItems().size());
    Assert.assertEquals("Sword", player.getItems().get(0).getName());
  }

  /**
   * Tests displaying the game map.
   */
  @Test
  public void testDisplayMap() throws IOException {
    GameView view = new GameView();
    view.displayMap(world, "res/test_map.png");
    File file = new File("res/test_map.png");
    Assert.assertTrue(file.exists());
  }
  
  /**
   * Tests moving a player to a neighboring room.
   */
  @Test
  public void testMoveToNeighbor() {
    Iroom initialRoom = world.getRoomByIndex(0); // Armory
    Iroom neighborRoom = world.getNeighbors(initialRoom).get(0); // Billiard Room
    Iplayer player = new Player("Test Player", initialRoom.getCoordinates(), 5);
    world.addPlayer(player);
    world.movePlayer(player, neighborRoom.getCoordinates());
    Assert.assertEquals(neighborRoom.getCoordinates(), player.getCoordinates());
  }
  
  /**
   * Tests that the player cannot move to a non-neighboring room.
   */
  @Test
  public void testInvalidMove() {
    Iroom initialRoom = world.getRoomByIndex(0); 
    Iroom nonNeighborRoom = world.getRoomByIndex(20);
    Iplayer player = new Player("Test Player", initialRoom.getCoordinates(), 5);
    world.addPlayer(player);
    world.movePlayer(player, nonNeighborRoom.getCoordinates());
    Assert.assertNotEquals(nonNeighborRoom.getCoordinates(), player.getCoordinates());
    Assert.assertEquals(initialRoom.getCoordinates(), player.getCoordinates());
  }
  
  /**
   * Tests that verifies the player's behavior when trying to pick up an item that isn't available.
   */
  @Test
  public void testPickItemNotAvailable() {
    Iroom initialRoom = world.getRoomByIndex(0); 
    Iplayer player = new Player("Test Player", initialRoom.getCoordinates(), 5);
    world.addPlayer(player);

    int initialItemCount = player.getItems().size();
    if (initialRoom.getItems().isEmpty()) {
      player.addItem(new Item("Non-existent Item", 0, 0)); 
    }
    int finalItemCount = player.getItems().size();

    Assert.assertEquals(initialItemCount, finalItemCount);
  }
  
  /**
   * Tests that the player cannot pick up an item when they are already at their maximum capacity.
   */
  @Test
  public void testPickItemBeyondMaximum() {
      Iroom initialRoom = world.getRoomByIndex(0); 
      Iplayer player = new Player("Test Player", initialRoom.getCoordinates(), 1); 
      Iitem item1 = new Item("Test Item 1", 10, 0);
      Iitem item2 = new Item("Test Item 2", 20, 0);
      player.addItem(item1);
      initialRoom.addItem(item2);
      world.addPlayer(player);

      int initialItemCount = player.getItems().size();
      player.addItem(item2); 
      int finalItemCount = player.getItems().size();

      Assert.assertEquals(initialItemCount, finalItemCount);
  }
  
  
}
