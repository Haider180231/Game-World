package game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the Room class.
 */
public class RoomTest {

  private Igameworld world;

  @Before
  public void setUp() throws IOException {
    world = MansionParser.parseMansion("res/mansion.txt");
  }

  /**
   * Tests the getName method.
   */
  @Test
  public void testGetName() {
    Iroom room = world.getRoomByIndex(0);
    assertEquals("Armory", room.getName());
  }

  /**
   * Tests the getCoordinates method.
   */
  @Test
  public void testGetCoordinates() {
    Iroom room = world.getRoomByIndex(0);
    Tuple<Integer, Integer> coords = room.getCoordinates();
    assertEquals(new Tuple<>(22, 19), coords);
  }

  /**
   * Tests the getEndingCoordinates method.
   */
  @Test
  public void testGetEndingCoordinates() {
    Iroom room = world.getRoomByIndex(0);
    Tuple<Integer, Integer> endingCoords = room.getEndingCoordinates();
    assertEquals(new Tuple<>(23, 26), endingCoords);
  }

  /**
   * Tests the addItem method.
   */
  @Test
  public void testAddItem() {
    Iroom room = world.getRoomByIndex(0);
    List<Iitem> items = room.getItems();
    int initialSize = items.size(); // Capture the initial size of items
    Item item = new Item("Sword", 10, 0);
    room.addItem(item);
    items = room.getItems();
    assertEquals(initialSize + 1, items.size());
    assertEquals("Sword", items.get(items.size() - 1).getName());
  }

  /**
   * Tests the getItems method.
   */
  @Test
  public void testGetItems() {
    Iroom room = world.getRoomByIndex(0);
    List<Iitem> items = room.getItems();
    assertNotNull(items);
  }

  /**
   * Tests the isNeighbor method.
   */
  @Test
  public void testIsNeighbor() {
    Iroom room1 = world.getRoomByIndex(0); // Armory
    Iroom room2 = world.getRoomByIndex(1); // Billiard Room
    Iroom room3 = world.getRoomByIndex(20); // Winter Garden (not a neighbor)

    assertTrue(room1.isNeighbor((Room) room2));
    assertFalse(room1.isNeighbor((Room) room3));
  }
}
