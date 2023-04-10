package edu.duke.sm952.battleship;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomCarrierShipTest {

    @Test
    void testGetMask() {
        assertArrayEquals(new int[][]{{1, 0}, {1, 0}, {1, 1}, {1, 1}, {0, 1}}, CustomCarrierShip.getMask('U'));
        assertArrayEquals(new int[][]{{0, 1, 1, 1, 1}, {1, 1, 1, 0, 0}}, CustomCarrierShip.getMask('R'));
        assertArrayEquals(new int[][]{{1, 0}, {1, 1}, {1, 1}, {0, 1}, {0, 1}}, CustomCarrierShip.getMask('D'));
        assertArrayEquals(new int[][]{{0, 0, 1, 1, 1}, {1, 1, 1, 1, 0}}, CustomCarrierShip.getMask('L'));
        assertThrows(IllegalArgumentException.class, () -> CustomCarrierShip.getMask('H'));
    }

    @Test
    public void testGetCoordinates() {
        Coordinate upperLeft = new Coordinate(1, 2);
        Placement placement = new Placement(upperLeft, 'u');
        String name = "battleship";
        BasicShip<Character> basicShip = new CustomCarrierShip<>(name, placement, 's', '*');
        Set<Coordinate> coordinateSet = new HashSet<>();
        coordinateSet.add(new Coordinate(1, 2));
        coordinateSet.add(new Coordinate(2, 2));
        coordinateSet.add(new Coordinate(3, 2));
        coordinateSet.add(new Coordinate(3, 3));
        coordinateSet.add(new Coordinate(4, 2));
        coordinateSet.add(new Coordinate(4, 3));
        coordinateSet.add(new Coordinate(5, 3));
        assertEquals(coordinateSet, basicShip.getCoordinates());
        assertEquals(name, basicShip.getName());

        upperLeft = new Coordinate(3, 1);
        placement = new Placement(upperLeft, 'l');
        basicShip = new CustomCarrierShip<>(name, placement, 's', '*');
        coordinateSet = new HashSet<>();
        coordinateSet.add(new Coordinate(3, 3));
        coordinateSet.add(new Coordinate(3, 4));
        coordinateSet.add(new Coordinate(3, 5));
        coordinateSet.add(new Coordinate(4, 1));
        coordinateSet.add(new Coordinate(4, 2));
        coordinateSet.add(new Coordinate(4, 3));
        coordinateSet.add(new Coordinate(4, 4));
        assertEquals(coordinateSet, basicShip.getCoordinates());
    }
}