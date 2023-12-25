package Map_of_Denmark.model;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.paint.Color;

import javafx.scene.canvas.GraphicsContext;
import Map_of_Denmark.view.View;

/**
 * Class for creating Forest
 */
public class Forest extends Way implements Serializable {

    /**
     * Constructor that implements coordinates from way
     * @param way arraylist of nodes
     */
    public Forest(ArrayList<Node> way) {
        super(way);
    }

    /**
     * Draws our forests
     * @param gc graphics context from view and JavaFX
     */
    public void draw(GraphicsContext gc) {
        super.draw(gc);

        if(View.colorBlind){
            gc.setFill(Color.rgb(171, 171, 46));
        } else {
            gc.setFill(Color.rgb(13,102,3));
        }
        gc.fill();
    }

}