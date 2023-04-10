package edu.duke.sm952.battleship;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoCollisionRuleCheckerTest {

    @Test
    public void testCheckMyRule() {

        AbstractShipFactory<Character> f = new V1ShipFactory();

        Placement v1_2 = new Placement(new Coordinate(1, 2), 'V');
        Ship<Character> dst = f.makeDestroyer(v1_2);

        InBoundsRuleChecker<Character> inBoundsRuleChecker = new InBoundsRuleChecker<>(null);
        NoCollisionRuleChecker<Character> noCollisionRuleChecker = new NoCollisionRuleChecker<>(inBoundsRuleChecker);
        BattleShipBoard<Character> battleShipBoard = new BattleShipBoard<>(5, 5, noCollisionRuleChecker, 'X');
        assertNull(noCollisionRuleChecker.checkMyRule(dst, battleShipBoard));
        assertNull(noCollisionRuleChecker.checkPlacement(dst, battleShipBoard));
        battleShipBoard.tryAddShip(dst);
        assertEquals("That placement is invalid: the ship overlaps another ship.", noCollisionRuleChecker.checkMyRule(dst, battleShipBoard));

    }

}