package Map_of_Denmark.utilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;

import Map_of_Denmark.model.Highway;

/**
 * This class is used to collect all the ways from the three categories
 */
public class WayCollect {

    List<Highway> wayCollect = new ArrayList<Highway>();
    HashSet<Long> refset = new HashSet<Long>();
    HashMap<Long, Integer> nodeid2id = new HashMap<Long, Integer>();

    /**
     * This constructor is used to collect all the ways from the three categories
     * @param highwayCatList1 List of ways from the first category
     * @param highwayCatList2 List of ways from the second category
     * @param highwayCatList3 List of ways from the third category
     * @return List of all the ways from the three categories
     */
    public List<Highway> wayCollect(List<Highway>highwayCatList1, List<Highway>highwayCatList2, List<Highway>highwayCatList3){

        wayCollect.addAll(highwayCatList1);
        wayCollect.addAll(highwayCatList2);
        wayCollect.addAll(highwayCatList3);

        return wayCollect;
    }

    /**
     *  This method is used to create a set of all the nodes from the ways
     * @param wayCollect List of all the ways from the three categories
     * @return HashSet of all the nodes from the ways
     */
    public HashMap<Long, Integer> CreateNode2id2(List<Highway> wayCollect){

        for(int i = 0; i<wayCollect.size(); i++)
        {
            Highway hWay = wayCollect.get(i);
            for( int j = 0 ; j < hWay.getList().size(); j++)
            {
                Long nodeId = hWay.getList().get(j).getNodeID();
                refset.add(nodeId);
            }
        }

        int s = 0;
        for(Long element : refset)
        {
            nodeid2id.put(element,s);
            s = s+1;
        }
        return nodeid2id;
    }

}
