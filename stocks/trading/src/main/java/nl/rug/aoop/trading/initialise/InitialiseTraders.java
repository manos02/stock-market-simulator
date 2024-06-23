package nl.rug.aoop.trading.initialise;

import lombok.Getter;
import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.messagequeue.message.MessageLogger;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.networking.messageHandler.MessageHandler;
import nl.rug.aoop.stockexchange.traders.Trader;
import nl.rug.aoop.trading.commandHandler.TraderCommandHandler;
import nl.rug.aoop.trading.application.TraderBot;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.awaitility.Awaitility.await;

/**
 * Responsible for creating and setting up trader bots for trading applications.
 */
@Getter
public class InitialiseTraders {
    private final List<TraderBot> traderBots = new ArrayList<>();
    private final int port;
    private String hostName;

    /**
     * Constructs an InitialiseTraders instance with the specified port.
     *
     * @param port The port to connect to for trading applications.
     */
    public InitialiseTraders(int port, String hostName) {
        this.port = port;
        this.hostName = hostName;
    }

    /**
     * Creates a list of trader bots and adds them to the list of trader bots.
     *
     * @return The list of created trader bots.
     */
    public List<TraderBot> createTraderBots() {
        for (int i = 0; i < 9; i++) {
            Trader trader = new Trader();
            TraderBot traderBot = new TraderBot(trader);
            traderBots.add(traderBot);
        }
        return traderBots;
    }

    /**
     * Sets up trader bots by creating client connections, command handlers, and message handlers.
     *
     * @return The list of trader bots with established client connections.
     * @throws IOException If an I/O error occurs during setup.
     */
    public List<TraderBot> setupTraders() throws IOException {
        for (TraderBot traderBot : traderBots) {
            CommandHandler commandHandler = new TraderCommandHandler(traderBot).createCommandHandler();
            MessageHandler messageHandler = new MessageLogger(commandHandler);
            Client client = new Client(new InetSocketAddress(hostName, port), messageHandler);
            new Thread(client).start();
            await().atMost(Duration.ofSeconds(3)).until(client::isRunning);
            traderBot.setClient(client);
        }
        return traderBots;
    }
}