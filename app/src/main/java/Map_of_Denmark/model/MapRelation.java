package Map_of_Denmark.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Maprelation class. Made to handle the Osm Relations. Relation consist of multiple ways (consisting of multiple nodes)
 * Drawing Relations differently matching what type of tag we consider them related to.
 */
public class MapRelation implements Serializable {
    private final RelationEnum type;
    private final ArrayList<Way> innerWays = new ArrayList<>();
    private final ArrayList<Way> outerWays = new ArrayList<>();

    /**
     * Constructor for the MapRelation class
     * @param relationInner takes ways assigned as inners as an ArrayList<Way>
     * @param relationOuter takes ways assigned as inners as an ArrayList<Way>
     * @param type takes type as a RelationEnum. used to determine draw functionality
     */
    public MapRelation(ArrayList<Way> relationInner, ArrayList<Way> relationOuter, RelationEnum type) {
        this.type = type;
        innerWays.addAll(relationInner);

        outerWays.addAll(relationOuter);
    }

    /**
     * draws the ways of the relation in a way specific to the relations type variable.
     * @param gc graphicsContext
     */
    public void draw(GraphicsContext gc){
        var oldWidth = gc.getLineWidth();
        var oldStroke = gc.getStroke();
        var widthMod = Math.sqrt(gc.getTransform().determinant());

        gc.setLineWidth(1/widthMod);
        switch (type) {
            case WATER, COASTLINE -> {
                gc.setStroke(Color.LIGHTSKYBLUE);
                gc.setFill(Color.LIGHTSKYBLUE);
            }
            case FOREST -> {
                gc.setStroke(Color.FORESTGREEN);
                gc.setFill(Color.FORESTGREEN);
            }
            case ISLAND, PENINSULA -> {
                gc.setStroke(Color.MEDIUMPURPLE);
                gc.setFill(Color.RED);
            }
        }
        for (Way outerWay : outerWays) {
        	if(outerWay == null) continue;
            outerWay.draw(gc);
        }

        switch (type) {
            case WATER, COASTLINE -> {
                gc.setStroke(Color.rgb(174, 216, 242));
                gc.setFill(Color.rgb(135, 206, 250));
            }
            case FOREST -> {
                gc.setStroke(Color.rgb(55, 171, 55));
                gc.setFill(Color.rgb(55, 171, 5));
            }
            case ISLAND, PENINSULA -> {
                gc.setStroke(Color.rgb(179, 152, 235));
                gc.setFill(Color.rgb(230, 73, 73));
            }
        }
        for (Way innerWay : innerWays) {
        	if(innerWay == null) continue;
        	innerWay.draw(gc);
        }

        gc.setStroke(oldStroke);
        gc.setLineWidth(oldWidth);
    }

    /**
     * toString method
     * @return string holding the type, amount of outerWays and amount of innerways.
     */
    public String toString(){

        if (outerWays.size()>2 || innerWays.size()>2){
            return type + " " + outerWays.size() + " " + innerWays.size() + "\n";
        }else {
            return "";
        }

    }
}

