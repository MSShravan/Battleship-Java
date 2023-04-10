package edu.duke.sm952.battleship;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InBoundsRuleCheckerTest {

    @Test
    public void testCheckMyRule() {
        AbstractShipFactory<Character> f = new V1ShipFactory();

        Placement v1_2 = new Placement(new Coordinate(1, 2), 'V');
        Ship<Character> dst = f.makeDestroyer(v1_2);

        InBoundsRuleChecker<Character> inBoundsRuleChecker = new InBoundsRuleChecker<>(null);
        assertNull(inBoundsRuleChecker.checkMyRule(dst, new BattleShipBoard<>(5, 5, 'X')));
        assertEquals("That placement is invalid: the ship goes off the bottom of the board.", inBoundsRuleChecker.checkMyRule(dst, new BattleShipBoard<>(2, 2, 'X')));

        Placement v1_5 = new Placement(new Coordinate(1, 5), 'V');
        Ship<Character> dst_5 = f.makeDestroyer(v1_5);
        assertEquals("That placement is invalid: the ship goes off the right of the board.", inBoundsRuleChecker.checkMyRule(dst_5, new BattleShipBoard<>(3, 7, 'X')));

        Placement v1_2_neg = new Placement(new Coordinate(1, -2), 'V');
        Ship<Character> dst_neg = f.makeDestroyer(v1_2_neg);
        assertEquals("That placement is invalid: the ship goes off the left of the board.", inBoundsRuleChecker.checkMyRule(dst_neg, new BattleShipBoard<>(5, 5, 'X')));

        Placement v1_neg_2 = new Placement(new Coordinate(-1, 2), 'V');
        Ship<Character> neg_dst = f.makeDestroyer(v1_neg_2);
        assertEquals("That placement is invalid: the ship goes off the top of the board.", inBoundsRuleChecker.checkMyRule(neg_dst, new BattleShipBoard<>(5, 5, 'X')));

    }

    @Test
    public void testCheckPlacement() {
        AbstractShipFactory<Character> f = new V1ShipFactory();

        Placement v1_2 = new Placement(new Coordinate(1, 2), 'V');
        Ship<Character> dst = f.makeDestroyer(v1_2);

        InBoundsRuleChecker<Character> inBoundsRuleChecker = new InBoundsRuleChecker<>(null);

        assertNull(inBoundsRuleChecker.checkPlacement(dst, new BattleShipBoard<>(5, 5, inBoundsRuleChecker, 'X')));
        assertEquals("That placement is invalid: the ship goes off the bottom of the board.", inBoundsRuleChecker.checkPlacement(dst, new BattleShipBoard<>(2, 2, inBoundsRuleChecker, 'X')));

    }

}