package nl.rug.aoop.trading;
import nl.rug.aoop.trading.application.TraderApplication;
import java.io.IOException;
/**
 * Main class for the Trader module.
 */

public class Main {

    /**
     * The entry point of the Trader module.
     *
     * @param args Command-line arguments.
     * @throws IOException          If an I/O error occurs.
     * @throws InterruptedException If the thread is interrupted.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        int port;
        try {
            port = Integer.parseInt(System.getenv("STOCK_EXCHANGE_PORT"));
        } catch (NumberFormatException e) {
            System.err.println("Invalid port number in STOCK_EXCHANGE_PORT environment variable.");
            port = 6200;
        }

        String hostName;
        try {
            hostName = System.getenv("STOCK_EXCHANGE_HOST");
        } catch (NumberFormatException e) {
            System.err.println("Invalid port number in STOCK_EXCHANGE_PORT environment variable.");
            hostName = "localhost";
        }

        TraderApplication traderApplication = new TraderApplication(port, hostName);
        Thread.sleep(3000);
        traderApplication.sendStockOrder();
    }
}
