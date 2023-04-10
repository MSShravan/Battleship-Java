package edu.duke.sm952.battleship;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class TextPlayerTest {

    @Test
    void test_read_placement() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player = createTextPlayer(10, 20, "B2V\nC8H\na4v\n", bytes);

        String prompt = "Please enter a location for a ship:";
        Placement[] expected = new Placement[3];
        expected[0] = new Placement(new Coordinate(1, 2), 'V');
        expected[1] = new Placement(new Coordinate(2, 8), 'H');
        expected[2] = new Placement(new Coordinate(0, 4), 'V');

        for (int i = 0; i < expected.length; i++) {
            Placement p = player.readPlacement(prompt);
            assertEquals(p, expected[i]); //did we get the right Placement back
            assertEquals(prompt + "\n", bytes.toString()); //should have printed prompt and newline
            bytes.reset(); //clear out bytes for next time around
        }

        TextPlayer player2 = createTextPlayer(10, 20, "", bytes);
        assertThrows(EOFException.class, () -> player2.readPlacement(prompt));

    }

    @Test
    public void testReadCoordinate() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player = createTextPlayer(10, 20, "B2\nC8\na4\n", bytes);

        String prompt = "Please enter a coordinate to attack:";
        Coordinate[] expected = new Coordinate[3];
        expected[0] = new Coordinate(1, 2);
        expected[1] = new Coordinate(2, 8);
        expected[2] = new Coordinate(0, 4);

        for (int i = 0; i < expected.length; i++) {
            Coordinate c = player.readCoordinate(prompt);
            assertEquals(c, expected[i]); //did we get the right Coordinate back
            assertEquals(prompt + "\n", bytes.toString()); //should have printed prompt and newline
            bytes.reset(); //clear out bytes for next time around
        }

        TextPlayer player2 = createTextPlayer(10, 20, "", bytes);
        assertThrows(EOFException.class, () -> player2.readCoordinate(prompt));
    }

    @Test
    void test_do_one_placement() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player = createTextPlayer(10, 20, "B2V\nC8H\na4v\n", bytes);
        AbstractShipFactory<Character> shipFactory = new V1ShipFactory();
        player.doOnePlacement("Destroyer", (p) -> shipFactory.makeDestroyer(p));

        String prompt = "Player " + player.name + " where do you want to place a Destroyer?\n";
        assertEquals(prompt + player.view.displayMyOwnBoard(), bytes.toString());
    }

    @Test
    void test_do_one_placement_invalidAddShip() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player = createTextPlayer(10, 20, "B9H\nB2V\n", bytes);
        AbstractShipFactory<Character> shipFactory = new V1ShipFactory();
        player.doOnePlacement("Destroyer", (p) -> shipFactory.makeDestroyer(p));

        String prompt = "Player " + player.name + " where do you want to place a Destroyer?\n";
        String expMsg = "That placement is invalid: the ship goes off the right of the board.\n";
        assertEquals(prompt + expMsg + prompt + player.view.displayMyOwnBoard(), bytes.toString());
    }

    @Test
    void test_do_one_placement_invalidAddShipPlacement() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player = createTextPlayer(10, 20, "B9C\nB2V\n", bytes);
        AbstractShipFactory<Character> shipFactory = new V1ShipFactory();
        player.doOnePlacement("Destroyer", (p) -> shipFactory.makeDestroyer(p));

        String prompt = "Player " + player.name + " where do you want to place a Destroyer?\n";
        String expMsg = "That placement is invalid: it does not have the correct format.\n";
        assertEquals(prompt + expMsg + prompt + player.view.displayMyOwnBoard(), bytes.toString());
    }

    @Test
    void testDoPlacementPhase() throws IOException {
        InputStream expectedInputStream = getClass().getClassLoader().getResourceAsStream("input_A.txt");
        assertNotNull(expectedInputStream);
        String expectedInput = new String(expectedInputStream.readAllBytes());

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player = createTextPlayer(10, 20, expectedInput, bytes);
        player.doPlacementPhase();

        InputStream expectedStream = getClass().getClassLoader().getResourceAsStream("output_A.txt");
        assertNotNull(expectedStream);
        String expected = new String(expectedStream.readAllBytes());
        assertEquals(expected, bytes.toString());
    }

    private TextPlayer createTextPlayerV1(int w, int h, String inputData, OutputStream bytes) {
        BufferedReader input = new BufferedReader(new StringReader(inputData));
        PrintStream output = new PrintStream(bytes, true);
        Board<Character> board = new BattleShipBoard<Character>(w, h, 'X');
        V1ShipFactory shipFactory = new V1ShipFactory();
        return new TextPlayer("A", board, input, output, shipFactory);
    }

    private TextPlayer createTextPlayer(int w, int h, String inputData, OutputStream bytes) {
        BufferedReader input = new BufferedReader(new StringReader(inputData));
        PrintStream output = new PrintStream(bytes, true);
        Board<Character> board = new BattleShipBoard<Character>(w, h, 'X');
        V2ShipFactory shipFactory = new V2ShipFactory();
        return new TextPlayer("A", board, input, output, shipFactory);
    }

    @Test
    public void testPlayOneTurn() throws IOException {
        String expected = "Player A's turn:\n" +
                "Coordinate string must be of size two. e.g. A2, D1\n" +
                "Player A's turn:\n" +
                "You hit a Destroyer\n" +
                "     Your ocean                           Player B's ocean\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                "B  | | | | | | | | |  B                B  | | | | | | | | |  B\n" +
                "C  | | | | | | | | |  C                C  | |X|d| | | | | |  C\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "\n" +
                "Player A's turn:\n" +
                "You missed!\n" +
                "     Your ocean                           Player B's ocean\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                "B  | | | | | | | | |  B                B  | | | | | | | | |  B\n" +
                "C  | | | | | | | | |  C                C  | |X|d| | | | | |  C\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "\n" +
                "Player A's turn:\n" +
                "You missed!\n" +
                "     Your ocean                           Player B's ocean\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                "B  | | | | | | | | |  B                B  | | | | | | | | |  B\n" +
                "C  | | | | | | | | |  C                C  | |X|d| | | | | |  C\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "\n" +
                "Player A's turn:\n" +
                "You hit a Carrier\n" +
                "     Your ocean                           Player B's ocean\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                "B  | | | | | | | | |  B                B  | | |c| | | | | |  B\n" +
                "C  | | | | | | | | |  C                C  | |X|d| | | | | |  C\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "\n" +
                "Player A's turn:\n" +
                "You hit a Battleship\n" +
                "     Your ocean                           Player B's ocean\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "A  | | | | | | | | |  A                A b| | | | | | | | |  A\n" +
                "B  | | | | | | | | |  B                B  | | |c| | | | | |  B\n" +
                "C  | | | | | | | | |  C                C  | |X|d| | | | | |  C\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "\n" +
                "Player A's turn:\n" +
                "You hit a Submarine\n" +
                "     Your ocean                           Player B's ocean\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "A  | | | | | | | | |  A                A b| | | | | | | |s|  A\n" +
                "B  | | | | | | | | |  B                B  | | |c| | | | | |  B\n" +
                "C  | | | | | | | | |  C                C  | |X|d| | | | | |  C\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "\n";
        AbstractShipFactory<Character> shipFactory = new V1ShipFactory();

        BattleShipBoard<Character> enemyBoard = new BattleShipBoard<>(10, 3, 'X');
        enemyBoard.tryAddShip(shipFactory.makeSubmarine(new Placement(new Coordinate("a8"), 'H')));
        enemyBoard.tryAddShip(shipFactory.makeBattleship(new Placement(new Coordinate("a0"), 'H')));
        enemyBoard.tryAddShip(shipFactory.makeDestroyer(new Placement(new Coordinate("C3"), 'H')));
        enemyBoard.tryAddShip(shipFactory.makeCarrier(new Placement(new Coordinate("B3"), 'H')));
        enemyBoard.fireAt(new Coordinate("C2"));

        BoardTextView enemyBoardTextView = new BoardTextView(enemyBoard);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player = createTextPlayerV1(10, 3, "c3h\nc3\nc2\nc2\nb3\na0\na8\n", bytes);

        player.playOneTurn(enemyBoard, enemyBoardTextView, "B");
        player.playOneTurn(enemyBoard, enemyBoardTextView, "B");
        player.playOneTurn(enemyBoard, enemyBoardTextView, "B");
        player.playOneTurn(enemyBoard, enemyBoardTextView, "B");
        player.playOneTurn(enemyBoard, enemyBoardTextView, "B");
        player.playOneTurn(enemyBoard, enemyBoardTextView, "B");
        assertEquals(expected, bytes.toString());
    }

    @Test
    public void testReadAction() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String prompt = "Player A, what would you like to do?";

        TextPlayer player1 = createTextPlayer(10, 20, "F\n", bytes);
        assertEquals('F', player1.readActions(prompt));
        bytes.reset();

        TextPlayer player2 = createTextPlayer(10, 20, "M\n", bytes);
        assertEquals('M', player2.readActions(prompt));
        bytes.reset();

        TextPlayer player3 = createTextPlayer(10, 20, "S\n", bytes);
        assertEquals('S', player3.readActions(prompt));
        bytes.reset();

        TextPlayer player4 = createTextPlayer(10, 20, "", bytes);
        assertThrows(EOFException.class, () -> player4.readActions(prompt));

        TextPlayer player5 = createTextPlayer(10, 20, "X\n", bytes);
        assertThrows(IllegalArgumentException.class, () -> player5.readActions(prompt));

        TextPlayer player6 = createTextPlayer(10, 20, "FMS\n", bytes);
        assertThrows(IllegalArgumentException.class, () -> player6.readActions(prompt));

    }

    @Test
    public void testPlayOneTurnV2() throws IOException {
        String expected = "Possible actions for Player A:\n" +
                "\n" +
                " F Fire at a square\n" +
                " M Move a ship to another square (3 remaining)\n" +
                " S Sonar scan (3 remaining)\n" +
                "\n" +
                "Player A, what would you like to do?\n" +
                "Player A's turn:\n" +
                "Coordinate string must be of size two. e.g. A2, D1\n" +
                "Possible actions for Player A:\n" +
                "\n" +
                " F Fire at a square\n" +
                " M Move a ship to another square (3 remaining)\n" +
                " S Sonar scan (3 remaining)\n" +
                "\n" +
                "Player A, what would you like to do?\n" +
                "Player A's turn:\n" +
                "You hit a Destroyer\n" +
                "     Your ocean                           Player B's ocean\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                "B  | | | | | | | | |  B                B  | | | | | | | | |  B\n" +
                "C  | | | | | | | | |  C                C  | |X|d| | | | | |  C\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "\n";

        AbstractShipFactory<Character> shipFactory = new V1ShipFactory();

        BattleShipBoard<Character> enemyBoard = new BattleShipBoard<>(10, 3, 'X');
        enemyBoard.tryAddShip(shipFactory.makeDestroyer(new Placement(new Coordinate("C3"), 'H')));
        enemyBoard.fireAt(new Coordinate("C2"));

        BoardTextView enemyBoardTextView = new BoardTextView(enemyBoard);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player = createTextPlayer(10, 3, "F\nc3h\nF\nc3\n", bytes);

        player.playOneTurnV2(enemyBoard, enemyBoardTextView, "B");
        assertEquals(expected, bytes.toString());
        bytes.reset();

        TextPlayer player2 = createTextPlayer(10, 3, "F\nc3h\nF\nc2\n", bytes);

        player2.playOneTurnV2(enemyBoard, enemyBoardTextView, "B");
        expected = "Possible actions for Player A:\n" +
                "\n" +
                " F Fire at a square\n" +
                " M Move a ship to another square (3 remaining)\n" +
                " S Sonar scan (3 remaining)\n" +
                "\n" +
                "Player A, what would you like to do?\n" +
                "Player A's turn:\n" +
                "Coordinate string must be of size two. e.g. A2, D1\n" +
                "Possible actions for Player A:\n" +
                "\n" +
                " F Fire at a square\n" +
                " M Move a ship to another square (3 remaining)\n" +
                " S Sonar scan (3 remaining)\n" +
                "\n" +
                "Player A, what would you like to do?\n" +
                "Player A's turn:\n" +
                "You missed!\n" +
                "     Your ocean                           Player B's ocean\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                "B  | | | | | | | | |  B                B  | | | | | | | | |  B\n" +
                "C  | | | | | | | | |  C                C  | |X|d| | | | | |  C\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "\n";
        assertEquals(expected, bytes.toString());
    }

    @Test
    public void testPlayOneTurnV2ToMoveShip() throws IOException {
        String expected = "Possible actions for Player A:\n" +
                "\n" +
                " F Fire at a square\n" +
                " M Move a ship to another square (3 remaining)\n" +
                " S Sonar scan (3 remaining)\n" +
                "\n" +
                "Player A, what would you like to do?\n" +
                "Player A, which ship would you like to move? Enter coordinate:\n" +
                "Coordinate string must be of size two. e.g. A2, D1\n" +
                "Possible actions for Player A:\n" +
                "\n" +
                " F Fire at a square\n" +
                " M Move a ship to another square (2 remaining)\n" +
                " S Sonar scan (3 remaining)\n" +
                "\n" +
                "Player A, what would you like to do?\n" +
                "Player A, which ship would you like to move? Enter coordinate:\n" +
                "No ship found at this coordinate!\n" +
                "\n" +
                "Possible actions for Player A:\n" +
                "\n" +
                " F Fire at a square\n" +
                " M Move a ship to another square (1 remaining)\n" +
                " S Sonar scan (3 remaining)\n" +
                "\n" +
                "Player A, what would you like to do?\n" +
                "Player A, which ship would you like to move? Enter coordinate:\n" +
                "Player A, where do you want to move Submarine?\n" +
                "     Your ocean                           Player B's ocean\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "A  | | | |*| | | | |  A                A  | | | | | | | | |  A\n" +
                "B  | | | |s| | | | |  B                B  | | | | | | | | |  B\n" +
                "C  | | | | | | | | |  C                C  | |X| | | | | | |  C\n" +
                "D  | | | | | | | | |  D                D  | | | | | | | | |  D\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "\n" +
                "Possible actions for Player A:\n" +
                "\n" +
                " F Fire at a square\n" +
                " M Move a ship to another square (0 remaining)\n" +
                " S Sonar scan (3 remaining)\n" +
                "\n" +
                "Player A, what would you like to do?\n" +
                "Move ship action exhausted!\n" +
                "Possible actions for Player A:\n" +
                "\n" +
                " F Fire at a square\n" +
                " M Move a ship to another square (0 remaining)\n" +
                " S Sonar scan (3 remaining)\n" +
                "\n" +
                "Player A, what would you like to do?\n" +
                "Actions must be F, M, or S but was C!\n" +
                "\n" +
                "Possible actions for Player A:\n" +
                "\n" +
                " F Fire at a square\n" +
                " M Move a ship to another square (0 remaining)\n" +
                " S Sonar scan (3 remaining)\n" +
                "\n" +
                "Player A, what would you like to do?\n" +
                "Actions must be F, M, or S but was A!\n" +
                "\n" +
                "Possible actions for Player A:\n" +
                "\n" +
                " F Fire at a square\n" +
                " M Move a ship to another square (0 remaining)\n" +
                " S Sonar scan (3 remaining)\n" +
                "\n" +
                "Player A, what would you like to do?\n" +
                "Player A's turn:\n" +
                "You missed!\n" +
                "     Your ocean                           Player B's ocean\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "A  | | | |*| | | | |  A                A X| | | | | | | | |  A\n" +
                "B  | | | |s| | | | |  B                B  | | | | | | | | |  B\n" +
                "C  | | | | | | | | |  C                C  | |X| | | | | | |  C\n" +
                "D  | | | | | | | | |  D                D  | | | | | | | | |  D\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "\n";

        AbstractShipFactory<Character> shipFactory = new V1ShipFactory();
        BattleShipBoard<Character> enemyBoard = new BattleShipBoard<>(10, 4, 'X');
        enemyBoard.tryAddShip(shipFactory.makeDestroyer(new Placement(new Coordinate("C3"), 'H')));
        enemyBoard.fireAt(new Coordinate("C2"));
        BoardTextView enemyBoardTextView = new BoardTextView(enemyBoard);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player = createTextPlayer(10, 4, "M\nc3h\nM\nc1\nM\nc4\na4v\nM\nc4\na0v\nF\na0\n", bytes);

        player.theBoard.tryAddShip(shipFactory.makeSubmarine(new Placement(new Coordinate("C3"), 'H')));
        player.theBoard.fireAt(new Coordinate("C2"));
        player.theBoard.fireAt(new Coordinate("C3"));

        player.playOneTurnV2(enemyBoard, enemyBoardTextView, "B");
        player.playOneTurnV2(enemyBoard, enemyBoardTextView, "B");
        assertEquals(expected, bytes.toString());
    }

    @Test
    public void testPlayOneTurnV2SonarScan() throws IOException {
        String expected = "Possible actions for Player A:\n" +
                "\n" +
                " F Fire at a square\n" +
                " M Move a ship to another square (3 remaining)\n" +
                " S Sonar scan (3 remaining)\n" +
                "\n" +
                "Player A, what would you like to do?\n" +
                "Player A, what is the sonar scan center coordinate?\n" +
                "Coordinate string must be of size two. e.g. A2, D1\n" +
                "Possible actions for Player A:\n" +
                "\n" +
                " F Fire at a square\n" +
                " M Move a ship to another square (3 remaining)\n" +
                " S Sonar scan (2 remaining)\n" +
                "\n" +
                "Player A, what would you like to do?\n" +
                "Player A, what is the sonar scan center coordinate?\n" +
                "Submarines occupy 0 squares\n" +
                "Destroyers occupy 2 squares\n" +
                "Battleships occupy 0 squares\n" +
                "Carriers occupy 0 square\n";

        AbstractShipFactory<Character> shipFactory = new V1ShipFactory();

        BattleShipBoard<Character> enemyBoard = new BattleShipBoard<>(10, 3, 'X');
        enemyBoard.tryAddShip(shipFactory.makeDestroyer(new Placement(new Coordinate("C3"), 'H')));
        enemyBoard.fireAt(new Coordinate("C2"));

        BoardTextView enemyBoardTextView = new BoardTextView(enemyBoard);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player = createTextPlayer(10, 3, "S\nc3h\nS\na3\n", bytes);

        player.playOneTurnV2(enemyBoard, enemyBoardTextView, "B");
        assertEquals(expected, bytes.toString());

        bytes.reset();
        TextPlayer player2 = createTextPlayer(10, 3, "S\nc3\nS\na3\nS\nb1\nS\nM\nc1\nM\nc1\nM\nc1\nM\nc1\nF\nc3\n", bytes);
        player2.playOneTurnV2(enemyBoard, enemyBoardTextView, "B");
        player2.playOneTurnV2(enemyBoard, enemyBoardTextView, "B");
        player2.playOneTurnV2(enemyBoard, enemyBoardTextView, "B");
        player2.playOneTurnV2(enemyBoard, enemyBoardTextView, "B");
        expected = "Possible actions for Player A:\n" +
                "\n" +
                " F Fire at a square\n" +
                " M Move a ship to another square (3 remaining)\n" +
                " S Sonar scan (3 remaining)\n" +
                "\n" +
                "Player A, what would you like to do?\n" +
                "Player A, what is the sonar scan center coordinate?\n" +
                "Submarines occupy 0 squares\n" +
                "Destroyers occupy 3 squares\n" +
                "Battleships occupy 0 squares\n" +
                "Carriers occupy 0 square\n" +
                "Possible actions for Player A:\n" +
                "\n" +
                " F Fire at a square\n" +
                " M Move a ship to another square (3 remaining)\n" +
                " S Sonar scan (2 remaining)\n" +
                "\n" +
                "Player A, what would you like to do?\n" +
                "Player A, what is the sonar scan center coordinate?\n" +
                "Submarines occupy 0 squares\n" +
                "Destroyers occupy 2 squares\n" +
                "Battleships occupy 0 squares\n" +
                "Carriers occupy 0 square\n" +
                "Possible actions for Player A:\n" +
                "\n" +
                " F Fire at a square\n" +
                " M Move a ship to another square (3 remaining)\n" +
                " S Sonar scan (1 remaining)\n" +
                "\n" +
                "Player A, what would you like to do?\n" +
                "Player A, what is the sonar scan center coordinate?\n" +
                "Submarines occupy 0 squares\n" +
                "Destroyers occupy 1 squares\n" +
                "Battleships occupy 0 squares\n" +
                "Carriers occupy 0 square\n" +
                "Possible actions for Player A:\n" +
                "\n" +
                " F Fire at a square\n" +
                " M Move a ship to another square (3 remaining)\n" +
                " S Sonar scan (0 remaining)\n" +
                "\n" +
                "Player A, what would you like to do?\n" +
                "Sonar scan action exhausted!\n" +
                "Possible actions for Player A:\n" +
                "\n" +
                " F Fire at a square\n" +
                " M Move a ship to another square (3 remaining)\n" +
                " S Sonar scan (0 remaining)\n" +
                "\n" +
                "Player A, what would you like to do?\n" +
                "Player A, which ship would you like to move? Enter coordinate:\n" +
                "No ship found at this coordinate!\n" +
                "\n" +
                "Possible actions for Player A:\n" +
                "\n" +
                " F Fire at a square\n" +
                " M Move a ship to another square (2 remaining)\n" +
                " S Sonar scan (0 remaining)\n" +
                "\n" +
                "Player A, what would you like to do?\n" +
                "Player A, which ship would you like to move? Enter coordinate:\n" +
                "No ship found at this coordinate!\n" +
                "\n" +
                "Possible actions for Player A:\n" +
                "\n" +
                " F Fire at a square\n" +
                " M Move a ship to another square (1 remaining)\n" +
                " S Sonar scan (0 remaining)\n" +
                "\n" +
                "Player A, what would you like to do?\n" +
                "Player A, which ship would you like to move? Enter coordinate:\n" +
                "No ship found at this coordinate!\n" +
                "\n" +
                "Possible actions for Player A:\n" +
                "\n" +
                " F Fire at a square\n" +
                " M Move a ship to another square (0 remaining)\n" +
                " S Sonar scan (0 remaining)\n" +
                "\n" +
                "Player A, what would you like to do?\n" +
                "Move ship action exhausted!\n" +
                "Possible actions for Player A:\n" +
                "\n" +
                " F Fire at a square\n" +
                " M Move a ship to another square (0 remaining)\n" +
                " S Sonar scan (0 remaining)\n" +
                "\n" +
                "Player A, what would you like to do?\n" +
                "Actions must be F, M, or S but was C!\n" +
                "\n" +
                "Possible actions for Player A:\n" +
                "\n" +
                " F Fire at a square\n" +
                " M Move a ship to another square (0 remaining)\n" +
                " S Sonar scan (0 remaining)\n" +
                "\n" +
                "Player A, what would you like to do?\n" +
                "Player A's turn:\n" +
                "You hit a Destroyer\n" +
                "     Your ocean                           Player B's ocean\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "A  | | | | | | | | |  A                A  | | | | | | | | |  A\n" +
                "B  | | | | | | | | |  B                B  | | | | | | | | |  B\n" +
                "C  | | | | | | | | |  C                C  | |X|d| | | | | |  C\n" +
                "  0|1|2|3|4|5|6|7|8|9                    0|1|2|3|4|5|6|7|8|9\n" +
                "\n";
        assertEquals(expected, bytes.toString());
    }

    @Test
    public void testPlayOneTurnV2AsComp() throws IOException {
        String expected = "Player A missed at A0!\n";

        AbstractShipFactory<Character> shipFactory = new V1ShipFactory();

        BattleShipBoard<Character> enemyBoard = new BattleShipBoard<>(10, 3, 'X');
        enemyBoard.tryAddShip(shipFactory.makeDestroyer(new Placement(new Coordinate("C3"), 'H')));
        enemyBoard.fireAt(new Coordinate("C2"));

        BoardTextView enemyBoardTextView = new BoardTextView(enemyBoard);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        TextPlayer player = createTextPlayer(10, 3, "", bytes);
        player.preparePlayerAsComp();

        player.playOneTurnV2(enemyBoard, enemyBoardTextView, "B");
        assertEquals(expected, bytes.toString());
        bytes.reset();

        TextPlayer player2 = createTextPlayer(10, 3, "", bytes);
        player2.preparePlayerAsComp();
        expected = "Player A hit your Destroyer at A0!\n";
        enemyBoard.tryAddShip(shipFactory.makeDestroyer(new Placement(new Coordinate("a0"), 'H')));
        player2.playOneTurnV2(enemyBoard, enemyBoardTextView, "B");
        assertEquals(expected, bytes.toString());

        bytes.reset();
        TextPlayer player3 = createTextPlayer(10, 20, "", bytes);
        player3.preparePlayerAsComp();
        for (String shipName : player3.shipsToPlace) {
            player3.doOnePlacement(shipName, player3.shipCreationFns.get(shipName));
        }
        String expectedBoardDisplay = "  0|1|2|3|4|5|6|7|8|9\n" +
                "A  | | | |d| | | | |  A\n" +
                "B  | |s| |d| | | | |  B\n" +
                "C  | |s| |d| | | |s|s C\n" +
                "D  | | | | | | | | |  D\n" +
                "E  | | | | |d|d|d| |  E\n" +
                "F  | | | | | | | | |  F\n" +
                "G  | | | |b| | | | |  G\n" +
                "H  | | |b|b|b| | | |  H\n" +
                "I  | | | | |c| | | |  I\n" +
                "J  | | |d| |c|c| | |  J\n" +
                "K  | | |d| |c|c| | |  K\n" +
                "L  | | |d| | |c| | |  L\n" +
                "M  | | | | | |c|b| |  M\n" +
                "N  | | | | | | |b|b|  N\n" +
                "O  | | | | | | |b| |  O\n" +
                "P  | |b|b|b| | | | |  P\n" +
                "Q  | | |b| | | | | |  Q\n" +
                "R  | | | | |c|c|c| |  R\n" +
                "S  | | |c|c|c|c| | |  S\n" +
                "T  | | | | | | | | |  T\n" +
                "  0|1|2|3|4|5|6|7|8|9\n";
        assertEquals(expectedBoardDisplay, player3.view.displayMyOwnBoard());

    }
}