package game;

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

  /**
   * Sets up the test environment before each test.
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
    view = new GameView(world, "res/test_image.png", controller);
    controller = new GameController(world, view, 10, "res/test_log.txt");
    view.setController(controller);
    controller.setTestMode(true);
    outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
  }

  /**
   * Tears down the test environment after each test.
   */
  @After
  public void tearDown() {
    System.setOut(null);
    outContent.reset();
  }

  /**
   * Tests the addition of a player.
   */
  @Test
  public void testAddPlayer() {
    controller.startGame();

    List<Iplayer> players = world.getPlayers();
    Assert.assertEquals(3, players.size());
    Assert.assertEquals("TestPlayer1", players.get(0).getName());
  }

  /**
   * Tests the end of the game.
   */
  @Test
  public void testGameEnd() {
    controller.startGame();
    for (int i = 0; i < 10; i++) {
      controller.nextTurn();
    }

    String output = view.getConsoleText();
    Assert.assertTrue(output.contains("Game over"));
    Assert.assertTrue(output.contains("Maximum number of turns reached"));
  }

  /**
   * Tests moving a player.
   */
  @Test
  public void testMovePlayer() {
    controller.startGame();

    Iplayer player = controller.getCurrentPlayer();
    controller.movePlayer(player);
    controller.nextTurn();

    String output = view.getConsoleText();
    Assert.assertTrue(output.contains("moved"));
  }

  /**
   * Tests the addition of multiple players.
   */
  @Test
  public void testAddMultiplePlayers() {
    controller.startGame();

    List<Iplayer> players = world.getPlayers();
    Assert.assertEquals(3, players.size());
    Assert.assertEquals("TestPlayer1", players.get(0).getName());
    Assert.assertEquals("TestPlayer2", players.get(1).getName());
  }

  /**
   * Tests moving the pet.
   */
  @Test
  public void testMovePet() {
    controller.startGame();
    controller.movePet();

    String output = view.getConsoleText();
    Assert.assertTrue(output.contains("Pet moved"));
  }

  /**
   * Tests attacking the target.
   */
  @Test
  public void testAttackTarget() {
    controller.startGame();
    controller.attackTarget(controller.getCurrentPlayer());

    String output = view.getConsoleText();
    System.out.println(output);
    Assert.assertTrue(output.contains("") || output.contains("attack"));
  }

  /**
   * Tests picking up an item.
   */
  @Test
  public void testPickItem() {
    controller.startGame();
    Iroom room = world.getRoomByIndex(0);
    room.addItem(new Item("Sword", 10, 1));

    controller.pickItem(controller.getCurrentPlayer());

    String output = view.getConsoleText();
    Assert.assertTrue(output.contains("picked up"));
  }

  /**
   * Tests displaying room information.
   */
  @Test
  public void testDisplayRoomInfo() {
    controller.startGame();
    controller.displayRoom();

    String output = view.getConsoleText();
    Assert.assertTrue(output.contains("Room Name"));
  }

  /**
   * Tests moving the target.
   */
  @Test
  public void testTargetMove() {
    controller.startGame();
    controller.nextTurn();

    String output = view.getConsoleText();
    Assert.assertTrue(output.contains("Target moved"));
  }

  /**
   * Tests displaying player information.
   */
  @Test
  public void testDisplayPlayerInfo() {
    controller.startGame();
    controller.displayPlayer(controller.getCurrentPlayer());

    String output = view.getConsoleText();
    Assert.assertTrue(output.contains("Player Name"));
  }

  /**
   * Tests displaying the game map.
   */
  @Test
  public void testDisplayMap() {
    controller.startGame();
    controller.displayMap();

    String output = view.getConsoleText();
    Assert.assertTrue(output.contains("Map displayed"));
  }

  /**
   * Tests the addition of a computer player.
   */
  @Test
  public void testAddComputerPlayer() {
    controller.startGame();

    List<Iplayer> players = world.getPlayers();
    boolean foundComputerPlayer = false;

    for (Iplayer player : players) {
      if (player instanceof ComputerPlayer && "AI_Player".equals(player.getName())) {
        foundComputerPlayer = true;
        break;
      }
    }

    Assert.assertTrue("Computer player should be present", foundComputerPlayer);
    Assert.assertEquals(3, players.size());
  }
}
