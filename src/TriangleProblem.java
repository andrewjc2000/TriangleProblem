import java.util.*;

/**
 * Contains the Main execution of the program
 * @author Andrew Chafos
 * @version 1.0
 */
public class TriangleProblem {

    private static int ITERATIONS = 0;
    private static int NUM_SOLUTIONS = 0;
    private static int ROWS = 5;
    private static int TRI_NUM = triangleNumber(ROWS);
    private static List<List<Move>> solutions = new ArrayList<>();
    private static Mode mode = Mode.PRINT;
    private static int MAX_SOLUTIONS = Integer.MAX_VALUE;

    private enum Mode {
        AMOUNT,
        PRINT
    }

    /**
     * main method: begins the execution of the program
     * @param args optional array of arguments for the program to interpret
     *
     *             The program runs in 2 modes: one merely printing
     *             the number of solutions and possible games, and the other
     *             actually printing out the visual representations
     *             of the solutions.
     *
     *             The first mode is called "amount", and the other is called "print"
     *             In either mode, an integer can be added specifying the number of rows,
     *             which is 5 by default.
     *             Additionally, only in print mode, a 3rd integer argument can be added
     *             specifying the maximum number of solutions desired to be printed.
     *             Examples of valid parameter combinations:
     *             java TriangleProblem amount 6
     *             java TriangleProgram print 4
     *             java TriangleProgram print 5 22
     */
    public static void main(String[] args){

        if (args.length > 3) {
            throw new IllegalArgumentException("too many cmd arguments");
        }

        if (args.length > 1) {
            try {
                ROWS = Integer.parseInt(args[1]);
                TRI_NUM = triangleNumber(ROWS);
            } catch (Throwable t) {
                throw new IllegalArgumentException("2nd argument must be an int!");
            }
        }

        if (args.length > 0) {
            String m = args[0];
            if (m.equals("amount")) {
                mode = Mode.AMOUNT;
            } else if (m.equals("print")) {
                if (args.length == 3) {
                    try {
                        MAX_SOLUTIONS = Integer.parseInt(args[2]);
                    } catch (Throwable t) {
                        throw new IllegalArgumentException("2nd argument must be an int!");
                    }
                }
            } else {
                throw new IllegalArgumentException("invalid mode; needs to be"
                    + "\"amount\" or \"print\""
                );
            }
        }

        Graph<Marker> graph = triangleGraph();
        makeMoves(graph, new LinkedList<>());
        printResults();
    }

    /**
     * prints the overall results of the program execution
     */
    private static void printResults() {
        if (NUM_SOLUTIONS == MAX_SOLUTIONS) {
            System.out.println("Found " + NUM_SOLUTIONS + " solutions!");
        }
        System.out.println("Number of Rows on Board: " + ROWS);
        System.out.println("Number of solutions: " + NUM_SOLUTIONS);
        System.out.println("Number of possible games: " + ITERATIONS);
        if (solutions.size() != 0 && mode == Mode.PRINT) {
            printSolutions();
        }
    }

    /**
     * Generates a graph representing the triangular board
     * of the problem in question.  Every node in the graph is of type
     * Marker, which has 2 states, Empty and Filled.
     * The triangle is formed in a V shape; the node at the very bottom
     * is the only node in the Empty state, whereas the rest are Filled.
     * The Nodes in the graph are connected by edges, whose weights represent
     * the direction the edge would be going in on a physical board.
     * Weight 2 corresponds to horizontal, weight 1 corresponds to
     * upper-left to lower-right diagonal, and weight 3 corresponds to
     * upper-right to lower-left diagonal.
     * @return graph of Marker nodes as described above
     */
    private static Graph<Marker> triangleGraph() {
        ++ITERATIONS;
        List<Vertex<Marker>> vertexList = new ArrayList<>();
        Set<Edge<Marker>> edgeSet = new HashSet<>();
        for (int i = 1; i < TRI_NUM; i++) {
            vertexList.add(new Vertex<>(new Marker(i, Marker.State.FILLED)));
        }
        vertexList.add(new Vertex<>(new Marker(TRI_NUM, Marker.State.EMPTY)));
        int index = 0;
        for (int i = ROWS; i > 1; i--) {
            for (int j = 1; j < i; j++) {
                edgeSet.add(new Edge<>(vertexList.get(index), vertexList.get(index + 1), 2));
                edgeSet.add(new Edge<>(vertexList.get(index + 1), vertexList.get(index), 2));
                edgeSet.add(new Edge<>(vertexList.get(index), vertexList.get(index + i), 1));
                edgeSet.add(new Edge<>(vertexList.get(index + i), vertexList.get(index), 1));
                edgeSet.add(new Edge<>(vertexList.get(index + 1), vertexList.get(index + i), 3));
                edgeSet.add(new Edge<>(vertexList.get(index + i), vertexList.get(index + 1), 3));
                index++;
            }
            index++;
        }

        return new Graph<>(new HashSet<>(vertexList), edgeSet);
    }

