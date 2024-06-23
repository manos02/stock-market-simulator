package nl.rug.aoop.stockexchange.model;

import lombok.Getter;
import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.messagequeue.mqconsumer.MQueueConsumer;
import nl.rug.aoop.stockexchange.network.NetworkSetup;
import nl.rug.aoop.messagequeue.message.MessageQueue;
import nl.rug.aoop.messagequeue.queues.SafeOrderedQueue;
import nl.rug.aoop.networking.Server.ClientHandler;
import nl.rug.aoop.networking.Server.Server;
import nl.rug.aoop.stockexchange.stocks.Stock;
import nl.rug.aoop.stockexchange.traders.Trader;
import nl.rug.aoop.stockexchange.yaml.LoadYamlData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The main application class for the stock exchange simulation.
 */
@Getter
public class StockApplication {
    private final Server server;
    private final int port;
    private final NetworkSetup networkSetup;
    private final StockMarket stockMarket;
    private final MessageQueue messageQueue;
    private final LoadYamlData yamlLoader = new LoadYamlData();
    private final MQueueConsumer consumer;
    private final Map<String, ClientHandler> linkTraders = new HashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    /**
     * Constructs a StockApplication with the specified port number.
     *
     * @param port The port number for the stock exchange server.
     * @throws InterruptedException If there are issues with server initialization.
     */
    public StockApplication(int port) throws InterruptedException {
        this.messageQueue = new SafeOrderedQueue();
        this.consumer = new MQueueConsumer(messageQueue);
        this.stockMarket = new StockMarket(loadExistingStocks(), loadExistingTraders());
        this.networkSetup = new NetworkSetup(messageQueue, stockMarket, port);
        this.server = networkSetup.getServer();
        this.port = port;
        linkTradersWClientHandlers();
        scheduler.scheduleAtFixedRate(this::sendInfo, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Loads existing stocks from a data source.
     *
     * @return A map of stocks loaded from the data source.
     */
    public Map<String, Stock> loadExistingStocks() {
        return yamlLoader.loadStocks();
    }

    /**
     * Loads existing traders from a data source.
     *
     * @return A list of traders loaded from the data source.
     */
    public List<Trader> loadExistingTraders() {
        return yamlLoader.loadTraders();
    }

    /**
     * Links traders with client handlers.
     *
     * @throws InterruptedException If there are issues while linking traders with client handlers.
     */
    public void linkTradersWClientHandlers() throws InterruptedException {
        List<ClientHandler> clientHandlers;
        do {
            clientHandlers = server.getClientHandlers();
            Thread.sleep(50);
        } while (clientHandlers.size() != 9);

        int i = 0;
        for (Trader trader : stockMarket.getTraders()) {
            if (i < clientHandlers.size()) {
                linkTraders.put(trader.getId(), clientHandlers.get(i));
                i++;
            }
        }
    }

    /**
     * Polls the message queue for incoming messages and executes corresponding stock market orders.
     */
    public void pollMessageQueue() {
        while (true) {
            if (messageQueue.getSize() != 0) {
                Message message = consumer.poll();
                stockMarket.executeOrder(message);
            }
        }
    }

    /**
     * Sends updated stock information to connected traders.
     */
    public void sendInfo() {
        updateInfoTrader();
        updateTradersStockInfo();
    }

    /**
     * Updates information about each trader and sends it to the respective client handler.
     */
    public void updateInfoTrader() {
        for (Trader trader : stockMarket.getTraders()) {
            ClientHandler clientHandler = linkTraders.get(trader.getId());
            Message message = new Message("TraderInfo", trader.toStringMessage());
            clientHandler.send(message.convertToJsonString());
        }
    }

    /**
     * Updates stock information and sends it to connected client handlers.
     */
    public void updateTradersStockInfo() {
        for (ClientHandler clientHandler : server.getClientHandlers()) {
            Message message = new Message("StockInfo", stockMarket.convertStocksToString());
            clientHandler.send(message.convertToJsonString());
        }
    }
}
