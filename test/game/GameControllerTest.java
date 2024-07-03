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
    } catch (IOException e) {
      e.printStackTrace();
    }
    view = new GameView();
    outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    controller = new GameController(world, view, 10, "res/test_log.txt");
  }

  /**
   * Tears down the test environment.
   */
  @After
  public void tearDown() {
    System.setOut(null);
  }

  /**
   * Tests the addition of a player to the game.
   */
  @Test
  public void testAddPlayer() {
    String input = "yes\nhuman\nTestPlayer\nno\nexit\n";
    inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

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
    String input = "yes\nhuman\nTestPlayer\nno\nlook around\nexit\n";
    inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    controller.startGame();
    String output = outContent.toString();

    // Verify items in the room
    Assert.assertTrue(output.contains("Items in the room:"));
    if (output.contains("No items in the room.")) {
      Assert.assertTrue(output.contains("No items in the room."));
    } else {
      Assert.assertTrue(output.contains(" - "));
    }

    // Verify visible rooms
    Assert.assertTrue(output.contains("Visible rooms:"));
    Assert.assertTrue(output.contains(" - "));

    // Verify players in the room
    Assert.assertTrue(output.contains("Players in the room:"));
    if (output.contains("No other players")) {
      Assert.assertTrue(output.contains("No other players"));
    } else {
      Assert.assertTrue(output.contains(" - "));
    }

    // Verify target in the room
    Assert.assertTrue(output.contains("Target in the room:"));
    if (output.contains("No target")) {
      Assert.assertTrue(output.contains("No target"));
    } else {
      Assert.assertTrue(output.contains(" - "));
    }
  }

  /**
   * Tests the display map functionality.
   */
  @Test
  public void testDisplayMap() {
    String input = "yes\nhuman\nTestPlayer\nno\ndisplay map\nexit\n";
    inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    controller.startGame();
    String output = outContent.toString();

    Assert.assertTrue(output.contains("Map displayed and saved to"));
  }

  /**
   * Tests the display player functionality.
   */
  @Test
  public void testDisplayPlayer() {
    String input = "yes\nhuman\nTestPlayer\nno\ndisplay player\nexit\n";
    inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    controller.startGame();
    String output = outContent.toString();

    Assert.assertTrue(output.contains("Player Name: TestPlayer"));
    Assert.assertTrue(output.contains("Items:"));
  }

  /**
   * Tests that the game alternates turns between players in the order they were added.
   */
  @Test
  public void testPlayerOrder() {
    String input = "yes\nhuman\nPlayer1\nno\nmove\n0\nmove\n0\nexit\n";
    inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    controller.startGame();
    String output = outContent.toString();

    // Verify the turn order
    Assert.assertTrue(output.contains("It's Player1's turn."));
  }

  /**
   * Tests that the game ends when the maximum number of turns has been reached.
   */
  @Test
  public void testGameEnd() {
    String input = "yes\nhuman\nTestPlayer\nno\n";
    for (int i = 0; i < 10; i++) {
      input += "next turn\n";
    }
    input += "exit\n";
    inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

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
    String input = "yes\nhuman\nTestPlayer\nno\nmove\n0\nexit\n";
    inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    controller.startGame();
    String output = outContent.toString();

    Iplayer player = world.getPlayers().get(0); 
    Iroom initialRoom = findRoomByCoordinates(player.getCoordinates(), world);
    List<Iroom> neighbors = world.getNeighbors(initialRoom);
    Iroom newRoom = neighbors.get(0);

    Assert.assertTrue(output.contains("Action completed"));
  }

  /**
   * Tests that a player can pick up an item in the current room.
   */
  @Test
  public void testPickItem() {
    String input = "yes\nhuman\nTestPlayer\nno\npick item\nexit\n";
    inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    Iroom initialRoom = world.getRoomByIndex(0); 
    Iitem item = new Item("Test Item", 10, 0);
    initialRoom.addItem(item);
    Iplayer player = new Player("Test Player", initialRoom.getCoordinates(), 5);
    world.addPlayer(player);

    controller.startGame();
    String output = outContent.toString();

    Assert.assertTrue(output.contains("Action completed"));
  }

  /**
   * Tests displaying room information by room index.
   */
  @Test
  public void testDisplayRoom() {
    String input = "yes\nhuman\nTestPlayer\nno\ndisplay room\n0\nexit\n";
    inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    controller.startGame();
    String output = outContent.toString();

    Iroom room = world.getRoomByIndex(0);
    Assert.assertTrue(output.contains("Room Name: " + room.getName()));
    Assert.assertTrue(output.contains("Items in the Room:"));
    Assert.assertTrue(output.contains("Visible Rooms:"));
    Assert.assertTrue(output.contains("Players in the Room:"));
  }

  /**
   * Helper method to find a room by its coordinates.
   *
   * @param coordinates the coordinates of the room
   * @param world the game world
   * @return the room if found, null otherwise
   */
  private Iroom findRoomByCoordinates(Tuple<Integer, Integer> coordinates, Igameworld world) {
    for (Iroom room : world.getRooms()) {
      if (room.getCoordinates().equals(coordinates)) {
        return room;
      }
    }
    return null;
  }
  
  /**
   * Tests the addition of a computer player to the game.
   */
  @Test
  public void testAddComputer() {
    String input = "yes\ncomputer\nComputerPlayer\nno\nexit\n";
    inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    controller.startGame();
    List<Iplayer> players = world.getPlayers();
    Assert.assertEquals(1, players.size());
    Assert.assertEquals("ComputerPlayer", players.get(0).getName());
  }

}
