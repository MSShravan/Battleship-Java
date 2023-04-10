package edu.duke.sm952.battleship;

public class Placement {

    /**
     * The location coordinate for a ship placement
     */
    private final Coordinate where;

    /**
     * The orientation of a ship e.g. H, V
     */
    private final char orientation;

    /**
     * Constructs Placement based on coordinate and orientation
     *
     * @param where       is the coordinate location
     * @param orientation can be H, V
     */
    public Placement(Coordinate where, char orientation) {
        this.where = where;
        this.orientation = orientation;
    }

    /**
     * Constructs Placement based on string descriptor and parses to orientation and coordinate
     *
     * @param descr the string descriptor of format e.g. A2H, d1v
     */
    public Placement(String descr) {
        this(descr, 1);
    }

    /**
     * Constructs Placement based on string descriptor and parses to orientation and coordinate
     *
     * @param descr the string descriptor of format e.g. A2H, d1v for version 1 and A2u, d1R for version 2
     * @throws IllegalArgumentException if the format is not supported
     */
    public Placement(String descr, int version) {
        descr = descr.toUpperCase();
        if (descr.length() == 3) {
            if ((version == 1 && (descr.charAt(2) == 'H' || descr.charAt(2) == 'V'))
                    || (version == 2 && (descr.charAt(2) == 'H' || descr.charAt(2) == 'V' || descr.charAt(2) == 'U' || descr.charAt(2) == 'R' || descr.charAt(2) == 'D' || descr.charAt(2) == 'L'))) {
                where = new Coordinate(descr.substring(0, 2));
                orientation = descr.charAt(2);
                return;
            }
        }
        throw new IllegalArgumentException("That placement is invalid: it does not have the correct format.");
    }

    /**
     * Returns the coordinate
     */
    public Coordinate getWhere() {
        return where;
    }

    /**
     * Returns the orientation
     */
    public char getOrientation() {
        return orientation;
    }

    /**
     * The overridden toString method
     */
    @Override
    public String toString() {
        return "Placement{" + "where=" + where + ", orientation=" + orientation + '}';
    }

    /**
     * The overridden equals method
     */
    @Override
    public boolean equals(Object o) {
        if (o.getClass().equals(getClass())) {
            Placement p = (Placement) o;
            return orientation == p.orientation && where.equals(p.where);
        }
        return false;
    }

    /**
     * The overridden hashCode method
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
