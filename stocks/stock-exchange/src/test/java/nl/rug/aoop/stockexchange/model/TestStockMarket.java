package nl.rug.aoop.stockexchange.model;

import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.stockexchange.orders.RandomOrder;
import nl.rug.aoop.stockexchange.stocks.Stock;
import nl.rug.aoop.stockexchange.traders.Trader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TestStockMarket {

    private StockMarket stockMarket = null;
    private Trader trader1;
    private Trader trader2;


    @BeforeEach
    void setUp() {
        stockMarket = new StockMarket(getMockStocks(), getMockTraders());
    }

    @Test
    void testStockMarketConstructor() {
        assertNotNull(stockMarket);
    }

    @Test
    void testStocksNotNull() {
        assertNotNull(stockMarket.getStocks());
    }

    @Test
    void testTradersNotNull() {
        assertNotNull(stockMarket.getTraders());
    }

    @Test
    void testExecuteOrder() {
        CommandHandler commandHandler = Mockito.mock(CommandHandler.class);
        stockMarket.setCommandHandler(commandHandler);
        Message message = new Message("Buy", "RandomOrder");
        Map<String, Object> commandMap = new HashMap<>();
        commandMap.put("header", "Buy");
        commandMap.put("body", "RandomOrder");
        stockMarket.executeOrder(message);
        Mockito.verify(stockMarket.getCommandHandler()).findCommand(commandMap);
    }

    @Test
    void testExecuteOrderNullMessage() {
        assertThrows(NullPointerException.class, () -> stockMarket.executeOrder(null));
    }

    @Test
    void testHandleTransaction() {
        RandomOrder order = new RandomOrder("Buy", "Advanced Micro Devices, Inc.", 120.0, "1", 5, "1AMDBuy");

        stockMarket.handleTransaction(order);
        assertEquals(1, trader1.getTransactions().size());
    }

    @Test
    void testHandleTransactionFunds() {
        RandomOrder order = new RandomOrder("Buy", "Advanced Micro Devices, Inc.", 120.0, "1", 5, "1AMDBuy");

        stockMarket.handleTransaction(order);
        assertEquals(9400, trader1.getFunds());
    }


    @Test
    void testHandleTransactionStockPortfolioOwnedStock() {
        RandomOrder order = new RandomOrder("Buy", "Advanced Micro Devices, Inc.", 120.0, "1", 5, "1AMDBuy");
        stockMarket.handleTransaction(order);
        assertEquals(25, trader1.getStockPortfolio().get("AMD"));
    }

    @Test
    void testHandleTransactionStockPortfolioNotOwnedStock() {
        RandomOrder order = new RandomOrder("Buy", "Advanced Micro Devices, Inc.", 120.0, "2", 5, "2AMDBuy");
        stockMarket.handleTransaction(order);
        assertEquals(5, trader2.getStockPortfolio().get("AMD"));
    }

    @Test
    void testHandleTransactionNullTrader() {
        RandomOrder order = new RandomOrder("Buy", "Advanced Micro Devices, Inc.", 120.0, "-1", 5, "2AMDBuy");
        assertThrows(NullPointerException.class, () -> stockMarket.handleTransaction(order));
    }

    @Test
    void testHandleTransactionSellFunds() {
        RandomOrder order = new RandomOrder("Sell", "Advanced Micro Devices, Inc.", 100.0, "1", 3, "1AMDSell");
        stockMarket.handleTransaction(order);
        assertEquals(10300, trader1.getFunds());
    }

    @Test
    void testHandleTransactionSellTS() {
        RandomOrder order = new RandomOrder("Sell", "Advanced Micro Devices, Inc.", 100.0, "1", 3, "1AMDSell");
        stockMarket.handleTransaction(order);
        assertEquals(1, trader1.getTransactions().size());
    }

    @Test
    void testHandleTransactionSPSell() {
        RandomOrder order = new RandomOrder("Sell", "Advanced Micro Devices, Inc.", 120.0, "1", 3, "1AMDSell");
        stockMarket.handleTransaction(order);
        assertEquals(17, trader1.getStockPortfolio().get("AMD"));
    }



    private Map<String, Stock> getMockStocks() {
        Map<String, Stock> stocks = new HashMap<>();
        Stock stock = new Stock();
        Stock stock1 = new Stock();

        stock.setSymbol("NVDA");
        stock.setName("NVIDIA Corporation");
        stock.setInitialPrice(222.42);
        stock.setSharesOutstanding(10000);

        stock1.setSymbol("AMD");
        stock1.setName("Advanced Micro Devices, Inc.");
        stock1.setInitialPrice(106.22);
        stock1.setSharesOutstanding(20000);

        stocks.put("NVIDIA Corporation", stock);
        stocks.put("Advanced Micro Devices, Inc.", stock1);
        return stocks;
    }


    private List<Trader> getMockTraders() {
        List<Trader> traders = new ArrayList<>();
        Map<String, Integer> sp1 = new HashMap<>();
        Map<String, Integer> sp2 = new HashMap<>();
        sp1.put("AMD", 20);
        sp1.put("AAPL", 10);
        sp2.put("NVDA", 90);
        sp2.put("TSLA", 100);
        trader1 = new Trader();
        trader1.setId("1");
        trader1.setName("John Doe");
        trader1.setFunds(10000);
        trader1.setStockPortfolio(sp1);
        trader2 = new Trader();
        trader2.setId("2");
        trader2.setName("Jane Smith");
        trader2.setFunds(15000);
        trader2.setStockPortfolio(sp2);
        traders.add(trader1);
        traders.add(trader2);

        return traders;
    }


}
