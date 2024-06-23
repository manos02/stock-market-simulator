package nl.rug.aoop.trading.traderCommandHandler;


import nl.rug.aoop.stockexchange.traders.Trader;
import nl.rug.aoop.trading.application.TraderBot;
import nl.rug.aoop.trading.commandHandler.TraderInfoCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class TestTraderInfoCommand {

    private TraderBot traderBot;
    private TraderInfoCommand traderInfoCommand = null;

    @BeforeEach
    public void setUp() {
        traderBot = Mockito.mock(TraderBot.class);
        traderInfoCommand = new TraderInfoCommand(traderBot);
    }

    @Test
    void testTraderInfoConstructor() {
        assertNotNull(traderInfoCommand);
    }


    @Test
    void testExecuteCommand() {
        String traderInfoJson = "{\"name\":\"John Bobson\",\"funds\":150000.0,\"id\":\"bot4\",\"stockPortfolio\"" +
                ":{\"NVDA\":35,\"AMD\":35,\"MSFT\":83,\"TSLA\":38,\"ADBE\":72,\"FB\":17},\"transactions\":[]}";
        Trader newTrader = Trader.parseTraderInfo(traderInfoJson);
        String idToUpdate = newTrader.getId();
        double funds = newTrader.getFunds();
        String name = newTrader.getName();
        Map<String, Integer> stockPortfolio = newTrader.getStockPortfolio();
        assertEquals("John Bobson", name);
        assertEquals("bot4", idToUpdate);
        assertEquals(150000.0, funds);
        assertEquals(35, stockPortfolio.get("NVDA"));
        assertEquals(35, stockPortfolio.get("AMD"));
        assertEquals(83, stockPortfolio.get("MSFT"));
        assertEquals(38, stockPortfolio.get("TSLA"));
        assertEquals(72, stockPortfolio.get("ADBE"));
        assertEquals(17, stockPortfolio.get("FB"));
    }

}
