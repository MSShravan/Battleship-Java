package edu.duke.sm952.battleship;

/**
 * Class that can be used to check the placement rules of the ship against the board
 */
public abstract class PlacementRuleChecker<T> {

    /**
     * Used for chaining multiple rules
     */
    private final PlacementRuleChecker<T> next;

    /**
     * Constructor that can be used for chaining with other rules
     */
    public PlacementRuleChecker(PlacementRuleChecker<T> next) {
        this.next = next;
    }

    /**
     * The checkMyRule method that validates the rule against the ship
     *
     * @param theShip  the ship that has to be checked
     * @param theBoard the board to be checked against
     * @return String null if rule obeyed, error message otherwise
     */
    protected abstract String checkMyRule(Ship<T> theShip, Board<T> theBoard);

    /**
     * The checkPlacement method that checks the current rule and runs the next rule
     *
     * @param theShip  the ship that has to be checked
     * @param theBoard the board to be checked against
     * @return String null if rule is obeyed, error message otherwise
     */
    public String checkPlacement(Ship<T> theShip, Board<T> theBoard) {
        String status = checkMyRule(theShip, theBoard);
        //if we fail our own rule: stop the placement is not legal
        if (status != null) {
            return status;
        }
        //otherwise, ask the rest of the chain.
        if (next != null) {
            return next.checkPlacement(theShip, theBoard);
        }
        //if there are no more rules, then the placement is legal
        return null;
    }


}
