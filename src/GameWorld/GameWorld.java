package GameWorld;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.List;

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

    public int getTargetHealth() {
        return target.getHealth();
    }

    public String getTargetName() {
        return target.getName();
    }

    public List<IRoom> getRooms() {
        return rooms;
    }

    public void displayMap(String imageOutputPath) throws IOException {
        int cellSize = 20;  
        int padding = 10;   
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

            int x = colStart * cellSize + padding;
            int y = rowStart * cellSize + padding;
            int width = (colEnd - colStart + 1) * cellSize;
            int height = (rowEnd - rowStart + 1) * cellSize;

            g.drawLine(x, y, x + width, y);  
            g.drawLine(x, y, x, y + height);  
            g.drawLine(x + width, y, x + width, y + height); 
            g.drawLine(x, y + height, x + width, y + height);  

            g.drawString(roomName, x + 2, y + 12); 
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
