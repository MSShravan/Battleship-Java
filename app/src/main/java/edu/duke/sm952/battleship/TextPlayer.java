package edu.duke.sm952.battleship;

import java.io.*;
import java.util.*;
import java.util.function.Function;

public class TextPlayer {

    /**
     * The Board to play the game
     */
    final Board<Character> theBoard;

    /**
     * The Board as a view to be displayed
     */
    final BoardTextView view;

    /**
     * The input reader for user inputs
     */
    final BufferedReader inputReader;

    /**
     * The output stream displayed to the user
     */
    PrintStream out;

    /**
     * The output stream displayed to the user if computer is the player
     */
    PrintStream compOut;

    /**
     * Tells if computer is the player
     */
    boolean isComp;

    /**
     * Used to generate random coordinate
     */
    int currCoordIndex;

    /**
     * Coordinates list for comp as player
     */
    List<Coordinate> compCoordinates;

    /**
     * Used to generate random placement
     */
    int currPlacementIndex;

    /**
     * Placements list for comp as player
     */
    List<Placement> compPlacements;

    /**
     * The ship factory
     */
    final AbstractShipFactory<Character> shipFactory;

    /**
     * The name of the player
     */
    String name;

    /**
     * List of ships to e placed
     */
    final ArrayList<String> shipsToPlace;

    /**
     * The map of functions to be invoked to create the ships
     */
    final HashMap<String, Function<Placement, Ship<Character>>> shipCreationFns;

    /**
     * Tracks the number of ship moves available
     */
    int shipMoves;

    /**
     * Tracks the number of sonar scans available
     */
    int sonarScans;

    /**
     * Constructs the App with below params
     *
     * @param name        name of the player
     * @param theBoard    the game board
     * @param inputReader the user input reader
     * @param out         the output stream
     * @param shipFactory the ship factory to create different ships
     */
    public TextPlayer(String name, Board<Character> theBoard, BufferedReader inputReader, PrintStream out, AbstractShipFactory<Character> shipFactory) {
        this.theBoard = theBoard;
        this.view = new BoardTextView(this.theBoard);
        this.inputReader = inputReader;
        this.out = out;
        this.shipFactory = shipFactory;
        this.name = name;
        this.shipsToPlace = new ArrayList<>();
        this.shipCreationFns = new HashMap<>();
        setupShipCreationList();
        setupShipCreationMap();
        this.shipMoves = 3;
        this.sonarScans = 3;
        this.compOut = new PrintStream(OutputStream.nullOutputStream());
        this.isComp = false;
        this.currCoordIndex = 0;
        this.compCoordinates = new ArrayList<>();
        generateAllCoord();
        this.currPlacementIndex = 0;
        this.compPlacements = new ArrayList<>();
        generatePlacements();
    }

    /**
     * Prepares computer as the player
     */
    public void preparePlayerAsComp() {
        isComp = true;
        compOut = out;
        out = new PrintStream(OutputStream.nullOutputStream());
    }

    /**
     * Presents a prompt and accepts user input for ship placement
     *
     * @param prompt displayed to the user
     * @return Placement that is the coordinate and orientation of the ship
     */
    public Placement readPlacement(String prompt) throws IOException {
        out.println(prompt);
        String s = inputReader.readLine();
        if (s == null) throw new EOFException();
        return new Placement(s, 2);
    }

    /**
     * Places the ship as per the placement coordinates and displays the board
     *
     * @param shipName the ship to be created
     * @param createFn the function that will create the ship
     */
    public void doOnePlacement(String shipName, Function<Placement, Ship<Character>> createFn) throws IOException {
        String prompt = "Player " + name + " where do you want to place a " + shipName + "?";
        try {
            Placement placement = !isComp ? readPlacement(prompt) : getPlacementForComp();
            Ship<Character> ship = createFn.apply(placement);
            String status = theBoard.tryAddShip(ship);
            if (status != null) {
                out.println(status);
                doOnePlacement(shipName, createFn);
            } else {
                out.print(view.displayMyOwnBoard());
            }
        } catch (EOFException | IllegalArgumentException e) {
            out.println(e.getMessage());
            doOnePlacement(shipName, createFn);
        }
    }

