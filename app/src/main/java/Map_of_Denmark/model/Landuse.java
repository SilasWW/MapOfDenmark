package Map_of_Denmark.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class fir creating our landuse areas
 */
public class Landuse extends Way implements Serializable {
    private final int type;

    /**
     * Constructor that implements coordinates from way, as well as initializing field type.
     * @param way
     * @param type
     */
    public Landuse(ArrayList<Node> way, int type){
        super(way);
        this.type = type;
    }

    /**
     * Draws landuse areas depending on what type it is.
     * @param gc graphics context from view and JavaFX
     */
    public void draw(GraphicsContext gc){
        switch (type) {
            case 1 -> {
                gc.setStroke(Color.LIGHTGREY);
                super.draw(gc);
                gc.setFill(Color.LIGHTGREY);
                gc.fill();
            }
            case 2 -> {
                gc.setStroke(Color.BEIGE);
                super.draw(gc);
                gc.setFill(Color.BEIGE);
                gc.fill();
            }
        }

    }
}
