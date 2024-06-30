package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GameView {

    public void displayMap(Igameworld world, String imageOutputPath) throws IOException {
        int cellSize = 100;  
        int padding = 10;    
        BufferedImage image = new BufferedImage(world.getColumns() * cellSize + 2 * padding,
                world.getRows() * cellSize + 2 * padding, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, world.getColumns() * cellSize + 2 * padding, world.getRows() * cellSize + 2 * padding);

        g.setColor(Color.BLACK);
        for (Iroom room : world.getRooms()) {
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

            // Draw room name in the top-left corner of the room
            g.drawString(roomName, x + 2, y + 12);

            // Draw items in the room in the top-right corner
            int itemOffsetY = 12;
            g.setColor(Color.ORANGE); 
            for (Iitem item : room.getItems()) {
                g.drawString(item.getName(), x + width - g.getFontMetrics().stringWidth(item.getName()) - 2, y + itemOffsetY);
                itemOffsetY += 15;
            }
            g.setColor(Color.BLACK); 
        }

        // Draw the players in the bottom-right corner of their respective rooms
        int playerSize = 15;  
        for (Iplayer player : world.getPlayers()) {
            Tuple<Integer, Integer> playerCoords = player.getCoordinates();
            g.setColor(Color.BLUE);
            int playerX = playerCoords.getSecond() * cellSize + padding + cellSize - playerSize - 2;
            int playerY = playerCoords.getFirst() * cellSize + padding + cellSize - playerSize - 2;
            g.fillRect(playerX, playerY, playerSize, playerSize);
            g.drawString(player.getName(), playerX + playerSize + 2, playerY + playerSize / 2);
        }

        // Draw the target character in the bottom-left corner of its current location
        int targetSize = 15;  
        Tuple<Integer, Integer> targetCoords = world.getTarget().getCoordinates();
        if (targetCoords != null) {
            g.setColor(Color.RED);
            int targetX = targetCoords.getSecond() * cellSize + padding + 2;
            int targetY = targetCoords.getFirst() * cellSize + padding + cellSize - targetSize - 2;
            g.fillOval(targetX, targetY, targetSize, targetSize);
        }

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
