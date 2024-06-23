package nl.rug.aoop.stockexchange.traders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class TestTrader {

    Trader trader = null;
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        trader = new Trader();
        trader.setId("123");
        trader.setName("John");
        trader.setFunds(1000.0);
        trader.setStockPortfolio(new HashMap<>());
        objectMapper = new ObjectMapper();
    }

    @Test
    void testTraderConstructor() {
        assertNotNull(trader);
    }

    @Test
    public void testToStringMessage() throws JsonProcessingException {
        String jsonString = trader.toStringMessage();
        assertNotNull(jsonString);

        Trader deserializedTrader = objectMapper.readValue(jsonString, Trader.class);
        assertEquals(trader.getId(), deserializedTrader.getId());
        assertEquals(trader.getName(), deserializedTrader.getName());
        assertEquals(trader.getFunds(), deserializedTrader.getFunds(), 0.001);
        assertEquals(trader.getStockPortfolio(), deserializedTrader.getStockPortfolio());
        assertEquals(trader.getTransactions(), deserializedTrader.getTransactions());
    }

    @Test
    public void testParseTraderInfo() {
        String jsonString = "{\"id\":\"123\",\"name\":\"John\",\"funds\":1000.0,\"stockPortfolio\":{},\"transactions\":[]}";
        Trader parsedTrader = Trader.parseTraderInfo(jsonString);
        assertNotNull(parsedTrader);

        assertEquals(trader.getId(), parsedTrader.getId());
        assertEquals(trader.getName(), parsedTrader.getName());
        assertEquals(trader.getFunds(), parsedTrader.getFunds(), 0.001);
        assertEquals(trader.getStockPortfolio(), parsedTrader.getStockPortfolio());
        assertEquals(trader.getTransactions(), parsedTrader.getTransactions());
    }

    @Test
    public void testParseTraderInfoWithInvalidJson() {
        String invalidJson = "invalid json";
        Trader parsedTrader = Trader.parseTraderInfo(invalidJson);
        assertNull(parsedTrader);
    }


}
