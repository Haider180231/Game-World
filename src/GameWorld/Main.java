package GameWorld;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            String filePath = "res/mansion.txt";
            String imageOutputPath = "res/world.png";
            GameWorld world = MansionParser.parseMansion(filePath);
            world.displayRoomInfo(2);  // Display information of the first room
            world.displayMap(imageOutputPath);  // Display and save the map

            // Test the getNeighbors method
            IRoom room = world.getRoomByIndex(2);
            List<IRoom> neighbors = world.getNeighbors(room);
            System.out.println("Neighbors of " + room.getName() + ":");
            for (IRoom neighbor : neighbors) {
                System.out.println(neighbor.getName());
            }
            world.moveTarget();  // Move the target character
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
