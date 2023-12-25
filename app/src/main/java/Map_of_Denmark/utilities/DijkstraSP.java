package Map_of_Denmark.utilities;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;

/**
 * This class is an implementation of Dijkstra's algorithm to find the shortest path between two nodes in a graph.
 * Reference for dijkstra: https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/DijkstraSP.java.html
 */
public class DijkstraSP {
    private double[] distTo;          // distTo[v] = distance  of shortest s->v path
    private DirectedEdge[] edgeTo;    // edgeTo[v] = last edge on shortest s->v path
    private IndexMinPQ<Double> pq;    // priority queue of vertices


    /**
     * Computes a shortest-paths tree from the source vertex {@code s} to every other vertex in the edge-weighted digraph {@code G}.
     * @param G the edge-weighted digraph
     * @param s the source vertex as an integer
     */
    public DijkstraSP(EdgeWeightedDigraph G, int s) {
        for (DirectedEdge e : G.edges()) {
            if (e.weight() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
        }

        distTo = new double[G.V()];
        edgeTo = new DirectedEdge[G.V()];

        validateVertex(s);

        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        pq = new IndexMinPQ<Double>(G.V());
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (DirectedEdge e : G.adj(v))
                relax(e);
        }

        assert check(G, s);
    }

    /**
     * This method relaxes the edge e.
     * @param e the edge to be relaxed as DirectedEdge
     */
    private void relax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
            if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
            else                pq.insert(w, distTo[w]);
        }
    }

    /**
     * This method returns the length of the shortest path from the source vertex {@code s} to vertex {@code v}.
     * @param v the vertex as an integer
     * @return the length of the shortest path from the source vertex {@code s} to vertex {@code v} as a double
     */
    public double distTo(int v) {
        validateVertex(v);
        return distTo[v];
    }

    /**
     * This method returns true if there is a path from the source vertex {@code s} to vertex {@code v}.
     * @param v the vertex as an integer
     * @return true if there is a path from the source vertex {@code s} to vertex {@code v}, false otherwise
     */
    public boolean hasPathTo(int v) {
        validateVertex(v);
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    /**
     * This method returns a shortest path from the source vertex {@code s} to vertex {@code v}.
     * @param v the vertex as an integer
     * @return a shortest path from the source vertex {@code s} to vertex {@code v} as an Iterable of DirectedEdges
     */
    public Iterable<DirectedEdge> pathTo(int v) {
        validateVertex(v);
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }

    /**
     * This method checks optimality conditions.
     * @param G the edge-weighted digraph
     * @param s the source vertex as an integer
     * @return true if optimality conditions are met, false otherwise
     */
    private boolean check(EdgeWeightedDigraph G, int s) {

        for (DirectedEdge e : G.edges()) {
            if (e.weight() < 0) {
                System.err.println("negative edge weight detected");
                return false;
            }
        }

        if (distTo[s] != 0.0 || edgeTo[s] != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent");
            return false;
        }
        for (int v = 0; v < G.V(); v++) {
            if (v == s) continue;
            if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
                System.err.println("distTo[] and edgeTo[] inconsistent");
                return false;
            }
        }

        
        for (int v = 0; v < G.V(); v++) {
            for (DirectedEdge e : G.adj(v)) {
                int w = e.to();
                if (distTo[v] + e.weight() < distTo[w]) {
                    System.err.println("edge " + e + " not relaxed");
                    return false;
                }
            }
        }

        for (int w = 0; w < G.V(); w++) {
            if (edgeTo[w] == null) continue;
            DirectedEdge e = edgeTo[w];
            int v = e.from();
            if (w != e.to()) return false;
            if (distTo[v] + e.weight() != distTo[w]) {
                System.err.println("edge " + e + " on shortest path not tight");
                return false;
            }
        }
        return true;
    }

    /**
     * This method validates the vertex.
     * @param v the vertex as an integer
     */
    private void validateVertex(int v) {
        int V = distTo.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }
}