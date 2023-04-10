package edu.duke.sm952.battleship;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlacementTest {

    @Test
    public void testPlacement() {
        Coordinate c1 = new Coordinate(1, 2);
        char o1 = 'H';
        Placement p1 = new Placement(c1, o1);
        assertEquals(c1, p1.getWhere());
        assertEquals('H', p1.getOrientation());
    }

    @Test
    public void testPlacementWithDescr() {
        assertEquals('H', new Placement("a3h").getOrientation());
        assertEquals('V', new Placement("a3v").getOrientation());
        assertEquals(0, new Placement("a3h").getWhere().getRow());
        assertEquals(3, new Placement("a3h").getWhere().getColumn());
        assertThrows(IllegalArgumentException.class, () -> new Placement("1234"));
        assertThrows(IllegalArgumentException.class, () -> new Placement("A3A"));
        assertThrows(IllegalArgumentException.class, () -> new Placement("A3A"));
        assertThrows(IllegalArgumentException.class, () -> new Placement("13A"));

        assertEquals('U', new Placement("a3u", 2).getOrientation());
        assertEquals('R', new Placement("a3r", 2).getOrientation());
        assertEquals('D', new Placement("a3d", 2).getOrientation());
        assertEquals('L', new Placement("a3l", 2).getOrientation());
        assertEquals(0, new Placement("a3u", 2).getWhere().getRow());
        assertEquals(3, new Placement("a3u", 2).getWhere().getColumn());
        assertEquals(0, new Placement("a3r", 2).getWhere().getRow());
        assertEquals(3, new Placement("a3r", 2).getWhere().getColumn());
        assertEquals(0, new Placement("a3d", 2).getWhere().getRow());
        assertEquals(3, new Placement("a3d", 2).getWhere().getColumn());
        assertEquals(0, new Placement("a3l", 2).getWhere().getRow());
        assertEquals(3, new Placement("a3l", 2).getWhere().getColumn());
        assertThrows(IllegalArgumentException.class, () -> new Placement("13A", 2));
        assertThrows(IllegalArgumentException.class, () -> new Placement("13A", 3));
    }

    @Test
    public void testToString() {
        assertEquals("Placement{where=(0, 2), orientation=H}", new Placement("A2H").toString());
    }

    @Test
    public void testEquals() {
        Placement p1 = new Placement("a1v");
        Placement p2 = new Placement("a1v");
        Placement p3 = new Placement("a2h");
        Placement p4 = new Placement("b4h");
        assertEquals(p1, p1);
        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertNotEquals(p1, p4);
        assertNotEquals(p2, p3);
        assertNotEquals(p2, p4);
        assertNotEquals(p3, p4);
        assertNotEquals("Placement{where=(0, 2), orientation=H}", p1);
        assertNotEquals(p1, new Coordinate(1, 2));
    }

    @Test
    public void testHashCode() {
        Placement p1 = new Placement("a1v");
        Placement p2 = new Placement("a1v");
        Placement p3 = new Placement("a2h");
        Placement p4 = new Placement("b4h");
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1.hashCode(), p3.hashCode());
        assertNotEquals(p1.hashCode(), p4.hashCode());
        assertNotEquals(p2.hashCode(), p3.hashCode());
        assertNotEquals(p2.hashCode(), p4.hashCode());
    }

}