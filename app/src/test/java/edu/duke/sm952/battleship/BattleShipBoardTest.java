package edu.duke.sm952.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class BattleShipBoardTest {
    @Test
    public void test_width_and_height() {
        Board<Character> b1 = new BattleShipBoard<>(10, 20, 'X');
        assertEquals(10, b1.getWidth());
        assertEquals(20, b1.getHeight());
    }

    @Test
    public void test_invalid_dimensions() {
        assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(10, 0, 'X'));
        assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(0, 20, 'X'));
        assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(10, -5, 'X'));
        assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(-8, 20, 'X'));
    }

    @Test
    public void testEmptyBoard() {
        checkWhatIsAtBoard(new BattleShipBoard<>(2, 3, 'X'), new Character[2][3]);
    }

    @Test
    public void testShipPlacement() {
        Coordinate c1 = new Coordinate(1, 1);
        RectangleShip<Character> s1 = new RectangleShip<Character>(c1, 's', '*');
        BattleShipBoard<Character> b1 = new BattleShipBoard<>(2, 3, 'X');
        assertNull(b1.tryAddShip(s1));
        assertNotNull(b1.tryAddShip(s1));
        Character[][] loc = new Character[2][3];
        loc[1][1] = 's';
        checkWhatIsAtBoard(b1, loc);
    }

    @Test
    public void testFireAt() {
        BattleShipBoard<Character> b1 = new BattleShipBoard<>(5, 3, 'X');
        Coordinate c1 = new Coordinate(1, 1);
        Ship<Character> ship = new RectangleShip<>(c1, 's', '*');
        b1.tryAddShip(ship);
        assertFalse(ship.isSunk());
        assertSame(ship, b1.fireAt(c1));
        assertTrue(ship.isSunk());
        assertTrue(b1.enemyMisses.isEmpty());
        assertEquals(1, b1.enemyHits.size());
        assertEquals('s', b1.enemyHits.get(c1));

        Coordinate c2 = new Coordinate(0, 0);
        assertNull(b1.fireAt(c2));
        assertEquals(1, b1.enemyMisses.size());
        assertTrue(b1.enemyMisses.contains(c2));
        assertEquals(1, b1.enemyHits.size());
        assertEquals('s', b1.enemyHits.get(c1));

        BattleShipBoard<Character> b2 = new BattleShipBoard<>(5, 3, 'X');
        assertTrue(b2.enemyMisses.isEmpty());
        assertTrue(b2.enemyHits.isEmpty());
        assertNull(b2.fireAt(c2));
        assertTrue(b2.enemyMisses.contains(c2));
        assertTrue(b2.enemyHits.isEmpty());
    }

    @Test
    public void testWhatIsAtForEnemy() {
        BattleShipBoard<Character> b1 = new BattleShipBoard<>(5, 3, 'X');
        Coordinate c1 = new Coordinate(1, 1);
        Ship<Character> ship = new RectangleShip<>(c1, 's', '*');
        b1.tryAddShip(ship);
        assertFalse(ship.isSunk());
        assertSame(ship, b1.fireAt(c1));
        assertTrue(ship.isSunk());
        assertEquals('s', b1.whatIsAtForEnemy(c1));

        Coordinate c2 = new Coordinate(0, 0);
        assertNull(b1.whatIsAtForEnemy(c2));
        assertNull(b1.fireAt(c2));
        assertEquals('X', b1.whatIsAtForEnemy(c2));
    }

    @Test
    public void testHasAllShipsSunk() {
        BattleShipBoard<Character> b1 = new BattleShipBoard<>(5, 3, 'X');
        Coordinate c1 = new Coordinate(1, 1);
        Coordinate c2 = new Coordinate(2, 1);
        Ship<Character> ship1 = new RectangleShip<>(c1, 's', '*');
        Ship<Character> ship2 = new RectangleShip<>(c2, 's', '*');
        b1.tryAddShip(ship1);
        b1.tryAddShip(ship2);
        assertFalse(b1.hasAllShipsSunk());
        b1.fireAt(c1);
        assertFalse(b1.hasAllShipsSunk());
        b1.fireAt(c2);
        assertTrue(b1.hasAllShipsSunk());
    }

    private <T> void checkWhatIsAtBoard(BattleShipBoard<T> b, T[][] expected) {
        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[0].length; j++) {
                assertEquals(b.whatIsAtForSelf(new Coordinate(i, j)), expected[i][j]);
            }
        }
    }

    @Test
    public void testSonarScan() {
        AbstractShipFactory<Character> shipFactory = new V1ShipFactory();
        BattleShipBoard<Character> myBoard = new BattleShipBoard<>(10, 20, 'X');
        myBoard.tryAddShip(shipFactory.makeSubmarine(new Placement(new Coordinate("A0"), 'V')));
        myBoard.tryAddShip(shipFactory.makeDestroyer(new Placement(new Coordinate("B2"), 'V')));
        myBoard.tryAddShip(shipFactory.makeCarrier(new Placement(new Coordinate("A8"), 'V')));
        myBoard.tryAddShip(shipFactory.makeDestroyer(new Placement(new Coordinate("F2"), 'V')));
        myBoard.tryAddShip(shipFactory.makeBattleship(new Placement(new Coordinate("G6"), 'V')));
        myBoard.tryAddShip(shipFactory.makeCarrier(new Placement(new Coordinate("K0"), 'H')));
        myBoard.tryAddShip(shipFactory.makeSubmarine(new Placement(new Coordinate("M4"), 'H')));
        myBoard.tryAddShip(shipFactory.makeBattleship(new Placement(new Coordinate("O6"), 'V')));
        myBoard.tryAddShip(shipFactory.makeDestroyer(new Placement(new Coordinate("T0"), 'H')));

        Map<String, Integer> scan = new HashMap<>();
        assertEquals(scan, myBoard.sonarScan(new Coordinate("O0")));

        scan.put("Destroyer", 1);
        scan.put("Submarine", 2);
        assertEquals(scan, myBoard.sonarScan(new Coordinate(0, 0)));

        scan = new HashMap<>();
        scan.put("Carrier", 5);
        scan.put("Submarine", 2);
        scan.put("Battleship", 1);
        assertEquals(scan, myBoard.sonarScan(new Coordinate("k4")));
    }

    @Test
    public void testMoveShip() {
        Coordinate c1 = new Coordinate(1, 1);
        RectangleShip<Character> s1 = new RectangleShip<Character>("testShip1", c1, 2, 1, 's', '*');
        BattleShipBoard<Character> b1 = new BattleShipBoard<>(4, 4, 'X');
        assertNull(b1.tryMoveShip(new Coordinate(1, 0), s1));
        assertNull(b1.tryAddShip(s1));
        assertNotNull(b1.tryAddShip(s1));
        Character[][] loc = new Character[2][3];
        loc[1][1] = 's';
        loc[1][2] = 's';
        checkWhatIsAtBoard(b1, loc);

        Coordinate c2 = new Coordinate(0, 0);
        RectangleShip<Character> s2 = new RectangleShip<Character>("testShip2", c2, 1, 2, 's', '*');
        b1.tryMoveShip(c1, s2);
        loc[1][1] = null;
        loc[1][2] = null;
        loc[0][0] = 's';
        loc[1][0] = 's';
        checkWhatIsAtBoard(b1, loc);

        b1.fireAt(new Coordinate(1, 0));
        assertEquals('s', b1.whatIsAtForEnemy(new Coordinate(1, 0)));
        assertNull(b1.whatIsAtForEnemy(new Coordinate(1, 2)));
        b1.tryMoveShip(new Coordinate(1, 0), s1);
        loc[0][0] = null;
        loc[1][0] = null;
        loc[1][1] = 's';
        loc[1][2] = '*';
        checkWhatIsAtBoard(b1, loc);
        assertEquals('s', b1.whatIsAtForEnemy(new Coordinate(1, 2)));
        assertEquals('s', b1.whatIsAtForEnemy(new Coordinate(1, 0)));
        b1.fireAt(new Coordinate(1, 0));
        assertEquals('X', b1.whatIsAtForEnemy(new Coordinate(1, 0)));

        assertNull(b1.tryMoveShip(new Coordinate(3, 3), s1));

        RectangleShip<Character> s3 = new RectangleShip<Character>("testShip3", c2, 10, 10, 's', '*');
        assertNotNull(b1.tryMoveShip(c1, s3));
        checkWhatIsAtBoard(b1, loc);
        assertEquals('s', b1.whatIsAtForEnemy(new Coordinate(1, 2)));
        assertEquals('X', b1.whatIsAtForEnemy(new Coordinate(1, 0)));
    }

    @Test
    public void testGetShipNameByCoordinate() {
        Coordinate c1 = new Coordinate(1, 1);
        RectangleShip<Character> s1 = new RectangleShip<Character>(c1, 's', '*');
        BattleShipBoard<Character> b1 = new BattleShipBoard<>(2, 3, 'X');
        assertNull(b1.getShipNameByCoordinate(c1));
        assertNull(b1.tryAddShip(s1));
        assertEquals("testship", b1.getShipNameByCoordinate(c1));
        assertNull(b1.getShipNameByCoordinate(new Coordinate(1, 2)));
    }

}
