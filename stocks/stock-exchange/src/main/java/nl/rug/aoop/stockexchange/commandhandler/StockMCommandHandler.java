package nl.rug.aoop.stockexchange.commandhandler;

import nl.rug.aoop.command.AbstractCommandHandler;
import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.stockexchange.model.StockMarket;

/**
 * Command handler for stock market operations, including "Buy" and "Sell" commands.
 */
public class StockMCommandHandler implements AbstractCommandHandler {

    private final StockMarket stockMarket;

    /**
     * Constructs a StockMCommandHandler with the provided StockMarket.
     *
     * @param stockMarket The StockMarket to be associated with this command handler.
     */
    public StockMCommandHandler(StockMarket stockMarket) {
        this.stockMarket = stockMarket;
    }

    /**
     * Creates and configures a command handler for stock market operations.
     *
     * @return A configured CommandHandler with "Buy" and "Sell" commands.
     */
    @Override
    public CommandHandler createCommandHandler() {
        CommandHandler commandHandler = new CommandHandler();
        commandHandler.placeCommand("Buy", new BuyOrderCommand(stockMarket));
        commandHandler.placeCommand("Sell", new SellOrderCommand(stockMarket));
        return commandHandler;
    }
}
