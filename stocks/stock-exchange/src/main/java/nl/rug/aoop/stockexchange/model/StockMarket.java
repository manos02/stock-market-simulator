package nl.rug.aoop.stockexchange.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.model.StockDataModel;
import nl.rug.aoop.model.StockExchangeDataModel;
import nl.rug.aoop.model.TraderDataModel;
import nl.rug.aoop.stockexchange.orders.RandomOrder;
import nl.rug.aoop.stockexchange.commandhandler.StockMCommandHandler;
import nl.rug.aoop.stockexchange.stocks.Stock;
import nl.rug.aoop.stockexchange.traders.Trader;
import nl.rug.aoop.stockexchange.orders.Transaction;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The StockMarket class represents the core of the stock exchange simulation.
 */

@Slf4j
@Getter
@Setter
public class StockMarket implements StockExchangeDataModel {

    private Map<String, Stock> stocks;
    private List<Trader> traders;
    private CommandHandler commandHandler;
    private Map<String, List<RandomOrder>> buyOrders = new ConcurrentHashMap<>();
    private Map<String, List<RandomOrder>> sellOrders = new ConcurrentHashMap<>();

    /**
     * Constructs a StockMarket with the specified stocks and traders.
     *
     * @param stocks  A map of stocks to initialize the stock market with.
     * @param traders A list of traders to initialize the stock market with.
     */

    public StockMarket(Map<String, Stock> stocks, List<Trader> traders) {
        this.commandHandler = setupCommandHandler();
        this.stocks = stocks;
        this.traders = traders;
    }

    /**
     * Set up the command handler for handling incoming messages.
     *
     * @return The initialized CommandHandler.
     */
    public CommandHandler setupCommandHandler() {
        StockMCommandHandler stockMCommandHandler = new StockMCommandHandler(this);
        this.commandHandler = stockMCommandHandler.createCommandHandler();
        return commandHandler;
    }

    /**
     * Execute an order based on the given message.
     *
     * @param message The message containing the order information.
     */
    public void executeOrder(Message message) {
        if (message != null) {
            Map<String, Object> commandMap = new HashMap<>();
            commandMap.put("header", message.getHeader());
            commandMap.put("body", message.getBody());

            commandHandler.findCommand(commandMap);
        } else {
            throw new NullPointerException("Message is null");
        }
    }

    /**
     * Update the stock price based on the latest transaction.
     *
     * @param stock The stock to change the price of.
     * @param price The price to change to.
     */
    public void updateStockPrice(Stock stock, double price) {
        stock.setInitialPrice(price);
    }

    /**
     * Handle a transaction for a specific order and update trader data accordingly.
     *
     * @param order The order for which a transaction is to be handled.
     */
    public void handleTransaction(RandomOrder order) {
        Trader trader = matchTraderId(order.getTraderId());
        if (trader != null) {
            Transaction transaction = order.returnTransaction();
            trader.getTransactions().add(transaction);
            updateFunds(trader, (int) (order.getPrice() * order.getQuantity()), order.getType());
            updateTradersStocks(trader, order.getQuantity(), order.getStockName(), order.getType());
        } else {
            throw new NullPointerException("Trader is null: " + order.getTraderId());
        }
    }

    /**
     * Update the stock quantity in a trader's portfolio based on an order's execution.
     *
     * @param trader   The trader for whom to update the stock portfolio.
     * @param quantity The quantity of stocks to update.
     * @param stockName The name of the stock to update the portfolio for.
     * @param type The type of order (Buy or Sell).
     */
    public void updateTradersStocks(Trader trader, int quantity, String stockName, String type) {
        String stockSymbol = stocks.get(stockName).getSymbol();
        Map<String, Integer> stockPortfolio = trader.getStockPortfolio();

        int currentQuantity = stockPortfolio.getOrDefault(stockSymbol, 0);

        if (type.equals("Buy")) {
            stockPortfolio.put(stockSymbol, currentQuantity + quantity);
        } else {
            stockPortfolio.put(stockSymbol, currentQuantity - quantity);
        }
    }

    /**
     * Update a trader's funds based on an order's execution.
     *
     * @param trader The trader for whom to update the funds.
     * @param amount The amount to update.
     * @param type   The type of order (Buy or Sell).
     */
    public void updateFunds(Trader trader, int amount, String type) {
        trader.setFunds(type.equals("Sell") ? trader.getFunds() + amount : trader.getFunds() - amount);
    }

    /**
     * Find a stock by its name.
     *
     * @param stockName The name of the stock to find.
     * @return The Stock object with the specified name.
     */
    public Stock findStock(String stockName) {
        if (stocks.containsKey(stockName)) {
            return stocks.get(stockName);
        } else {
            throw new NoSuchElementException("Cannot find this stock: " + stockName);
        }
    }

    /**
     * Find a trader by their ID.
     *
     * @param traderId The ID of the trader to find.
     * @return The Trader object with the specified ID.
     */
    public Trader matchTraderId(String traderId) {
        return traders.stream().filter(trader -> trader.getId().equals(traderId)).findFirst().orElse(null);
    }
    /**
     * Retrieve a Stock by its index.
     *
     * @param index The index of the Stock to retrieve.
     * @return The Stock at the specified index.
     */

    @Override
    public StockDataModel getStockByIndex(int index) {
        return this.stocks.values().stream().skip(index).findAny().orElse(null);
    }

    /**
     * Convert the list of Stock objects into a JSON string.
     *
     * @return A JSON representation of the Stock objects.
     */
    public String convertStocksToString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Stock> stocksArr = new ArrayList<>(stocks.values());
            return mapper.writeValueAsString(stocksArr);
        } catch (JsonProcessingException e) {
            log.error("Could not convert to string", e);
        }
        return null;
    }

    /**
     * Add an order to the specified orders map (buyOrders or sellOrders).
     *
     * @param order  The order to add.
     * @param orders The map of orders (buyOrders or sellOrders).
     */
    public void addOrder(RandomOrder order, Map<String, List<RandomOrder>> orders) {
        if (orders.containsKey(order.getStockName())) {
            orders.get(order.getStockName()).add(order);
        } else {
            List<RandomOrder> newList = new CopyOnWriteArrayList<>();
            newList.add(order);
            orders.put(order.getStockName(), newList);
        }
    }

    /**
     * Get the number of available stocks.
     *
     * @return The number of stocks.
     */
    @Override
    public int getNumberOfStocks() {
        return this.stocks.size();
    }

    /**
     * Get a Trader by their index.
     *
     * @param index The index of the Trader to retrieve.
     * @return The Trader at the specified index.
     */
    @Override
    public TraderDataModel getTraderByIndex(int index) {
        return this.traders.get(index);
    }

    /**
     * Get the number of traders.
     *
     * @return The number of traders.
     */
    @Override
    public int getNumberOfTraders() {
        return this.traders.size();
    }
}

