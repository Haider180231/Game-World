package game;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the GameController.
 */
public class GameControllerTest {
  private Igameworld world;
  private GameView view;
  private GameController controller;
  private ByteArrayOutputStream outContent;
  private ByteArrayInputStream inContent;

  /**
   * Sets up the test environment.
   */
  @Before
  public void setUp() {
    try {
      world = MansionParser.parseMansion("res/mansion.txt");

      Iroom room0 = world.getRoomByIndex(0);
      Tuple<Integer, Integer> room0Coordinates = room0.getCoordinates();

      Itarget staticTarget = new Target(1, "Static Target", room0Coordinates);
      ((GameWorld) world).setTarget(staticTarget);

      ((GameWorld) world).setTargetShouldMove(false);

    } catch (IOException e) {
      e.printStackTrace();
    }
    view = new GameView();
    outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
  }

  /**
   * Tears down the test environment.
   */
  @After
  public void tearDown() {
    System.setOut(null);
    System.setIn(System.in);  
    if (inContent != null) {
      inContent.reset();
    }
    outContent.reset();
  }

  /**
   * Tests the addition of a player to the game.
   */
  @Test
  public void testAddPlayer() {
    String input = "yes\nhuman\nTestPlayer\n0\nno\n0\nexit\n";
    inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    controller = new GameController(world, view, 10, "res/test_log.txt");
    controller.startGame();

    List<Iplayer> players = world.getPlayers();
    Assert.assertEquals(1, players.size());
    Assert.assertEquals("TestPlayer", players.get(0).getName());
  }

  /**
   * Tests the look around functionality.
   */
  @Test
  public void testLookAround() {
    String input = "yes\nhuman\nTestPlayer\n0\nno\n0\nlook around\nexit\n";
    inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    controller = new GameController(world, view, 10, "res/test_log.txt");
    controller.startGame();
    String output = outContent.toString();

    // Verify items in the room
    Assert.assertTrue(output.contains("Player TestPlayer is in:"));
    Assert.assertTrue(output.contains("Items in the room:"));
    Assert.assertTrue(output.contains("Visible rooms:"));
    Assert.assertTrue(output.contains("Players in the room:"));
    Assert.assertTrue(output.contains("Pet:") || output.contains("Target:"));
  }

  /**
   * Tests the display map functionality.
   */
  @Test
  public void testDisplayMap() {
    String input = "yes\nhuman\nTestPlayer\n0\nno\n0\ndisplay map\nexit\n";
    inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    controller = new GameController(world, view, 10, "res/test_log.txt");
    controller.startGame();
    String output = outContent.toString();

    Assert.assertTrue(output.contains("Map displayed and saved to"));
  }

  /**
   * Tests the display player functionality.
   */
  @Test
  public void testDisplayPlayer() {
    String input = "yes\nhuman\nTestPlayer\n0\nno\n0\ndisplay player\nexit\n";
    inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    controller = new GameController(world, view, 10, "res/test_log.txt");
    controller.startGame();
    String output = outContent.toString();

    Assert.assertTrue(output.contains("You are at coordinates:"));
    Assert.assertTrue(output.contains("Player Name: TestPlayer"));
    Assert.assertTrue(output.contains("Coordinates:"));
    Assert.assertTrue(output.contains("Items:"));
  }

  /**
   * Tests that the game alternates turns between players in the order they were added.
   */
  @Test
  public void testPlayerOrder() {
    String input = "yes\nhuman\nPlayer1\n0\nyes\nhuman\nPlayer2\n1\nno\n0\nmove\n0\nmove\n0\nexit\n";
    inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    controller = new GameController(world, view, 10, "res/test_log.txt");
    controller.startGame();
    String output = outContent.toString();

    // Verify the turn order
    Assert.assertTrue(output.contains("It's Player1's turn."));
    Assert.assertTrue(output.contains("It's Player2's turn."));
  }

