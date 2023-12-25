package Map_of_Denmark.model;

import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.TST;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import Map_of_Denmark.utilities.Ewgraph;
import Map_of_Denmark.utilities.FindRoute;
import Map_of_Denmark.utilities.Trie;
import Map_of_Denmark.utilities.WayCollect;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.*;
import java.util.zip.ZipInputStream;

/**
 * Model class for the model layer of MVC structure. in charge of parsing through the osm file and keeping relevant
 * objects stored in datastructures within. In charge of creating and storing data for the KD-Trees. partly used to call
 * search methods and to create points of interest on the map.
 */
public class Model implements Serializable {
    public List<Line> lines = new ArrayList<>();
    public List<Way> wayList = new ArrayList<>();
    public List<Way> buildingList = new ArrayList<>();
    public List<Tree> treeList = new ArrayList<>();
    public List<Way> parkList = new ArrayList<>();
    public List<Way> waterList = new ArrayList<>();
    public List<Way> parkingList = new ArrayList<>();
    public List<Way> grassList = new ArrayList<>();
    public List<Way> gardenList = new ArrayList<>();
    public List<Way> forestList = new ArrayList<>();
    public List<Way> coastlineList = new ArrayList<>();
    public List<Highway> highwayCat1List = new ArrayList<>();
    public List<Highway> highwayCat2List = new ArrayList<>();
    public List<Highway> highwayCat3List = new ArrayList<>();
    public List<Highway> highwayCat4List = new ArrayList<>();
    public List<Way> developedAreasList = new ArrayList<>();
    public List<Way> natureAreasList = new ArrayList<>();
    public List<MapRelation> relationList = new ArrayList<>();
    public ArrayList<Address> AddressList = new ArrayList<>();
    public ArrayList<Route> RouteList = new ArrayList<>();
    public List<MapRelation> boundingRelationList = new ArrayList<>();
    public List<MapRelation> geoStructureRelationList = new ArrayList<>();
    public List<InterestPoint> interestPointList = new ArrayList<>();
    private HashMap<Long, Node> id2node = new HashMap<>();

    private Boolean carRoute = true;

    public TreeNode wayTreeRoot;
    public TreeNode buildingTreeRoot;
    public TreeNode parkTreeRoot;
    public TreeNode waterTreeRoot;
    public TreeNode parkingTreeRoot;
    public TreeNode grassTreeRoot;
    public TreeNode gardenTreeRoot;
    public TreeNode forestTreeRoot;
    public TreeNode coastlineTreeRoot;
    public TreeNode highwayCat1TreeRoot;
    public TreeNode highwayCat2TreeRoot;
    public TreeNode highwayCat3TreeRoot;
    public TreeNode highwayCat4TreeRoot;
    public TreeNode developedAreasTreeRoot;
    public TreeNode natureAreasTreeRoot;


    public double minlat;
    public double maxlat;
    public double minlon;
    public double maxlon;

    /**
     * loads a file from filename variable. If dealing with an obj file creates an objectInputStream.
     * @param filename filename (location in datafolder) as string
     * @return model object for use elsewhere.
     * @throws IOException exception
     * @throws ClassNotFoundException exception
     * @throws XMLStreamException exception
     * @throws FactoryConfigurationError exception
     */
    public static Model load(String filename) throws IOException, ClassNotFoundException, XMLStreamException, FactoryConfigurationError {
        if (filename.endsWith(".obj")) {
            try (var in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
                return (Model) in.readObject();
            }
        }
        return new Model(filename);
    }

    /**
     * Model constructor calling the proper helper method to support different fileformats. Saves model as an obj file
     * after calling parseosm, parseZip or parsetxt to create data.
     * @param filename filename (location in datafolder) as string
     * @throws XMLStreamException exception
     * @throws FactoryConfigurationError exception
     * @throws IOException exception
     */
    public Model(String filename) throws XMLStreamException, FactoryConfigurationError, IOException {
        if (filename.endsWith(".osm.zip")) {
            parseZIP(filename);
        } else if (filename.endsWith(".osm")) {
            parseOSM(filename);
        } else {
            parseTXT(filename);
        }
        save(filename+".obj");
    }

    /**
     * saves model object as an obj file for later use.
     * @param filename filename (location in datafolder) as string
     * @throws IOException exception
     */
    void save(String filename) throws IOException {
        try (var out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        }
    }

