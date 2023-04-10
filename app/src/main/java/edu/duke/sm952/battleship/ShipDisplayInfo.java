package edu.duke.sm952.battleship;

/**
 * This interface supports displaying the information of the ship status
 */
public interface ShipDisplayInfo<T> {

    /**
     * This method returns the info based on the where coordinate and if hit
     *
     * @param where the coordinate to be checked
     * @param hit   boolean representing a hit, otherwise false
     */
    public T getInfo(Coordinate where, boolean hit);
}

