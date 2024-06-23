
package nl.rug.aoop.trading.strategies;

/**
 * A class responsible for creating a strategy handler and populating it with predefined trading strategies.
 */

public class TraderStrategyHandler {

    /**
     * Creates a new instance of StrategyHandler and populates it with predefined trading strategies: "Buy" and "Sell.".
     *
     * @return A StrategyHandler instance with the predefined trading strategies.
     */
    public StrategyHandler createStrategyHandler() {
        StrategyHandler strategyHandler = new StrategyHandler();
        strategyHandler.placeOrder("Buy", new RandomBuyStrategy());
        strategyHandler.placeOrder("Sell", new RandomSellStrategy());
        return strategyHandler;
    }
}
