package game;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the Item class.
 */
public class ItemTest {

  private Item item;

  /**
   * Sets up the test environment by creating a new item.
   */
  @Before
  public void setUp() {
    item = new Item("Sword", 10, 0);
  }

  /**
   * Tests the getName method.
   */
  @Test
  public void testGetName() {
    assertEquals("Sword", item.getName());
  }

  /**
   * Tests the getDamage method.
   */
  @Test
  public void testGetDamage() {
    assertEquals(10, item.getDamage());
  }

  /**
   * Tests the getRoomIndex method.
   */
  @Test
  public void testGetRoomIndex() {
    assertEquals(0, item.getRoomIndex());
  }
}
