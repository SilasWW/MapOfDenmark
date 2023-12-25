package Map_of_Denmark;

import org.junit.jupiter.api.Test;
import Map_of_Denmark.model.Node;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class NodeTest {

    @Test
    public void testGetters() {
        Node node = new Node(1, 12.34, 56.78);
        assertEquals(1, node.getNodeID());
        assertEquals(12.34, node.getLat(), 0.0001);
        assertEquals(56.78, node.getLon(), 0.0001);
    }

    @Test
    public void testSetters() {
        Node node = new Node(1, 12.34, 56.78);
        node.setNodeID(2);
        node.setLat(98.76);
        node.setLon(54.32);
        assertEquals(2, node.getNodeID());
        assertEquals(98.76, node.getLat(), 0.0001);
        assertEquals(54.32, node.getLon(), 0.0001);
    }
}

