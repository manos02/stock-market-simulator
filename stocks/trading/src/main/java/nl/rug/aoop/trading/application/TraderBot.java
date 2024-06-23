package nl.rug.aoop.trading.application;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.stockexchange.orders.RandomOrder;
import nl.rug.aoop.stockexchange.stocks.Stock;
import nl.rug.aoop.stockexchange.traders.Trader;
import nl.rug.aoop.trading.strategies.StrategyHandler;
import nl.rug.aoop.trading.strategies.TradingStrategy;
import nl.rug.aoop.stockexchange.orders.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.lang.Thread.sleep;

/**
 * Represents a trading bot associated with a trader.
 */
@Setter
@Getter
@Slf4j
public class TraderBot {
    private Trader trader;
    private Client client;
    private List<Stock> stocks;
    private static final Random RANDOM = new Random();
    private static final int MIN_DELAY = 1000;
    private static final int MAX_DELAY = 4000;

    /**
     * Constructs a TraderBot instance associated with a trader.
     *
     * @param trader The trader to which the trading bot is associated.
     */
    public TraderBot(Trader trader) {
        this.trader = trader;
    }

    /**
     * Sends a trading order based on a selected trading strategy.
     *
     * @param strategyHandler The handler for trading strategies.
     * @throws InterruptedException If an error occurs during order execution.
     */
    public void sendOrder(StrategyHandler strategyHandler) throws InterruptedException {
        Random random = new Random();
        int randomDelay = random.nextInt(MIN_DELAY, MAX_DELAY);
        sleep(randomDelay);

        TradingStrategy tradingStrategy = getRandomTradingStrategy(strategyHandler);
        Map<String, Object> strategyValues = tradingStrategy.executeStrategy(this);

        String stock = (String) strategyValues.get("stock");
        double price = (double) strategyValues.get("price");
        String option = (String) strategyValues.get("option");
        int quantity = (int) strategyValues.get("quantity");

        if (quantity != -1 && price != -1) {
            String uniqueId = trader.getId() + stock + option + price;
            RandomOrder newOrder = new RandomOrder(option, stock, price, trader.getId(), quantity, uniqueId);
            Transaction transaction = new Transaction(option, stock, quantity, (int) price, uniqueId, false);
            addTransaction(transaction);
            sendOrderMessage(newOrder);

        }
    }

    /**
     * Checks for handled transactions and updates the trader's transactions accordingly.
     *
     * @param transactions The list of transactions to check.
     */
    public void checkTransactions(List<Transaction> transactions) {
        if (!getTrader().getTransactions().isEmpty() && !transactions.isEmpty()) {
            Transaction transaction = transactions.get(transactions.size() - 1);
            if (transaction.isHandled()) {
                replaceTransaction(transaction);
            }
        }
    }

    /**
     * Replaces an existing transaction with a new one in the trader's transactions list.
     *
     * @param foundTransaction The new transaction to replace the existing one.
     */
    public void replaceTransaction(Transaction foundTransaction) {
        List<Transaction> transactions = getTrader().getTransactions();
        for (int i = 0; i < transactions.size(); i++) {
            Transaction transaction = transactions.get(i);
            if (transaction.getUniqueId().equals(foundTransaction.getUniqueId())) {
                transactions.set(i, foundTransaction);
            }
        }
    }

    /**
     * Retrieves a random trading strategy from the strategy handler.
     *
     * @param strategyHandler The handler for trading strategies.
     * @return A random trading strategy.
     */
    public TradingStrategy getRandomTradingStrategy(StrategyHandler strategyHandler) {
        List<Object> strategies = List.of(strategyHandler.getOrderMap().values().toArray());
        return (TradingStrategy) strategies.get(RANDOM.nextInt(strategies.size()));
    }

    /**
     * Adds a transaction to the trader's list of transactions.
     *
     * @param transaction The transaction to add.
     */
    public void addTransaction(Transaction transaction) {
        trader.getTransactions().add(transaction);
    }

    /**
     * Sends a trading order message using a client.
     *
     * @param order The trading order to send.
     */
    public void sendOrderMessage(RandomOrder order) {
        Message newMessage = new Message(order.getType(), order.convertToJsonString());
        String stringMessage = newMessage.convertToJsonString();
        client.send(stringMessage);
        log.info(stringMessage);
    }
}