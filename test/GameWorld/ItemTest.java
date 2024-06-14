package GameWorld;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ItemTest {

    private Item item;

    @Before
    public void setUp() {
        item = new Item("Sword", 10, 0);
    }

    @Test
    public void testGetName() {
        assertEquals("Sword", item.getName());
    }

    @Test
    public void testGetDamage() {
        assertEquals(10, item.getDamage());
    }

    @Test
    public void testGetRoomIndex() {
        assertEquals(0, item.getRoomIndex());
    }
}
