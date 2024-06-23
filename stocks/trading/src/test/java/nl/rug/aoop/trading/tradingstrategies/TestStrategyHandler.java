package nl.rug.aoop.trading.tradingstrategies;

import nl.rug.aoop.trading.application.TraderBot;
import nl.rug.aoop.trading.strategies.StrategyHandler;
import nl.rug.aoop.trading.strategies.TradingStrategy;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestStrategyHandler {
    private StrategyHandler strategyHandler;

    @BeforeEach
    public void setUp() {
        strategyHandler = new StrategyHandler();
    }

    @Test
    public void testStrategyHandlerConstructor() {
        assertNotNull(strategyHandler);
    }

    @Test
    public void testPlaceOrder() {
        TradingStrategy strategy = mock(TradingStrategy.class);
        strategyHandler.placeOrder("RandomBuy", strategy);
        assertEquals(strategy, strategyHandler.getOrderMap().get("RandomBuy"));
    }


    @Test
    public void testFindOrder() {
        TradingStrategy strategy = mock(TradingStrategy.class);
        TraderBot traderBot = Mockito.mock(TraderBot.class);
        strategyHandler.placeOrder("RandomBuy", strategy);
        strategyHandler.findOrder("RandomBuy", traderBot);
        verify(strategy).executeStrategy(traderBot);
    }

    @Test
    public void testFindOrderWhenStrategyNotFound() {
        TraderBot traderBot = Mockito.mock(TraderBot.class);
        strategyHandler.findOrder("NonExistentStrategy", traderBot);
        verify(traderBot, never()).getTrader();
    }

}
