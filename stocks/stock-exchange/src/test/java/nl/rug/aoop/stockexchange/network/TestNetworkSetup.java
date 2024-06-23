package nl.rug.aoop.stockexchange.network;

import nl.rug.aoop.messagequeue.message.MessageQueue;
import nl.rug.aoop.stockexchange.model.StockMarket;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestNetworkSetup {
    private static NetworkSetup networkSetup = null;
    private static MessageQueue messageQueue;
    private static StockMarket stockMarket;
    private static final int port = 6200;

    @BeforeAll
    public static void setUp() {
        messageQueue = mock(MessageQueue.class);
        stockMarket = mock(StockMarket.class);
        networkSetup = new NetworkSetup(messageQueue, stockMarket, port);
    }

    @AfterAll
    public static void terminateServer() throws InterruptedException {
        networkSetup.getServer().terminate();
    }

    @Test
    void testNetworkConstructor() {
        assertNotNull(networkSetup);
    }

    @Test
    void testServerNotNull() {
        assertNotNull(networkSetup.getServer());
    }

    @Test
    void testServerIsRunning() {
        assertTrue(networkSetup.getServer().isRunning());
    }

    @Test
    void testPort() {
        assertEquals(6200, networkSetup.getPort());
    }

}
