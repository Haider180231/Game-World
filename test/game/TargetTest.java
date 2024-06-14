package game;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the Target class.
 */
public class TargetTest {

  private Target target;

  /**
   * Sets up the test environment by creating a new target.
   */
  @Before
  public void setUp() {
    target = new Target(50, "Doctor Lucky", new Tuple<>(0, 0));
  }

  /**
   * Tests the getHealth method.
   */
  @Test
  public void testGetHealth() {
    assertEquals(50, target.getHealth());
  }

  /**
   * Tests the getName method.
   */
  @Test
  public void testGetName() {
    assertEquals("Doctor Lucky", target.getName());
  }

  /**
   * Tests the getCoordinates method.
   */
  @Test
  public void testGetCoordinates() {
    Tuple<Integer, Integer> coords = target.getCoordinates();
    assertEquals(new Tuple<>(0, 0), coords);
  }

  /**
   * Tests the moveTarget method.
   */
  @Test
  public void testMoveTarget() {
    Tuple<Integer, Integer> newCoords = new Tuple<>(1, 1);
    target.moveTarget(newCoords);
    assertEquals(newCoords, target.getCoordinates());
  }
}
