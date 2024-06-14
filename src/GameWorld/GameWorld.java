package GameWorld;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.List;
import java.util.ArrayList;

public class GameWorld {
    private int rows;
    private int columns;
    private String name;
    private ITarget target;
    private List<IRoom> rooms;

    public GameWorld(int rows, int columns, String name, ITarget target, List<IRoom> rooms) {
        this.rows = rows;
        this.columns = columns;
        this.name = name;
        this.target = target;
        this.rooms = rooms;
    }

    public void addRoom(IRoom room) {
        rooms.add(room);
    }

    public IRoom getRoomByIndex(int index) {
        return rooms.get(index);
    }

    public List<IRoom> getRooms() {
        return rooms;
    }

    public ITarget getTarget() {
        return target;
    }

    public List<IRoom> getNeighbors(IRoom room) {
        List<IRoom> neighbors = new ArrayList<>();
        for (IRoom r : rooms) {
            if (r != room && ((Room) room).isNeighbor((Room) r)) {
                neighbors.add(r);
            }
        }
        return neighbors;
    }

    public void displayRoomInfo(int index) {
        if (index < 0 || index >= rooms.size()) {
            System.out.println("Invalid room index.");
            return;
        }
        IRoom room = rooms.get(index);
        System.out.println("Room Name: " + room.getName());
        System.out.println("Items in the Room:");
        for (IItem item : room.getItems()) {
            System.out.println(" - " + item.getName() + " (Damage: " + item.getDamage() + ")");
        }
        List<IRoom> neighbors = getNeighbors(room);
        System.out.println("Visible Rooms:");
        for (IRoom neighbor : neighbors) {
            System.out.println(" - " + neighbor.getName());
        }
    }

    public void moveTarget() {
        for (int i = 0; i < rooms.size(); i++) {
            IRoom room = rooms.get(i);
            target.moveTarget(room.getCoordinates());
            System.out.println("Target moved to: " + room.getName());
        }
    }

    public void displayMap(String imageOutputPath) throws IOException {
        int cellSize = 20;  // Size of each room
        int padding = 10;   // Padding around the image
        BufferedImage image = new BufferedImage(columns * cellSize + 2 * padding, rows * cellSize + 2 * padding, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, columns * cellSize + 2 * padding, rows * cellSize + 2 * padding);
        
        g.setColor(Color.BLACK);
        for (IRoom room : rooms) {
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
