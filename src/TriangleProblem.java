import java.util.*;

/**
 * Contains the Main execution of the program
 * @author Andrew Chafos
 * @version 1.0
 */
public class TriangleProblem {

    private static int ITERATIONS = 0;
    private static final int ROWS = 5;
    private static final int TRI_NUM = triangleNumber(ROWS);
    private static List<List<Move>> solutions = new ArrayList<>();

    public static void main(String[] args) {
        Graph<Marker> graph = triangleGraph();
        makeMoves(graph, new LinkedList<>());
        System.out.println(solutions.size());
        System.out.println(ITERATIONS);
        printSolutions();
    }

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

    private static void makeMoves(Graph<Marker> graph, List<Move> moves) {
        ++ITERATIONS;
        List<Move> possMoves = getValidMoves(graph);
        if (ITERATIONS < 0) {
            System.out.println(possMoves.size());
        }
        for (Move m: possMoves) {
            moves.add(0, m);
            doMove(graph, m);
            makeMoves(graph, moves);
            moves.remove(0);
            undoMove(graph, m);
        }
        if (moves.size() == TRI_NUM - 2) {
            List<Move> solution = new ArrayList<>(moves);
            Collections.reverse(solution);
            solutions.add(solution);
        }
    }

    private static void printSolutions() {
        //for (List<Move> runningSolution: solutions) {
        List<Move> runningSolution = solutions.get(0);
        System.out.println(runningSolution.size());
        System.out.println(Arrays.toString(runningSolution.toArray()));
        Graph<Marker> freshGraph = triangleGraph();
        for (Move move : runningSolution) {
            printGraph(freshGraph);
            doMove(freshGraph, move);
        }
        printGraph(freshGraph);
        //}
    }

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

    private static void undoMove (Graph<Marker> graph, Move move) {
        for (Vertex<Marker> v: graph.getVertices()) {
            int id = v.getData().ID;
            if (id == move.start || id == move.middle || id == move.end) {
                v.getData().reverseState();
            }
        }
    }

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



    private static int triangleNumber(int n) {
        if (n < 1) return n;
        return n + triangleNumber(n - 1);
    }

}