    /**
     * parses an osm.zip file to be readable with parseOSM
     * @param filename filename (location in datafolder) as string
     * @throws IOException exception
     * @throws XMLStreamException exception
     * @throws FactoryConfigurationError exception
     */
    private void parseZIP(String filename) throws IOException, XMLStreamException, FactoryConfigurationError {
        var input = new ZipInputStream(new FileInputStream(filename));
        input.getNextEntry();
        parseOSM(input);
    }

    /**
     * a parseosm method that calls the proper Parseosm. This is a helper method that converts a filename given as string
     * to a fileinputstream that is usable to parseosm method
     * @param filename filename (location in datafolder) as string
     * @throws FileNotFoundException exception
     * @throws XMLStreamException  exception
     * @throws FactoryConfigurationError exception
     */
    private void parseOSM(String filename) throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        parseOSM(new FileInputStream(filename));
    }

    /**
     * the main parseosm method. Iterates through the OSM file one line at a time creating objects from the data
     * collected and storing it along the way. creates all objects used for the KDTree and calls the build method for this.
     * More precise information regarding the internal actions can be found in the methods in-line comments
     * @param inputStream osmfile inputstream oftentimes passed on from parseosm helper method
     * @throws XMLStreamException exception
     * @throws FactoryConfigurationError exception
     */
    private void parseOSM(InputStream inputStream) throws XMLStreamException, FactoryConfigurationError {
        var input = XMLInputFactory.newInstance().createXMLStreamReader(new InputStreamReader(inputStream));

        //Data structures used to store ids and objects together and a collection of reference ids

        long wayId = 0;
        var way = new ArrayList<Node>();

        var id2way = new HashMap<Long, Way>();
        HashSet<Long> refset = new HashSet<>();
        id2node = new HashMap<>();

        //Collecting data for ways (picking up nodes) and for relations (picking up ways)
        var relationInner = new ArrayList<Way>();
        var relationOuter = new ArrayList<Way>();

        //dataStructures relating to graphing and searching the roads
        var wayCollect = new ArrayList<Highway>();
        List<List<Node>> wayCol1 = new ArrayList<>();

        TagEnum tag = TagEnum.UNDEFINED;
        boolean addressTag = false;

        int highwayCat = 0;

        String city, houseNumber, postCode, street, wayName;
        city = houseNumber = postCode = street = wayName = "n/a";

        long nodeID = 0;

        /*
            picking up one element at a time we go through the osm Document picking up relevant information to create objects
            for each element if it is applicable. When a tag like "member" is a child element of i.e. "relation" we gather the
            information of the member tags to create the "relation" object when the relation end_element is encountered.
            Used with ways and nodes as well.
         */
        while (input.hasNext()) {
            var tagKind = input.next();

            /*
                encountering a start_element we start to gather all new data. therefore we reset all tags by assigning them
                FALSE and clear the dataStructures used to make the object i.e. "way" arraylist is cleared to hold a new
                set of nodes for the new way object.
             */
            if (tagKind == XMLStreamConstants.START_ELEMENT) {
                var tagName = input.getLocalName();

                switch (tagName) {
                    case "relation" -> {
                        relationInner.clear();
                        relationOuter.clear();
                    }
                    case "member" -> {
                        String type = input.getAttributeValue(null, "type");
                        if (type.equals("way")) {
                            String checker = input.getAttributeValue(null, "role");
                            Way addWay = id2way.get(Long.parseLong(input.getAttributeValue(null, "ref")));

                            if (checker.equals("inner")) {
                                relationInner.add(addWay);
                            } else {
                                relationOuter.add(addWay);
                            }
                        }
                    }
                    case "way" -> {
                        wayId = Long.parseLong(input.getAttributeValue(null, "id"));
                        way.clear();
                        highwayCat = 0;
                    }
                    case "bounds" -> {
                        minlat = Double.parseDouble(input.getAttributeValue(null, "minlat"));
                        maxlat = Double.parseDouble(input.getAttributeValue(null, "maxlat"));
                        minlon = Double.parseDouble(input.getAttributeValue(null, "minlon"));
                        maxlon = Double.parseDouble(input.getAttributeValue(null, "maxlon"));
                    }
                    case "node" -> {
                        nodeID = Long.parseLong(input.getAttributeValue(null, "id"));
                        var lat = Double.parseDouble(input.getAttributeValue(null, "lat"));
                        var lon = Double.parseDouble(input.getAttributeValue(null, "lon"));
                        id2node.put(nodeID, new Node(nodeID, lat, lon));
                    }
                    case "tag" -> {
                        /*
                            the "tag" element in osm data is associated with the AttributeValues "k" and "v".
                            this is the key/valuepair of the tag. we have switch statements for both "v" and "k" this
                            could be altered to have the "v" switch cases under the "k" switch cases as partially done.
                        */
                        var v = input.getAttributeValue(null, "v");
                        var k = input.getAttributeValue(null, "k");

                        switch (v) {
                            case "coastline" -> tag = TagEnum.COASTLINE;
                            case "tree" -> tag = TagEnum.TREENODE;
                            case "park" -> tag = TagEnum.PARK;
                            case "water" -> tag = TagEnum.WATER;
                            case "grass" -> tag = TagEnum.GRASS;
                            case "garden" -> tag = TagEnum.GARDEN;
                            case "forest" -> tag = TagEnum.FOREST;
                        }
                        switch (k) {
                            case "building":
                                tag = TagEnum.BUILDING;
                                break;
                            case "parking":
                                tag = TagEnum.PARKING;
                                break;
                            case "landuse": {
                                tag = switch (v) {
                                    case "residential", "commercial", "construction", "education", "industrial", "retail", "institutional" ->
                                            TagEnum.DEVELOPEDAREA;
                                    case "allotments", "farmland", "farmyard", "forest", "meadow", "orchard", "plant_nursery", "vineyard", "landfill" ->
                                            TagEnum.NATUREAREA;
                                    default -> tag;
                                };
                            }
                            break;
                            case "natural": {
                                tag = switch (v) {
                                    case "wood" -> TagEnum.NATUREAREA;
                                    case "water" -> TagEnum.WATER;
                                    case "peninsula" -> TagEnum.PENINSULA;
                                    default -> tag;
                                };
                            }
                            break;
                            case "place": {
                                if ("island".equals(v)) {
                                    tag = TagEnum.ISLAND;
                                }
                            }
                            break;
                            case "highway": {
                                tag = TagEnum.HIGHWAY;
                                highwayCat = switch (v) {
                                    case "motorway", "motorway_link" -> 1;
                                    case "trunk", "trunk_link", "primary", "primary_link" -> 2;
                                    case "secondary", "secondary_link", "tertiary", "tertiary_link", "unclassified", "residential", "service" ->
                                            3; /*???*/
                                    /*footway to cycleway could be used for bikedirections?*/
                                    case "living_street", "pedestrian", "track", "bus_guideway", "road", "busway", "mini_roundabout", "footway", "bridleway", "path", "cycleway" ->
                                            4;
                                    default -> highwayCat;
                                };
                            }
                            case "name": {
                                wayName = v;
                            }
                            break;

                            case "addr:city":
                                city = v;
                                break;
                            case "addr:housenumber":
                                houseNumber = v;
                                break;
                            case "addr:postcode":
                                postCode = v;
                                break;
                            case "addr:street":
                                street = v;
                                addressTag = true;
                                break;
                        }

                    }
                    case "nd" -> {
                        var ref = Long.parseLong(input.getAttributeValue(null, "ref"));
                        var node = id2node.get(ref);
                        way.add(node);
                    }
                }

            } else if (tagKind == XMLStreamConstants.END_ELEMENT) {
                var name = input.getLocalName();

                /*
                    the three cases for end element (way,relation and node) handles different types of tags and creates
                    an object corresponding to the tag boolean associated with it, so we can draw it accordingly and
                    handle specifics to the abstracted concept of object.
                */
                switch (name) {
                    case "way" -> {
                        Way useWay = new Way(way);
                        id2way.put(wayId, useWay);

                        switch (tag) {
                            case COASTLINE -> coastlineList.add(new Coastline(way));
                            case FOREST -> forestList.add(new Forest(way));
                            case WATER -> waterList.add(new Water(way));
                            case PARK -> parkList.add(new Park(way));
                            case GRASS -> grassList.add(new Grass(way));
                            case GARDEN -> gardenList.add(new Garden(way));
                            case PARKING -> parkingList.add(new Parking(way));
                            case BUILDING -> buildingList.add(new Building(way));
                            case NATUREAREA -> natureAreasList.add(new Landuse(way, 2));
                            case DEVELOPEDAREA -> developedAreasList.add(new Landuse(way, 1));
                            case HIGHWAY -> {
                                switch (highwayCat) {
                                    case 1 -> highwayCat1List.add(new Highway(way, 1, wayName));
                                    case 2 -> highwayCat2List.add(new Highway(way, 2, wayName));
                                    case 3 -> highwayCat3List.add(new Highway(way, 3, wayName));
                                    case 4 -> highwayCat4List.add(new Highway(way, 4, wayName));
                                }
                            }
                            case UNDEFINED -> wayList.add(new Way(way));
                        }


                    }
                    case "relation" -> {

                        switch (tag) {
                            case COASTLINE ->
                                    geoStructureRelationList.add(new MapRelation(relationInner, relationOuter, RelationEnum.COASTLINE));
                            case FOREST ->
                                    geoStructureRelationList.add(new MapRelation(relationInner, relationOuter, RelationEnum.FOREST));
                            case WATER ->
                                    geoStructureRelationList.add(new MapRelation(relationInner, relationOuter, RelationEnum.WATER));
                            case ISLAND ->
                                    boundingRelationList.add(new MapRelation(relationInner, relationOuter, RelationEnum.ISLAND));
                            case PENINSULA ->
                                    boundingRelationList.add(new MapRelation(relationInner, relationOuter, RelationEnum.PENINSULA));
                        }

                    }
                    case "node" -> {
                        if (tag == TagEnum.TREENODE) {
                            treeList.add(new Tree(id2node.get(nodeID)));
                        } else if (addressTag) {
                            Address address = new Address(street, houseNumber, postCode, city, id2node.get(nodeID));
                            AddressList.add(address);
                        }
                    }
                }
            }
        }

        //Adds all highways that are loopways to waycollect
        for(Way loopWay : highwayCat1List) if(loopWay instanceof Highway) wayCollect.add((Highway) loopWay);
        for(Way loopWay : highwayCat2List) if(loopWay instanceof Highway) wayCollect.add((Highway) loopWay);
        for(Way loopWay : highwayCat3List) if(loopWay instanceof Highway) wayCollect.add((Highway) loopWay);
        for(Way loopWay : highwayCat4List) if(loopWay instanceof Highway) wayCollect.add((Highway) loopWay);

        /*
        builds kdtrees for each type of treenode from a List of ways. as these are already sorted out to different types
        of geographical element we make different treeroots for each
         */
        wayTreeRoot = new TreeNode(0).build(wayList);
        buildingTreeRoot = new TreeNode(0).build(buildingList);
        parkTreeRoot = new TreeNode(0).build(parkList);
        waterTreeRoot = new TreeNode(0).build(waterList);
        parkingTreeRoot = new TreeNode(0).build(parkingList);
        grassTreeRoot = new TreeNode(0).build(grassList);
        gardenTreeRoot = new TreeNode(0).build(gardenList);
        forestTreeRoot = new TreeNode(0).build(forestList);

        coastlineTreeRoot = new TreeNode(0).build(coastlineList);

        highwayCat1TreeRoot = new TreeNode(0).buildHighway(highwayCat1List);
        highwayCat2TreeRoot = new TreeNode(0).buildHighway(highwayCat2List);
        highwayCat3TreeRoot = new TreeNode(0).buildHighway(highwayCat3List);
        highwayCat4TreeRoot = new TreeNode(0).buildHighway(highwayCat4List);

        natureAreasTreeRoot = new TreeNode(0).build(natureAreasList);
        developedAreasTreeRoot = new TreeNode(0).build(developedAreasList);
    }

    /**
     * parses file. helper method to read a txt file as contain osm data that we can parse
     * @param filename Location of file in data folder as a string
     * @throws FileNotFoundException exception
     */
    private void parseTXT(String filename) throws FileNotFoundException {

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(new Line(line));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * adds a line from one point to another. Low usage but might be important
     * @param p1 first point
     * @param p2 second point
     */
    public void add(Point2D p1, Point2D p2) {
        lines.add(new Line(p1, p2));
    }

    /**
     * Adds point of interest to the map. from coordinates supplied from UserInteface class. Picks random color and adds
     * to the list of currently shown POI
     * @param lat latitudinal coordinate
     * @param lon longitudinal coordinate
     */
    public void addPOI(double lat, double lon) {
        /*
            instantiate new Random in charge of picking POI color
            instantiate new POI
            asserts name of POI and adds to the list
         */
        Random r = new Random();
        InterestPoint poi = new InterestPoint(lat, lon, Color.rgb(r.nextInt(50, 256), r.nextInt(50, 256), r.nextInt(50, 256)));

        poi.setName("Point " + (interestPointList.size() + 1));
        interestPointList.add(poi);
    }

    /**
     * Deletes previous route. Three lists are selected on basis of the carRoute boolean assigned by the UI buttons,
     * in order to choose between roads where Biking is possible and routes where car traffic is possible.
     * Builds an Edgeweighteddigraph (ewtree) with the three lists. Uses Findroute, Trie, DijkstraSP classes and methods
     * to find the shortest possible route and adds this route as a route object in an ArrayList<Route>
     * @param start starting address given as a string. Format: "Roadname Number town postalcode"
     * @param slut End address given as a string. Format: "Roadname Number town postalcode"
     */
    public void search(String start, String slut){
        RouteList.clear();
        WayCollect wc = new WayCollect();

        List<Highway> wayCol = new ArrayList<>();
        if(carRoute){
            wayCol = wc.wayCollect(highwayCat1List, highwayCat2List, highwayCat3List);
        } else{
            wayCol = wc.wayCollect(highwayCat2List, highwayCat3List, highwayCat4List);

        }

        HashMap<Long,Integer> nodeid2id = wc.CreateNode2id2(wayCol);

        Ewgraph ewgraph = new Ewgraph();
        EdgeWeightedDigraph Tree = ewgraph.buildGraph(wayCol, nodeid2id);

        Long tmpNode = Long.MAX_VALUE;
        Long tmpNode2 = Long.MAX_VALUE;

        FindRoute FR = new FindRoute();

        Trie tst = new Trie();
        TST<String> S =  tst.CreateTST(AddressList);

        String AddressStart = start;
        String AddressSlut = slut;

        for(int i = 0; i<AddressList.size(); i++)
        {

            if(AddressList.get(i).getTheAddress().equals(tst.findAddress(S,AddressStart)))
            {
                Address tmpAddress = AddressList.get(i);
                tmpNode = FR.FindNode(wayCol, tmpAddress);
            }


            if(AddressList.get(i).getTheAddress().equals(tst.findAddress(S,AddressSlut)))
            {
                Address tmpAddress2 = AddressList.get(i);
                tmpNode2 = FR.FindNode(wayCol, tmpAddress2);
            }
        }

        RouteList = FR.Route(Tree, nodeid2id, id2node, tmpNode, tmpNode2);

    }

    /**
     * Helper method as a middleman between the ui and the tries as we want to give tst the adresslist from model and
     * the string we get as userinput. is called everytime the user presses a button in one of the two searchbars.
     * calls Trie tst.findmatch(). returns the same as the tst.findMatch() method.
     * @param searchword the string that the user has entered into the searchbar including the last typed character.
     *                   to be passed on to findmatch()
     * @return returns Iterable<String> match which is the same as findmatch() method.
     */
    public Iterable<String> preSearch(String searchword){

        Trie tst = new Trie();
        Iterable<String> match = tst.findMatch(AddressList, searchword);

        return match;
    }

    /**
     * setter method for carroute boolean
     * @param carRoute boolean value
     */
    public void setCarRoute(Boolean carRoute) {
        this.carRoute = carRoute;
    }

    /**
     * getter method for wayList
     * @return list of way that did not fit in to other categories
     */
    public List<Way> getWayList() {
        return wayList;
    }

    /**
     * getter method for lines
     * @return return list of lines
     */
    public List<Line> getLines() {
        return lines;
    }

    /**
     * getter for buiildinglist
     * @return return list of buildings on the map
     */
    public List<Way> getBuildingList() {
        return buildingList;
    }
    /**
     * getter for treelist
     * @return return list of trees on the map
     */
    public List<Tree> getTreeList() {
        return treeList;
    }

    /**
     * getter for parklist
     * @return return list of parks on the map
     */
    public List<Way> getParkList() {
        return parkList;
    }

    /**
     * getter for waterlist
     * @return return list of way with the water tag on the map
     */
    public List<Way> getWaterList() {
        return waterList;
    }

    /**
     * getter for parkinglist
     * @return return list of parkingspots on the map
     */
    public List<Way> getParkingList() {
        return parkingList;
    }

    /**
     * getter for grasslist
     * @return return list of ways with the grass tag on the map
     */
    public List<Way> getGrassList() {
        return grassList;
    }

    /**
     * getter for gradenlist
     * @return return list of gardens on the map
     */
    public List<Way> getGardenList() {
        return gardenList;
    }

    /**
     * getter for forestlist
     * @return return list of ways with the forest tag on the map
     */
    public List<Way> getForestList() {
        return forestList;
    }

    /**
     * getter for highwaycat1list
     * @return return list of major highways on the map
     */
    public List<Highway> getHighwayCat1List() {
        return highwayCat1List;
    }

    /**
     * getter for highwaycat2list
     * @return return list of all large roads (highways) on the map
     */
    public List<Highway> getHighwayCat2List() {
        return highwayCat2List;
    }

    /**
     * getter for highwaycat3list
     * @return return list of minor roads  (highways) on the map
     */
    public List<Highway> getHighwayCat3List() {
        return highwayCat3List;
    }

    /**
     * getter for highwaycat4list
     * @return return list of bicycle and walking only roads (highways) on the map
     */
    public List<Highway> getHighwayCat4List() {
        return highwayCat4List;
    }

    /**
     * getter method for relationlist
     * @return list of relation on the map
     */
    public List<MapRelation> getRelationList() {
        return relationList;
    }

    /**
     * getter method for the adresslist
     * @return list of adress objects
     */
    public ArrayList<Address> getAddressList() {
        return AddressList;
    }

    /**
     * getter for routelist
     * @return list of route objects
     */
    public ArrayList<Route> getRouteList() {
        return RouteList;
    }

    /**
     * getter for boundingrelationlist
     * @return list of maprelation object containing relation like island and peninsula
     */
    public List<MapRelation> getBoundingRelationList() {
        return boundingRelationList;
    }

    /**
     * getter for geostructurerelationlist
     * @return list of maprelation objects containing relation like forests and other cartographically relevant structures
     */
    public List<MapRelation> getGeoStructureRelationList() {
        return geoStructureRelationList;
    }

    /**
     * getter for interestpointlist
     * @return list of POI's currently added to the map by the user and shown
     */
    public List<InterestPoint> getInterestPointList() {
        return interestPointList;
    }

    /**
     * getter for treeroot
     * @return way treeroot object
     */
    public TreeNode getWayTreeRoot() {
        return wayTreeRoot;
    }

    /**
     * getter for treeroot
     * @return building treeroot object
     */
    public TreeNode getBuildingTreeRoot() {
        return buildingTreeRoot;
    }

    /**
     * getter for treeroot
     * @return park treeroot object
     */
    public TreeNode getParkTreeRoot() {
        return parkTreeRoot;
    }

    /**
     * getter for treeroot
     * @return water treeroot object
     */
    public TreeNode getWaterTreeRoot() {
        return waterTreeRoot;
    }

    /**
     * getter for treeroot
     * @return parking treeroot object
     */
    public TreeNode getParkingTreeRoot() {
        return parkingTreeRoot;
    }

    /**
     * getter for treeroot
     * @return grass treeroot object
     */
    public TreeNode getGrassTreeRoot() {
        return grassTreeRoot;
    }

    /**
     * getter for treeroot
     * @return garden treeroot object
     */
    public TreeNode getGardenTreeRoot() {
        return gardenTreeRoot;
    }

    /**
     * getter for treeroot
     * @return forest treeroot object
     */
    public TreeNode getForestTreeRoot() {
        return forestTreeRoot;
    }

    /**
     * getter for treeroot
     * @return coastline treeroot object
     */
    public TreeNode getCoastlineTreeRoot() {
        return coastlineTreeRoot;
    }

    /**
     * getter for treeroot
     * @return highwaycat1 treeroot object
     */
    public TreeNode getHighwayCat1TreeRoot() {
        return highwayCat1TreeRoot;
    }

    /**
     * getter for treeroot
     * @return highwaycat2 treeroot object
     */
    public TreeNode getHighwayCat2TreeRoot() {
        return highwayCat2TreeRoot;
    }

    /**
     * getter for treeroot
     * @return highwaycat3 treeroot object
     */
    public TreeNode getHighwayCat3TreeRoot() {
        return highwayCat3TreeRoot;
    }

    /**
     * getter for treeroot
     * @return highwaycat4 treeroot object
     */
    public TreeNode getHighwayCat4TreeRoot() {
        return highwayCat4TreeRoot;
    }

    /**
     * getter for minimum lattitude
     * @return double minlat
     */
    public double getMinlat() {
        return minlat;
    }

    /**
     * getter maximum lattitude
     * @return double maxlat
     */
    public double getMaxlat() {
        return maxlat;
    }

    /**
     * getter for minimum longitude
     * @return double minlon
     */
    public double getMinlon() {
        return minlon;
    }

    /**
     * getter for maximum longitude
     * @return double maxlon
     */
    public double getMaxlon() {
        return maxlon;
    }
}