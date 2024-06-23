package nl.rug.aoop.stockexchange.integrationTesting;

import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.stockexchange.commandhandler.SellOrderCommand;
import nl.rug.aoop.stockexchange.model.StockMarket;
import nl.rug.aoop.stockexchange.orders.RandomOrder;
import nl.rug.aoop.stockexchange.stocks.Stock;
import nl.rug.aoop.stockexchange.traders.Trader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestSellOrderIntegrationTesting {

    private StockMarket stockMarket = null;
    private SellOrderCommand sellOrderCommand = null;

    @BeforeEach
    void setUp() {
        stockMarket = new StockMarket(getMockStocks(), getMockTraders());
        stockMarket.setBuyOrders(initBuyOrders());
        stockMarket.setSellOrders(initSellOrders());
        sellOrderCommand = new SellOrderCommand(stockMarket);
    }

    @Test
    void testSellOrderConstructor() {
        assertNotNull(sellOrderCommand);
    }

    @Test
    void testSellOrderMatch() {
        RandomOrder order = new RandomOrder("Sell", "NVIDIA Corporation", 102.0, "1", 5, "1NVDABuy");
        Message message = new Message("Sell", order.convertToJsonString());
        Map<String, Object> map = new HashMap<>();
        map.put("header", message.getHeader());
        map.put("body", message.getBody());
        sellOrderCommand.executeCommand(map);
        assertNotNull(sellOrderCommand.getSellOrder());
    }

    @Test
    void testSellOrderResolved() {
        RandomOrder order = new RandomOrder("Sell", "NVIDIA Corporation", 102.0, "1", 2, "1NVDABuy");
        Message message = new Message("Sell", order.convertToJsonString());
        Map<String, Object> map = new HashMap<>();
        map.put("header", message.getHeader());
        map.put("body", message.getBody());
        sellOrderCommand.executeCommand(map);
        assertTrue(sellOrderCommand.getSellOrder().isResolved());
    }

    @Test
    void testSellOrderResolvedQuantity() {
        RandomOrder order = new RandomOrder("Sell", "NVIDIA Corporation", 102.0, "1", 1, "1NVDABuy");
        Message message = new Message("Sell", order.convertToJsonString());
        Map<String, Object> map = new HashMap<>();
        map.put("header", message.getHeader());
        map.put("body", message.getBody());
        sellOrderCommand.executeCommand(map);
        assertEquals(1, sellOrderCommand.getSellOrder().getQuantity());
    }

    @Test
    void testSellOrderResolvedSameQuantity() {
        RandomOrder order = new RandomOrder("Sell", "NVIDIA Corporation", 102.0, "1", 3, "1NVDABuy");
        Message message = new Message("Sell", order.convertToJsonString());
        Map<String, Object> map = new HashMap<>();
        map.put("header", message.getHeader());
        map.put("body", message.getBody());
        sellOrderCommand.executeCommand(map);
        assertEquals(3, sellOrderCommand.getSellOrder().getQuantity());
    }

    @Test
    void testSellOrderListOrder() {
        RandomOrder resolvedOrder = new RandomOrder("Sell", "Advanced Micro Devices, Inc.", 100.0, "1", 12, "1AMDBuy");
        resolvedOrder.setResolved(true);
        stockMarket.getSellOrders().get("Advanced Micro Devices, Inc.").add(0, resolvedOrder);
        RandomOrder order = new RandomOrder("Sell", "Advanced Micro Devices, Inc.", 102.0, "1", 3, "2AMDBuy");
        Message message = new Message("Sell", order.convertToJsonString());
        Map<String, Object> map = new HashMap<>();
        map.put("header", message.getHeader());
        map.put("body", message.getBody());
        sellOrderCommand.executeCommand(map);
        assertEquals("2AMDBuy", stockMarket.getSellOrders().get("Advanced Micro Devices, Inc.").get(0).getUniqueId());
    }

    @Test
    void testSellOrderListOrderPrice() {
        RandomOrder resolvedOrder = new RandomOrder("Sell", "Advanced Micro Devices, Inc.", 100.0, "1", 12, "1AMDBuy");
        resolvedOrder.setResolved(true);
        stockMarket.getSellOrders().get("Advanced Micro Devices, Inc.").add(0, resolvedOrder);
        RandomOrder order = new RandomOrder("Sell", "Advanced Micro Devices, Inc.", 102.0, "1", 3, "2AMDBuy");
        Message message = new Message("Sell", order.convertToJsonString());
        Map<String, Object> map = new HashMap<>();
        map.put("header", message.getHeader());
        map.put("body", message.getBody());
        sellOrderCommand.executeCommand(map);
        assertEquals("2AMDBuy", stockMarket.getSellOrders().get("Advanced Micro Devices, Inc.").get(0).getUniqueId());
    }

    @Test
    void testSellOrderPartialResolved() {
        RandomOrder order = new RandomOrder("Sell", "NVIDIA Corporation", 102.0, "1", 20, "1NVDABuy");
        Message message = new Message("Sell", order.convertToJsonString());
        Map<String, Object> map = new HashMap<>();
        map.put("header", message.getHeader());
        map.put("body", message.getBody());
        sellOrderCommand.executeCommand(map);
        assertFalse(sellOrderCommand.getSellOrder().isResolved());
    }

    @Test
    void testSellOrderPartialResolvedQuantity() {
        RandomOrder order = new RandomOrder("Sell", "NVIDIA Corporation", 102.0, "1", 20, "1NVDABuy");
        Message message = new Message("Sell", order.convertToJsonString());
        Map<String, Object> map = new HashMap<>();
        map.put("header", message.getHeader());
        map.put("body", message.getBody());
        sellOrderCommand.executeCommand(map);
        assertEquals(17, sellOrderCommand.getSellOrder().getQuantity());
    }

    @Test
    void testSellOrderNotResolved() {
        RandomOrder order = new RandomOrder("Sell", "Adobe Systems Inc.", 102.0, "1", 5, "1ADBESell");
        Message message = new Message("Sell", order.convertToJsonString());
        Map<String, Object> map = new HashMap<>();
        map.put("header", message.getHeader());
        map.put("body", message.getBody());
        sellOrderCommand.executeCommand(map);
        assertFalse(sellOrderCommand.getSellOrder().isResolved());
    }


    public Map<String, Stock> getMockStocks() {
        Map<String, Stock> stocks = new HashMap<>();
        Stock stock = new Stock();
        Stock stock1 = new Stock();

        stock.setSymbol("NVDA");
        stock.setName("NVIDIA Corporation");
        stock.setInitialPrice(105.0);
        stock.setSharesOutstanding(10000);

        stock1.setSymbol("AMD");
        stock1.setName("Advanced Micro Devices, Inc.");
        stock1.setInitialPrice(106.22);
        stock1.setSharesOutstanding(20000);

        stocks.put("NVIDIA Corporation", stock);
        stocks.put("Advanced Micro Devices, Inc.", stock1);
        return stocks;
    }


    public List<Trader> getMockTraders() {
        List<Trader> traders = new ArrayList<>();
        Map<String, Integer> sp1 = new HashMap<>();
        Map<String, Integer> sp2 = new HashMap<>();
        Map<String, Integer> sp3 = new HashMap<>();
        Map<String, Integer> sp4 = new HashMap<>();
        sp1.put("AMD", 20);
        sp1.put("AAPL", 10);
        sp2.put("NVDA", 90);
        sp2.put("TSLA", 100);
        sp3.put("NVDA", 100);
        sp3.put("AMD", 90);
        sp4.put("AMD", 100);
        sp4.put("NVDA", 100);

        Trader trader1 = initTrader("1", "John Doe", 10000, sp1);
        Trader trader2 = initTrader("2", "Jane Smith", 15000, sp2);
        Trader trader3 = initTrader("3", "Bob Johnson", 80000, sp3);
        Trader trader4 = initTrader("4", "Alice Brown", 12000, sp4);

        traders.add(trader1);
        traders.add(trader2);
        traders.add(trader3);
        traders.add(trader4);

        return traders;
    }

    public Map<String, List<RandomOrder>> initBuyOrders() {
        Map<String, List<RandomOrder>> buyOrders = new HashMap<>();
        List<RandomOrder> nvdaOrders = new ArrayList<>();
        List<RandomOrder> amdOrders = new ArrayList<>();
        nvdaOrders.add(new RandomOrder("Buy", "NVIDIA Corporation", 112.0, "2", 3, "2NVDABuy"));
        nvdaOrders.add(new RandomOrder("Buy", "NVIDIA Corporation", 110.0, "1", 5, "1NVDABuy"));
        buyOrders.put("NVIDIA Corporation", nvdaOrders);
        buyOrders.put("Advanced Micro Devices, Inc.", amdOrders);
        return buyOrders;
    }

    public Map<String, List<RandomOrder>> initSellOrders() {
        Map<String, List<RandomOrder>> sellOrders = new HashMap<>();
        List<RandomOrder> nvdaOrders = new ArrayList<>();
        List<RandomOrder> amdOrders = new ArrayList<>();
        nvdaOrders.add(new RandomOrder("Sell", "NVIDIA Corporation", 100.0, "4", 2, "2NVDASell"));
        nvdaOrders.add(new RandomOrder("Sell", "NVIDIA Corporation", 98.0, "3", 8, "1NVDASell"));

        sellOrders.put("NVIDIA Corporation", nvdaOrders);
        sellOrders.put("Advanced Micro Devices, Inc.", amdOrders);

        return sellOrders;
    }

    public Trader initTrader(String id, String name, double funds, Map<String, Integer> stockPortfolio) {
        Trader trader = new Trader();
        trader.setFunds(funds);
        trader.setId(id);
        trader.setName(name);
        trader.setStockPortfolio(stockPortfolio);
        return trader;
    }
}
