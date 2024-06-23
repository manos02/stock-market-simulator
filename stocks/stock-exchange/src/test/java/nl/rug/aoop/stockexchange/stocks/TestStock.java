package nl.rug.aoop.stockexchange.stocks;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestStock {
    private Stock stock;

    @Before
    public void setUp() {
        stock = new Stock();
        stock.setName("Example Stock");
        stock.setSymbol("ES");
        stock.setInitialPrice(100.0);
        stock.setSharesOutstanding(1000L);
        }

        @Test
        public void testGetMarketCap() {
            double expectedMarketCap = stock.getSharesOutstanding() * stock.getInitialPrice();
            assertEquals(expectedMarketCap, stock.getMarketCap(), 0.01);
        }

        @Test
        public void testGetPrice() {
            assertEquals(100.0, stock.getPrice(), 0.01);
    }
}
