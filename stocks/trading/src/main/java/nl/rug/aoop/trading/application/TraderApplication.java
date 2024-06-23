package nl.rug.aoop.trading.application;

import lombok.Getter;
import nl.rug.aoop.trading.initialise.InitialiseTraders;
import java.io.IOException;
import java.util.List;

/**
 * Represents the main application for managing and controlling trader bots and transactions.
 */
@Getter
public class TraderApplication {
    private List<TraderBot> tradersBots;
    private final TraderManager traderManager;
    private final InitialiseTraders initialiseTraders;
    private final int port;
    private final String hostName;

    /**
     * Constructs a TraderApplication instance with the specified port.
     *
     * @param port The port used for initialization.
     * @throws IOException If an I/O error occurs during initialization.
     */
    public TraderApplication(int port, String hostName) throws IOException {
        this.port = port;
        this.hostName = hostName;
        initialiseTraders = new InitialiseTraders(port, hostName);
        tradersBots = initialiseTraders.createTraderBots();
        tradersBots = initialiseTraders.setupTraders();
        this.traderManager = new TraderManager(tradersBots);
    }

    /**
     * Sends stock orders and manages trader activities continuously.
     *
     * @throws InterruptedException If an error occurs during the execution of trading activities.
     */
    public void sendStockOrder() throws InterruptedException {
        while (true) {
            traderManager.sendOrders();
        }
    }

}