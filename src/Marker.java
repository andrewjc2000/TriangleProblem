import java.util.LinkedList;
import java.util.List;

/**
 * Marker class
 * Represents a place on the peg board, which has a particular state
 * and "ID", which is essentially its data/location in the graph.
 *
 * @author Andrew Chafos
 * @version 1.o
 */
public class Marker {
    public final int ID;
    private State state;
    private List<State> previousStates;
    public enum State {
        EMPTY,
        FILLED,
    }

    /**
     * Creates a Marker object, given an "ID" representing the Marker's
     * position in the graph, as well as a state indicating whether or not
     * the Marker is empty or filled
     * @param ID integer as described above
     * @param state State object as described above
     */
    public Marker(int ID, State state) {
        this.ID = ID;
        this.state = state;
        this.previousStates = new LinkedList<>();
    }

    /**
     * Used in TriangleProblem.doMove()
     * Updates this Marker object's state, and stores
     * the previous state at the beginning of a linked list.
     * @param newState the modified State of this Marker object
     */
    public void changeState(State newState) {
        previousStates.add(0, state);
        state = newState;
    }

    /**
     * Used in TriangleProblem.undoMove(); reverts this
     * Marker object to its most recent previous state.
     */
    public void reverseState() {
        if (previousStates.size() == 0) {
            throw new IllegalStateException("this node has not had a previous state");
        }
        state = previousStates.remove(0);
    }

    /**
     * @return the current state of this marker object
     */
    public State getState() {
        return state;
    }

    @Override
    public String toString () {
        return state.name() + " " + ID;
    }
}
