package nl.rug.aoop.stockexchange.orders;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTransaction {

    @Test
    void testTransactionConstructor() {
        String type = "buy";
        String stock = "ABC";
        int quantityShares = 100;
        double transactionPrice = 50.0;
        String uniqueId = "12345";
        boolean handled = true;

        Transaction transaction = new Transaction(type, stock, quantityShares, transactionPrice, uniqueId, handled);

        assertEquals(type, transaction.getType());
        assertEquals(stock, transaction.getStock());
        assertEquals(quantityShares, transaction.getQuantityShares());
        assertEquals(transactionPrice, transaction.getTransactionPrice());
        assertEquals(uniqueId, transaction.getUniqueId());
        assertEquals(handled, transaction.isHandled());
    }

    @Test
    void testJsonSerializationAndDeserialization() throws Exception {
        String type = "buy";
        String stock = "XYZ";
        int quantityShares = 75;
        double transactionPrice = 60.0;
        String uniqueId = "67890";
        boolean handled = false;

        Transaction originalTransaction = new Transaction(type, stock, quantityShares, transactionPrice, uniqueId, handled);

        ObjectMapper objectMapper = new ObjectMapper();

        String json = objectMapper.writeValueAsString(originalTransaction);
        Transaction deserializedTransaction = objectMapper.readValue(json, Transaction.class);

        assertEquals(type, deserializedTransaction.getType());
        assertEquals(stock, deserializedTransaction.getStock());
        assertEquals(quantityShares, deserializedTransaction.getQuantityShares());
        assertEquals(transactionPrice, deserializedTransaction.getTransactionPrice());
        assertEquals(uniqueId, deserializedTransaction.getUniqueId());
        assertEquals(handled, deserializedTransaction.isHandled());
    }
}
