package nl.rug.aoop.trading.traderapplication;

import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.stockexchange.orders.RandomOrder;
import nl.rug.aoop.stockexchange.traders.Trader;
import nl.rug.aoop.trading.application.TraderBot;
import nl.rug.aoop.trading.strategies.StrategyHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;


public class TestTraderBot {

    private TraderBot traderBot = null;

    @BeforeEach
    public void setUp() {
        StrategyHandler strategyHandler = Mockito.mock(StrategyHandler.class);
        traderBot = new TraderBot(Mockito.mock(Trader.class));
    }

    @Test
    void testTraderBotConstructor() {
        assertNotNull(traderBot);
    }

    @Test
    void testSendOrderMessage() {
        RandomOrder order = new RandomOrder("Buy", "Apple", 190.0, "bot6", 30, "bot6AAPl");
        Client mockClient = Mockito.mock(Client.class);
        traderBot.setClient(mockClient);
        traderBot.sendOrderMessage(order);
        Mockito.verify(mockClient).send(Mockito.anyString());
    }

}
