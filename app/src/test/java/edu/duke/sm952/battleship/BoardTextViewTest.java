package edu.duke.sm952.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BoardTextViewTest {
    @Test
    public void test_display_empty_2by2() {
        Board<Character> b1 = new BattleShipBoard<>(2, 2, 'X');
        BoardTextView view = new BoardTextView(b1);
        String expectedHeader = "  0|1\n";
        assertEquals(expectedHeader, view.makeHeader());
        String expected = expectedHeader +
                "A  |  A\n" +
                "B  |  B\n" +
                expectedHeader;
        assertEquals(expected, view.displayMyOwnBoard());

    }

    @Test
    public void test_invalid_board_size() {
        Board<Character> wideBoard = new BattleShipBoard<>(11, 20, 'X');
        Board<Character> tallBoard = new BattleShipBoard<>(10, 27, 'X');
        assertThrows(IllegalArgumentException.class, () -> new BoardTextView(wideBoard));
        assertThrows(IllegalArgumentException.class, () -> new BoardTextView(tallBoard));
    }

    @Test
    public void test_display_empty_3by2() {
        String expectedHeader = "  0|1|2\n";
        String expectedBody = "A  | |  A\n" +
                "B  | |  B\n";
        emptyBoardHelper(3, 2, expectedHeader, expectedBody);
    }

    @Test
    public void test_display_empty_3by5() {
        String expectedHeader = "  0|1|2\n";
        String expectedBody = "A  | |  A\n" +
                "B  | |  B\n" +
                "C  | |  C\n" +
                "D  | |  D\n" +
                "E  | |  E\n";
        emptyBoardHelper(3, 5, expectedHeader, expectedBody);
    }

    @Test
    public void test_display_3by5() {
        String expectedHeader = "  0|1|2\n";
        String expectedBody = "A  | |  A\n" +
                "B  |s|  B\n" +
                "C  | |  C\n" +
                "D  | |  D\n" +
                "E s| |  E\n";
        Board<Character> b1 = new BattleShipBoard<>(3, 5, 'X');
        b1.tryAddShip(new RectangleShip<Character>(new Coordinate(1, 1), 's', '*'));
        b1.tryAddShip(new RectangleShip<Character>(new Coordinate(4, 0), 's', '*'));
        BoardTextView view = new BoardTextView(b1);
        assertEquals(expectedHeader, view.makeHeader());
        String expected = expectedHeader + expectedBody + expectedHeader;
        assertEquals(expected, view.displayMyOwnBoard());
    }

    @Test
    public void test_display_enemy_3by5() {
        String expectedHeader = "  0|1|2\n";
        String expectedBody = "A  | |  A\n" +
                "B  |s|  B\n" +
                "C  | |  C\n" +
                "D  | |  D\n" +
                "E s| |  E\n";
        Board<Character> b1 = new BattleShipBoard<>(3, 5, 'X');
        b1.tryAddShip(new RectangleShip<Character>(new Coordinate(1, 1), 's', '*'));
        b1.tryAddShip(new RectangleShip<Character>(new Coordinate(4, 0), 's', '*'));
        BoardTextView view = new BoardTextView(b1);
        assertEquals(expectedHeader, view.makeHeader());
        String expected = expectedHeader + expectedBody + expectedHeader;
        assertEquals(expected, view.displayMyOwnBoard());

        String expectedEnemyBody = "A  | |  A\n" +
                "B  |s|  B\n" +
                "C  |X|  C\n" +
                "D  | |X D\n" +
                "E s| |  E\n";
        Coordinate c1 = new Coordinate(1, 1);
        Coordinate c2 = new Coordinate(2, 1);
        Coordinate c3 = new Coordinate(3, 2);
        Coordinate c4 = new Coordinate(4, 0);
        b1.fireAt(c1);
        b1.fireAt(c2);
        b1.fireAt(c3);
        b1.fireAt(c4);
        String expectedEnemy = expectedHeader + expectedEnemyBody + expectedHeader;
        assertEquals(expectedEnemy, view.displayEnemyBoard());
    }

    @Test
    public void testDisplayMyBoardWithEnemyNextToIt() {
        String expected = "     Your ocean                           Player B's ocean\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "A s| | | | | | | |c|  A                A  | | | | | | | | |  A\n" +
                "B s| |d| | | | | |c|  B                B  | | | | | | | | |  B\n" +
                "C  | |*| | | | | |c|  C                C  | |X| | | | | | |  C\n" +
                "D  | |d| | | | | |c|  D                D  |X|d|d| | | | | |  D\n" +
                "E  | | | | | | | |c|  E                E  | |X| | | | | | |  E\n" +
                "F  | |d| | | | | |c|  F                F  | | | | | | | | |  F\n" +
                "G  | |d| | | |b| | |  G                G  | | | | | | | | |  G\n" +
                "H  | |d| | | |b| | |  H                H  | | | | | | | | |  H\n" +
                "I  | | | | | |b| | |  I                I  | | | | | | | | |  I\n" +
                "J  | | | | | |b| | |  J                J  | | |X| | | | | |  J\n" +
                "K c|c|c|c|c|c| | | |  K                K  | | | | | | | | |  K\n" +
                "L  | | | | | | | | |  L                L  | | | |X| | | | |  L\n" +
                "M  | | | |s|s| | | |  M                M  | | | | | | | | |  M\n" +
                "N  | | | | | | | | |  N                N  | | | | | | | | |  N\n" +
                "O  | | | | | |b| | |  O                O  | | | | |s|s| | |  O\n" +
                "P  | | | | | |b| | |  P                P  | | | | | | | | |  P\n" +
                "Q  | | | | | |b| | |  Q                Q  | | | | | | | | |  Q\n" +
                "R  | | | | | |b| | |  R                R  | | | | | | | | |  R\n" +
                "S  | | | | | | | | |  S                S  | | | | | | | | |  S\n" +
                "T d|d|d| | | | | | |  T                T  | | | | | | | | |  T\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n";
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
        myBoard.fireAt(new Coordinate("C2"));

        BattleShipBoard<Character> enemyBoard = new BattleShipBoard<>(10, 20, 'X');
        enemyBoard.tryAddShip(shipFactory.makeSubmarine(new Placement(new Coordinate("O5"), 'H')));
        enemyBoard.tryAddShip(shipFactory.makeDestroyer(new Placement(new Coordinate("D2"), 'H')));
        enemyBoard.fireAt(new Coordinate("C2"));
        enemyBoard.fireAt(new Coordinate("D1"));
        enemyBoard.fireAt(new Coordinate("D2"));
        enemyBoard.fireAt(new Coordinate("D3"));
        enemyBoard.fireAt(new Coordinate("E2"));
        enemyBoard.fireAt(new Coordinate("J3"));
        enemyBoard.fireAt(new Coordinate("L4"));
        enemyBoard.fireAt(new Coordinate("O5"));
        enemyBoard.fireAt(new Coordinate("O6"));

        BoardTextView boardTextView = new BoardTextView(myBoard);
        BoardTextView enemyBoardTextView = new BoardTextView(enemyBoard);
        assertEquals(expected, boardTextView.displayMyBoardWithEnemyNextToIt(enemyBoardTextView, "Your ocean", "Player B's ocean"));
    }

    private void emptyBoardHelper(int w, int h, String expectedHeader, String expectedBody) {
        Board<Character> b1 = new BattleShipBoard<>(w, h, 'X');
        BoardTextView view = new BoardTextView(b1);
        assertEquals(expectedHeader, view.makeHeader());
        String expected = expectedHeader + expectedBody + expectedHeader;
        assertEquals(expected, view.displayMyOwnBoard());
    }

}
