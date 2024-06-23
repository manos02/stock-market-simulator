package nl.rug.aoop.trading.traderapplication;

import nl.rug.aoop.trading.application.TraderBot;
import nl.rug.aoop.trading.application.TraderManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;


import java.util.ArrayList;
import java.util.List;

public class TestTraderManager {

    private TraderManager traderManager;
    private final List<TraderBot> traderBots = new ArrayList<>();

    @BeforeEach
    void setUp() {
        traderManager = new TraderManager(traderBots);
    }

    @Test
    void testTradeManagerConstructor() {
        assertNotNull(traderManager);
    }

    @Test
    void testAddTraderBot() {
        TraderBot traderBot = Mockito.mock(TraderBot.class);
        traderBots.add(traderBot);
        assert(traderManager.getTraderBots().contains(traderBot));
    }

    @Test
    void testRemoveTraderBot() {
        TraderBot traderBot = Mockito.mock(TraderBot.class);
        traderBots.add(traderBot);
        traderManager.removeTraderBot(traderBot);
        assert(!traderBots.contains(traderBot));
    }


}
