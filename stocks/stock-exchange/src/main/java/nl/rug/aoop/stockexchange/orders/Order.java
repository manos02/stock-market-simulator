package nl.rug.aoop.stockexchange.orders;

/**
 * The `Order` interface represents an order in the stock exchange system.
 * Orders can be for buying or selling stocks.
 */
public interface Order {
    /**
     * Converts the order to a JSON string for serialization.
     *
     * @return The order as a JSON string.
     */
    String convertToJsonString();

    /**
     * Modifies the order by reducing its quantity.
     *
     * @param quantity The quantity to be modified.
     */
    void modifyOrder(int quantity);

    /**
     * Returns a transaction associated with the order.
     *
     * @return The transaction resulting from this order.
     */
    Transaction returnTransaction();
}