  /**
   * Tests that the game ends when the maximum number of turns has been reached.
   */
  @Test
  public void testGameEnd() {
    String input = "yes\nhuman\nTestPlayer\n0\nno\n0\n";
    for (int i = 0; i < 10; i++) {
      input += "move\n0\n";
    }
    input += "exit\n";
    inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    controller = new GameController(world, view, 10, "res/test_log.txt");
    controller.startGame();
    String output = outContent.toString();

    // Verify the game end
    Assert.assertTrue(output.contains("Game over! Maximum number of turns reached."));
  }

  /**
   * Tests moving a player to a neighboring room.
   */
  @Test
  public void testMovePlayer() {
    String input = "yes\nhuman\nTestPlayer\n0\nno\n0\nmove\n0\nexit\n";
    inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    controller = new GameController(world, view, 10, "res/test_log.txt");
    controller.startGame();
    String output = outContent.toString();

    Assert.assertTrue(output.contains("Action completed"));
  }

  /**
   * Tests that a player can pick up an item in the current room.
   */
  @Test
  public void testPickItem() {
    String input = "yes\nhuman\nTestPlayer\n0\nno\n0\npick item\nexit\n";
    inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    Iroom initialRoom = world.getRoomByIndex(0);
    Iitem item = new Item("Test Item", 10, 0);
    initialRoom.addItem(item);
    Iplayer player = new Player("Test Player", initialRoom.getCoordinates(), 5);
    world.addPlayer(player);

    controller = new GameController(world, view, 10, "res/test_log.txt");
    controller.startGame();
    String output = outContent.toString();

    Assert.assertTrue(output.contains("Action completed"));
  }

  /**
   * Tests displaying room information by room index.
   */
  @Test
  public void testDisplayRoom() {
    String input = "yes\nhuman\nTestPlayer\n0\nno\n0\ndisplay room\n0\nexit\n";
    inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    controller = new GameController(world, view, 10, "res/test_log.txt");
    controller.startGame();
    String output = outContent.toString();

    Iroom room = world.getRoomByIndex(0);
    Assert.assertTrue(output.contains("Room Name: " + room.getName()));
    Assert.assertTrue(output.contains("Items in the Room:"));
    Assert.assertTrue(output.contains("Visible Rooms:"));
    Assert.assertTrue(output.contains("Players in the Room:"));
  }

  /**
   * Tests the addition of a computer player to the game.
   */
  @Test
  public void testAddComputer() {
    String input = "yes\ncomputer\nComputerPlayer\n0\nno\n0\nexit\n";
    inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    controller = new GameController(world, view, 10, "res/test_log.txt");
    controller.startGame();
    List<Iplayer> players = world.getPlayers();
    Assert.assertEquals(1, players.size());
    Assert.assertEquals("ComputerPlayer", players.get(0).getName());
  }

  /**
   * Tests moving the pet to a specified room.
   */
  @Test
  public void testMovePet() {
    String input = "yes\nhuman\nTestPlayer\n0\nno\n0\nmove pet\n1\nexit\n";
    inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    controller = new GameController(world, view, 10, "res/test_log.txt");
    controller.startGame();
    String output = outContent.toString();

    // Verify the pet has been moved to room 1
    Iroom petRoom = world.getRoomByIndex(1);
    Assert.assertTrue(output.contains("Pet moved to room"));
  }

  /**
   * Tests the look around functionality with other players in the same room.
   */
  @Test
  public void testLookAroundWithPlayersInSameRoom() {
    String input = "yes\nhuman\nTestPlayer1\n0\nyes"
        + "\nhuman\nTestPlayer2"
        + "\n0\nno\n0\nlook around\nexit\n";
    inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    controller = new GameController(world, view, 10, "res/test_log.txt");
    controller.startGame();
    String output = outContent.toString();

    // Verify that both players are listed in the room
    Assert.assertTrue(output.contains("Players in the room:"));
    Assert.assertTrue(output.contains("TestPlayer1"));
    Assert.assertTrue(output.contains("TestPlayer2"));
  }

