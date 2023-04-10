package edu.duke.sm952.battleship;

public class Coordinate {

    /**
     * The row
     */
    private final int row;

    /**
     * The column
     */
    private final int column;

    private String descr;

    /**
     * The location coordinate default constructor
     *
     * @param row    is the row
     * @param column is the column
     */
    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
        descr = null;
    }

    /**
     * Constructs the coordinate by parsing a string descriptor. Must follow format e.g. A2, D1
     *
     * @param descr is the descriptor
     */
    public Coordinate(String descr) {
        if (descr.length() != 2) {
            throw new IllegalArgumentException("Coordinate string must be of size two. e.g. A2, D1");
        } else {
            descr = descr.toUpperCase();
            this.descr = descr;
            if (descr.charAt(0) < 'A' || descr.charAt(0) > 'Z') {
                throw new IllegalArgumentException("Coordinate string's first character must be a letter. e.g. A2, D1");
            } else if (descr.charAt(1) - '0' < 0 || descr.charAt(1) - '0' > 9) {
                throw new IllegalArgumentException("Coordinate string's second character must be a digit. e.g. A2, D1");
            } else {
                this.row = descr.charAt(0) - 'A';
                this.column = descr.charAt(1) - '0';
            }
        }
    }

    /**
     * @return int the row value
     */
    public int getRow() {
        return row;
    }

    /**
     * @return int the column value
     */
    public int getColumn() {
        return column;
    }

    /**
     * @return String the coordinate value
     */
    public String getDescr() {
        return descr;
    }

    /**
     * @param descr String the coordinate value
     */
    public void setDescr(String descr) {
        this.descr = descr;
    }

    /**
     * The overridden equals for object comparison
     */
    @Override
    public boolean equals(Object o) {
        if (o.getClass().equals(getClass())) {
            Coordinate c = (Coordinate) o;
            return row == c.row && column == c.column;
        }
        return false;
    }

    /**
     * The overridden toString implementation
     */
    @Override
    public String toString() {
        return "(" + row + ", " + column + ")";
    }

    /**
     * The overridden hashCode implementation
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

}
