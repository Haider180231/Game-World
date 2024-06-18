package game;

import java.io.IOException;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the GameWorld class.
 */
public class GameWorldTest {

  private GameWorld world;

  /**
   * Sets up the test environment by parsing the mansion file.
   *
   * @throws IOException if an error occurs while reading the file
   */
  @Before
  public void setUp() throws IOException {
    String filePath = "res/mansion.txt";
    world = MansionParser.parseMansion(filePath);
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
    world.moveTarget();

    Iroom lastRoom = world.getRoomByIndex(world.getRooms().size() - 1);
    Assert.assertEquals(lastRoom.getCoordinates(), world.getTarget().getCoordinates());
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
}
