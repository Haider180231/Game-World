package GameWorld;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MansionParser {
    public static GameWorld parseMansion(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        
        // Read the first line: number of rows, number of columns, and the name
        String line = reader.readLine();
        if (line == null || line.trim().isEmpty()) {
            throw new IOException("File is empty or first line is invalid");
        }
        String[] dimensions = line.trim().split("\\s+");
        int rows = Integer.parseInt(dimensions[0]);
        int columns = Integer.parseInt(dimensions[1]);
        String name = dimensions[2];

        // Read the second line: target's health and name
        line = reader.readLine();
        if (line == null || line.trim().isEmpty()) {
            throw new IOException("File is missing target information");
        }
        String[] targetInfo = line.trim().split("\\s+");
        int targetHealth = Integer.parseInt(targetInfo[0]);
        StringBuilder targetNameBuilder = new StringBuilder();
        for (int i = 1; i < targetInfo.length; i++) {
            targetNameBuilder.append(targetInfo[i]).append(" ");
        }
        String targetName = targetNameBuilder.toString().trim();
        Target target = new Target(targetHealth, targetName, new Tuple<>(0, 0));

        // Read the third line: number of rooms
        line = reader.readLine();
        if (line == null || line.trim().isEmpty()) {
            throw new IOException("File is missing room count information");
        }
        int numRooms = Integer.parseInt(line.trim());

        List<IRoom> rooms = new ArrayList<>();
        for (int i = 0; i < numRooms; i++) {
            line = reader.readLine();
            if (line == null || line.trim().isEmpty()) {
                throw new IOException("File is missing room information at index " + i);
            }
            String[] roomInfo = line.trim().split("\\s+");
            int rowStart = Integer.parseInt(roomInfo[0]);
            int colStart = Integer.parseInt(roomInfo[1]);
            int rowEnd = Integer.parseInt(roomInfo[2]);
            int colEnd = Integer.parseInt(roomInfo[3]);
            StringBuilder roomNameBuilder = new StringBuilder();
            for (int j = 4; j < roomInfo.length; j++) {
                roomNameBuilder.append(roomInfo[j]).append(" ");
            }
            String roomName = roomNameBuilder.toString().trim();
            Room room = new Room(roomName, new Tuple<>(rowStart, colStart), new Tuple<>(rowEnd, colEnd));
            rooms.add(room);
        }

        // Read the number of items
        line = reader.readLine();
        if (line == null || line.trim().isEmpty()) {
            throw new IOException("File is missing item count information");
        }
        int numItems = Integer.parseInt(line.trim());

        for (int i = 0; i < numItems; i++) {
            line = reader.readLine();
            if (line == null || line.trim().isEmpty()) {
                throw new IOException("File is missing item information at index " + i);
            }
            String[] itemInfo = line.trim().split("\\s+");
            int roomIndex = Integer.parseInt(itemInfo[0]);
            int damage = Integer.parseInt(itemInfo[1]);
            StringBuilder itemNameBuilder = new StringBuilder();
            for (int j = 2; j < itemInfo.length; j++) {
                itemNameBuilder.append(itemInfo[j]).append(" ");
            }
            String itemName = itemNameBuilder.toString().trim();
            Item item = new Item(itemName, damage, roomIndex);
            rooms.get(roomIndex).addItem(item);
        }

        reader.close();
        return new GameWorld(rows, columns, name, target, rooms);
    }
}
