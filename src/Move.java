/**
 * Move class
 * Represents a move on the peg board, with a starting, middle, and ending
 * node, each represented by the ID of that particular node.
 *
 * @author Andrew Chafos
 * @version 1.o
 */
public class Move {
    public final int start, middle, end;
    public Move(int start, int middle, int end) {
        this.start = start;
        this.middle = middle;
        this.end = end;
    }
    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof Move)) return false;
        Move otherM = (Move) other;
        return otherM.start == start && otherM.middle == middle
                && otherM.end == end;
    }
    @Override
    public String toString() {
        return "[" + start + ", " + middle + ", " + end + "]";
    }
}
