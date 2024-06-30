package game;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



/**
 * Unit tests for the Player class.
 */
public class PlayerTest {

  private Iplayer player;
  private Iroom room;

  /**
   * Sets up the test environment.
   */
  @Before
  public void setUp() {
    room = new Room("Test Room", new Tuple<>(0, 0), new Tuple<>(1, 1));
    player = new Player("Test Player", room.getCoordinates(), 5);
  }

  /**
   * Tests the getName method.
   */
  @Test
  public void testGetName() {
    Assert.assertEquals("Test Player", player.getName());
  }

  /**
   * Tests the getCoordinates method.
   */
  @Test
  public void testGetCoordinates() {
    Assert.assertEquals(room.getCoordinates(), player.getCoordinates());
  }

  /**
   * Tests the move method.
   */
  @Test
  public void testMove() {
    Tuple<Integer, Integer> newCoordinates = new Tuple<>(2, 2);
    player.move(newCoordinates);
    Assert.assertEquals(newCoordinates, player.getCoordinates());
  }

  /**
   * Tests the addItem method.
   */
  @Test
  public void testAddItem() {
    Iitem item = new Item("Test Item", 10, 0);
    player.addItem(item);
    List<Iitem> items = player.getItems();
    Assert.assertEquals(1, items.size());
    Assert.assertEquals("Test Item", items.get(0).getName());
  }

  /**
   * Tests the getItems method.
   */
  @Test
  public void testGetItems() {
    Iitem item1 = new Item("Test Item 1", 10, 0);
    Iitem item2 = new Item("Test Item 2", 20, 0);
    player.addItem(item1);
    player.addItem(item2);
    List<Iitem> items = player.getItems();
    Assert.assertEquals(2, items.size());
    Assert.assertTrue(items.contains(item1));
    Assert.assertTrue(items.contains(item2));
  }

  /**
   * Tests the getMaxItems method.
   */
  @Test
  public void testGetMaxItems() {
    Assert.assertEquals(5, player.getMaxItems());
  }
}
