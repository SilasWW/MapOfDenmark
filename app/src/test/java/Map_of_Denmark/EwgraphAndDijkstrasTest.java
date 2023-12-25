package Map_of_Denmark;

import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import Map_of_Denmark.model.Highway;
import Map_of_Denmark.model.Node;
import Map_of_Denmark.utilities.DijkstraSP;
import Map_of_Denmark.utilities.Ewgraph;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 */
public class EwgraphAndDijkstrasTest {
    static ArrayList<Highway> ways = new ArrayList<>();
    static EdgeWeightedDigraph eWTree;

    @BeforeAll
    static void setUp(){
        Node one = new Node(1,1.0,2.0);
        Node two = new Node(2, 3.0, 4.0);
        Node three = new Node(3, 4.0, 5.0);
        Node four = new Node(4, 6.0, 7.0);
        Node five = new Node(5, 8.0, 9.0);
        Node six = new Node(6, 10.0, 11.0);
        Node seven = new Node(7, 12.0, 13.0);
        Node eight = new Node(8,14.0,15.0);
        Node nine = new Node(9, 16.0, 17.0);

        ArrayList<Node> nl1 = new ArrayList<>();
        ArrayList<Node> nl2 = new ArrayList<>();
        ArrayList<Node> nl3 = new ArrayList<>();
        ArrayList<Node> nl4 = new ArrayList<>();

        nl1.add(one);nl1.add(two);nl1.add(six);
        nl2.add(two);nl2.add(three);nl2.add(seven);
        nl3.add(three);nl3.add(four);nl3.add(eight);
        nl4.add(four);nl4.add(five);nl4.add(nine);

        Highway way1 = new Highway(nl1, 1, "Amagerfælled");
        Highway way2 = new Highway(nl2, 2, "Vesterbrogade");
        Highway way3 = new Highway(nl3,3,"vesterfælledvej");
        Highway way4 = new Highway(nl4,4, "Cykelstien");

        ways.add(way1);
        ways.add(way2);
        ways.add(way3);
        ways.add(way4);

        HashMap<Long, Integer> idtwonode = new HashMap<>();
        for (int i = 0; i < 9; i++) {
            idtwonode.put(Long.valueOf(i+1),i);
        }
        Ewgraph ewgraph = new Ewgraph();
        eWTree = ewgraph.buildGraph(ways,idtwonode);
    }
    @Test
    public void graphTest(){
        assertEquals(9,eWTree.V());
        assertEquals(16, eWTree.E());
    }

    @Test
    public void testSearch(){
        DijkstraSP dijkstraSP = new DijkstraSP(eWTree,1);
        assertEquals(true, dijkstraSP.hasPathTo(3));
        assertEquals(157, Math.floor(dijkstraSP.distTo(2)));
    }
}
