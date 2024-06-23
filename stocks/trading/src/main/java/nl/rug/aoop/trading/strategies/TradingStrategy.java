
package nl.rug.aoop.trading.strategies;

import nl.rug.aoop.trading.application.TraderBot;
import java.util.Map;

/**
 * An interface representing a trading strategy for a TraderBot.
 */

public interface TradingStrategy {

    /**
     * Executes a trading strategy for a TraderBot.
     *
     * @param traderBot The TraderBot for which the strategy is executed.
     * @return A map containing the details of the trading strategy's actions.
     */
    Map<String, Object> executeStrategy(TraderBot traderBot);
}
