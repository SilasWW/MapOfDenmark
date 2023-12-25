package Map_of_Denmark.model;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;
import java.util.ArrayList;

import static javafx.scene.paint.Color.*;

/**
 * Route class that is an abstraction of the physical route we find through a maps roads. It is stored as an
 * ArrayList<Node> so that is exactly similar to way objects datastructures.
 * handles own draw method
 */
public class Route extends Way implements Serializable {

    public ArrayList<Node> way;

    /**
     * constructor for the route object accepting list of nodes assinging the to the way variable in route
     * @param way arraylist of nodes (a way ) holding all vertices the route goes through
     */
    public Route(ArrayList<Node> way){
        super(way);
        this.way = new ArrayList<>(way);
    }

    /**
     * draw method for route object taking graphicsContext and changing relevant variables
     * @param gc graphicscontext from view
     */
    public void draw(GraphicsContext gc){
        var oldStroke = gc.getStroke();
        var oldWidth = gc.getLineWidth();
        var widthMod = Math.sqrt(gc.getTransform().determinant());

        var col1 = BLUE;
        var col2 = BLUE;
        int width1 = 1;
        int width2 = 1;

        gc.setStroke(col1);
        gc.setLineWidth(width1 / widthMod);
        super.draw(gc);
        gc.setStroke(col2);
        gc.setLineWidth(width2 / widthMod);
        super.draw(gc);


        gc.setStroke(oldStroke);
        gc.setLineWidth(oldWidth);
    }

    /**
     * getter method for way (arraylist of node objects)
     * @return way a list of nodes (the route through the map )
     */
    public ArrayList<Node> getList(){
        return way;
    }
}
