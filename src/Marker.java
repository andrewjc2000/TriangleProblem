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
    public Marker(int ID, State state) {
        this.ID = ID;
        this.state = state;
        this.previousStates = new LinkedList<>();
    }

    public void changeState(State newState) {
        previousStates.add(0, state);
        state = newState;
    }

    public void reverseState() {
        if (previousStates.size() == 0) {
            throw new IllegalStateException("this node has not had a previous state");
        }
        state = previousStates.remove(0);
    }

    public State getState() {
        return state;
    }

    @Override
    public String toString () {
        return state.name() + " " + ID;
    }
}
