package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Represents the game world containing rooms and a target.
 */
public class GameWorld {
  private int rows;
  private int columns;
  private String name;
  private Itarget target;
  private List<Iroom> rooms;

  /**
   * Constructs a GameWorld with specified dimensions, name, target, and rooms.
   *
   * @param rows    the number of rows in the game world
   * @param columns the number of columns in the game world
   * @param name    the name of the game world
   * @param target  the target in the game world
   * @param rooms   the list of rooms in the game world
   */
  public GameWorld(int rows, int columns, String name, Itarget target, List<Iroom> rooms) {
    this.rows = rows;
    this.columns = columns;
    this.name = name;
    this.target = target;
    this.rooms = rooms;
  }

  /**
   * Adds a room to the game world.
   *
   * @param room the room to add
   */
  public void addRoom(Iroom room) {
    rooms.add(room);
  }

  /**
   * Retrieves a room by its index.
   *
   * @param index the index of the room
   * @return the room at the specified index
   */
  public Iroom getRoomByIndex(int index) {
    return rooms.get(index);
  }

  /**
   * Retrieves the list of rooms in the game world.
   *
   * @return the list of rooms
   */
  public List<Iroom> getRooms() {
    return rooms;
  }

  /**
   * Retrieves the target in the game world.
   *
   * @return the target
   */
  public Itarget getTarget() {
    return target;
  }

  /**
   * Retrieves the neighboring rooms of a specified room.
   *
   * @param room the room to find neighbors for
   * @return the list of neighboring rooms
   */
  public List<Iroom> getNeighbors(Iroom room) {
    List<Iroom> neighbors = new ArrayList<>();
    for (Iroom r : rooms) {
      if (r != room && ((Room) room).isNeighbor((Room) r)) {
        neighbors.add(r);
      }
    }
    return neighbors;
  }

  /**
   * Displays information about a room specified by its index.
   *
   * @param index the index of the room
   */
  public void displayRoomInfo(int index) {
    if (index < 0 || index >= rooms.size()) {
      System.out.println("Invalid room index.");
      return;
    }
    Iroom room = rooms.get(index);
    System.out.println("Room Name: " + room.getName());
    System.out.println("Items in the Room:");
    for (Iitem item : room.getItems()) {
      System.out.println(" - " + item.getName() + " (Damage: " + item.getDamage() + ")");
    }
    List<Iroom> neighbors = getNeighbors(room);
    System.out.println("Visible Rooms:");
    for (Iroom neighbor : neighbors) {
      System.out.println(" - " + neighbor.getName());
    }
  }

  /**
   * Moves the target to each room in the game world sequentially.
   */
  public void moveTarget() {
    for (int i = 0; i < rooms.size(); i++) {
      Iroom room = rooms.get(i);
      target.moveTarget(room.getCoordinates());
      System.out.println("Target moved to: " + room.getName());
    }
  }

  /**
   * Displays a map of the game world and saves it as an image.
   *
   * @param imageOutputPath the path to save the image
   * @throws IOException if an error occurs during image creation or saving
   */
  public void displayMap(String imageOutputPath) throws IOException {
    int cellSize = 20;  // Size of each room
    int padding = 10;   // Padding around the image
    BufferedImage image = new BufferedImage(columns * cellSize + 2 * padding, 
        rows * cellSize + 2 * padding, BufferedImage.TYPE_INT_ARGB);
    Graphics g = image.getGraphics();
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, columns * cellSize + 2 * padding, rows * cellSize + 2 * padding);

    g.setColor(Color.BLACK);
    for (Iroom room : rooms) {
      Tuple<Integer, Integer> coords = room.getCoordinates();
      int rowStart = coords.getFirst();
      int colStart = coords.getSecond();
      int rowEnd = room.getEndingCoordinates().getFirst();
      int colEnd = room.getEndingCoordinates().getSecond();
      String roomName = room.getName();

      // Draw each room's border
      int x = colStart * cellSize + padding;
      int y = rowStart * cellSize + padding;
      int width = (colEnd - colStart + 1) * cellSize;
      int height = (rowEnd - rowStart + 1) * cellSize;

      g.drawLine(x, y, x + width, y);  // Top
      g.drawLine(x, y, x, y + height);  // Left
      g.drawLine(x + width, y, x + width, y + height);  // Right
      g.drawLine(x, y + height, x + width, y + height);  // Bottom

      g.drawString(roomName, x + 2, y + 12);  // Adjust room name position
    }

    // Draw the target character
    Tuple<Integer, Integer> targetCoords = target.getCoordinates();
    g.setColor(Color.RED);
    int targetX = targetCoords.getSecond() * cellSize + padding;
    int targetY = targetCoords.getFirst() * cellSize + padding;
    g.fillOval(targetX, targetY, cellSize, cellSize);

    g.dispose();
    System.out.println("Image created successfully.");
    File outputFile = new File(imageOutputPath);
    if (ImageIO.write(image, "png", outputFile)) {
      System.out.println("Image saved successfully to " + imageOutputPath);
    } else {
      throw new IOException("Failed to save the image to " + imageOutputPath);
    }
  }
}
