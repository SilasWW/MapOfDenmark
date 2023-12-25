package Map_of_Denmark.utilities;

import Map_of_Denmark.model.Highway;
import Map_of_Denmark.model.Node;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.DirectedEdge;
import java.util.List;
import java.util.HashMap;

/**
 * This class is used to build the EdgeWeightedDigraph
 * Reference for graph: https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/EdgeWeightedGraph.java.html
 */
public class Ewgraph {

    /**
     * This method is used to build the EdgeWeightedDigraph
     * @param Ways List of Highways in the map
     * @param vID HashMap of the Nodes in the map
     * @return EdgeWeightedDigraph
     */
    public EdgeWeightedDigraph buildGraph(List<Highway> Ways, HashMap<Long, Integer> vID ){

        int n = vID.size();
        EdgeWeightedDigraph EWtree = new EdgeWeightedDigraph(n);

        for(int i = 0; i < Ways.size(); i++)
        {

            Highway hWay = Ways.get(i);

            for(int j = 0 ; j < hWay.getList().size()-1; j++)
            {
                Node N1 = hWay.getList().get(j);
                Node N2 = hWay.getList().get(j+1);

                Double weight = Haversine.haversine(N1.getLat(),N1.getLon(),N2.getLat(),N2.getLon());

                int v = vID.get(N1.getNodeID());
                int w = vID.get(N2.getNodeID());

                DirectedEdge e1 = new DirectedEdge(v,w,weight);
                DirectedEdge e2 = new DirectedEdge(w,v,weight);
                EWtree.addEdge(e1);
                EWtree.addEdge(e2);
            }
        }
        return EWtree;
    }
}
