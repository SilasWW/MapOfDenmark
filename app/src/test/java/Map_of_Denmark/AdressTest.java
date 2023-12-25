package Map_of_Denmark;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import Map_of_Denmark.model.Address;
import Map_of_Denmark.model.Node;

class AddressTest {

    @Test
    void testGetAddress() {
        Address address = new Address("Main Street", "123", "12345", "Anytown", new Node(1, 0.0, 0.0));
        assertEquals("Main Street", address.getStreet());
        assertEquals("123", address.getHouseNumber());
        assertEquals("12345", address.getPostCode());
        assertEquals("Anytown", address.getCity());
    }

    @Test
    void testGetLat() {
        Address address = new Address("Main Street", "123", "12345", "Anytown", new Node(2, 0.0, 0.0));
        assertEquals(0.0, address.getLat());
    }

    @Test
    void testGetLon() {
        Address address = new Address("Main Street", "123", "12345", "Anytown", new Node(3,0.0, 0.0));
        assertEquals(0.0, address.getLon());
    }

}

