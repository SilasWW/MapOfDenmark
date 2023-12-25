package Map_of_Denmark.model;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.paint.Color;

import javafx.scene.canvas.GraphicsContext;
import Map_of_Denmark.view.View;

/**
 * class that creates parks
 */
public class Park extends Way implements Serializable {

    /**
     * Constructor that implements coordinates from way
     * @param way arraylist of nodes
     */
    public Park(ArrayList<Node> way) {
        super(way);
    }

    /**
     * draws the parks
     * @param gc graphics context from view and JavaFX
     */
    public void draw(GraphicsContext gc) {
        super.draw(gc);
        if(View.colorBlind){
            gc.setFill(Color.rgb(192, 192, 0));
        } else {
            gc.setFill(Color.LIGHTGREEN);
        }
        gc.fill();
    }

}