    /**
     * Shows player prompt and places the ship as per the placement coordinates and displays the board
     */
    public void doPlacementPhase() throws IOException {
        out.println(view.displayMyOwnBoard());
        String insn_str = "Player " + name + ": you are going to place the following ships (which are all\n" +
                "rectangular). For each ship, type the coordinate of the upper left\n" +
                "side of the ship, followed by either H (for horizontal) or V (for\n" +
                "vertical).  For example M4H would place a ship horizontally starting\n" +
                "at M4 and going to the right.  You have\n" +
                "\n" +
                "2 \"Submarines\" ships that are 1x2\n" +
                "3 \"Destroyers\" that are 1x3\n" +
                "3 \"Battleships\n" +
                "2 \"Carriers";
        out.println(insn_str);
        for (String shipName : shipsToPlace) {
            doOnePlacement(shipName, shipCreationFns.get(shipName));
        }
    }

    /**
     * Presents a prompt and accepts user input for attack coordinate
     *
     * @param prompt displayed to the user
     * @return Coordinate that is the coordinate of the attack
     */
    public Coordinate readCoordinate(String prompt) throws IOException {
        out.println(prompt);
        String s = inputReader.readLine();
        if (s == null) throw new EOFException();
        return new Coordinate(s);
    }

    /**
     * Fires at the given coordinate and display the boards
     *
     * @param enemyBoard the board of the enemy
     * @param enemyView  the board view of the enemy
     * @param enemyName  the name of the enemy
     */
    public void playOneTurn(Board<Character> enemyBoard, BoardTextView enemyView, String enemyName) throws IOException {
        try {
            Coordinate where = readCoordinate("Player " + name + "'s turn:");
            Ship<Character> enemyShip = enemyBoard.fireAt(where);
            if (enemyShip != null) {
                Character what = enemyBoard.whatIsAtForEnemy(where);
                String enemyShipName;
                if (what == 's') enemyShipName = "Submarine";
                else if (what == 'b') enemyShipName = "Battleship";
                else if (what == 'd') enemyShipName = "Destroyer";
                else enemyShipName = "Carrier";
                out.println("You hit a " + enemyShipName);
            } else {
                out.println("You missed!");
            }
            out.println(view.displayMyBoardWithEnemyNextToIt(enemyView, "Your ocean", "Player " + enemyName + "'s ocean"));
        } catch (EOFException | IllegalArgumentException e) {
            out.println(e.getMessage());
            playOneTurn(enemyBoard, enemyView, enemyName);
        }
    }

    /**
     * Presents a prompt and accepts user input for action
     *
     * @param prompt displayed to the user
     * @return char that is the action
     */
    public char readActions(String prompt) throws IOException {
        out.println(prompt);
        String s = inputReader.readLine();
        if (s == null) throw new EOFException();
        char action = Character.toUpperCase(s.charAt(0));
        if (s.length() == 1 && (action == 'F' || action == 'M' || action == 'S'))
            return action;
        else
            throw new IllegalArgumentException("Actions must be F, M, or S but was " + action + "!\n");
    }

