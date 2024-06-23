
package nl.rug.aoop.stockexchange.orders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents an order to buy or sell a stock.
 */
@Setter
@Getter
public class RandomOrder implements Order {
    /**
     * The type of the order (buy or sell).
     */
    protected String type;

    /**
     * The name of the stock.
     */
    protected String stockName;

    /**
     * The price at which the order is placed.
     */
    protected double price;

    /**
     * The ID of the trader placing the order.
     */
    protected String traderId;

    /**
     * The quantity of stock to buy or sell.
     */
    protected int quantity;

    /**
     * A unique identifier for the order.
     */
    protected String uniqueId;

    /**
     * Indicates whether the order is resolved.
     */
    protected boolean resolved;

    /**
     * Constructs a new RandomOrder instance with the specified parameters.
     *
     * @param type      The type of the order (buy or sell).
     * @param stockName The name of the stock.
     * @param price     The price at which the order is placed.
     * @param traderId  The ID of the trader placing the order.
     * @param quantity  The quantity of stock to buy or sell.
     * @param uniqueId  A unique identifier for the order.
     */
    public RandomOrder(String type, String stockName, double price, String traderId, int quantity, String uniqueId) {
        this.type = type;
        this.stockName = stockName;
        this.price = price;
        this.traderId = traderId;
        this.quantity = quantity;
        this.uniqueId = uniqueId;
        this.resolved = false;
    }

    /**
     * Creates a Gson instance with a custom adapter for RandomOrder objects.
     *
     * @return A Gson instance for JSON serialization and deserialization.
     */
    public static Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(RandomOrder.class, new RandomOrderAdapter());
        return gsonBuilder.create();
    }

    /**
     * Converts a JSON string to a RandomOrder object.
     *
     * @param jsonOrder A JSON representation of a RandomOrder.
     * @return The RandomOrder object deserialized from the JSON string.
     */
    public static RandomOrder convertToOrder(String jsonOrder) {
        return createGson().fromJson(jsonOrder, RandomOrder.class);
    }

    /**
     * Converts the RandomOrder object to a JSON string.
     *
     * @return A JSON representation of the RandomOrder object.
     */
    @Override
    public String convertToJsonString() {
        return createGson().toJson(this);
    }

    /**
     * Modifies the order by subtracting a specified quantity from the current quantity.
     *
     * @param newQuantity The quantity to subtract from the order.
     */
    @Override
    public void modifyOrder(int newQuantity) {
        quantity -= newQuantity;
    }

    /**
     * Returns a transaction representing the RandomOrder.
     *
     * @return A Transaction object representing the RandomOrder.
     */
    @Override
    public Transaction returnTransaction() {
        return new Transaction(getType(), stockName, getQuantity(), getPrice(), getUniqueId(), true);
    }
}
