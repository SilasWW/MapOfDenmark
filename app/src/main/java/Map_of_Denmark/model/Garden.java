package Map_of_Denmark.model;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.paint.Color;

import javafx.scene.canvas.GraphicsContext;
import Map_of_Denmark.view.View;

/**
 * Class for creating garden area
 */
public class Garden extends Way implements Serializable {

    /**
     * Constructor that implements coordinates from way
     * @param way arraylist of nodes
     */
    public Garden(ArrayList<Node> way) {
        super(way);
    }

    /**
     * Draws our garden-areas
     * @param gc graphics context from view JavaFX
     */
    public void draw(GraphicsContext gc) {
        super.draw(gc);
        if(View.colorBlind){
            gc.setFill(Color.rgb(213, 213, 41));
        } else {
            gc.setFill(Color.LIGHTGREEN);
        }
        gc.fill();
    }

}