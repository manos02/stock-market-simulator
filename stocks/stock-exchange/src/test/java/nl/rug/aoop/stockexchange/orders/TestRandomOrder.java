package nl.rug.aoop.stockexchange.orders;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestRandomOrder {

    @Test
    void testConstructor() {
        // Arrange
        String type = "buy";
        String stockName = "ABC";
        double price = 50.0;
        String traderId = "Trader123";
        int quantity = 100;
        String uniqueId = "12345";

        RandomOrder randomOrder = new RandomOrder(type, stockName, price, traderId, quantity, uniqueId);

        assertEquals(type, randomOrder.getType());
        assertEquals(stockName, randomOrder.getStockName());
        assertEquals(price, randomOrder.getPrice());
        assertEquals(traderId, randomOrder.getTraderId());
        assertEquals(quantity, randomOrder.getQuantity());
        assertEquals(uniqueId, randomOrder.getUniqueId());
        assertFalse(randomOrder.isResolved());
    }

    @Test
    void testConvertToOrderAndConvertToJsonString() {
        RandomOrder expectedOrder = new RandomOrder("buy", "XYZ", 60.0, "Trader789", 75, "67890");
        expectedOrder.setResolved(true);

        String expectedJson = expectedOrder.convertToJsonString();

        RandomOrder actualOrder = RandomOrder.convertToOrder(expectedJson);

        assertEquals(expectedOrder.getType(), actualOrder.getType());
        assertEquals(expectedOrder.getStockName(), actualOrder.getStockName());
        assertEquals(expectedOrder.getPrice(), actualOrder.getPrice());
        assertEquals(expectedOrder.getTraderId(), actualOrder.getTraderId());
        assertEquals(expectedOrder.getQuantity(), actualOrder.getQuantity());
        assertEquals(expectedOrder.getUniqueId(), actualOrder.getUniqueId());
        assertFalse(actualOrder.isResolved());
    }



    @Test
    void testModifyOrder() {
        RandomOrder randomOrder = new RandomOrder("buy", "ABC", 50.0, "Trader123", 100, "12345");
        int newQuantity = 25;
        int expectedQuantity = randomOrder.getQuantity() - newQuantity;

        randomOrder.modifyOrder(newQuantity);

        assertEquals(expectedQuantity, randomOrder.getQuantity());
    }

    @Test
    void testReturnTransaction() {
        RandomOrder randomOrder = new RandomOrder("buy", "XYZ", 60.0, "Trader789", 75, "67890");

        Transaction transaction = randomOrder.returnTransaction();
        assertEquals(randomOrder.getType(), transaction.getType());
        assertEquals(randomOrder.getStockName(), transaction.getStock());
        assertEquals(randomOrder.getQuantity(), transaction.getQuantityShares());
        assertEquals(randomOrder.getPrice(), transaction.getTransactionPrice());
        assertEquals(randomOrder.getUniqueId(), transaction.getUniqueId());
        assertTrue(transaction.isHandled());
    }
}



