
package edu.duke.sm952.battleship;

import java.util.*;

/**
 * The Battleship board that takes in generic T of ships
 */
public class BattleShipBoard<T> implements Board<T> {

    /**
     * Width of the Board
     */
    private final int width;

    /**
     * Height of the Board
     */
    private final int height;

    /**
     * The list of ships on the board
     */
    final ArrayList<Ship<T>> myShips;

    /**
     * Placement rule checker of the Board
     */
    private final PlacementRuleChecker<T> placementChecker;

    /**
     * Set to track where the enemy has missed
     */
    HashSet<Coordinate> enemyMisses;

    /**
     * Set to track where the enemy has hit
     */
    Map<Coordinate, T> enemyHits;

    /**
     * the miss info
     */
    final T missInfo;

    /**
     * Constructs a BattleShipBoard with the specified width
     * , height, and placement checker
     *
     * @param width            is the width of the newly constructed board.
     * @param height           is the height of the newly constructed board.
     * @param placementChecker is the placement rule checker of the newly constructed board.
     * @throws IllegalArgumentException if the width or height are less than or
     *                                  equal to zero.
     */
    public BattleShipBoard(int width, int height, PlacementRuleChecker<T> placementChecker, T missInfo) {
        if (width <= 0) {
            throw new IllegalArgumentException("BattleShipBoard's width must be positive but is " + width);
        }
        if (height <= 0) {
            throw new IllegalArgumentException("BattleShipBoard's height must be positive but is " + height);
        }

        this.width = width;
        this.height = height;
        myShips = new ArrayList<>();
        this.placementChecker = placementChecker;
        enemyMisses = new HashSet<>();
        enemyHits = new HashMap<>();
        this.missInfo = missInfo;
    }

    /**
     * Constructs a BattleShipBoard with the specified width and height
     *
     * @param width  is the width of the newly constructed board.
     * @param height is the height of the newly constructed board.
     */
    public BattleShipBoard(int width, int height, T missInfo) {
        this(width, height, new InBoundsRuleChecker<T>(new NoCollisionRuleChecker<T>(null)), missInfo);
    }


    /**
     * This returns the height of the board.
     *
     * @return the int that is the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * This returns the width of the board.
     *
     * @return the int that is the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * This tries to add a ship to a list if placement rules are obeyed
     *
     * @return the String that is null if success, error message otherwise
     */
    public String tryAddShip(Ship<T> toAdd) {
        String status = placementChecker.checkPlacement(toAdd, this);
        if (status == null) {
            myShips.add(toAdd);
        }
        return status;
    }

    /**
     * This tries to move a ship to a list if placement rules are obeyed
     *
     * @param oldCoordinate the old coordinate of the ship
     * @param toMove        the new ship to be moved
     * @return the String that is null if success, error message otherwise
     */
    public String tryMoveShip(Coordinate oldCoordinate, Ship<T> toMove) {
        for (Ship<T> s : myShips) {
            if (s.occupiesCoordinates(oldCoordinate)) {
                myShips.remove(s);
                String moveResult = tryAddShip(toMove);
                if (moveResult != null) {
                    tryAddShip(s);
                    return moveResult;
                }
                toMove.updateHitsFromPreviousLocation(s);
                return null;
            }
        }
        return null;
    }

    /**
     * This gets the ship name based on coordinate
     *
     * @param where the coordinate in the board
     * @return the String that is null if ship not found, ship name otherwise
     */
    public String getShipNameByCoordinate(Coordinate where) {
        for (Ship<T> s : myShips) {
            if (s.occupiesCoordinates(where)) {
                return s.getName();
            }
        }
        return null;
    }

    /**
     * This returns what is at a particular coordinate for self.
     *
     * @return the generic T that is the ship or null
     */
    public T whatIsAtForSelf(Coordinate where) {
        return whatIsAt(where, true);
    }

    /**
     * This returns what is at a particular coordinate for enemy.
     *
     * @return the generic T that is the ship or null
     */
    public T whatIsAtForEnemy(Coordinate where) {
        return whatIsAt(where, false);
    }

    /**
     * This returns what is at a particular coordinate.
     *
     * @return the generic T that is the ship or null
     */
    protected T whatIsAt(Coordinate where, boolean isSelf) {
        for (Ship<T> s : myShips) {
            if (s.occupiesCoordinates(where)) {
                return s.getDisplayInfoAt(where, isSelf);
            }
        }
        if (!isSelf && enemyMisses.contains(where))
            return missInfo;
        if (!isSelf && enemyHits.containsKey(where))
            return enemyHits.get(where);
        return null;
    }

    /**
     * This lets to fire at a coordinate
     *
     * @param c coordinate on the board
     * @return Ship with generic T
     */
    public Ship<T> fireAt(Coordinate c) {
        for (Ship<T> ship : myShips) {
            if (ship.occupiesCoordinates(c)) {
                ship.recordHitAt(c);
                enemyHits.put(c, ship.getDisplayInfoAt(c, false));
                enemyMisses.remove(c);
                return ship;
            }
        }
        enemyMisses.add(c);
        enemyHits.remove(c);
        return null;
    }

    /**
     * This helps to find out if all ships have sunk
     *
     * @return boolean true if all ships sunk, false otherwise
     */
    @Override
    public boolean hasAllShipsSunk() {
        for (Ship<T> ship : myShips) {
            if (!ship.isSunk())
                return false;
        }
        return true;
    }

    /**
     * This reports on the number of squares occupied by each type of ship in that region
     *
     * @return Map<T, Integer> data T as key and count values as integer
     */
    @Override
    public Map<String, Integer> sonarScan(Coordinate center) {
        Map<String, Integer> scan = new HashMap<>();
        int[][] mask = getMask();
        int startRow = center.getRow() - 3;
        int startColumn = center.getColumn() - 3;
        for (int i = startRow; i <= startRow + 6; i++) {
            for (int j = startColumn; j <= startColumn + 6; j++) {
                if (mask[i - startRow][j - startColumn] == 1) {
                    String shipName = getShipNameByCoordinate(new Coordinate(i, j));
                    if (shipName != null)
                        scan.put(shipName, scan.getOrDefault(shipName, 0) + 1);
                }
            }
        }
        return scan;
    }

    /**
     * This generates mask for sonar scan region
     *
     * @return int[][] the matrix which has values as 1 for sonar scan region
     */
    private int[][] getMask() {
        int[][] mask = new int[7][7];
        int rows = 6;
        for (int i = 0; i <= rows; i++) {
            int offset = Math.abs(rows / 2 - i % rows);
            for (int j = offset; j <= (2 * (rows / 2 - offset) + offset); j++) {
                mask[i][j] = 1;
            }
        }
        return mask;
    }

}
