package edu.duke.sm952.battleship;

import java.util.HashMap;
import java.util.Map;

/**
 * The Basic ship that takes in generic T to display info
 */
public abstract class BasicShip<T> implements Ship<T> {

    /**
     * The location of the ship with coordinates indicated hit or safe
     */
    protected HashMap<Coordinate, Boolean> myPieces;

    /**
     * The location of the ship with coordinates in order
     */
    protected HashMap<Integer, Coordinate> orderedPieces;

    /**
     * The ship display info
     */
    protected ShipDisplayInfo<T> myDisplayInfo;

    /**
     * The ship display info for enemy
     */
    protected ShipDisplayInfo<T> enemyDisplayInfo;

    /**
     * Constructs a BasicShip with the specified location coordinate
     *
     * @param where is the location coordinates of the ship in order
     */
    public BasicShip(Map<Integer, Coordinate> where, ShipDisplayInfo<T> myDisplayInfo, ShipDisplayInfo<T> enemyDisplayInfo) {
        orderedPieces = new HashMap<>(where);
        myPieces = new HashMap<>();
        for (Coordinate c : where.values()) {
            myPieces.put(c, false);
        }
        this.myDisplayInfo = myDisplayInfo;
        this.enemyDisplayInfo = enemyDisplayInfo;
    }

    /**
     * This checks if a coordinate is occupied in the board.
     *
     * @param where is the coordinate to be checked
     * @return the boolean that implies if coordinate occupied by a ship and if so is it safe or hit
     */
    @Override
    public boolean occupiesCoordinates(Coordinate where) {
        return myPieces.containsKey(where);
    }

    /**
     * This checks if ship is sunk or not
     *
     * @return the boolean that is sunk status of the ship. True if all values in myPieces are true, otherwise false
     */
    @Override
    public boolean isSunk() {
        for (Boolean status : myPieces.values()) {
            if (!status)
                return false;
        }
        return true;
    }

    /**
     * This records a hit in a coordinate
     *
     * @param where is the coordinate to be hit
     */
    @Override
    public void recordHitAt(Coordinate where) {
        checkCoordinateInThisShip(where);
        myPieces.put(where, true);
    }

    /**
     * This checks if a coordinate in the ship is hit or not
     *
     * @param where is the coordinate to be checked
     * @return the boolean that is if the coordinate is hit or not
     */
    @Override
    public boolean wasHitAt(Coordinate where) {
        checkCoordinateInThisShip(where);
        return myPieces.get(where);
    }

    /**
     * This displays info at a coordinate.
     *
     * @param where is the coordinate to be checked
     * @return the Character that is the info of that coordinate
     */
    @Override
    public T getDisplayInfoAt(Coordinate where, boolean myShip) {
        return myShip ? myDisplayInfo.getInfo(where, wasHitAt(where)) : enemyDisplayInfo.getInfo(where, wasHitAt(where));
    }

    /**
     * This checks if a coordinate is a valid location in a ship
     *
     * @param c is the coordinate to be checked
     */
    protected void checkCoordinateInThisShip(Coordinate c) {
        if (!myPieces.containsKey(c)) {
            throw new IllegalArgumentException("Specified coordinate not a valid location in ship! " + c.toString());
        }
    }

    /**
     * Get all the Coordinates that this Ship occupies.
     *
     * @return An Iterable with the coordinates that this Ship occupies
     */
    public Iterable<Coordinate> getCoordinates() {
        return myPieces.keySet();
    }

    /**
     * Get all the Coordinates that this Ship occupies in order
     *
     * @return A Map with the coordinates that this Ship occupies in order
     */
    public Map<Integer, Coordinate> getOrderedCoordinates() {
        return orderedPieces;
    }

    /**
     * This updates the ship with hits from earlier location
     *
     * @param prevShip is the ship at previous location
     */
    public void updateHitsFromPreviousLocation(Ship<T> prevShip) {
        Map<Integer, Coordinate> prevShipOrderedCoordinates = prevShip.getOrderedCoordinates();
        for (Map.Entry<Integer, Coordinate> coordinateEntry : orderedPieces.entrySet()) {
            myPieces.put(coordinateEntry.getValue(), prevShip.wasHitAt(prevShipOrderedCoordinates.get(coordinateEntry.getKey())));
        }
    }
}
