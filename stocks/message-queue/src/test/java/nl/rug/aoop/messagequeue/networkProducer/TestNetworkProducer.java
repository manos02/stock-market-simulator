package nl.rug.aoop.messagequeue.networkProducer;

import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.networking.client.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestNetworkProducer {
    private NetworkProducer networkProducer;
    private Client client;

    @BeforeEach
    public void setUp() {
        client = Mockito.mock(Client.class);
        networkProducer = new NetworkProducer(client);
    }

    @Test
    void testNetworkProducerConstructor() {
        assertNotNull(networkProducer);
    }

    @Test
    void testCreatePutMessage() {
        Message message = new Message("Test Message", "Test Body");
        String networkMessage = networkProducer.createPutMessage(message);
        Message newMessage = Message.convertToMessage(networkMessage);

        assertEquals("MqPut", newMessage.getHeader());
        assertEquals(message.convertToJsonString(), newMessage.getBody());
    }

    @Test
    void testPut() {
        String message = "test message";
        client.send(message);
        Mockito.verify(client).send(message);
    }

}
