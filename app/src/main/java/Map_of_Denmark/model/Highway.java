package Map_of_Denmark.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import Map_of_Denmark.view.View;

import java.io.Serializable;
import java.util.ArrayList;

import static javafx.scene.paint.Color.*;

/**
 * Class for creating highways
 */
public class Highway extends Way implements Serializable {
    private final int highwayCategory;
    private final ArrayList<Node> way;
    private final String wayName;

    /**
     * Implements coordinates from way, as well as initializing our fields
     * @param way arraylist of nodes
     * @param highwayCategory integer
     * @param wayName String
     */
    public Highway(ArrayList<Node> way, int highwayCategory, String wayName){
        super(way);
        this.way = new ArrayList<>(way);
        this.highwayCategory = highwayCategory;
        this.wayName = wayName;
    }

    /**
     * Draws our highways depending on which type of highway it is,
     * and changing color depending on what zoom level we are on
     * @param gc graphics context from view and JavaFX
     */
    public void draw(GraphicsContext gc){
        var oldStroke = gc.getStroke();
        var oldWidth = gc.getLineWidth();
        var widthMod = Math.sqrt(gc.getTransform().determinant());

        var col1 = Color.rgb(128,128,128);
        int width1;

        switch (highwayCategory) {
            case 1 -> {
                if (View.colorHighway) {
                    col1 = CRIMSON;
                } else {
                    col1 = Color.rgb(128, 128, 128);
                }
                width1 = 4;
            }
            case 2 -> {
                if(View.colorHighway){
                    col1 = ORANGE;
                } else {
                    col1 = Color.rgb(128, 128, 128);
                }
                width1 = 4;
            }
            default -> width1 = 2;
        }

        gc.setStroke(col1);
        gc.setLineWidth(width1 / widthMod);
        super.draw(gc);

        gc.setStroke(oldStroke);
        gc.setLineWidth(oldWidth);
    }

    /**
     * Returns our ArrayList containing nodes
     * @return ArrayList
     */
    public ArrayList<Node> getList(){
        return way;
    }

    /**
     * Returns the name of a way as a String
     * @return String
     */
    public String getwayName(){
        return wayName;
    }
}
