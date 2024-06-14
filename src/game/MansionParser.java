package game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses the mansion configuration file and creates a GameWorld object.
 */
public class MansionParser {

  /**
   * Parses the mansion configuration file and creates a GameWorld object.
   *
   * @param filePath the path to the mansion configuration file
   * @return the created GameWorld object
   * @throws IOException if an error occurs while reading the file
   */
  public static GameWorld parseMansion(String filePath) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(filePath));

    // Read the first line: number of rows, number of columns, and the name
    final String line = reader.readLine();
    if (line == null || line.trim().isEmpty()) {
      throw new IOException("File is empty or first line is invalid");
    }
    final String[] dimensions = line.trim().split("\\s+");
    final int rows = Integer.parseInt(dimensions[0]);
    final int columns = Integer.parseInt(dimensions[1]);
    final String name = dimensions[2];

    // Read the second line: target's health and name
    String line2 = reader.readLine();
    if (line2 == null || line2.trim().isEmpty()) {
      throw new IOException("File is missing target information");
    }
    final String[] targetInfo = line2.trim().split("\\s+");
    final int targetHealth = Integer.parseInt(targetInfo[0]);
    final StringBuilder targetNameBuilder = new StringBuilder();
    for (int i = 1; i < targetInfo.length; i++) {
      targetNameBuilder.append(targetInfo[i]).append(" ");
    }
    final String targetName = targetNameBuilder.toString().trim();
    final Target target = new Target(targetHealth, targetName, new Tuple<>(0, 0));

    // Read the third line: number of rooms
    String line3 = reader.readLine();
    if (line3 == null || line3.trim().isEmpty()) {
      throw new IOException("File is missing room count information");
    }
    final int numRooms = Integer.parseInt(line3.trim());

    final List<Iroom> rooms = new ArrayList<>();
    for (int i = 0; i < numRooms; i++) {
      final String roomLine = reader.readLine();
      if (roomLine == null || roomLine.trim().isEmpty()) {
        throw new IOException("File is missing room information at index " + i);
      }
      final String[] roomInfo = roomLine.trim().split("\\s+");
      final int rowStart = Integer.parseInt(roomInfo[0]);
      final int colStart = Integer.parseInt(roomInfo[1]);
      final int rowEnd = Integer.parseInt(roomInfo[2]);
      final int colEnd = Integer.parseInt(roomInfo[3]);
      final StringBuilder roomNameBuilder = new StringBuilder();
      for (int j = 4; j < roomInfo.length; j++) {
        roomNameBuilder.append(roomInfo[j]).append(" ");
      }
      final String roomName = roomNameBuilder.toString().trim();
      final Room room = new Room(roomName, new Tuple<>(rowStart, colStart), new Tuple<>(rowEnd, colEnd));
      rooms.add(room);
    }

    // Read the number of items
    String line4 = reader.readLine();
    if (line4 == null || line4.trim().isEmpty()) {
      throw new IOException("File is missing item count information");
    }
    final int numItems = Integer.parseInt(line4.trim());

    for (int i = 0; i < numItems; i++) {
      final String itemLine = reader.readLine();
      if (itemLine == null || itemLine.trim().isEmpty()) {
        throw new IOException("File is missing item information at index " + i);
      }
      final String[] itemInfo = itemLine.trim().split("\\s+");
      final int roomIndex = Integer.parseInt(itemInfo[0]);
      final int damage = Integer.parseInt(itemInfo[1]);
      final StringBuilder itemNameBuilder = new StringBuilder();
      for (int j = 2; j < itemInfo.length; j++) {
        itemNameBuilder.append(itemInfo[j]).append(" ");
      }
      final String itemName = itemNameBuilder.toString().trim();
      final Item item = new Item(itemName, damage, roomIndex);
      rooms.get(roomIndex).addItem(item);
    }

    reader.close();
    return new GameWorld(rows, columns, name, target, rooms);
  }
}
