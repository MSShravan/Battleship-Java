package edu.duke.sm952.battleship;

import java.util.HashMap;
import java.util.Map;

/**
 * A Rectangle ship class that takes a generic type T for displaying info
 */
public class RectangleShip<T> extends BasicShip<T> {

    /**
     * Name of this Ship, such as "submarine".
     */
    final String name;

    /**
     * Constructs a RectangleShip with the specified parameters by invoking the super
     *
     * @param name            is the ship name
     * @param upperLeft       is the location coordinate of the ship's upper left portion.
     * @param width           int that is the width of the ship
     * @param height          int that is the height of the ship
     * @param shipDisplayInfo that is the info about the ship's display status
     */
    public RectangleShip(String name, Coordinate upperLeft, int width, int height, ShipDisplayInfo<T> shipDisplayInfo, ShipDisplayInfo<T> enemyDisplayInfo) {
        super(makeCoords(upperLeft, width, height), shipDisplayInfo, enemyDisplayInfo);
        this.name = name;
    }

    /**
     * Constructs a RectangleShip with the specified parameters by invoking the super
     *
     * @param name      is the ship name
     * @param upperLeft is the location coordinate of the ship's upper left portion.
     * @param width     int that is the width of the ship
     * @param height    int that is the height of the ship
     * @param data      T that is the data info
     * @param onHit     T that is the hit info
     */
    public RectangleShip(String name, Coordinate upperLeft, int width, int height, T data, T onHit) {
        this(name, upperLeft, width, height, new SimpleShipDisplayInfo<>(data, onHit), new SimpleShipDisplayInfo<T>(null, data)
        );
    }

    /**
     * Constructs a RectangleShip with the specified parameters by invoking the super
     *
     * @param upperLeft is the location coordinate of the ship's upper left portion.
     * @param data      T that is the data info
     * @param onHit     T that is the hit info
     */
    public RectangleShip(Coordinate upperLeft, T data, T onHit) {
        this("testship", upperLeft, 1, 1, data, onHit);
    }

    /**
     * Constructs a RectangleShip with the specified parameters by invoking the super
     *
     * @param upperLeft is the location coordinate of the ship's upper left portion.
     * @param width     int that is the width of the ship
     * @param height    int that is the height of the ship
     * @return Map<Integer, Coordinate> of ship's coordinate locations in order
     */
    static Map<Integer, Coordinate> makeCoords(Coordinate upperLeft, int width, int height) {
        Map<Integer, Coordinate> coords = new HashMap<>();
        int key = 0;
        for (int i = upperLeft.getRow(); i < upperLeft.getRow() + height; i++) {
            for (int j = upperLeft.getColumn(); j < upperLeft.getColumn() + width; j++) {
                coords.put(key++, new Coordinate(i, j));
            }
        }
        return coords;
    }

    @Override
    public String getName() {
        return name;
    }
}
