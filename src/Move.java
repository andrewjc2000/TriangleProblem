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

    /**
     * Instantiates a Move object, given 3 Marker IDs
     * @param start the starting Marker object, which is being moved
     * @param middle the middle Marker object, which is being hopped over
     * @param end the final Marker object, where the starting Marker will end up
     *            Note that the above describes what happens when
     *            TriangleProblem.doMove(Graph graph, Move move)
     *            is called.
     */
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
