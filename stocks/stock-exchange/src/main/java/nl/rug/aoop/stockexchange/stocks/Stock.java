package nl.rug.aoop.stockexchange.stocks;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import nl.rug.aoop.model.StockDataModel;

/**
 * Represents a stock on the stock exchange.
 */
@Setter
@Getter
public class Stock implements StockDataModel {
    /**
     * The name of the stock.
     */
    protected String name;

    /**
     * The symbol representing the stock.
     */
    protected String symbol;

    /**
     * The initial price of the stock.
     */
    protected double initialPrice;

    /**
     * The total number of shares outstanding for the stock.
     */
    protected long sharesOutstanding;

    /**
     * The market capitalization of the stock. (Note: This field is ignored during JSON serialization.)
     */
    @JsonIgnore
    protected double marketCap;

    /**
     * Default constructor for the Stock class.
     */
    public Stock() {
    }

    /**
     * Retrieves the market capitalization of the stock.
     *
     * @return The market capitalization of the stock, calculated as the product of
     * shares outstanding and initial price.
     */
    @JsonIgnore
    @Override
    public double getMarketCap() {
        return sharesOutstanding * initialPrice;
    }

    /**
     * Retrieves the current price of the stock, which is the same as the initial price.
     *
     * @return The initial price of the stock.
     */
    @JsonIgnore
    @Override
    public double getPrice() {
        return initialPrice;
    }
}