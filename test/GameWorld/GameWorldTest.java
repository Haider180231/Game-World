package GameWorld;

import static org.junit.Assert.*;
import java.io.IOException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class GameWorldTest {

    private GameWorld world;

    @Before
    public void setUp() throws IOException {
        String filePath = "res/mansion.txt";
        world = MansionParser.parseMansion(filePath);
    }

    @Test
    public void testLoadRoomsAndItems() {
        assertNotNull(world);
        assertEquals(21, world.getRooms().size());

        IRoom firstRoom = world.getRoomByIndex(0);
        assertEquals("Armory", firstRoom.getName());

        List<IItem> items = firstRoom.getItems();
        assertNotNull(items);
        assertFalse(items.isEmpty());
    }

    @Test
    public void testGetNeighbors() {
        IRoom firstRoom = world.getRoomByIndex(0);
        List<IRoom> neighbors = world.getNeighbors(firstRoom);
        assertNotNull(neighbors);
        assertFalse(neighbors.isEmpty());

        IRoom neighbor = neighbors.get(0);
        assertEquals("Billiard Room", neighbor.getName());
    }

    @Test
    public void testMoveTarget() {
        world.moveTarget();

        IRoom lastRoom = world.getRoomByIndex(world.getRooms().size() - 1);
        assertEquals(lastRoom.getCoordinates(), world.getTarget().getCoordinates());
    }

    @Test
    public void testDisplayRoomInfo() {
        IRoom firstRoom = world.getRoomByIndex(0);
        List<IItem> items = firstRoom.getItems();
        List<IRoom> neighbors = world.getNeighbors(firstRoom);

        //DisplayRoomInfo method prints the information to the console
        world.displayRoomInfo(0);

        assertNotNull(items);
        assertFalse(items.isEmpty());
        assertNotNull(neighbors);
        assertFalse(neighbors.isEmpty());
    }

    @Test
    public void testGetRoomByIndex() {
        IRoom room = world.getRoomByIndex(0);
        assertNotNull(room);
        assertEquals("Armory", room.getName());
    }

    @Test
    public void testGetRooms() {
        List<IRoom> rooms = world.getRooms();
        assertNotNull(rooms);
        assertEquals(21, rooms.size());
    }

    @Test
    public void testGetTarget() {
        ITarget target = world.getTarget();
        assertNotNull(target);
        assertEquals("Doctor Lucky", target.getName());
    }
}
