package nl.rug.aoop.stockexchange.network;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.messagequeue.message.MessageQueue;
import nl.rug.aoop.networking.Server.Server;
import nl.rug.aoop.networking.messageHandler.MessageHandler;
import nl.rug.aoop.stockexchange.messageManager.MessageManager;
import nl.rug.aoop.stockexchange.model.StockMarket;

import java.io.IOException;

/**
 * Manages the setup and initialization of the server for communication with clients.
 */
@Slf4j
@Getter
public class NetworkSetup {
    private Server server;
    private final MessageHandler messageHandler;
    private final MessageQueue messageQueue;
    private final StockMarket stockMarket;
    private final int port;

    /**
     * Initializes the NetworkSetup with the required components for communication.
     *
     * @param messageQueue The message queue for storing incoming messages.
     * @param stockMarket  The stock market to associate with this network setup.
     * @param port         The port number on which the server will listen for incoming connections.
     */
    public NetworkSetup(MessageQueue messageQueue, StockMarket stockMarket, int port) {
        this.stockMarket = stockMarket;
        this.messageQueue = messageQueue;
        this.port = port;
        messageHandler = new MessageManager(messageQueue);
        setupServer();
    }

    /**
     * Initializes and starts the server, enabling communication with clients.
     */
    public void setupServer() {
        try {
            server = new Server(port, messageHandler);
            new Thread(server).start();
            log.info("Server started at port " + server.getPort());
        } catch (IOException e) {
            log.error("Server failed to start", e);
        }
    }
}