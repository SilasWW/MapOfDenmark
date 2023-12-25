package Map_of_Denmark.model;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.paint.Color;

import javafx.scene.canvas.GraphicsContext;

/**
 * Class that creates the parking areas
 */
public class Parking extends Way implements Serializable {

    /**
     * Constructor that implements coordinates from way
     * @param way arraylist of nodes
     */
    public Parking(ArrayList<Node> way) {
        super(way);
    }

    /**
     * Draws the parking areas
     * @param gc graphics context from view and JavaFX
     */
    public void draw(GraphicsContext gc) {
        super.draw(gc);
        gc.setFill(Color.LIGHTGREY);
        gc.fill();
    }

}