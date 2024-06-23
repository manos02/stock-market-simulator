package nl.rug.aoop.trading.strategies;

import nl.rug.aoop.stockexchange.stocks.Stock;
import nl.rug.aoop.trading.application.TraderBot;

import java.util.*;

/**
 * Represents a trading strategy for randomly buying stocks.
 */
public class RandomBuyStrategy implements TradingStrategy {
    private final Random random = new Random();
    private static final double MAX_PRICE_FACTOR = 1.03;

    /**
     * Executes the random buy strategy to select a stock to buy, a price, and a quantity.
     *
     * @param traderBot The trader bot applying the strategy.
     * @return A map containing the selected stock, option (Buy), price, and quantity.
     */
    @Override
    public Map<String, Object> executeStrategy(TraderBot traderBot) {
        String selectedOption = "Buy";
        String selectedStock = selectRandomStock(traderBot.getStocks());
        double priceSelectedStock = findStockPrice(selectedStock, traderBot);
        double selectedPrice = selectRandomPrice(priceSelectedStock);
        int quantity = selectRandomAmount(traderBot.getTrader().getFunds(), selectedPrice);

        Map<String, Object> finalValues = new HashMap<>();
        finalValues.put("stock", selectedStock);
        finalValues.put("option", selectedOption);
        finalValues.put("price", selectedPrice);
        finalValues.put("quantity", quantity);
        return finalValues;
    }

    /**
     * Finds the price of a selected stock based on its name.
     *
     * @param selectedStock The name of the selected stock.
     * @param traderBot     The trader bot that contains the list of stocks.
     * @return The price of the selected stock.
     */
    public double findStockPrice(String selectedStock, TraderBot traderBot) {
        if (traderBot == null) {
            throw new IllegalArgumentException("TraderBot is null.");
        }
        for (Stock stock : traderBot.getStocks()) {
            if (stock.getName().equals(selectedStock)) {
                return stock.getInitialPrice();
            }
        }
        throw new IllegalArgumentException("Stock not found: " + selectedStock);
    }

    /**
     * Selects a random amount of stock to buy based on available funds and the selected price.
     *
     * @param funds         The available funds for trading.
     * @param selectedPrice The selected price of the stock.
     * @return The selected quantity of stock to buy, or -1 if no stock can be bought.
     */
    public int selectRandomAmount(double funds, double selectedPrice) {
        if (funds < selectedPrice) {
            return -1;
        }
        int max = (int) ((funds / 2) / selectedPrice);
        if (max <= 1) {
            return -1;
        }
        int randomAmount = random.nextInt(1, max);
        if (funds < randomAmount * selectedPrice) {
            return -1;
        } else {
            return randomAmount;
        }
    }

    /**
     * Selects a random price for the stock within a certain range.
     *
     * @param price The initial price of the stock.
     * @return The selected random price.
     */
    public double selectRandomPrice(double price) {
        double max = MAX_PRICE_FACTOR * price;
        return price + (max - price) * random.nextDouble();
    }

    /**
     * Selects a random stock from the list of available stocks.
     *
     * @param stocks The list of available stocks.
     * @return The name of the randomly selected stock.
     */
    public String selectRandomStock(List<Stock> stocks) {
        if (stocks == null) {
            throw new IllegalArgumentException("Stocks list is null.");
        }
        int randomOption = random.nextInt(stocks.size());
        Stock stock = stocks.get(randomOption);
        return stock.getName();
    }
}