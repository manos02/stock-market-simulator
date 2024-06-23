
package nl.rug.aoop.trading.strategies;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.trading.application.TraderBot;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that handles trading strategies for a trader bot.
 * It allows for the storage and execution of various trading strategies.
 */

@Slf4j
@Getter
public class StrategyHandler {
    private final Map<String, Object> orderMap;
    private boolean found = false;

    /**
     * Constructor for StrategyHandler. Initializes the order map to store trading strategies.
     */
    public StrategyHandler() {
        this.orderMap = new HashMap<>();
    }

    /**
     * Places a trading strategy in the order map.
     *
     * @param strategyName The name of the trading strategy.
     * @param strategy     The trading strategy to be stored.
     * @throws NullPointerException if the provided strategy is null.
     */
    public void placeOrder(String strategyName, TradingStrategy strategy) {
        if (strategy != null) {
            orderMap.put(strategyName, strategy);
        } else {
            throw new NullPointerException("strategy is null");
        }
    }

    /**
     * Finds and executes a trading strategy based on its name.
     *
     * @param strategy   The name of the trading strategy to be executed.
     * @param traderBot  The trader bot instance on which the strategy will be executed.
     */
    public void findOrder(String strategy, TraderBot traderBot) {
        if (orderMap.containsKey(strategy)) {
            found = true;
            TradingStrategy tradingStrategy = (TradingStrategy) orderMap.get(strategy);
            tradingStrategy.executeStrategy(traderBot);
        } else {
            log.error("could not find this strategy: " + strategy);
        }
    }
}
