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

  /**
   * Tests the lookAround method.
   */
  @Test
  public void testLookAround() {
    Igameworld world = new GameWorld(10, 10, "Test World", 
        new Target(100, "Test Target", new Tuple<>(0, 0)), null, new ArrayList<>());
    world.addRoom(room);
    String output = player.lookAround(world);
    Assert.assertTrue(output.contains("Player Test Player is in: Test Room"));
  }

  /**
   * Tests the attack method when no other players can see.
   */
  @Test
  public void testAttackUnseen() {
    Igameworld world = new GameWorld(10, 10, "Test World", 
        new Target(100, "Test Target", new Tuple<>(0, 0)), null, new ArrayList<>());
    world.addRoom(room);
    player.addItem(new Item("Test Item", 10, 0));
    String result = player.attack(world.getTarget(), world);
    Assert.assertTrue(result.contains("Player Test Player attacked Test Target with 10 damage."));
  }

  /**
   * Tests the attack method when other players can see.
   */
  @Test
  public void testAttackSeen() {
    Igameworld world = new GameWorld(10, 10, "Test World", 
        new Target(100, "Test Target", new Tuple<>(0, 0)), null, new ArrayList<>());
    world.addRoom(room);
    Iplayer otherPlayer = new Player("Other Player", room.getCoordinates(), 5);
    world.addPlayer(otherPlayer);
    world.addPlayer(player);
    String result = player.attack(world.getTarget(), world);
    Assert.assertTrue(result.contains("Attack seen by Other Player. Attack stopped."));
  }

  /**
   * Tests the canSee method when the other player is in the same room.
   */
  @Test
  public void testCanSeeInSameRoom() {
    Iplayer otherPlayer = new Player("Other Player", room.getCoordinates(), 5);
    Igameworld world = new GameWorld(10, 10, "Test World", 
        new Target(100, "Test Target", new Tuple<>(0, 0)), null, new ArrayList<>());
    world.addRoom(room);
    Assert.assertTrue(player.canSee(otherPlayer, world));
  }

  /**
   * Tests the canSee method when the other player is in a neighboring room.
   */
  @Test
  public void testCanSeeInNeighborRoom() {
    Iroom neighborRoom = new Room("Neighbor Room", new Tuple<>(1, 2), new Tuple<>(2, 3));
    Iplayer otherPlayer = new Player("Other Player", neighborRoom.getCoordinates(), 5);
    Igameworld world = new GameWorld(10, 10, "Test World", 
        new Target(100, "Test Target", new Tuple<>(0, 0)), null, new ArrayList<>());
    world.addRoom(room);
    world.addRoom(neighborRoom);
    Assert.assertTrue(player.canSee(otherPlayer, world));
  }

  /**
   * Tests the canSee method when the other player is in a different, non-neighboring room.
   */
  @Test
  public void testCannotSeeInDifferentRoom() {
    Iroom differentRoom = new Room("Different Room", new Tuple<>(3, 3), new Tuple<>(4, 4));
    Iplayer otherPlayer = new Player("Other Player", differentRoom.getCoordinates(), 5);
    Igameworld world = new GameWorld(10, 10, "Test World", 
        new Target(100, "Test Target", new Tuple<>(0, 0)), null, new ArrayList<>());
    world.addRoom(room);
    world.addRoom(differentRoom);
    Assert.assertFalse(player.canSee(otherPlayer, world));
  }

  /**
   * Tests the canSee method when players are in the same room with the pet.
   */
  @Test
  public void testCanSeeInSameRoomWithPet() {
    Ipet pet = new Pet("Test Pet", room.getCoordinates());
    Iplayer otherPlayer = new Player("Other Player", room.getCoordinates(), 5);
    Igameworld world = new GameWorld(10, 10, "Test World", 
        new Target(100, "Test Target", new Tuple<>(0, 0)), pet, new ArrayList<>());
    world.addRoom(room);
    world.addPlayer(player);
    world.addPlayer(otherPlayer);
    world.addPet(pet);
    Assert.assertTrue(player.canSee(otherPlayer, world));
  }

  /**
   * Tests the canSee method when players are in a neighboring room with the pet.
   */
  @Test
  public void testCanSeeInNeighborRoomWithPet() {
    Iroom neighborRoom = new Room("Neighbor Room", new Tuple<>(1, 2), new Tuple<>(2, 3));
    Iplayer otherPlayer = new Player("Other Player", neighborRoom.getCoordinates(), 5);
    Ipet pet = new Pet("Test Pet", neighborRoom.getCoordinates());
    Igameworld world = new GameWorld(10, 10, "Test World", 
        new Target(100, "Test Target", new Tuple<>(0, 0)), pet, new ArrayList<>());
    world.addRoom(room);
    world.addRoom(neighborRoom);
    world.addPlayer(player);
    world.addPlayer(otherPlayer);
    world.addPet(pet);
    Assert.assertFalse(player.canSee(otherPlayer, world));
  }

  /**
   * Tests the attack method when no items are 
   * used and the default damage is applied (poking in the eye).
   */
  @Test
  public void testAttackByPokingInEye() {
    Igameworld world = new GameWorld(10, 10, "Test World", 
        new Target(100, "Test Target", new Tuple<>(0, 0)), null, new ArrayList<>());
    world.addRoom(room);
    world.addPlayer(player);
    String result = player.attack(world.getTarget(), world);
    Assert.assertTrue(result.contains("Player Test Player attacked Test Target with 1 damage."));
  }

  /**
   * Tests the attack method by killing the target using the highest damage item.
   */
  @Test
  public void testKillTargetWithItem() {
    Igameworld world = new GameWorld(10, 10, "Test World", 
        new Target(10, "Test Target", new Tuple<>(0, 0)), null, new ArrayList<>());
    world.addRoom(room);
    Iitem highDamageItem = new Item("High Damage Item", 10, 0);
    player.addItem(highDamageItem);
    world.addPlayer(player);
    String result = player.attack(world.getTarget(), world);
    Assert.assertTrue(result.contains("Player Test Player attacked Test Target with 10 damage."));
    Assert.assertTrue(world.getTarget().getHealth() <= 0);
  }

  /**
   * Tests the attack method by killing the target using default damage (poking in the eye).
   */
  @Test
  public void testKillTargetByPokingInEye() {
    Igameworld world = new GameWorld(10, 10, "Test World", 
        new Target(1, "Test Target", new Tuple<>(0, 0)), null, new ArrayList<>());
    world.addRoom(room);
    world.addPlayer(player);
    String result = player.attack(world.getTarget(), world);
    Assert.assertTrue(result.contains("Player Test Player attacked Test Target with 1 damage."));
    Assert.assertTrue(world.getTarget().getHealth() <= 0);
  }

  /**
   * Tests that an attempt cannot be made using an item that is not in the player's inventory.
   */
  @Test
  public void testCannotAttackWithItemNotInInventory() {
    Igameworld world = new GameWorld(10, 10, "Test World", 
        new Target(100, "Test Target", new Tuple<>(0, 0)), null, new ArrayList<>());
    world.addRoom(room);
    Iitem notOwnedItem = new Item("Not Owned Item", 10, 0);
    world.addPlayer(player);
    // Ensure the player does not have the item
    Assert.assertFalse(player.getItems().contains(notOwnedItem));
    String result = player.attack(world.getTarget(), world);
    // Check that the default damage is applied
    Assert.assertTrue(result.contains("Player Test Player attacked Test Target with 1 damage."));
    Assert.assertEquals(99, world.getTarget().getHealth());
  }

  /**
   * Tests that an attempt made using an item takes the item out of play.
   */
  @Test
  public void testAttackWithItemRemovesItemFromPlay() {
    Igameworld world = new GameWorld(10, 10, "Test World", 
        new Target(100, "Test Target", new Tuple<>(0, 0)), null, new ArrayList<>());
    world.addRoom(room);
    Iitem item = new Item("Test Item", 10, 0);
    player.addItem(item);
    world.addPlayer(player);

    String result = player.attack(world.getTarget(), world);

    Assert.assertTrue(result.contains("Player Test Player attacked Test Target with 10 damage."));
    Assert.assertEquals(90, world.getTarget().getHealth());
    Assert.assertTrue(player.getItems().isEmpty());
  }

  /**
   * Tests that an attempt is unsuccessful 
   * if the target character is not in the same space as the player.
   */
  @Test
  public void testAttackFailsIfTargetNotInSameSpace() {
    Iroom differentRoom = new Room("Different Room", new Tuple<>(3, 3), new Tuple<>(4, 4));
    Igameworld world = new GameWorld(10, 10, "Test World", 
        new Target(100, "Test Target", differentRoom.getCoordinates()), null, new ArrayList<>());
    world.addRoom(room);
    world.addRoom(differentRoom);
    world.addPlayer(player);

    // Attempt to attack the target
    String result = player.attack(world.getTarget(), world);

    // Verify the attack does not occur
    Assert.assertTrue(result.contains("The target is not in the same room. Attack not possible."));
    Assert.assertEquals(100, world.getTarget().getHealth());
  }

  /**
   * Tests that an attempt is unsuccessful 
   * if there is another player in a neighboring space when the pet is not present.
   */
  @Test
  public void testAttackFailsIfAnotherPlayerInNeighboringSpace() {
    Iroom neighborRoom = new Room("Neighbor Room", new Tuple<>(1, 2), new Tuple<>(2, 3));
    Iplayer otherPlayer = new Player("Other Player", neighborRoom.getCoordinates(), 5);
    Igameworld world = new GameWorld(10, 10, "Test World", 
        new Target(100, "Test Target", room.getCoordinates()), null, new ArrayList<>());
    world.addRoom(room);
    world.addRoom(neighborRoom);
    world.addPlayer(player);
    world.addPlayer(otherPlayer);

    // Attempt to attack the target
    String result = player.attack(world.getTarget(), world);

    // Verify the attack does not occur
    Assert.assertTrue(result.contains("Attack seen by Other Player. Attack stopped."));
    Assert.assertEquals(100, world.getTarget().getHealth()); 
  }

  /**
   * Tests that an attempt is unsuccessful 
   * if there is another player in an neighboring space that they share with the pet.
   */
  @Test
  public void testAttackFailsIfAnotherPlayerInNeighboringSpaceWithPet() {
    Iroom neighborRoom = new Room("Neighbor Room", new Tuple<>(1, 2), new Tuple<>(2, 3));
    Iplayer otherPlayer = new Player("Other Player", neighborRoom.getCoordinates(), 5);
    Ipet pet = new Pet("Test Pet", neighborRoom.getCoordinates());
    Igameworld world = new GameWorld(10, 10, "Test World", 
        new Target(100, "Test Target", room.getCoordinates()), pet, new ArrayList<>());
    world.addRoom(room);
    world.addRoom(neighborRoom);
    world.addPlayer(player);
    world.addPlayer(otherPlayer);
    world.addPet(pet);

    // Attempt to attack the target
    String result = player.attack(world.getTarget(), world);

    // Verify the attack does not occur
    Assert.assertTrue(result.contains("Attack seen by Other Player. Attack stopped."));
    Assert.assertEquals(100, world.getTarget().getHealth()); 
  }

  /**
   * Tests that an attempt is successful 
   * if the player is in the same space as the target character and the pet,
   * even though there are players in a neighbor space.
   */
  @Test
  public void testAttackSuccessWithTargetAndPetInSameRoomAndPlayersInNeighboringSpace() {
    Iroom neighboringRoom = new Room("Neighbor Room", new Tuple<>(1, 2), new Tuple<>(2, 3));
    Igameworld world = new GameWorld(10, 10, "Test World", 
        new Target(100, "Test Target", new Tuple<>(0, 0)), null, new ArrayList<>());
    world.addRoom(room);
    world.addRoom(neighboringRoom);

    Ipet pet = new Pet("Test Pet", room.getCoordinates());
    world.addPet(pet);
    Iplayer neighboringPlayer = new Player(
        "Neighboring Player", neighboringRoom.getCoordinates(), 5);
    world.addPlayer(neighboringPlayer);
    world.addPlayer(player);

    String result = player.attack(world.getTarget(), world);
    Assert.assertTrue(result.contains("Player Test Player attacked Test Target with 1 damage."));
    Assert.assertEquals(99, world.getTarget().getHealth());
  }

  /**
   * Tests that the health of the target player is reduced whenever an attempt is successful.
   */
  @Test
  public void testTargetHealthReducedOnSuccessfulAttack() {
    Igameworld world = new GameWorld(10, 10, "Test World", 
        new Target(100, "Test Target", new Tuple<>(0, 0)), null, new ArrayList<>());
    world.addRoom(room);
    world.addPlayer(player);

    int initialHealth = world.getTarget().getHealth();
    player.attack(world.getTarget(), world);

    Assert.assertTrue(world.getTarget().getHealth() < initialHealth);
  }
}
