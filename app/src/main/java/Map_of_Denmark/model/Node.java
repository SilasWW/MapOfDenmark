package Map_of_Denmark.model;
import java.io.Serializable;

/**
 * Class that creates the nodes
 */
public class Node implements Serializable{
    private long nodeID;
    private double lat, lon;

    /**
     * Initializes lat coordinate
     * @param lat double
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * Initializes lon coordinate
     * @param lon double
     */
    public void setLon(double lon) {
        this.lon = lon;
    }

    /**
     * Initializes node id
     * @param nodeID long
     */
    public void setNodeID(long nodeID) {
        this.nodeID = nodeID;
    }

    /**
     * Constructor that initializes fields
     * @param nodeID long
     * @param lat double
     * @param lon double
     */
    public Node(long nodeID, double lat, double lon) {
        this.nodeID = nodeID;
        this.lat = lat;
        this.lon = lon;
        this.nodeID=nodeID;
    }

    /**
     * Returns lat coordinate
     * @return lat double
     */
    public double getLat(){
        return this.lat;
    }

    /**
     * Returns lot coordinate
     * @return lot double
     */
    public double getLon(){
        return this.lon;
    }

    /**
     * Returns node id
     * @return nodeID long
     */
    public long getNodeID(){
        return this.nodeID;
    }
}