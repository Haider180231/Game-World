package game;

import java.io.IOException;
import javax.swing.SwingUtilities;

/**
 * The main class that launches the game application.
 */
public class Main {
  /**
   * The entry point of the application.
   * 
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      /**
       * The main execution code to be run on the Event Dispatch Thread (EDT).
       */
      public void run() {
        final String filePath = "res/mansion.txt";
        final String logFilePath = "res/run_log.txt";
        final int maxTurns = 10;

        try {
          Igameworld world = MansionParser.parseMansion(filePath);
          GameView view = new GameView(world, logFilePath, null);
          GameController controller = new GameController(world, view, maxTurns, logFilePath);

          view.setController(controller);
          view.setVisible(true);
          controller.startGame();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }
}
