package Map_of_Denmark;

import org.junit.jupiter.api.Test;
import Map_of_Denmark.model.Node;
import Map_of_Denmark.model.Way;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WayTest {
    @Test
    public void testWayCreation() {
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(new Node(1, 0,0));
        nodes.add(new Node(2, 0,1));
        nodes.add(new Node(3, 1,1));
        nodes.add(new Node(4, 1,0));
        Way way = new Way(nodes);

        double[] expectedCoords = {0.0, -0.0, 0.56, -0.0, 0.56, -1.0, 0.0, -1.0 };
        double[] actualCoords = way.getCoords();


        for (int i = 0; i < expectedCoords.length; i++) {
            assertEquals(expectedCoords[i] , actualCoords[i]);
        }

        assertEquals(0, way.getMinX(), 0.0001);
        assertEquals(-1, way.getMinY(), 0.0001);
        assertEquals(0.56, way.getMaxX(), 0.0001);
        assertEquals(0, way.getMaxY(), 0.0001);
    }
}