    /**
     * recursive function containing the actual iteration through all possible
     * games.  It takes in 2 inputs: the current state of the graph,
     * and a running list of the moves used to generate the current state of
     * the graph.  On each iteration, it considers all of the possible moves
     * that can be made validly based on the current state of the graph,
     * and then proceeds to explore each of the new graph configurations generated
     * from those possible moves.
     *
     * Note that if there are no valid moves, the method does not generate another
     * recursive call.  This is essentially how a "branch" on the tree representing
     * all possible game configurations is terminated at a leaf node; that "node"
     * has no children to  explore.
     *
     * @param graph current state of the graph of Marker nodes
     * @param moves the list of Moves used to create the current graph, in the order
     *              in which they were executed on the graph.
     */
    private static void makeMoves(Graph<Marker> graph, List<Move> moves) {
        ++ITERATIONS;
        List<Move> possMoves = getValidMoves(graph);
        for (Move m: possMoves) {
            moves.add(0, m);
            doMove(graph, m);
            makeMoves(graph, moves);
            moves.remove(0);
            undoMove(graph, m);
        }
        if (moves.size() == TRI_NUM - 2) {
            NUM_SOLUTIONS++;
            if (mode == Mode.PRINT) {
                List<Move> solution = new ArrayList<>(moves);
                Collections.reverse(solution);
                solutions.add(solution);
            }
            if (NUM_SOLUTIONS == MAX_SOLUTIONS) {
                printResults();
                System.exit(1);
            }
        }
    }

    /**
     * Prints out all of the solutions found in the program; this method
     * is used only in Print mode.  Note that the number of solutions
     * can also be capped by command-line arguments.
     */
    private static void printSolutions() {
        for (List<Move> runningSolution: solutions) {
            System.out.println(Arrays.toString(runningSolution.toArray()));
            Graph<Marker> freshGraph = triangleGraph();
            for (Move move : runningSolution) {
                printGraph(freshGraph);
                doMove(freshGraph, move);
            }
            printGraph(freshGraph);
            System.out.println();
        }
    }

    /**
     * Obtains the valid Moves possible, given a graph in an arbitrary state.
     * Essentially, the algorithm looks for all filled nodes that are adjacent to
     * another filled node such that, when the first node "jumps" over the second
     * to the node across from it, it lands in an empty space.
     * If a particular situation satisfies these criteria, a Move object is generated,
     * which lists the ids of the Nodes involved with the Move, and this object
     * is added to the return list
     * @param graph the current state of the graph representing the game
     * @return a list of Moves fulfilling the criteria described above
     */
    private static List<Move> getValidMoves(Graph<Marker> graph) {
        List<Move> moves = new ArrayList<>();
        for (Vertex<Marker> start: graph.getVertices()) {
            if (!start.getData().getState().equals(Marker.State.EMPTY)) {
                int startID = start.getData().ID;
                for (VertexDistance<Marker> middle: graph.getAdjList().get(start)) {
                    int distance = middle.getDistance();
                    if (!middle.getVertex().getData().getState().equals(Marker.State.EMPTY)) {
                        for (VertexDistance<Marker> end: graph.getAdjList().get(middle.getVertex())) {
                            Marker endMarker = end.getVertex().getData();
                            if (endMarker.getState() == Marker.State.EMPTY && end.getDistance() == distance
                                && endMarker.ID != startID) {
                                moves.add(
                                    new Move(
                                        startID,
                                        middle.getVertex().getData().ID,
                                        endMarker.ID
                                    )
                                );
                            }
                        }
                    }
                }
            }
        }
        return moves;
    }

    /**
     * executes a given Move on a given graph in a particular state
     * Note that this is a void method because the Move actually has
     * direct consequences on the input Graph.
     * @param graph the current state of the graph/game
     * @param move the move to be performed on the graph
     */
    private static void doMove (Graph<Marker> graph, Move move) {
        for (Vertex<Marker> v: graph.getVertices()) {
            int id = v.getData().ID;
            if (id == move.start || id == move.middle) {
                v.getData().changeState(Marker.State.EMPTY);
            } else if (id == move.end) {
                v.getData().changeState(Marker.State.FILLED);
            }
        }
    }

    /**
     * Reverses a move previously done on a graph; does the opposite
     * of the method described above.
     * @param graph the current state of the graph/game
     * @param move the move to be reversed on the graph
     */
    private static void undoMove (Graph<Marker> graph, Move move) {
        for (Vertex<Marker> v: graph.getVertices()) {
            int id = v.getData().ID;
            if (id == move.start || id == move.middle || id == move.end) {
                v.getData().reverseState();
            }
        }
    }

    /**
     * Prints out a visual representation of any triangular graph generated
     * from the triangularGraph() method, with Moves potentially being applied to it
     * @param graph the graph to be printed out
     */
    private static void printGraph(Graph<Marker> graph) {
        int index = 1;
        for (int i = ROWS; i > 0; i--) {
            for (int k = ROWS; k > i; k--) {
                System.out.print(" ");
            }
            for (int j = 0; j < i; j++) {
                for (Vertex<Marker> vertex: graph.getVertices()) {
                    if (vertex.getData().ID == index) {
                        System.out.print(vertex.getData().getState().ordinal() + " ");
                    }
                }
                index++;
            }
            System.out.println();
        }
    }


    /**
     * returns the nth triangular number
     * @param n integer index into the triangular number list
     * @return the nth triangular number
     */
    private static int triangleNumber(int n) {
        if (n < 1) return n;
        return n + triangleNumber(n - 1);
    }

}
