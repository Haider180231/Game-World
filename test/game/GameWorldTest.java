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
}
