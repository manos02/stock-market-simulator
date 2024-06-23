package nl.rug.aoop.trading.commandHandler;

import nl.rug.aoop.command.AbstractCommandHandler;
import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.trading.application.TraderBot;

/**
 * Responsible for creating and managing command handlers for trader-related commands.
 */
public class TraderCommandHandler implements AbstractCommandHandler {
    private final TraderBot traderBot;

    /**
     * Constructs a TraderCommandHandler with the associated trader bot.
     *
     * @param traderBot The trader bot for which to handle commands.
     */
    public TraderCommandHandler(TraderBot traderBot) {
        this.traderBot = traderBot;
    }

    /**
     * Creates and configures a command handler for trader-related commands.
     *
     * @return A command handler with registered commands for trader-related actions.
     */
    @Override
    public CommandHandler createCommandHandler() {
        CommandHandler commandHandler = new CommandHandler();
        commandHandler.placeCommand("StockInfo", new StockInfoCommand(traderBot));
        commandHandler.placeCommand("TraderInfo", new TraderInfoCommand(traderBot));
        return commandHandler;
    }
}