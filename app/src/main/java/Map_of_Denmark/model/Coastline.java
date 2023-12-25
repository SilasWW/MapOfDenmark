package Map_of_Denmark.model;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;
import java.util.ArrayList;

import static javafx.scene.paint.Color.PURPLE;

/**
 * Class for creating coastlines
 */
public class Coastline extends Way implements Serializable {

    /**
     * Constructor that implements coordinates from Way
     * @param way arraylist of nodes
     */
    public Coastline(ArrayList<Node> way) {
        super(way);
    }

    /**
     * Draws our coastlines
     * @param gc graphics context from view and JavaFX
     */
    public void draw(GraphicsContext gc) {
        gc.setStroke(PURPLE);
        super.draw(gc);
    }
}
