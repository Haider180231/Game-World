package game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for parsing a mansion description file and creating a game world.
 */
public class MansionParser {

  /**
   * Parses the mansion description file and creates a game world.
   *
   * @param filePath the path to the mansion description file
   * @return the created game world
   * @throws IOException if an error occurs while reading or parsing the file
   */
  public static Igameworld parseMansion(String filePath) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

      // Read the first line: number of rows, number of columns, and the name
      final String line = reader.readLine();
      if (line == null || line.trim().isEmpty()) {
        throw new IOException("File is empty or first line is invalid");
      }
      final String[] dimensions = line.trim().split("\\s+");
      if (dimensions.length < 3) {
        throw new IOException("Invalid dimensions line");
      }
      final int rows = parseInteger(dimensions[0], "Invalid row value");
      final int columns = parseInteger(dimensions[1], "Invalid column value");
      final String name = dimensions[2];

      // Read the second line: target's health and name
      String line2 = reader.readLine();
      if (line2 == null || line2.trim().isEmpty()) {
        throw new IOException("File is missing target information");
      }
      final String[] targetInfo = line2.trim().split("\\s+");
      if (targetInfo.length < 2) {
        throw new IOException("Invalid target information");
      }
      final int targetHealth = parseInteger(targetInfo[0], "Invalid target health value");
      final StringBuilder targetNameBuilder = new StringBuilder();
      for (int i = 1; i < targetInfo.length; i++) {
        targetNameBuilder.append(targetInfo[i]).append(" ");
      }
      final String targetName = targetNameBuilder.toString().trim();
      final Target target = new Target(targetHealth, targetName, new Tuple<>(0, 0));

      // Read the third line: pet's name
      String line3 = reader.readLine();
      if (line3 == null || line3.trim().isEmpty()) {
        throw new IOException("File is missing pet information");
      }
      final String petName = line3.trim();
      final Pet pet = new Pet(petName, new Tuple<>(0, 0));

      // Read the fourth line: number of rooms
      String line4 = reader.readLine();
      if (line4 == null || line4.trim().isEmpty()) {
        throw new IOException("File is missing room count information");
      }
      final int numRooms = parseInteger(line4.trim(), "Invalid room count value");

      final List<Iroom> rooms = new ArrayList<>();
      for (int i = 0; i < numRooms; i++) {
        final String roomLine = reader.readLine();
        if (roomLine == null || roomLine.trim().isEmpty()) {
          throw new IOException("File is missing room information at index " + i);
        }
        final String[] roomInfo = roomLine.trim().split("\\s+");
        if (roomInfo.length < 5) {
          throw new IOException("Invalid room information at index " + i);
        }
        final int rowStart = parseInteger(roomInfo[0], "Invalid room row start value");
        final int colStart = parseInteger(roomInfo[1], "Invalid room column start value");
        final int rowEnd = parseInteger(roomInfo[2], "Invalid room row end value");
        final int colEnd = parseInteger(roomInfo[3], "Invalid room column end value");
        final StringBuilder roomNameBuilder = new StringBuilder();
        for (int j = 4; j < roomInfo.length; j++) {
          roomNameBuilder.append(roomInfo[j]).append(" ");
        }
        final String roomName = roomNameBuilder.toString().trim();
        final Room room = new Room(roomName, 
            new Tuple<>(rowStart, colStart), new Tuple<>(rowEnd, colEnd));
        rooms.add(room);
      }

      // Read the number of items
      String line5 = reader.readLine();
      if (line5 == null || line5.trim().isEmpty()) {
        throw new IOException("File is missing item count information");
      }
      final int numItems = parseInteger(line5.trim(), "Invalid item count value");

      for (int i = 0; i < numItems; i++) {
        final String itemLine = reader.readLine();
        if (itemLine == null || itemLine.trim().isEmpty()) {
          throw new IOException("File is missing item information at index " + i);
        }
        final String[] itemInfo = itemLine.trim().split("\\s+");
        if (itemInfo.length < 3) {
          throw new IOException("Invalid item information at index " + i);
        }
        final int roomIndex = parseInteger(itemInfo[0], "Invalid item room index value");
        final int damage = parseInteger(itemInfo[1], "Invalid item damage value");
        final StringBuilder itemNameBuilder = new StringBuilder();
        for (int j = 2; j < itemInfo.length; j++) {
          itemNameBuilder.append(itemInfo[j]).append(" ");
        }
        final String itemName = itemNameBuilder.toString().trim();
        final Item item = new Item(itemName, damage, roomIndex);
        rooms.get(roomIndex).addItem(item);
      }

      // Set target and pet initial coordinates to the first room's coordinates
      if (!rooms.isEmpty()) {
        Tuple<Integer, Integer> initialCoordinates = rooms.get(0).getCoordinates();
        target.moveTarget(initialCoordinates);
        pet.move(initialCoordinates);
      }

      GameWorld gameWorld = new GameWorld(rows, columns, name, target, pet, rooms);
      return gameWorld;
    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
      throw new IOException("Failed to parse file", e);
    }
  }

  /**
   * Parses a string value as an integer, 
   * throwing an IOException with a custom error message if parsing fails.
   *
   * @param value the string value to parse
   * @param errorMessage the error message for the exception if parsing fails
   * @return the parsed integer value
   * @throws IOException if the string value cannot be parsed as an integer
   */
  private static int parseInteger(String value, String errorMessage) throws IOException {
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      throw new IOException(errorMessage, e);
    }
  }
}
