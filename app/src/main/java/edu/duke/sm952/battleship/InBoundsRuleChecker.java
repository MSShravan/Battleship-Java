package edu.duke.sm952.battleship;

/**
 * Class that can be used to check if the coordinates are within the bounds of the board
 */
public class InBoundsRuleChecker<T> extends PlacementRuleChecker<T> {

    /**
     * Constructor that can be used for chaining with other rules
     */
    public InBoundsRuleChecker(PlacementRuleChecker<T> next) {
        super(next);
    }

    /**
     * The checkMyRule method that validates the rule against the ship
     *
     * @param theShip  the ship that has to be checked
     * @param theBoard the board to be checked against
     * @return String null if within the bounds, error message otherwise
     */
    @Override
    protected String checkMyRule(Ship<T> theShip, Board<T> theBoard) {
        for (Coordinate coordinate : theShip.getCoordinates()) {
            if (coordinate.getRow() < 0)
                return "That placement is invalid: the ship goes off the top of the board.";
            if (coordinate.getRow() >= theBoard.getHeight())
                return "That placement is invalid: the ship goes off the bottom of the board.";
            if (coordinate.getColumn() < 0)
                return "That placement is invalid: the ship goes off the left of the board.";
            if (coordinate.getColumn() >= theBoard.getWidth())
                return "That placement is invalid: the ship goes off the right of the board.";
        }
        return null;
    }
}
