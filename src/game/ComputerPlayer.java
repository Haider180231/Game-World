package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComputerPlayer implements Iplayer {
    private String name;
    private Tuple<Integer, Integer> coordinates;
    private int maxItems;
    private List<Iitem> items;

    public ComputerPlayer(String name, Tuple<Integer, Integer> coordinates, int maxItems) {
        this.name = name;
        this.coordinates = coordinates;
        this.maxItems = maxItems;
        this.items = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Tuple<Integer, Integer> getCoordinates() {
        return coordinates;
    }

    @Override
    public int getMaxItems() {
        return maxItems;
    }

    @Override
    public void addItem(Iitem item) {
        if (items.size() < maxItems) {
            items.add(item);
        }
    }

    @Override
    public List<Iitem> getItems() {
        return items;
    }

    @Override
    public void move(Tuple<Integer, Integer> newCoordinates) {
        coordinates = newCoordinates;
    }

    public void takeTurn(Igameworld world) {
        Random random = new Random();
        int action = random.nextInt(3);

        switch (action) {
            case 0:
                // move
                List<Iroom> neighbors = world.getNeighbors(world.getRoomByCoordinates(coordinates));
                if (!neighbors.isEmpty()) {
                    Iroom newRoom = neighbors.get(random.nextInt(neighbors.size()));
                    move(newRoom.getCoordinates());
                }
                break;

            case 1:
                // pick item
                Iroom currentRoom = world.getRoomByCoordinates(coordinates);
                if (currentRoom != null && !currentRoom.getItems().isEmpty()) {
                    Iitem item = currentRoom.getItems().get(0); 
                    addItem(item);
                    currentRoom.getItems().remove(item);
                }
                break;

            case 2:
                // look around
                break;
        }
    }
}
