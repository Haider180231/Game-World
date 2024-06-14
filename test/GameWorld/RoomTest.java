package GameWorld;

import static org.junit.Assert.*;
import java.io.IOException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class RoomTest {

    private GameWorld world;

    @Before
    public void setUp() throws IOException {
        String filePath = "res/mansion.txt";
        world = MansionParser.parseMansion(filePath);
    }

    @Test
    public void testGetName() {
        IRoom room = world.getRoomByIndex(0);
        assertEquals("Armory", room.getName());
    }

    @Test
    public void testGetCoordinates() {
        IRoom room = world.getRoomByIndex(0);
        Tuple<Integer, Integer> coords = room.getCoordinates();
        assertEquals(new Tuple<>(22, 19), coords);
    }

    @Test
    public void testGetEndingCoordinates() {
        IRoom room = world.getRoomByIndex(0);
        Tuple<Integer, Integer> endingCoords = room.getEndingCoordinates();
        assertEquals(new Tuple<>(23, 26), endingCoords);
    }

    @Test
    public void testAddItem() {
        IRoom room = world.getRoomByIndex(0);
        List<IItem> items = room.getItems();
        int initialSize = items.size(); // Capture the initial size of items
        Item item = new Item("Sword", 10, 0);
        room.addItem(item);
        items = room.getItems();
        assertEquals(initialSize + 1, items.size());
        assertEquals("Sword", items.get(items.size() - 1).getName());
    }

    @Test
    public void testGetItems() {
        IRoom room = world.getRoomByIndex(0);
        List<IItem> items = room.getItems();
        assertNotNull(items);
    }

    @Test
    public void testIsNeighbor() {
        IRoom room1 = world.getRoomByIndex(0); // Armory
        IRoom room2 = world.getRoomByIndex(1); // Billiard Room
        IRoom room3 = world.getRoomByIndex(20); // Winter Garden (not a neighbor)

        assertTrue(room1.isNeighbor((Room) room2));
        assertFalse(room1.isNeighbor((Room) room3));
    }
}
