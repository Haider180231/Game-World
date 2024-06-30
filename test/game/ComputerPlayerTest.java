package game;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



/**
 * Unit tests for the ComputerPlayer class.
 */
public class ComputerPlayerTest {

  private Igameworld world;
  private Iplayer computerPlayer;
  private Iroom room;

  /**
   * Sets up the test environment.
   */
  @Before
  public void setUp() {
    room = new Room("Test Room", new Tuple<>(0, 0), new Tuple<>(1, 1));
    computerPlayer = new ComputerPlayer("Computer Player", room.getCoordinates(), 5);
    List<Iroom> rooms = new ArrayList<>();
    rooms.add(room);
    rooms.add(new Room("Neighbor Room", new Tuple<>(2, 2), new Tuple<>(3, 3)));
    Itarget target = new Target(100, "Test Target", new Tuple<>(0, 0));
    world = new GameWorld(10, 10, "Test World", target, rooms);
  }

  @Test
  public void testGetName() {
    Assert.assertEquals("Computer Player", computerPlayer.getName());
  }

  @Test
  public void testGetCoordinates() {
    Assert.assertEquals(room.getCoordinates(), computerPlayer.getCoordinates());
  }

  @Test
  public void testMove() {
    Tuple<Integer, Integer> newCoordinates = new Tuple<>(2, 2);
    computerPlayer.move(newCoordinates);
    Assert.assertEquals(newCoordinates, computerPlayer.getCoordinates());
  }

  @Test
  public void testAddItem() {
    Iitem item = new Item("Test Item", 10, 0);
    computerPlayer.addItem(item);
    List<Iitem> items = computerPlayer.getItems();
    Assert.assertEquals(1, items.size());
    Assert.assertEquals("Test Item", items.get(0).getName());
  }

  @Test
  public void testGetItems() {
    Iitem item1 = new Item("Test Item 1", 10, 0);
    Iitem item2 = new Item("Test Item 2", 20, 0);
    computerPlayer.addItem(item1);
    computerPlayer.addItem(item2);
    List<Iitem> items = computerPlayer.getItems();
    Assert.assertEquals(2, items.size());
    Assert.assertTrue(items.contains(item1));
    Assert.assertTrue(items.contains(item2));
  }

  @Test
  public void testGetMaxItems() {
    Assert.assertEquals(5, computerPlayer.getMaxItems());
  }

}
