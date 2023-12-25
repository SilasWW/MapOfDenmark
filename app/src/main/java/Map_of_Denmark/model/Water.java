package Map_of_Denmark.model;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.paint.Color;

import javafx.scene.canvas.GraphicsContext;

/**
 * Creates the water
 */
public class Water extends Way implements Serializable {

    /**
     * Constructor that implements coordinates from way
     * @param way arraylist of nodes
     */
    public Water(ArrayList<Node> way) {
        super(way);
    }

    /**
     * Draws water-areas
     * @param gc gc graphics context from view and JavaFX
     */
    public void draw(GraphicsContext gc) {
        super.draw(gc);
        gc.setFill(Color.LIGHTBLUE);
        gc.fill();
    }

}