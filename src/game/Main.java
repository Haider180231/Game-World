package game;

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
      GameWorld world = MansionParser.parseMansion(filePath);
      world.displayRoomInfo(2);  // Display information of the first room
      world.displayMap(imageOutputPath);  // Display and save the map

      // Test the getNeighbors method
      Iroom room = world.getRoomByIndex(2);
      List<Iroom> neighbors = world.getNeighbors(room);
      System.out.println("Neighbors of " + room.getName() + ":");
      for (Iroom neighbor : neighbors) {
        System.out.println(neighbor.getName());
      }
      world.moveTarget();  // Move the target character
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
