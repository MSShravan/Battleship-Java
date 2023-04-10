package edu.duke.sm952.battleship;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleShipDisplayInfoTest {

    @Test
    public void testGetInfoOnHit(){
        ShipDisplayInfo<Character> shipDisplayInfo = new SimpleShipDisplayInfo<>('d', 'h');
        assertEquals('h', shipDisplayInfo.getInfo(null, true));
    }

    @Test
    public void testGetInfoMyData(){
        ShipDisplayInfo<Character> shipDisplayInfo = new SimpleShipDisplayInfo<>('d', 'h');
        assertEquals('d', shipDisplayInfo.getInfo(null, false));
    }
}