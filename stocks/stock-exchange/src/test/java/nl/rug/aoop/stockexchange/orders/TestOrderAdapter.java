package nl.rug.aoop.stockexchange.orders;

import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class TestOrderAdapter {

    @Test
    void testWrite() throws IOException {
        RandomOrder randomOrder = new RandomOrder("buy", "ABC", 50.0, "Trader123", 100, "12345");
        String json = randomOrder.convertToJsonString();
        String expectedJson = "{\"type\":\"buy\",\"stock\":\"ABC\",\"price\":50.0,\"traderId\":\"Trader123\",\"quantity\":100,\"uniqueId\":\"12345\",\"resolved\":false}";
        assertEquals(expectedJson, json);
    }

    @Test
    void testRead() throws IOException {
        String json = "{\"type\":\"buy\",\"stock\":\"XYZ\",\"price\":60.0,\"traderId\":\"Trader789\",\"quantity\":75,\"uniqueId\":\"67890\",\"resolved\":true}";
        JsonReader jsonReader = new JsonReader(new StringReader(json));
        RandomOrderAdapter adapter = new RandomOrderAdapter();

        RandomOrder randomOrder = adapter.read(jsonReader);

        assertEquals("buy", randomOrder.getType());
        assertEquals("XYZ", randomOrder.getStockName());
        assertEquals(60.0, randomOrder.getPrice());
        assertEquals("Trader789", randomOrder.getTraderId());
        assertEquals(75, randomOrder.getQuantity());
        assertEquals("67890", randomOrder.getUniqueId());
        assertFalse(randomOrder.isResolved()); // Assert that "resolved" is false
    }

}
