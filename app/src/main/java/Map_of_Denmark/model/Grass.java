package Map_of_Denmark.model;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.paint.Color;

import javafx.scene.canvas.GraphicsContext;
import Map_of_Denmark.view.View;

/**
 * Class for creating grass areas
 */
public class Grass extends Way implements Serializable {

    /**
     * Implements the coordinates from way
     * @param way arraylist of nodes
     */
    public Grass(ArrayList<Node> way) {
        super(way);
    }

    /**
     * Draws our grass-areas
     * @param gc graphics context from view and JavaFX
     */
    public void draw(GraphicsContext gc) {
        super.draw(gc);
        if(View.colorBlind){
            gc.setFill(Color.YELLOW);
        } else {
            gc.setFill(Color.rgb(19,188,0));
        }
        gc.fill();
    }

}