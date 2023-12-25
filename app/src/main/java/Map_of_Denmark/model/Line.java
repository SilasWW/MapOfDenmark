package Map_of_Denmark.model;

import java.io.Serializable;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

/**
 * Class for creating lines
 */
public class Line implements Serializable {
    private final double x1;
    private final double y1;
    private final double x2;
    private final double y2;

    /**
     * Constructor that initializes our fields to two x and y coordinates
     * @param line
     */
    public Line(String line) {
        String[] coord = line.split(" ");
        x1 = Double.parseDouble(coord[1]);
        y1 = Double.parseDouble(coord[2]);
        x2 = Double.parseDouble(coord[3]);
        y2 = Double.parseDouble(coord[4]);
    }

    /**
     * Gets our x and y coordinates
     * @param p1 point 2 D
     * @param p2 point 2 D
     */
    public Line(Point2D p1, Point2D p2) {
        x1 = p1.getX();
        y1 = p1.getY();
        x2 = p2.getX();
        y2 = p2.getY();
    }

    /**
     * Draws our lines
     * @param gc graphics context from view and JavaFX
     */
    public void draw(GraphicsContext gc) {
        gc.beginPath();
        gc.moveTo(x1, y1);
        gc.lineTo(x2, y2);
        gc.stroke();
    }

}