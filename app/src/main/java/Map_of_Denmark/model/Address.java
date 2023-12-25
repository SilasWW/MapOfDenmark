package Map_of_Denmark.model;

import java.io.Serializable;

/**
 * Stores an adress as a string (TheAddress) including street, housenumber, postcode and city. Stores the coordinates
 * of the adress gotten from the node object.
 */
public class Address implements Serializable{

//ArrayList<String> Address = new ArrayList<>();
private final double lat;
private final double lon;
private final String street;
private final String houseNumber;
private final String postCode;
private final String city;
private final String TheAddress;

    /**
     * Constructor for the Adress class
     * @param street The streetname of the Highway as string
     * @param houseNumber The housenumber of the point as a string
     * @param postCode The postcode of the city as string
     * @param city The name of the city of the point as a string
     * @param node The Node of the map that translates to a point in Denmark
     */
    public Address(String street, String houseNumber, String postCode, String city, Node node){
        lat = node.getLat();
        lon = node.getLon();
        this.street = street;
        this.houseNumber = houseNumber;
        this.postCode = postCode;
        this.city = city;
        TheAddress = street + " " + houseNumber + " " + postCode + " " + city;
    }

    /**
     * getter for street object
     * @return returns street object
     */
    public String getStreet(){
        return this.street;
    }

    /**
     * getter for housenumber
      * @return returns housenumber as string
     */
    public String getHouseNumber(){
        return this.houseNumber;
    }

    /**
     * getter for postcode
     * @return returns postcode as a string
     */
    public String getPostCode(){
        return this.postCode;
    }

    /**
     * getter for city
     * @return returns city as a string
     */
    public String getCity(){
        return this.city;
    }

    /**
     * getter for TheAdress. Adress  not as an object but as a string. Similar to a toString method.
     * @return adress as a single string
     */
    public String getTheAddress(){
        return TheAddress;
    }

    /**
     * getter for latitudinal coordinate
     * @return lattitude (lat) as a double
     */
    public double getLat(){
        return this.lat;
    }

    /**
     * getter for longitudinal coordinate
     * @return longitude (lon) as a double
     */
    public double getLon(){
        return this.lon;
    }
}
