package nl.rug.aoop.stockexchange.orders;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a transaction involving the buying or selling of stocks on the stock exchange.
 */
@Setter
@Getter
@JsonRootName("transaction")
@JsonPropertyOrder({ "type", "stock", "quantityShares", "transactionPrice", "uniqueId", "handled" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction {
    /**
     * The type of the order (buy or sell).
     */
    @JsonProperty("type")
    protected String type;

    /**
     * The name of the stock.
     */
    @JsonProperty("stock")
    protected String stock;

    /**
     * The quantity of shares involved in the transaction.
     */
    @JsonProperty("quantityShares")
    protected int quantityShares;

    /**
     * The price at which the transaction was executed.
     */
    @JsonProperty("transactionPrice")
    protected double transactionPrice;

    /**
     * A unique identifier for the order.
     */
    @JsonProperty("uniqueId")
    protected String uniqueId;

    /**
     * Indicates whether the order has been handled.
     */
    @JsonProperty("handled")
    protected boolean handled;

    /**
     * Constructs a new Transaction instance with the specified parameters.
     *
     * @param type             The type of the transaction.
     * @param stock            The name of the stock involved in the transaction.
     * @param quantityShares   The quantity of shares involved in the transaction.
     * @param transactionPrice The price at which the transaction occurred.
     * @param uniqueId         A unique identifier for the transaction.
     * @param handled          A boolean indicating if the transaction has been handled.
     */
    public Transaction(String type, String stock, int quantityShares, double transactionPrice,
                       String uniqueId, boolean handled) {
        this.type = type;
        this.stock = stock;
        this.quantityShares = quantityShares;
        this.transactionPrice = transactionPrice;
        this.uniqueId = uniqueId;
        this.handled = handled;
    }

    /**
     * Default constructor for the Transaction class.
     */
    public Transaction() {
    }

    /**
     * Factory method to create a Transaction instance.
     *
     * @param type             The type of the transaction.
     * @param stock            The name of the stock involved in the transaction.
     * @param quantityShares   The quantity of shares involved in the transaction.
     * @param transactionPrice The price at which the transaction occurred.
     * @param uniqueId         A unique identifier for the transaction.
     * @param handled          A boolean indicating if the transaction has been handled.
     * @return                 A new Transaction instance.
     */
    @JsonCreator
    public static Transaction createTransaction(
            @JsonProperty("type") String type,
            @JsonProperty("stock") String stock,
            @JsonProperty("uniqueId") String uniqueId,
            @JsonProperty("quantityShares") int quantityShares,
            @JsonProperty("transactionPrice") double transactionPrice,
            @JsonProperty("handled") boolean handled
    ) {
        return new Transaction(type, stock, quantityShares, transactionPrice, uniqueId, handled);
    }
}