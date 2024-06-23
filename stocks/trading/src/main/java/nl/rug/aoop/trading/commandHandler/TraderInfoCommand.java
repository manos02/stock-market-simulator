package nl.rug.aoop.trading.commandHandler;

import nl.rug.aoop.command.Command;
import nl.rug.aoop.trading.application.TraderBot;
import nl.rug.aoop.stockexchange.orders.Transaction;
import nl.rug.aoop.stockexchange.traders.Trader;

import java.util.List;
import java.util.Map;

/**
 * Represents a command for updating trader information for a trader bot.
 */
public class TraderInfoCommand implements Command {
    private final TraderBot traderBot;

    /**
     * Constructs a TraderInfoCommand instance with the associated trader bot.
     *
     * @param traderBot The trader bot to update with trader information.
     */
    public TraderInfoCommand(TraderBot traderBot) {
        this.traderBot = traderBot;
    }

    /**
     * Executes the trader information command by updating the trader bot with the provided trader information.
     *
     * @param params The parameters for the command, including the trader information.
     */
    @Override
    public void executeCommand(Map<String, Object> params) {
        String body = (String) params.get("body");
        Trader newTrader = Trader.parseTraderInfo(body);

        if (newTrader != null) {
            String idToUpdate = newTrader.getId();
            double funds = newTrader.getFunds();
            String name = newTrader.getName();
            Map<String, Integer> stockPortfolio = newTrader.getStockPortfolio();
            List<Transaction> transactions = newTrader.getTransactions();
            traderBot.getTrader().setFunds(funds);
            traderBot.getTrader().setId(idToUpdate);
            traderBot.getTrader().setStockPortfolio(stockPortfolio);
            traderBot.getTrader().setName(name);
            traderBot.checkTransactions(transactions);
        }
    }
}