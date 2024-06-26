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
