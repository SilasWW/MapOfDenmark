package Map_of_Denmark.model;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.paint.Color;

import javafx.scene.canvas.GraphicsContext;

/**
 * Class for drawing our buildings, also extends Way, since its almost the same.
 */
public class Building extends Way implements Serializable {

    /**
     * Builds our way for which the buildings is placed upon,
     * as well as getting the coordinates.
     * @param way arraylist of nodes
     */
    public Building(ArrayList<Node> way) {
        super(way);
    }

    /**
     * Draws our buildings
     * @param gc graphics context from view and JavaFX
     */
    public void draw(GraphicsContext gc) {
        gc.setStroke(Color.LIGHTGREY);
        super.draw(gc);
        gc.setFill(Color.LIGHTGREY);
        gc.fill();
    }

}