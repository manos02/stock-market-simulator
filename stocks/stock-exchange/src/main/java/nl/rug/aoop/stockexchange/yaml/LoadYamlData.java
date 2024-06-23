package nl.rug.aoop.stockexchange.yaml;

import nl.rug.aoop.stockexchange.stocks.Stock;
import nl.rug.aoop.stockexchange.traders.Trader;
import nl.rug.aoop.util.YamlLoader;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsible for loading data from YAML files for stocks and traders.
 */
public class LoadYamlData {

    /**
     * Loads stock data from a YAML file and returns it as a map.
     *
     * @return A map of stock data where the key is the stock name and the value is a Stock object.
     * @throws RuntimeException If an error occurs while loading the stock data from YAML.
     */
    public Map<String, Stock> loadStocks() {
        YamlLoader loader = new YamlLoader(Paths.get("data/stocks.yaml"));
        try {
            List<Stock> stocks = loader.loadList(Stock.class);
            Map<String, Stock> stockMap = new LinkedHashMap<>();
            for (Stock stock : stocks) {
                stockMap.put(stock.getName(), stock);
            }
            return stockMap;
        } catch (IOException e) {
            throw new RuntimeException("Error loading Stock data from YAML.", e);
        }
    }

    /**
     * Loads trader data from a YAML file and returns it as a list of Trader objects.
     *
     * @return A list of Trader objects containing trader data.
     * @throws RuntimeException If an error occurs while loading the trader data from YAML.
     */
    public List<Trader> loadTraders() {
        YamlLoader loader = new YamlLoader(Paths.get("data/traders.yaml"));
        try {
            return loader.loadList(Trader.class);
        } catch (IOException e) {
            throw new RuntimeException("Error loading Trader data from YAML.", e);
        }
    }
}