  /**
   * Tests the look around functionality with players in neighboring spaces.
   */
  @Test
  public void testLookAroundWithPlayersInNeighboringSpaces() {
    String input = "yes\nhuman\nTestPlayer1\n0\nyes\nhuman"
        + "\nTestPlayer2\n1\nno\n0\nlook around\nexit\n";
    inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    controller = new GameController(world, view, 10, "res/test_log.txt");
    controller.startGame();
    String output = outContent.toString();

    // Verify that there are players in neighboring spaces
    Assert.assertTrue(output.contains("Players in the room:"));
    Assert.assertTrue(output.contains("TestPlayer1"));
    Assert.assertTrue(output.contains("Visible rooms:"));
    Assert.assertTrue(output.contains("TestPlayer2"));
  }

  /**
   * Tests that verifies the looking around command works correctly 
   * when there are no items in the same space.
   */
  @Test
  public void testLookAroundWithNoItemsInSameSpace() {
    String input = "yes\nhuman\nTestPlayer\n10\nno\n0\nlook around\nexit\n";
    inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    controller = new GameController(world, view, 10, "res/test_log.txt");
    controller.startGame();
    String output = outContent.toString();

    // Verify no items in the current room
    Assert.assertTrue(output.contains("Items in the room:"));
    Assert.assertTrue(output.contains(" - No items"));
  }

  /**
   * Tests that verifies the looking around command works correctly 
   * when there are no items in a neighboring space.
   */
  @Test
  public void testLookAroundWithNoItemsInNeighboringSpace() {
    String input = "yes\nhuman\nTestPlayer\n0\nno\n0\nlook around\nexit\n";
    inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    // Clear items in neighboring rooms
    Iroom firstRoom = world.getRoomByIndex(0);
    for (Iroom neighbor : world.getNeighbors(firstRoom)) {
      neighbor.getItems().clear();
    }

    controller = new GameController(world, view, 10, "res/test_log.txt");
    controller.startGame();
    String output = outContent.toString();

    // Verify no items in neighboring rooms are listed
    Assert.assertTrue(output.contains("Visible rooms:"));
    for (Iroom neighbor : world.getNeighbors(firstRoom)) {
      Assert.assertTrue(output.contains(neighbor.getName()));
      Assert.assertTrue(output.contains(" - No items"));
    }
  }

  /**
   * Tests that verifies the looking around command works correctly 
   * when the target character is in a neighbor space.
   */
  @Test
  public void testLookAroundWithTargetInNeighborSpace() {
    String input = "yes\nhuman\nTestPlayer\n0\nno\n20\nmove\n1\nlook around\nexit\n";
    inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    controller = new GameController(world, view, 10, "res/test_log.txt");
    controller.startGame();
    String output = outContent.toString();

    // Verify target in neighboring room is listed
    Assert.assertTrue(output.contains("Visible rooms:"));
    Assert.assertTrue(output.contains("Target: " + world.getTarget().getName() + " is here."));
  }

  /**
   * Tests that the game ends when the target character is killed.
   */
  @Test
  public void testGameEndsWhenTargetIsKilled() {
    String input = "yes\nhuman\nTestPlayer\n0\nno\n0\nattack\nexit\n";
    inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    controller = new GameController(world, view, 10, "res/test_log.txt");
    controller.startGame();
    String output = outContent.toString();

    // Verify the game end
    Assert.assertTrue(output.contains("has been defeated!"));
  }

  /**
   * Tests that the game ends when the maximum number of turns has been reached.
   */
  @Test
  public void testGameEndsWhenMaxTurnsReached() {
    String input = "yes\nhuman\nTestPlayer\n0\nno\n0\n";
    for (int i = 0; i < 10; i++) {
      input += "move\n0\n";
    }
    input += "exit\n";
    inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    controller = new GameController(world, view, 10, "res/test_log.txt");
    controller.startGame();
    String output = outContent.toString();

    // Verify the game end
    Assert.assertTrue(output.contains("Game over! Maximum number of turns reached."));
  }
}
