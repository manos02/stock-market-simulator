
package nl.rug.aoop.stockexchange;

import nl.rug.aoop.initialization.SimpleViewFactory;
import nl.rug.aoop.initialization.WebViewFactory;
import nl.rug.aoop.stockexchange.model.StockApplication;

/**
 * The main entry point for the stock exchange application. This class initializes the stock application and the
 * associated components, such as the message queue port and the user interface.
 */

public class Main {

    /**
     * The main method for starting the stock exchange application.
     *
     * @param args Command-line arguments (not used in this application).
     * @throws InterruptedException if there is an issue with thread sleep.
     */
    public static void main(String[] args) throws InterruptedException {
        int port;
        try {
            port = Integer.parseInt(System.getenv("STOCK_EXCHANGE_PORT"));
        } catch (NumberFormatException e) {
            System.err.println("Invalid port number in STOCK_EXCHANGE_PORT environment variable.");
            port = 6200;
        }

        StockApplication stockApplication = new StockApplication(port);
        WebViewFactory simpleViewFactory = new WebViewFactory();
        simpleViewFactory.createView(stockApplication.getStockMarket());
        Thread.sleep(2000);
        stockApplication.pollMessageQueue();
    }
}
