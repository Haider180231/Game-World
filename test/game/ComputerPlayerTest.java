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
  private Iroom neighborRoom;
  private Itarget target;

  /**
   * Sets up the test environment.
   */
  @Before
  public void setUp() {
    room = new Room("Test Room", new Tuple<>(0, 0), new Tuple<>(1, 1));
    neighborRoom = new Room("Neighbor Room", new Tuple<>(1, 2), new Tuple<>(2, 3)); 
    computerPlayer = new ComputerPlayer("Computer Player", room.getCoordinates(), 5);
    List<Iroom> rooms = new ArrayList<>();
    rooms.add(room);
    rooms.add(neighborRoom);
    target = new Target(100, "Test Target", room.getCoordinates());
    world = new GameWorld(10, 10, "Test World", target, null, rooms);
    world.addPlayer(computerPlayer);
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

  @Test
  public void testLookAround() {
    String lookAroundOutput = computerPlayer.lookAround(world);
    Assert.assertTrue(lookAroundOutput.contains("Player Computer Player is in: Test Room."));
    Assert.assertTrue(lookAroundOutput.contains("Items in the room:\n - No items"));
    Assert.assertTrue(lookAroundOutput.contains("Players in the room:\n - No other players"));
    Assert.assertTrue(lookAroundOutput.contains("Target: Test Target is here."));
  }

  @Test
  public void testAttackTargetWithItem() {
    Iitem item = new Item("Test Item", 10, 0);
    computerPlayer.addItem(item);
    String result = computerPlayer.attack(target, world);
    Assert.assertEquals(90, target.getHealth());
    Assert.assertTrue(computerPlayer.getItems().isEmpty());
    Assert.assertEquals(
        "Computer Player Computer Player attacked Test Target with 10 damage.", result);
  }

  @Test
  public void testAttackTargetWithoutItem() {
    String result = computerPlayer.attack(target, world);
    Assert.assertEquals(99, target.getHealth());
    Assert.assertEquals(
        "Computer Player Computer Player attacked Test Target with 1 damage.", result);
  }

  @Test
  public void testAttackTargetSeenByOtherPlayer() {
    Iplayer otherPlayer = new Player("Other Player", room.getCoordinates(), 5);
    world.addPlayer(otherPlayer);
    Iitem item = new Item("Test Item", 10, 0);
    computerPlayer.addItem(item);
    String result = computerPlayer.attack(target, world);
    Assert.assertEquals(100, target.getHealth());
    Assert.assertEquals("Attack seen by Other Player. Attack stopped.", result);
  }

  @Test
  public void testCanSeeOtherPlayerInSameRoom() {
    Iplayer otherPlayer = new Player("Other Player", room.getCoordinates(), 5);
    world.addPlayer(otherPlayer);
    Assert.assertTrue(computerPlayer.canSee(otherPlayer, world));
  }

  @Test
  public void testCanSeeOtherPlayerInNeighborRoom() {
    Iplayer otherPlayer = new Player("Other Player", neighborRoom.getCoordinates(), 5);
    world.addPlayer(otherPlayer);
    Assert.assertTrue(computerPlayer.canSee(otherPlayer, world));
  }

  @Test
  public void testCannotSeeOtherPlayerInDifferentRoom() {
    Iplayer otherPlayer = new Player("Other Player", new Tuple<>(4, 4), 5);
    world.addPlayer(otherPlayer);
    Assert.assertFalse(computerPlayer.canSee(otherPlayer, world));
  }

  @Test
  public void testTakeTurnMovesToNeighborRoom() {
    computerPlayer.takeTurn(world);
    Tuple<Integer, Integer> coordinates = computerPlayer.getCoordinates();
    Assert.assertTrue(coordinates.equals(room.getCoordinates()) 
        || coordinates.equals(neighborRoom.getCoordinates()));
  }

  @Test
  public void testTakeTurnAttacksTarget() {
    computerPlayer.takeTurn(world);
    Assert.assertTrue(target.getHealth() < 100);
  }
  
  /**
   * Tests that the computer player uses the item with the most damage when attacking.
   */
  @Test
  public void testAttackTargetWithMaxDamageItem() {
    Iitem lowDamageItem = new Item("Low Damage Item", 2, 0);
    Iitem highDamageItem = new Item("High Damage Item", 10, 0);
    computerPlayer.addItem(lowDamageItem);
    computerPlayer.addItem(highDamageItem);

    String result = computerPlayer.attack(target, world);
    Assert.assertEquals(90, target.getHealth());
    Assert.assertFalse(computerPlayer.getItems().contains(highDamageItem));
    Assert.assertTrue(computerPlayer.getItems().contains(lowDamageItem));
    Assert.assertEquals(
        "Computer Player Computer Player attacked Test Target with 10 damage.", result);
  }

}
