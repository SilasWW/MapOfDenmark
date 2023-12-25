package Map_of_Denmark.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import Map_of_Denmark.view.View;

import java.io.Serializable;

/**
 * Class that creates trees
 */
public class Tree implements Serializable {
    private final double lat;
    private final double lon;

    /**
     * Gets the lat coordinate
     * @return returns lat double
     */
    public double getLat() {
        return lat;
    }

    /**
     * Gets the lon coordinate
     * @return returns lom double
     */
    public double getLon() {
        return lon;
    }

    /**
     * Constructor that initializes lat and lon coordinates
     * @param node object
     */
    public Tree(Node node){
        this.lat = node.getLat();
        this.lon = node.getLon();
    }

    /**
     * Draws trees
     * @param gc gc graphics context from view and JavaFX
     */
    public void draw(GraphicsContext gc){
        double y = -lat;
        double x = 0.56 * lon;

        if(View.colorBlind){
            gc.setFill(Color.YELLOW);
        } else {
            gc.setFill(Color.GREEN);
        }

        gc.fillRoundRect(x, y, 0.00002, 0.00002, 0.00002, 0.00002);
    }

}