    /**
     * Plays one turn as per version 2
     *
     * @param enemyBoard the board of the enemy
     * @param enemyView  the board view of the enemy
     * @param enemyName  the name of the enemy
     */
    public void playOneTurnV2(Board<Character> enemyBoard, BoardTextView enemyView, String enemyName) throws IOException {
        try {
            String prompt = "Possible actions for Player " + name + ":\n" +
                    "\n" +
                    " F Fire at a square\n" +
                    " M Move a ship to another square (" + shipMoves + " remaining)\n" +
                    " S Sonar scan (" + sonarScans + " remaining)\n" +
                    "\n" +
                    "Player " + name + ", what would you like to do?";
            char action = !isComp ? readActions(prompt) : 'F';
            switch (action) {
                case 'M':
                    if (shipMoves == 0) {
                        throw new IllegalArgumentException("Move ship action exhausted!");
                    }
                    shipMoves--;
                    String selectShipPrompt = "Player " + name + ", which ship would you like to move? Enter coordinate:";
                    Coordinate oldCoordinate = readCoordinate(selectShipPrompt);
                    String shipName = theBoard.getShipNameByCoordinate(oldCoordinate);
                    if (shipName != null) {
                        String movePrompt = "Player " + name + ", where do you want to move " + shipName + "?";
                        Placement newPlacement = readPlacement(movePrompt);
                        Ship<Character> newShip = shipCreationFns.get(shipName).apply(newPlacement);
                        theBoard.tryMoveShip(oldCoordinate, newShip);
                        out.println(view.displayMyBoardWithEnemyNextToIt(enemyView, "Your ocean", "Player " + enemyName + "'s ocean"));
                    } else
                        throw new IllegalArgumentException("No ship found at this coordinate!\n");
                    break;
                case 'S':
                    if (sonarScans == 0) {
                        throw new IllegalArgumentException("Sonar scan action exhausted!");
                    }
                    sonarScans--;
                    String scanPrompt = "Player " + name + ", what is the sonar scan center coordinate?";
                    Coordinate scanCenter = readCoordinate(scanPrompt);
                    Map<String, Integer> scan = enemyBoard.sonarScan(scanCenter);
                    out.println("Submarines occupy " + scan.getOrDefault("Submarine", 0) + " squares\n" +
                            "Destroyers occupy " + scan.getOrDefault("Destroyer", 0) + " squares\n" +
                            "Battleships occupy " + scan.getOrDefault("Battleship", 0) + " squares\n" +
                            "Carriers occupy " + scan.getOrDefault("Carrier", 0) + " square");
                    break;
                default:
                    Coordinate where = !isComp ? readCoordinate("Player " + name + "'s turn:") : getCoordForComp();
                    Ship<Character> ship = enemyBoard.fireAt(where);
                    if (ship != null) {
                        String enemyShipName = enemyBoard.getShipNameByCoordinate(where);
                        out.println("You hit a " + enemyShipName);
                        compOut.println("Player " + name + " hit your " + enemyShipName + " at " + where.getDescr() + "!");
                    } else {
                        out.println("You missed!");
                        compOut.println("Player " + name + " missed at " + where.getDescr() + "!");
                    }
                    out.println(view.displayMyBoardWithEnemyNextToIt(enemyView, "Your ocean", "Player " + enemyName + "'s ocean"));
            }
        } catch (EOFException | IllegalArgumentException e) {
            out.println(e.getMessage());
            playOneTurnV2(enemyBoard, enemyView, enemyName);
        }
    }

    /**
     * Initializing the ship creation function map for different ships
     */
    protected void setupShipCreationMap() {
        shipCreationFns.put("Submarine", (p) -> shipFactory.makeSubmarine(p));
        shipCreationFns.put("Destroyer", (p) -> shipFactory.makeDestroyer(p));
        shipCreationFns.put("Battleship", (p) -> shipFactory.makeBattleship(p));
        shipCreationFns.put("Carrier", (p) -> shipFactory.makeCarrier(p));
    }

    /**
     * Initializing the ship creation list for multiple ships
     */
    protected void setupShipCreationList() {
        shipsToPlace.addAll(Collections.nCopies(2, "Submarine"));
        shipsToPlace.addAll(Collections.nCopies(3, "Destroyer"));
        shipsToPlace.addAll(Collections.nCopies(3, "Battleship"));
        shipsToPlace.addAll(Collections.nCopies(2, "Carrier"));
    }

    /**
     * Used when comp is the player to get a coordinate for comp player
     *
     * @return Coordinate is the coordinate location
     */
    protected Coordinate getCoordForComp() {
        return compCoordinates.get(currCoordIndex++);
    }

    /**
     * generates all possible coordinates on the board in order
     */
    protected void generateAllCoord() {
        for (int i = 0; i < theBoard.getHeight(); i++) {
            for (int j = 0; j < theBoard.getWidth(); j++) {
                Coordinate coord = new Coordinate(i, j);
                char row = (char) ('A' + i);
                coord.setDescr(String.valueOf(row) + j);
                compCoordinates.add(coord);
            }
        }
    }

    /**
     * Used when comp is the player to get a placement for comp player
     *
     * @return Placement is the placement of the ship
     */
    protected Placement getPlacementForComp() {
        return compPlacements.get(currPlacementIndex++);
    }

    /**
     * generates ship placements for comp as player
     */
    protected void generatePlacements() {
        compPlacements.add(new Placement("b2v"));
        compPlacements.add(new Placement("c8h"));

        compPlacements.add(new Placement("a4v"));
        compPlacements.add(new Placement("e5h"));
        compPlacements.add(new Placement("j3v"));

        compPlacements.add(new Placement("g3u", 2));
        compPlacements.add(new Placement("m7r", 2));
        compPlacements.add(new Placement("p2d", 2));

        compPlacements.add(new Placement("i5d", 2));
        compPlacements.add(new Placement("r3l", 2));
    }

}
