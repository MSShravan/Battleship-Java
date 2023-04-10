package edu.duke.sm952.battleship;

/**
 * This class supports displaying the information of the simple ship status
 */
public class SimpleShipDisplayInfo<T> implements ShipDisplayInfo<T> {

    /**
     * the info for data
     */
    T myData;

    /**
     * the info for hit
     */
    T onHit;

    /**
     * Constructs SimpleShipDisplayInfo based on myData and onHit
     *
     * @param myData is the data info
     * @param onHit  is the hit info
     */
    public SimpleShipDisplayInfo(T myData, T onHit) {
        this.myData = myData;
        this.onHit = onHit;
    }

    /**
     * Check if this ship at a coordinate is hit or not and get the info
     *
     * @return T that is onHit if hit else myData if not
     */
    @Override
    public T getInfo(Coordinate where, boolean hit) {
        if (hit)
            return onHit;
        return myData;
    }
}
