package edu.duke.sm952.battleship;

import java.util.HashMap;
import java.util.Map;

/**
 * A Custom battleship class that takes a generic type T for displaying info
 */
public class CustomBattleshipShip<T> extends BasicShip<T> {

    /**
     * Name of this Ship, such as "battleship".
     */
    final String name;

    /**
     * Constructs a CustomShip with the specified parameters by invoking the super
     *
     * @param name            is the ship name
     * @param placement       is the placement of the ship
     * @param shipDisplayInfo that is the info about the ship's display status
     */
    public CustomBattleshipShip(String name, Placement placement, ShipDisplayInfo<T> shipDisplayInfo, ShipDisplayInfo<T> enemyDisplayInfo) {
        super(makeCoords(placement), shipDisplayInfo, enemyDisplayInfo);
        this.name = name;
    }

    /**
     * Constructs CustomBattleshipShip coordinates
     *
     * @param name      is the ship name
     * @param placement is the placement of the ship
     * @param data      T that is the data info
     * @param onHit     T that is the hit info
     */
    public CustomBattleshipShip(String name, Placement placement, T data, T onHit) {
        this(name, placement, new SimpleShipDisplayInfo<>(data, onHit), new SimpleShipDisplayInfo<T>(null, data));
    }

    /**
     * Constructs a CustomShip with the specified parameters by invoking the super
     *
     * @param placement is the placement of the ship
     * @return HashSet<Coordinate> of ship's coordinate locations
     */
    static Map<Integer, Coordinate> makeCoords(Placement placement) {
        Map<Integer, Coordinate> coords = new HashMap<>();
        Coordinate upperLeft = placement.getWhere();
        int[][] mask = getMask(placement.getOrientation());
        int height = mask.length;
        int width = mask[0].length;
        int key = 0;
        for (int i = upperLeft.getRow(); i < upperLeft.getRow() + height; i++) {
            for (int j = upperLeft.getColumn(); j < upperLeft.getColumn() + width; j++) {
                if (mask[i - upperLeft.getRow()][j - upperLeft.getColumn()] == 1)
                    coords.put(key++, new Coordinate(i, j));
            }
        }
        return coords;
    }

    /**
     * Constructs a mask for ship location coordinates
     *
     * @param orientation is the orientation of the ship
     * @return int[][] where ship coordinates has to be added
     */
    static int[][] getMask(char orientation) {
        orientation = Character.toUpperCase(orientation);
        switch (orientation) {
            case 'U':
                return new int[][]{{0, 1, 0}, {1, 1, 1}};
            case 'R':
                return new int[][]{{1, 0}, {1, 1}, {1, 0}};
            case 'D':
                return new int[][]{{1, 1, 1}, {0, 1, 0}};
            case 'L':
                return new int[][]{{0, 1}, {1, 1}, {0, 1}};
            default:
                throw new IllegalArgumentException("Orientation must be either U, R, D, or L but was " + orientation);
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
