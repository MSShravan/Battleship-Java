package edu.duke.sm952.battleship;

import java.util.Map;

/**
 * The Board Interface that represents all boards of the battleship game. It is
 * generic in typename T, which is the type of information the board view needs to
 * display ships in the board.
 */
public interface Board<T> {

    /**
     * Returns the width of the board
     */
    public int getWidth();

    /**
     * Returns the height of the board
     */
    public int getHeight();

    /**
     * This tries to add a ship to a list.
     *
     * @return the String that is null if success or error message otherwise
     */
    public String tryAddShip(Ship<T> toAdd);

    /**
     * This tries to move a ship to a list if placement rules are obeyed
     *
     * @param oldCoordinate the old coordinate of the ship
     * @param toMove        the new ship to be moved
     * @return the String that is null if success, error message otherwise
     */
    public String tryMoveShip(Coordinate oldCoordinate, Ship<T> toMove);

    /**
     * This gets the ship name based on coordinate
     *
     * @param where the coordinate in the board
     * @return the String that is null if ship not found, ship name otherwise
     */
    public String getShipNameByCoordinate(Coordinate where);

    /**
     * This returns what is at a particular coordinate for self.
     *
     * @return the generic T that is the ship or null
     */
    public T whatIsAtForSelf(Coordinate where);

    /**
     * This returns what is at a particular coordinate for enemy.
     *
     * @return the generic T that is the ship or null
     */
    public T whatIsAtForEnemy(Coordinate where);

    /**
     * This lets to fire at a coordinate
     *
     * @param c coordinate on the board
     * @return Ship with generic T
     */
    public Ship<T> fireAt(Coordinate c);

    /**
     * This helps to find out if all ships have sunk
     *
     * @return boolean true if all ships sunk, false otherwise
     */
    public boolean hasAllShipsSunk();

    /**
     * This reports on the number of squares occupied by each type of ship in that region
     *
     * @param center the center coordinate of the sonar scan
     * @return Map<String, Integer> ship name as key and count values as integer
     */
    public Map<String, Integer> sonarScan(Coordinate center);
}
