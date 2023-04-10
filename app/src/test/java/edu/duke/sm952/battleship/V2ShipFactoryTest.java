package edu.duke.sm952.battleship;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class V2ShipFactoryTest {

    @Test
    public void testMakeShips() {
        AbstractShipFactory<Character> f = new V2ShipFactory();

        Placement v1_2 = new Placement(new Coordinate(1, 2), 'V');
        Ship<Character> dst = f.makeDestroyer(v1_2);
        checkShip(dst, "Destroyer", 'd', new Coordinate(1, 2), new Coordinate(2, 2), new Coordinate(3, 2));

        Placement v5_2 = new Placement(new Coordinate(5, 2), 'u');
        Ship<Character> bts = f.makeBattleship(v5_2);
        checkShip(bts, "Battleship", 'b', new Coordinate(5, 3), new Coordinate(6, 2), new Coordinate(6, 3), new Coordinate(6, 4));

        Placement v2_2 = new Placement(new Coordinate(2, 2), 'l');
        Ship<Character> crr = f.makeCarrier(v2_2);
        checkShip(crr, "Carrier", 'c', new Coordinate(2, 4), new Coordinate(2, 5), new Coordinate(2, 6), new Coordinate(3, 2), new Coordinate(3, 3), new Coordinate(3, 4), new Coordinate(3, 5));

        Placement v6_3 = new Placement(new Coordinate(6, 3), 'H');
        Ship<Character> sbm = f.makeSubmarine(v6_3);
        checkShip(sbm, "Submarine", 's', new Coordinate(6, 3), new Coordinate(6, 4));

        Placement v6_4 = new Placement(new Coordinate(6, 3), 'Z');
        assertThrows(IllegalArgumentException.class, () -> f.makeSubmarine(v6_4));
        assertThrows(IllegalArgumentException.class, () -> f.makeBattleship(v6_4));
        assertThrows(IllegalArgumentException.class, () -> f.makeCarrier(v6_4));
    }

    @Test
    public void testGetVersion(){
        AbstractShipFactory<Character> f = new V2ShipFactory();
        assertEquals(2, f.getVersion());
    }

    private void checkShip(Ship<Character> testShip, String expectedName, char expectedLetter, Coordinate... expectedLocs) {
        assertEquals(expectedName, testShip.getName());
        for (Coordinate expectedLoc : expectedLocs)
            assertEquals(expectedLetter, testShip.getDisplayInfoAt(expectedLoc, true));
    }

}