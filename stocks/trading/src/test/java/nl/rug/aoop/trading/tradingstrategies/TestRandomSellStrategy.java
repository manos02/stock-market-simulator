package nl.rug.aoop.trading.tradingstrategies;

import nl.rug.aoop.stockexchange.stocks.Stock;
import nl.rug.aoop.trading.application.TraderBot;
import nl.rug.aoop.trading.strategies.RandomSellStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


public class TestRandomSellStrategy {
    private RandomSellStrategy randomSellStrategy = null;
    private TraderBot traderBot;

    @BeforeEach
    public void setUp() {
        this.randomSellStrategy = new RandomSellStrategy();
        this.traderBot = Mockito.mock(TraderBot.class);
    }

    @Test
    void testRandomSellStrategyConstructor() {
        assertNotNull(randomSellStrategy);
    }

    @Test
    void testFindName() {
        List<Stock> stocks = new ArrayList<>();
        Stock stock = new Stock();
        Stock stock1 = new Stock();
        stock1.setSymbol("AMD");
        stock1.setName("Advanced Micro Devices, Inc.");
        stock.setSymbol("AAPL");
        stock.setName("Apple Inc.");
        stocks.add(stock);
        stocks.add(stock1);
        when(traderBot.getStocks()).thenReturn(stocks);
        String name = randomSellStrategy.findName("AAPL", traderBot);
        assertEquals("Apple Inc.", name);
    }

    @Test
    void testFindNameNotFound() {
        List<Stock> stocks = new ArrayList<>();
        when(traderBot.getStocks()).thenReturn(stocks);
        assertThrows(IllegalArgumentException.class, () -> randomSellStrategy.findName("AAPL", traderBot));
    }

    @Test
    void testFindStockPrice() {
        List<Stock> stocks = new ArrayList<>();
        Stock stock = new Stock();
        Stock stock1 = new Stock();
        stock1.setName("Advanced Micro Devices, Inc.");
        stock1.setInitialPrice(105.0);
        stock.setInitialPrice(967.91);
        stock.setName("Apple Inc.");
        stocks.add(stock);
        stocks.add(stock1);
        when(traderBot.getStocks()).thenReturn(stocks);
        double result = randomSellStrategy.findStockPrice("Advanced Micro Devices, Inc.", traderBot);
        assertEquals(result, 105);
    }

    @Test
    void testSelectRandomStock() {
        Map<String, Integer> map = new HashMap<>();
        map.put("Apple Inc.", 20);
        map.put("Advanced Micro Devices, Inc.", 10);
        String stockName = randomSellStrategy.selectRandomStock(map);
        assertNotNull(stockName);
    }

    @Test
    void testSelectRandomStockNull() {
        assertThrows(IllegalArgumentException.class, () -> randomSellStrategy.selectRandomStock(null));
    }


}
