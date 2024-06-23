package nl.rug.aoop.stockexchange.traders;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.model.TraderDataModel;
import nl.rug.aoop.stockexchange.orders.Transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a trader participating in the stock exchange.
 */
@Getter
@Setter
@Slf4j
public class Trader implements TraderDataModel {
    /**
     * The unique identifier for the trader.
     */
    protected String id;

    /**
     * The name of the trader.
     */
    protected String name;

    /**
     * The available funds that the trader possesses.
     */
    protected double funds;

    /**
     * A map representing the trader's stock portfolio, where keys are stock symbols,
     * and values are quantities of stock owned.
     */
    protected Map<String, Integer> stockPortfolio;

    /**
     * A list of transactions made by the trader.
     */
    protected List<Transaction> transactions = new ArrayList<>();

    /**
     * Default constructor for the Trader class.
     */

    public Trader() {

    }

    /**
     * Converts the Trader object to a JSON-formatted string.
     *
     * @return A JSON-formatted string representing the trader's information.
     */
    public String toStringMessage() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> traderMap = new HashMap<>();

        traderMap.put("id", id);
        traderMap.put("name", name);
        traderMap.put("funds", funds);
        traderMap.put("stockPortfolio", stockPortfolio);
        traderMap.put("transactions", transactions);
        try {
            return mapper.writeValueAsString(traderMap);
        } catch (JsonProcessingException e) {
            log.error("CANNOT CONVERT TO STRING");
            return null;
        }
    }

    /**
     * Parses trader information from a JSON-formatted string and creates a Trader object.
     *
     * @param jsonString The JSON-formatted string containing trader information.
     * @return           A Trader object created from the parsed information.
     */
    public static Trader parseTraderInfo(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonString, Trader.class);
        } catch (IOException e) {
            log.error("could not parse string: " + jsonString);
        }
        return null;
    }

    /**
     * Retrieves a list of stock symbols owned by the trader.
     *
     * @return A list of stock symbols owned by the trader.
     */
    @Override
    public List<String> getOwnedStocks() {
        return new ArrayList<>(stockPortfolio.keySet());
    }

    @Override
    public int getNumberOfOwnedShares(String stockSymbol) {
        return stockPortfolio.get(stockSymbol);
    }

}