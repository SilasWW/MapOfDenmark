package Map_of_Denmark.utilities;

/**
 * This class is used to calculate the distance between two points on the map using the Haversine formula.
 * Reference Haversine: https://rosettacode.org/wiki/Haversine_formula#Java
 */
public class Haversine {
    public static final double R = 6372.8; // In kilometers

    /**
     * This method calculates the distance between two points on the map using the Haversine formula.
     * @param lat1 The latitude of the first point.
     * @param lon1 The longitude of the first point.
     * @param lat2 The latitude of the second point.
     * @param lon2 The longitude of the second point.
     * @return The distance between the two points in km.
     */
    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        double dLat = lat2 - lat1;
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }
}