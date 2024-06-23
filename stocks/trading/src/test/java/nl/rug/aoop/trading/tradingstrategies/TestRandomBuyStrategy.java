package nl.rug.aoop.trading.tradingstrategies;

import nl.rug.aoop.stockexchange.stocks.Stock;
import nl.rug.aoop.trading.application.TraderBot;
import nl.rug.aoop.trading.strategies.RandomBuyStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TestRandomBuyStrategy {

    private RandomBuyStrategy randomBuyStrategy = null;
    private TraderBot traderBot;

    @BeforeEach
    public void setUp() {
        this.randomBuyStrategy = new RandomBuyStrategy();
        this.traderBot = Mockito.mock(TraderBot.class);
    }

    @Test
    void testRandomBuyStrategyConstructor() {
        assertNotNull(randomBuyStrategy);
    }

    @Test
    void testFindStockPrice() {
        List<Stock> stocks = new ArrayList<>();
        Stock stock = new Stock();
        stock.setName("Apple Inc.");
        stock.setInitialPrice(100.0);
        stocks.add(stock);
        String stockName = "Apple Inc.";
        when(traderBot.getStocks()).thenReturn(stocks);
        Double result = randomBuyStrategy.findStockPrice(stockName, traderBot);
        assertEquals(result, 100.0);
    }

    @Test
    void testRandomAmount() {
        double result = randomBuyStrategy.selectRandomAmount(5000.0, 200.0);
        assertNotEquals(result, -1);
    }

    @Test
    void testRandomAmountNoFunds() {
        double result = randomBuyStrategy.selectRandomAmount(100.0, 200.0);
        assertEquals(result, -1);
    }

    @Test
    void testSelectRandomStock() {
        List<Stock> stocks = new ArrayList<>();
        Stock stock = new Stock();
        stock.setName("Apple Inc.");
        stock.setInitialPrice(100.0);
        stocks.add(stock);
        when(traderBot.getStocks()).thenReturn(stocks);
        String stockName = randomBuyStrategy.selectRandomStock(traderBot.getStocks());
        assertNotNull(stockName);
    }

    @Test
    void testSelectRandomStockNull() {
        assertThrows(IllegalArgumentException.class , () -> randomBuyStrategy.selectRandomStock(null));
    }

}
