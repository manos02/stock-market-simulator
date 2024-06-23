package nl.rug.aoop.trading.commandHandler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rug.aoop.command.Command;
import nl.rug.aoop.stockexchange.stocks.Stock;
import nl.rug.aoop.trading.application.TraderBot;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Represents a command for updating stock information for a trader bot.
 */
public class StockInfoCommand implements Command {
    private final TraderBot traderBot;

    /**
     * Constructs a StockInfoCommand instance with the associated trader bot.
     *
     * @param trader The trader bot to update with stock information.
     */
    public StockInfoCommand(TraderBot trader) {
        this.traderBot = trader;
    }

    /**
     * Executes the stock information command by updating the trader bot with the provided stock information.
     *
     * @param params The parameters for the command, including the stock information.
     */
    @Override
    public void executeCommand(Map<String, Object> params) {
        String body = (String) params.get("body");
        List<Stock> updatedStocks = stringToList(body);

        if (traderBot != null) {
            traderBot.setStocks(updatedStocks);
        }
    }

    /**
     * Converts a JSON string to a list of stock objects.
     *
     * @param str The JSON string representing stock information.
     * @return A list of stock objects parsed from the JSON string.
     */
    public List<Stock> stringToList(String str) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(str, new TypeReference<>() {});
        } catch ( IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}