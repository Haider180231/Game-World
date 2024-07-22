package game;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Unit tests for the Pet class.
 */
public class PetTest {

  private Ipet pet;
  private Igameworld world;
  private Tuple<Integer, Integer> initialCoordinates;

  /**
   * Sets up the test environment.
   */
  @Before
  public void setUp() {
    // Initialize game world from mansion.txt
    try {
      world = MansionParser.parseMansion("res/mansion.txt");
      pet = world.getPet();

      // Set initial coordinates for the pet to the coordinates of room 0
      Iroom initialRoom = world.getRoomByIndex(0);
      initialCoordinates = initialRoom.getCoordinates();
      pet.move(initialCoordinates);
    } catch (IOException e) {
      e.printStackTrace();
      Assert.fail("Failed to parse mansion.txt");
    }
  }

  /**
   * Tests the getName method.
   */
  @Test
  public void testGetName() {
    Assert.assertEquals("Fortune the Cat", pet.getName());
  }

  /**
   * Tests the getCoordinates method.
   */
  @Test
  public void testGetCoordinates() {
    Assert.assertEquals(initialCoordinates, pet.getCoordinates());
  }

  /**
   * Tests the move method.
   */
  @Test
  public void testMove() {
    Tuple<Integer, Integer> newCoordinates = new Tuple<>(2, 2);
    pet.move(newCoordinates);
    Assert.assertEquals(newCoordinates, pet.getCoordinates());
  }

  /**
   * Tests the pet's DFS move functionality.
   */
  @Test
  public void testPetMovesDfs() {
    // Move the pet in the world using DFS
    pet.moveDfs(world);
    Assert.assertNotEquals(initialCoordinates, pet.getCoordinates());

    // Ensure the pet visits all rooms in DFS order
    Tuple<Integer, Integer> firstMove = pet.getCoordinates();
    pet.moveDfs(world);
    Assert.assertNotEquals(firstMove, pet.getCoordinates());
  }

  /**
   * Tests that DFS starts over when the pet is moved by a player.
   */
  @Test
  public void testDfsStartsOverWhenMoved() {
    // Move the pet in the world using DFS
    pet.moveDfs(world);
    Tuple<Integer, Integer> firstMove = pet.getCoordinates();
    Assert.assertNotEquals(initialCoordinates, firstMove);

    // Move the pet manually
    Tuple<Integer, Integer> newCoordinates = new Tuple<>(16, 21);
    pet.move(newCoordinates);
    Assert.assertEquals(newCoordinates, pet.getCoordinates());

    // Move the pet again using DFS
    pet.moveDfs(world);
    Assert.assertNotEquals(firstMove, pet.getCoordinates());
  }
}
