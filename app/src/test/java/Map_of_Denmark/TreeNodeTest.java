package Map_of_Denmark;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import Map_of_Denmark.model.Node;
import Map_of_Denmark.model.TreeNode;
import Map_of_Denmark.model.Way;

public class TreeNodeTest {
    static List<Way> ways = new ArrayList<>();

    @BeforeAll
    static void setUp(){


        Node one = new Node(1,1.0,2.0);
        Node two = new Node(2, 3.0, 4.0);
        Node three = new Node(3, 5.0, 6.0);
        Node four = new Node(4, 7.0, 8.0);
        Node five = new Node(5, 9.0, 10.0);
        Node six = new Node(6, 11.0, 12.0);
        Node seven = new Node(7, 13.0, 14.0);
        Node eight = new Node(8,15.0,16.0);

        ArrayList<Node> nl1 = new ArrayList<>();
        ArrayList<Node> nl2 = new ArrayList<>();
        ArrayList<Node> nl3 = new ArrayList<>();
        ArrayList<Node> nl4 = new ArrayList<>();

        nl1.add(one);nl1.add(two);
        nl2.add(three);nl2.add(four);
        nl3.add(five);nl3.add(six);
        nl4.add(seven);nl4.add(eight);

        Way way1 = new Way(nl1);
        Way way2 = new Way(nl2);
        Way way3 = new Way(nl3);
        Way way4 = new Way(nl4);

        ways.add(way1);
        ways.add(way2);
        ways.add(way3);
        ways.add(way4);
    }


    @Test
    public void testFindLeaf() {

        TreeNode treeNode = new TreeNode(0);
        treeNode.build(ways);

        // Test finding leaves using the whole bounds
        List<Way> result = treeNode.findLeaf(treeNode, true, 0.0, 0.0, 20.0, 20.0);
        assertEquals(4, result.size());
    }

    @Test
    public void testSortX() {
        TreeNode.SortX = true;
        TreeNode treeNode = new TreeNode(0);
        treeNode.build(ways);

        assertEquals(2*0.56, treeNode.getMinX());
        assertEquals(16*0.56, treeNode.getMaxX());

        TreeNode.SortX = false;
        treeNode = new TreeNode(0);
        treeNode.build(ways);

        assertEquals(15*-1, treeNode.getMinY());
        assertEquals(1*-1, treeNode.getMaxY());
    }

}


