
package nl.rug.aoop.trading.strategies;

import nl.rug.aoop.stockexchange.stocks.Stock;
import nl.rug.aoop.trading.application.TraderBot;
import nl.rug.aoop.stockexchange.orders.Transaction;

import java.util.*;

/**
 * This class implements a trading strategy that randomly selects a stock from the trader's
 * portfolio and generates a sell order for it. The quantity to sell,
 * stock price, and the selected stock are determined randomly.
 */

public class RandomSellStrategy implements TradingStrategy {
    private final Random random = new Random();
    private static final double MIN_PRICE_FACTOR = 0.97;

    /**
     * Executes the random sell strategy for the trader.
     *
     * @param traderBot The trader bot that owns the strategy.
     * @return A map containing the details of the sell order, including stock name, option (sell), price,
     * and quantity to sell.
     */
    @Override
    public Map<String, Object> executeStrategy(TraderBot traderBot) {
        Map<String, Object> finalValues = new HashMap<>();

        if (traderBot.getTrader().getStockPortfolio().isEmpty()) { // handle the case when stockPortfolio is empty
            finalValues.put("quantity", -1);
            return finalValues;
        }
        String selectedOption = "Sell";
        String selectedStockSymbol = selectRandomStock(traderBot.getTrader().getStockPortfolio());
        String selectedStockName = findName(selectedStockSymbol, traderBot);
        double priceSelectedStock = findStockPrice(selectedStockName, traderBot);

        double selectedPrice = selectRandomPrice(priceSelectedStock);
        int quantity = selectRandomAmount(selectedStockName, selectedStockSymbol, traderBot);

        finalValues.put("stock", selectedStockName);
        finalValues.put("option", selectedOption);
        finalValues.put("price", selectedPrice);
        finalValues.put("quantity", quantity);
        return finalValues;
    }

    /**
     * Finds the name of the stock based on its symbol.
     *
     * @param stockSymbol The symbol of the stock to find.
     * @param traderBot   The trader bot instance.
     * @return The name of the stock with the specified symbol.
     * @throws IllegalArgumentException if the stock with the specified symbol is not found.
     */
    public String findName(String stockSymbol, TraderBot traderBot) {
        for (Stock stock : traderBot.getStocks()) {
            if (stock.getSymbol().equals(stockSymbol)) {
                return stock.getName();
            }
        }
        throw new IllegalArgumentException("Stock not found: " + stockSymbol);
    }

    /**
     * Finds the initial price of a stock based on its name.
     *
     * @param selectedStock The name of the stock to find the price for.
     * @param traderBot     The trader bot instance.
     * @return The initial price of the stock with the specified name.
     * @throws IllegalArgumentException if the stock with the specified name is not found.
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
     * Selects a random quantity to sell for a specific stock, taking into account the current portfolio and
     * previous transactions.
     *
     * @param stockName    The name of the stock to select a quantity for.
     * @param stockSymbol  The symbol of the stock.
     * @param traderBot    The trader bot instance.
     * @return The randomly selected quantity to sell for the specified stock.
     */
    public int selectRandomAmount(String stockName, String stockSymbol, TraderBot traderBot) {
        int amount = traderBot.getTrader().getStockPortfolio().get(stockSymbol);
        List<Transaction> transactions = traderBot.getTrader().getTransactions();
        for (Transaction transaction : transactions) {
            if (!transaction.isHandled() && transaction.getType().equals("Sell") &&
                    transaction.getStock().equals(stockName)) {
                amount -= transaction.getQuantityShares();
            }
        }
        if (amount == 1) {
            return -1;
        }
        return random.nextInt(1, amount);
    }

    /**
     * Selects a random price for the stock to sell, based on a minimum price factor and the stock's initial price.
     *
     * @param price The initial price of the stock.
     * @return The randomly selected selling price.
     */
    public double selectRandomPrice(double price) {
        double min = MIN_PRICE_FACTOR * price;
        return min + (price - min) * random.nextDouble();
    }

    /**
     * Selects a random stock to sell from the trader's portfolio.
     *
     * @param stockPortfolio The trader's stock portfolio.
     * @return The randomly selected stock symbol to sell.
     * @throws IllegalArgumentException if the stock portfolio is null or empty.
     */
    public String selectRandomStock(Map<String, Integer> stockPortfolio) {
        if (stockPortfolio == null || stockPortfolio.isEmpty()) {
            throw new IllegalArgumentException("Stock portfolio is null or empty.");
        }
        List<String> keys = new ArrayList<>(stockPortfolio.keySet());
        int randomOption = random.nextInt(keys.size());
        return keys.get(randomOption);
    }
}
