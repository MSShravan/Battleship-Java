package edu.duke.sm952.battleship;

/**
 * Class that can be used to check the collision of the ship with existing ships in the board
 */
public class NoCollisionRuleChecker<T> extends PlacementRuleChecker<T> {
    /**
     * Constructor that can be used for chaining with other rules
     *
     * @param next rule that will be chained
     */
    public NoCollisionRuleChecker(PlacementRuleChecker<T> next) {
        super(next);
    }

    /**
     * The checkMyRule method that validates the rule against the ship
     *
     * @param theShip  the ship that has to be checked
     * @param theBoard the board to be checked against
     * @return String null if no collision with existing ships on the board, error message otherwise
     */
    @Override
    protected String checkMyRule(Ship<T> theShip, Board<T> theBoard) {
        for (Coordinate coordinate : theShip.getCoordinates()) {
            if (theBoard.whatIsAtForSelf(coordinate) != null)
                return "That placement is invalid: the ship overlaps another ship.";
        }
        return null;
    }
}
