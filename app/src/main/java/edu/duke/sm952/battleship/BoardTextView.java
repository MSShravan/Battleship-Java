package edu.duke.sm952.battleship;

import java.util.Collections;
import java.util.function.Function;

/**
 * This class handles textual display of
 * a Board (i.e., converting it to a string to show
 * to the user).
 * It supports two ways to display the Board:
 * one for the player's own board, and one for the
 * enemy's board.
 */

public class BoardTextView {
    /**
     * The Board to display
     */
    private final Board<Character> toDisplay;

    /**
     * Constructs a BoardView, given the board it will display.
     *
     * @param toDisplay is the Board to display
     * @throws IllegalArgumentException if the board is larger than 10x26.
     */
    public BoardTextView(Board<Character> toDisplay) {
        this.toDisplay = toDisplay;
        if (toDisplay.getWidth() > 10 || toDisplay.getHeight() > 26) {
            throw new IllegalArgumentException(
                    "Board must be no larger than 10x26, but is " + toDisplay.getWidth() + "x" + toDisplay.getHeight());
        }
    }

    /**
     * This displays the board for self.
     *
     * @return the String that is the board
     */
    public String displayMyOwnBoard() {
        return displayAnyBoard((c) -> toDisplay.whatIsAtForSelf(c));
    }

    /**
     * This displays the board for enemy.
     *
     * @return the String that is the board
     */
    public String displayEnemyBoard() {
        return displayAnyBoard((c) -> toDisplay.whatIsAtForEnemy(c));
    }

    /**
     * This displays the board of self and the enemy.
     *
     * @return the String that is the board of self and the enemy
     */
    public String displayMyBoardWithEnemyNextToIt(BoardTextView enemyView, String myHeader, String enemyHeader) {
        StringBuilder res = new StringBuilder();
        String myBoard = displayMyOwnBoard();
        String[] myBoardArr = myBoard.split("\n");
        String enemyBoard = enemyView.displayEnemyBoard();
        String[] enemyBoardArr = enemyBoard.split("\n");
        enemyBoardArr[0] = "  " + enemyBoardArr[0];
        enemyBoardArr[enemyBoardArr.length - 1] = "  " + enemyBoardArr[enemyBoardArr.length - 1];

        res.append(String.join("", Collections.nCopies(5, " ")))
                .append(myHeader)
                .append(String.join("", Collections.nCopies(27, " ")))
                .append(enemyHeader)
                .append("\n");

        for (int i = 0; i < myBoardArr.length; i++) {
            res.append(myBoardArr[i])
                    .append(String.join("", Collections.nCopies(16, " ")))
                    .append(enemyBoardArr[i])
                    .append("\n");
        }

        return res.toString();
    }

    /**
     * This displays the board for self and enemy.
     *
     * @param getSquareFn function to either for self or enemy
     * @return the String that is the board
     */
    protected String displayAnyBoard(Function<Coordinate, Character> getSquareFn) {
        StringBuilder ans = new StringBuilder(makeHeader());
        char c = 'A';
        for (int i = 0; i < toDisplay.getHeight(); i++) {
            String sep = "";
            ans.append(c).append(" ");
            for (int j = 0; j < toDisplay.getWidth(); j++) {
                ans.append(sep);
                Character ship = getSquareFn.apply(new Coordinate(i, j));
                if (ship != null) {
                    ans.append(ship);
                } else {
                    ans.append(" ");
                }
                sep = "|";
            }
            ans.append(" ").append(c);
            ans.append("\n");
            c++;
        }
        ans.append(makeHeader());
        return ans.toString();
    }

    /**
     * This makes the header line, e.g. 0|1|2|3|4\n
     *
     * @return the String that is the header line for the given board
     */
    String makeHeader() {
        StringBuilder ans = new StringBuilder("  "); // README shows two spaces at
        String sep = ""; // start with nothing to separate, then switch to | to separate
        for (int i = 0; i < toDisplay.getWidth(); i++) {
            ans.append(sep);
            ans.append(i);
            sep = "|";
        }
        ans.append("\n");
        return ans.toString();
    }

}
