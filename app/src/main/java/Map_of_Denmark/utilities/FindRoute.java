package Map_of_Denmark.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.List;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;

import Map_of_Denmark.model.Node;
import Map_of_Denmark.model.Highway;
import Map_of_Denmark.model.Address;
import Map_of_Denmark.model.Route;

/**
 * This class is used to find the route between two nodes.
 */
public class FindRoute {

    public ArrayList<Integer> RouteDef = new ArrayList<>();
    public ArrayList<Node> RouteNode = new ArrayList<>();
    public ArrayList<Long> RouteRef = new ArrayList<>();
    public ArrayList<Route> RouteList = new ArrayList<>();

    /**
     * This method is used to find the route between two nodes.
     * @param Tree The digraph to parse
     * @param nodeid2id The hashmap to parse as to id
     * @param id2node  The hashmap to parse as to node
     * @param startNode The start node
     * @param slutNode The end node
     * @return The route between the two nodes as an arraylist of routes
     */
    public ArrayList<Route> Route(EdgeWeightedDigraph Tree, HashMap<Long,Integer> nodeid2id, HashMap<Long, Node> id2node, Long startNode, Long slutNode ){

        DijkstraSP SR = new DijkstraSP(Tree, nodeid2id.get(startNode));
        Iterable<DirectedEdge> route =  SR.pathTo(nodeid2id.get(slutNode));

        for(DirectedEdge element : route)
        {
            if(!RouteDef.contains(element.from()))
            {
                RouteDef.add(element.from());
            }
            if(!RouteDef.contains(element.to()))
            {
                RouteDef.add(element.to());
            }
        }

        for(int i = 0; i < RouteDef.size(); i++)
        {
            for(long key: nodeid2id.keySet())
            {
                if(Objects.equals(nodeid2id.get(key), RouteDef.get(i)))
                {
                    RouteNode.add(id2node.get(key));
                }
            }
        }

        RouteList.add(new Route(RouteNode));

        return RouteList;
    }

    /**
     * This method is used to find the nearest point
     * @param wayCollect The list of highways to parse in wayCollect
     * @param address The address to parse in address
     * @return The route distance as a long
     */
    public Long FindNode(List<Highway> wayCollect, Address address){

        Long tmpNodeId = Long.MAX_VALUE;
        Double tmpDist = Double.MAX_VALUE;

        for(int i = 0; i<wayCollect.size(); i++)
        {
            if(wayCollect.get(i).getwayName().equals(address.getStreet()))
            {
                ArrayList <Node> myWay = wayCollect.get(i).getList();
                for(int j = 0; j < myWay.size(); j++)
                {
                    Double dist = Haversine.haversine(address.getLat(), address.getLon(), myWay.get(j).getLat(), myWay.get(j).getLon());
                    if(dist < tmpDist)
                    {
                        tmpDist = dist;
                        tmpNodeId = myWay.get(j).getNodeID();
                    }
                }
            }
        }
        return tmpNodeId;
    }

    /**
     * This method gets the directions as text
     * @param routeNode The route to parse as an arraylist of nodes
     * @return The directions as a string
     */
    public String getDirectionsText(ArrayList<Node> routeNode){
        StringBuilder sb = new StringBuilder();

        sb.append("Here is your Route from: (" + routeNode.get(0).getLat() + ", " + routeNode.get(0).getLon() + ") to the destination: (");
        sb.append(routeNode.get(routeNode.size()-1).getLat() + ", " + routeNode.get(routeNode.size()-1).getLon() + ") \n\n");
        sb.append("Start from (" + routeNode.get(0).getLat() + ", " + routeNode.get(0).getLon() + ")");

        for (int i = 2; i < routeNode.size(); i+=1) {
            sb.append("Go to (" + routeNode.get(i).getLat() + ", " + routeNode.get(i).getLon() + ") \n");
        }
        sb.append("You have now made it to your destination \n");

        return sb.toString();
    }
}
