package nl.rug.aoop.trading.application;

import lombok.Getter;
import nl.rug.aoop.trading.strategies.StrategyHandler;
import nl.rug.aoop.trading.strategies.TraderStrategyHandler;

import java.util.List;

/**
 * Manages a list of trader bots and coordinates the sending of trading orders.
 */
@Getter
public class TraderManager {

    private final List<TraderBot> traderBots;
    private TraderStrategyHandler traderStrategyHandler;
    private StrategyHandler strategyHandler;

    /**
     * Constructs a TraderManager instance with a list of trader bots.
     *
     * @param traders The list of trader bots to manage.
     */
    public TraderManager(List<TraderBot> traders){
        this.traderBots = traders;
        traderStrategyHandler = new TraderStrategyHandler();
        strategyHandler = traderStrategyHandler.createStrategyHandler();
    }

    /**
     * Adds a trader bot to the list of managed trader bots.
     *
     * @param traderBot The trader bot to add.
     */
    public void addTraderBot(TraderBot traderBot) {
        traderBots.add(traderBot);
    }

    /**
     * Removes a trader bot from the list of managed trader bots.
     *
     * @param traderBot The trader bot to remove.
     */
    public void removeTraderBot(TraderBot traderBot) {
        traderBots.remove(traderBot);
    }

    /**
     * Disconnects all trader bots by terminating their clients.
     */
    public void disconnectTraderBots() {
        traderBots.forEach(trader -> trader.getClient().terminate());
    }

    /**
     * Sends trading orders for each trader bot.
     *
     * @throws InterruptedException If an error occurs during order execution.
     */
    public void sendOrders() throws InterruptedException {
        for (TraderBot traderBot : traderBots) {
            traderBot.sendOrder(strategyHandler);
        }
    }
}