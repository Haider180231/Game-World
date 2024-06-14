package GameWorld;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TargetTest {

    private Target target;

    @Before
    public void setUp() {
        target = new Target(50, "Doctor Lucky", new Tuple<>(0, 0));
    }

    @Test
    public void testGetHealth() {
        assertEquals(50, target.getHealth());
    }

    @Test
    public void testGetName() {
        assertEquals("Doctor Lucky", target.getName());
    }

    @Test
    public void testGetCoordinates() {
        Tuple<Integer, Integer> coords = target.getCoordinates();
        assertEquals(new Tuple<>(0, 0), coords);
    }

    @Test
    public void testMoveTarget() {
        Tuple<Integer, Integer> newCoords = new Tuple<>(1, 1);
        target.moveTarget(newCoords);
        assertEquals(newCoords, target.getCoordinates());
    }
}
