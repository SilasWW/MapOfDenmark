package Map_of_Denmark.model;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;

/**
 * Class that creates ways
 */
public class Way implements Serializable, Comparable<Way> {
    private double[] coords;
    private double minX;
    private double minY;
    private double maxX;
    private double maxY;

    /**
     * gets the coordinates
     * @return returns array
     */
    public double[] getCoords() {
        return coords;
    }

    /**
     * sets the coordinates
     * @param coords array
     */
    public void setCoords(double[] coords) {
        this.coords = coords;
    }

    /**
     * gets the minimum x coordinate
     * @return returns min X
     */
    public double getMinX() {
        return minX;
    }

    /**
     * sets the minimum x coordinate
     * @param minX double
     */
    public void setMinX(double minX) {
        this.minX = minX;
    }

    /**
     * Gets the minimum Y coordinate
     * @return returns min Y
     */
    public double getMinY() {
        return minY;
    }

    /**
     * sets the minimum Y coordinate
     * @param minY double
     */
    public void setMinY(double minY) {
        this.minY = minY;
    }

    /**
     * Gets the maximum X coordinate
     * @return returns max X
     */
    public double getMaxX() {
        return maxX;
    }

    /**
     * Sets the maximum X coordinate
     * @param maxX double
     */
    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    /**
     * Gets the maximum Y coordinate
     * @return returns max Y
     */
    public double getMaxY() {
        return maxY;
    }

    /**
     * sets the maximum Y coordinate
     * @param maxY double
     */
    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    /**
     * Constructor that creates the way itself, also used in many other classes
     * @param way arraylist of nodes
     */
    public Way(ArrayList<Node> way) {
        coords = new double[way.size() * 2];
        for (int i = 0 ; i < way.size() ; ++i) {
            var node = way.get(i);
            coords[2 * i] = 0.56 * node.getLon();
            coords[2 * i + 1] = -node.getLat();

            
            if(i == 0) {
            	minX = 0.56 * node.getLon();
            	minY = -node.getLat();
            	maxX = 0.56 * node.getLon();
            	maxY = -node.getLat();
            }
            if(0.56 * node.getLon() < minX) minX = 0.56 * node.getLon();
            if(0.56 * node.getLon() > maxX) maxX = 0.56 * node.getLon();
            if(-node.getLat() < minY) minY = -node.getLat();
            if(-node.getLat() > maxY) maxY = -node.getLat();
        }
    }

    /**
     * Second simple constructor that initializes coords
     * @param way object of way
     */
    public Way(Way way){
        coords = new double[way.coords.length];
        coords = way.coords;
    }

    /**
     * Draws the way
     * @param gc gc graphics context from view and JavaFX
     */
    public void draw(GraphicsContext gc) {
        gc.beginPath();
        gc.moveTo(coords[0], coords[1]);
        for (int i = 2 ; i < coords.length ; i += 2) {
            gc.lineTo(coords[i], coords[i+1]);
        }

        gc.stroke();
    }

    /**
     * Compares minimum and maximum X and Y coordinates
     * @param way the object to be compared.
     * @return returns comparison between either ways min X or Y
     */
    @Override
    public int compareTo(Way way) {
    	if(TreeNode.SortX) {
    		return Double.compare(minX, way.minX);
    	} else {
    		return Double.compare(minY, way.minY);
    	}
    }
}