package Map_of_Denmark.utilities;

import edu.princeton.cs.algs4.TST;
import Map_of_Denmark.model.Address;

import java.util.ArrayList;

/**
 * This class is used to create a TST and find the address that matches the input.
 * Reference tries: https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/TST.java.html
 */
public class Trie {

    /**
     * This method creates a TST and puts the addresses in it.
     * @param AddressList The list of addresses that will be put in the TST.
     * @return tst The TST that contains the addresses.
     */
    public TST<String> CreateTST(ArrayList<Address> AddressList)
    {
        TST<String> tst = new TST<>();

        for(int i = 0; i < AddressList.size(); i++)
        {
            tst.put(AddressList.get(i).getTheAddress(),Integer.toString(i));
        }

        return tst;
    }

    /**
     * This method finds the address that matches the input.
     * @param tst The TST that contains the addresses.
     * @param adr The input that will be matched with the addresses.
     * @return address The address that matches the input.
     */
    public String findAddress(TST<String> tst, String adr)
    {
        String address = null;

        if(tst.contains(adr))
        {
            for(String element : tst.keysThatMatch(adr))
            {
                address = element;
            }
        }

        return address;
    }

    /**
     * Returns all of the keys in the set that starts with the user specified prefix
     * @param tst The TST that contains the addresses.
     * @param pattern The input that will be matched with the addresses.
     * @return match The addresses that match the input.
     */
    public Iterable<String> findMatch(TST<String> tst, String pattern)
    {
        Iterable<String> match = tst.keysWithPrefix(pattern);
        return match;
    }

    /**
     * This method creates a TST and puts the addresses in it.
     * @param AddressList The list of addresses that will be put in the TST.
     * @param pattern The input that will be matched with the addresses.
     * @return match The addresses that match the input as an Iterable.
     */
    public Iterable<String> findMatch(ArrayList<Address> AddressList, String pattern)
    {
        TST<String> tst = CreateTST(AddressList);
        Iterable<String> match = tst.keysWithPrefix(pattern);
        return match;
    }
}
