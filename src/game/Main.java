package game;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * The main class to run the game world simulation.
 */
public class Main {

  /**
   * The main method to run the game world simulation.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    try {
      String filePath = "res/mansion.txt";
      String imageOutputPath = "res/world.png";
      String logFilePath = "res/run_log.txt";

      GameWorld world = MansionParser.parseMansion(filePath);

      // Display information of the first room
      FileWriter logWriter = new FileWriter(logFilePath);
      logWriter.write("Displaying information of the first room:\n");
      logWriter.write("=======================================\n");
      logWriter.write("Room Name: " + world.getRoomByIndex(2).getName() + "\n");
      logWriter.write("Items in the Room:\n");
      for (Iitem item : world.getRoomByIndex(2).getItems()) {
        logWriter.write(" - " + item.getName() + " (Damage: " + item.getDamage() + ")\n");
      }
      List<Iroom> neighbors = world.getNeighbors(world.getRoomByIndex(2));
      logWriter.write("Visible Rooms:\n");
      for (Iroom neighbor : neighbors) {
        logWriter.write(" - " + neighbor.getName() + "\n");
      }

      // Generate and save the map
      world.displayMap(imageOutputPath);

      // Test the getNeighbors method
      logWriter.write("\nTesting getNeighbors method:\n");
      logWriter.write("============================\n");
      Iroom room = world.getRoomByIndex(2);
      neighbors = world.getNeighbors(room);
      logWriter.write("Neighbors of " + room.getName() + ":\n");
      for (Iroom neighbor : neighbors) {
        logWriter.write(neighbor.getName() + "\n");
      }

      // Move the target character
      logWriter.write("\nMoving the target character:\n");
      logWriter.write("============================\n");
      for (int i = 0; i < world.getRooms().size(); i++) {
        Iroom r = world.getRoomByIndex(i);
        world.getTarget().moveTarget(r.getCoordinates());
        logWriter.write("Target moved to: " + r.getName() + "\n");
      }

      logWriter.close();
      System.out.println("Run log saved to " + logFilePath);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
