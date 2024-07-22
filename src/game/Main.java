package game;

import java.io.IOException;

/**
 * The entry point of the game application.
 */
public class Main {

  /**
   * The main method to start the game.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    final String filePath = "res/mansion.txt";
    final String imageOutputPath = "res/world.png";
    final String logFilePath = "res/run_log.txt";
    final int maxTurns = 10;

    try {
      Igameworld world = MansionParser.parseMansion(filePath);

      Iroom room0 = world.getRoomByIndex(0);
      Tuple<Integer, Integer> room0Coordinates = room0.getCoordinates();

      Itarget staticTarget = new Target(5, "Static Target", room0Coordinates);
      ((GameWorld) world).setTarget(staticTarget);

      ((GameWorld) world).setTargetShouldMove(false);

      GameView view = new GameView();
      GameController controller = new GameController(world, view, maxTurns, logFilePath);

      controller.startGame();

      view.displayMap(world, imageOutputPath);
      System.out.println("Game map saved to " + imageOutputPath);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
