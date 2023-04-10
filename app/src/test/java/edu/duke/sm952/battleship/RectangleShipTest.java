package edu.duke.sm952.battleship;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RectangleShipTest {

    @Test
    public void testMakeCoords0x0() {
        Coordinate upperLeft = new Coordinate(1, 2);
        int width = 0;
        int height = 0;
        Map<Integer, Coordinate> coords = RectangleShip.makeCoords(upperLeft, width, height);
        assertEquals(0, coords.size());
    }

    @Test
    public void testMakeCoords1x3() {
        Coordinate upperLeft = new Coordinate(1, 2);
        int width = 1;
        int height = 3;
        Map<Integer, Coordinate> coords = RectangleShip.makeCoords(upperLeft, width, height);

        for (int i = upperLeft.getRow(); i < upperLeft.getRow() + height; i++) {
            for (int j = upperLeft.getColumn(); j < upperLeft.getColumn() + width; j++) {
                assertTrue(coords.containsValue(new Coordinate(i, j)));
            }
        }
        assertEquals(width * height, coords.size());
    }

    @Test
    public void testMakeCoords4x2() {
        Coordinate upperLeft = new Coordinate(1, 2);
        int width = 4;
        int height = 2;
        Map<Integer, Coordinate> coords = RectangleShip.makeCoords(upperLeft, width, height);

        for (int i = upperLeft.getRow(); i < upperLeft.getRow() + height; i++) {
            for (int j = upperLeft.getColumn(); j < upperLeft.getColumn() + width; j++) {
                assertTrue(coords.containsValue(new Coordinate(i, j)));
            }
        }
        assertEquals(width * height, coords.size());
    }

    @Test
    public void testConstructor() {
        Coordinate upperLeft = new Coordinate(1, 2);
        int width = 4;
        int height = 2;
        String name = "submarine";
        RectangleShip<Character> ship = new RectangleShip<>(name, upperLeft, width, height, 's', '*');
        for (int i = upperLeft.getRow(); i < upperLeft.getRow() + height; i++) {
            for (int j = upperLeft.getColumn(); j < upperLeft.getColumn() + width; j++) {
                assertTrue(ship.myPieces.containsKey(new Coordinate(i, j)));
            }
        }
        assertEquals(width * height, ship.myPieces.size());
        assertEquals(name, ship.getName());
    }

    @Test
    public void testWasHitAt() {
        Coordinate upperLeft = new Coordinate(1, 2);
        int width = 4;
        int height = 2;
        String name = "submarine";
        BasicShip<Character> basicShip = new RectangleShip<>(name, upperLeft, width, height, 's', '*');
        assertFalse(basicShip.wasHitAt(new Coordinate(2, 3)));
        basicShip.recordHitAt(new Coordinate(2, 3));
        assertTrue(basicShip.wasHitAt(new Coordinate(2, 3)));
        assertThrows(IllegalArgumentException.class, () -> basicShip.wasHitAt(new Coordinate(5, 6)));
        assertEquals(name, basicShip.getName());
    }

    @Test
    public void testRecordHitAt() {
        Coordinate upperLeft = new Coordinate(1, 2);
        int width = 4;
        int height = 2;
        String name = "submarine";
        BasicShip<Character> basicShip = new RectangleShip<>(name, upperLeft, width, height, 's', '*');
        assertFalse(basicShip.wasHitAt(new Coordinate(2, 3)));
        basicShip.recordHitAt(new Coordinate(2, 3));
        assertTrue(basicShip.wasHitAt(new Coordinate(2, 3)));
        assertThrows(IllegalArgumentException.class, () -> basicShip.recordHitAt(new Coordinate(5, 6)));
        assertEquals(name, basicShip.getName());
    }

    @Test
    public void testIsSunk() {
        Coordinate upperLeft = new Coordinate(1, 2);
        int width = 1;
        int height = 3;
        String name = "submarine";
        BasicShip<Character> basicShip = new RectangleShip<>(name, upperLeft, width, height, 's', '*');
        basicShip.recordHitAt(new Coordinate(1, 2));
        assertFalse(basicShip.isSunk());
        basicShip.recordHitAt(new Coordinate(2, 2));
        basicShip.recordHitAt(new Coordinate(3, 2));
        assertTrue(basicShip.isSunk());
        assertEquals(name, basicShip.getName());
    }

    @Test
    public void testGetDisplayInfoAt() {
        Coordinate upperLeft = new Coordinate(1, 2);
        int width = 1;
        int height = 3;
        String name = "submarine";
        BasicShip<Character> basicShip = new RectangleShip<>(name, upperLeft, width, height, 's', '*');
        basicShip.recordHitAt(new Coordinate(1, 2));
        assertEquals('*', basicShip.getDisplayInfoAt(new Coordinate(1, 2), true));
        assertEquals('s', basicShip.getDisplayInfoAt(new Coordinate(2, 2), true));
        assertNull(basicShip.getDisplayInfoAt(new Coordinate(2, 2), false));
        basicShip.recordHitAt(new Coordinate(2, 2));
        assertEquals('*', basicShip.getDisplayInfoAt(new Coordinate(2, 2), true));
        basicShip.recordHitAt(new Coordinate(3, 2));
        assertEquals('*', basicShip.getDisplayInfoAt(new Coordinate(3, 2), true));
        assertEquals(name, basicShip.getName());
        assertEquals('s', basicShip.getDisplayInfoAt(new Coordinate(3, 2), false));
    }

    @Test
    public void testGetCoordinates() {
        Coordinate upperLeft = new Coordinate(1, 2);
        int width = 1;
        int height = 3;
        String name = "submarine";
        BasicShip<Character> basicShip = new RectangleShip<>(name, upperLeft, width, height, 's', '*');
        Set<Coordinate> coordinateSet = new HashSet<>();
        coordinateSet.add(new Coordinate(1, 2));
        coordinateSet.add(new Coordinate(2, 2));
        coordinateSet.add(new Coordinate(3, 2));
        assertEquals(coordinateSet, basicShip.getCoordinates());
    }

    @Test
    public void testUpdateHitsFromPreviousShip() {
        Coordinate upperLeft1 = new Coordinate(1, 2);
        int width = 1;
        int height = 3;
        String name = "submarine";
        BasicShip<Character> basicShip1 = new RectangleShip<>(name, upperLeft1, width, height, 's', '*');
        Set<Coordinate> coordinateSet = new HashSet<>();
        coordinateSet.add(new Coordinate(1, 2));
        coordinateSet.add(new Coordinate(2, 2));
        coordinateSet.add(new Coordinate(3, 2));
        assertEquals(coordinateSet, basicShip1.getCoordinates());

        Coordinate upperLeft2 = new Coordinate(5, 7);
        BasicShip<Character> basicShip2 = new RectangleShip<>(name, upperLeft2, width, height, 's', '*');

        basicShip1.recordHitAt(new Coordinate(3, 2));

        basicShip2.updateHitsFromPreviousLocation(basicShip1);
        assertEquals('s', basicShip2.getDisplayInfoAt(new Coordinate(5, 7), true));
        assertEquals('s', basicShip2.getDisplayInfoAt(new Coordinate(6, 7), true));
        assertEquals('*', basicShip2.getDisplayInfoAt(new Coordinate(7, 7), true));

        basicShip1.recordHitAt(new Coordinate(2, 2));

        basicShip2.updateHitsFromPreviousLocation(basicShip1);
        assertEquals('s', basicShip2.getDisplayInfoAt(new Coordinate(5, 7), true));
        assertEquals('*', basicShip2.getDisplayInfoAt(new Coordinate(6, 7), true));
        assertEquals('*', basicShip2.getDisplayInfoAt(new Coordinate(7, 7), true));
    }

}