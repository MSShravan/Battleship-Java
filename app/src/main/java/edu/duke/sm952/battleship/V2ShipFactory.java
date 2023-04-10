package edu.duke.sm952.battleship;

public class V2ShipFactory implements AbstractShipFactory<Character> {
    @Override
    public Ship<Character> makeSubmarine(Placement where) {
        return createShip(where, 1, 2, 's', "Submarine");
    }

    @Override
    public Ship<Character> makeBattleship(Placement where) {
        return createShip(where, 2, 3, 'b', "Battleship");
    }

    @Override
    public Ship<Character> makeCarrier(Placement where) {
        return createShip(where, 2, 5, 'c', "Carrier");
    }

    @Override
    public Ship<Character> makeDestroyer(Placement where) {
        return createShip(where, 1, 3, 'd', "Destroyer");
    }

    /**
     * Constructs a RectangleShip with the specified parameters
     *
     * @param where  is the placement of the ship
     * @param w      int that is the width
     * @param h      int that is the height
     * @param letter char that is the ship's symbol
     * @param name   String that is the ship's name
     */
    protected Ship<Character> createShip(Placement where, int w, int h, char letter, String name) {
        if (letter == 's' || letter == 'd') {
            if (where.getOrientation() != 'H' && where.getOrientation() != 'V') {
                throw new IllegalArgumentException("Ship orientation must be either H or V");
            }
            if (where.getOrientation() == 'H') {
                int temp = w;
                w = h;
                h = temp;
            }
            return new RectangleShip<>(name, where.getWhere(), w, h, letter, '*');
        } else if (letter == 'b') {
            return new CustomBattleshipShip<>(name, where, letter, '*');
        } else {
            return new CustomCarrierShip<>(name, where, letter, '*');
        }
    }

    /**
     * Returns the factory version
     *
     * @return the factory version as 2
     */
    public int getVersion() {
        return 2;
    }
}
