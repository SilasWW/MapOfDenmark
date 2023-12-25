package Map_of_Denmark.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.Serializable;

/**
 * Class for creating points of interest
 */
public class InterestPoint implements Serializable {
	private final double lon;
	private final double lat;
	private final Color color;
	private String name;

    /**
     * constructor that initializes lat, lot and color fields
     * @param lat
     * @param lon
     * @param color
     */
	public InterestPoint(double lat, double lon, Color color) {
		this.lat = lat;
		this.lon = lon;
		this.color = color;
	}

    /**
     * Gets the lat coordinate
     * @return returns lat
     */
    public double getLat(){
        return this.lat;
    }

    /**
     * gets the lon coordinate
     * @return returns lon
     */
    public double getLon(){
        return this.lon;
    }

    /**
     * draws the circle with color and a set circle size
     * @param gc graphics context from view and JavaFX
     */
    public void draw(GraphicsContext gc){
        gc.setFill(Color.BLACK);
        gc.fillRoundRect(lat - 0.00002, lon - 0.00002, 0.00016, 0.00016, 0.00016, 0.00016);
        
        gc.setFill(color);
        gc.fillRoundRect(lat, lon, 0.00012, 0.00012, 0.00012, 0.00012);
    }

    /**
     * sets the name
     * @param name string
     */
    protected void setName(String name) {
    	this.name = name;
    }

    /**
     * Gets the name
     * @return returns name
     */
    public String getName() {
    	return name;
    }

    /**
     * gets the color
     * @return returns color
     */
    public Color getColor() {
    	return color;
    }
}
