package nl.rug.aoop.networking.Client;


import nl.rug.aoop.networking.Server.ClientHandler;
import nl.rug.aoop.networking.Server.Server;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.networking.messageHandler.MessageHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;

import static org.awaitility.Awaitility.await;

public class ClientServerIntegrationTest {
    MessageHandler messageHandler = Mockito.mock(MessageHandler.class);
    Server server;

    @BeforeEach
    public void setupServer() throws IOException {
        server = new Server(0, messageHandler);
        new Thread(server).start();
        await().atMost(Duration.ofSeconds(3)).until(server::isRunning);
    }

    void terminateServer() throws InterruptedException {
        server.terminate();
    }

    @Test
    public void testIntegration() throws IOException, InterruptedException {
        MessageHandler messageHandler1 = Mockito.mock(MessageHandler.class);

        InetSocketAddress address = new InetSocketAddress("localhost", server.getServerSocket().getLocalPort());
        Client client = new Client(address, messageHandler1);
        new Thread(client).start();
        await().atMost(Duration.ofSeconds(3)).until(client::isRunning);

        String message = "{\"header\":\"MqPut\",\"body\":\"TestMessage\",\"timestamp\":\"2023-09-29T12:00:00\"}";

        client.send(message);
        ClientHandler clientHandler = server.getClientHandlers().get(0);

        Thread.sleep(100);
        Mockito.verify(messageHandler).handleMessage(message, clientHandler);

        client.terminate();
        terminateServer();

    }

    @Test
    public void testIntegrationMultipleClients() throws IOException, InterruptedException {
        MessageHandler messageHandler1 = Mockito.mock(MessageHandler.class);
        InetSocketAddress address = new InetSocketAddress("localhost", server.getServerSocket().getLocalPort());
        Client client = new Client(address, messageHandler1);
        new Thread(client).start();
        await().atMost(Duration.ofSeconds(3)).until(client::isRunning);

        Client client1 = new Client(address, messageHandler1);
        new Thread(client1).start();
        await().atMost(Duration.ofSeconds(3)).until(client1::isRunning);

        String message = "Message1";
        String message1 = "Message1";

        client.send(message);
        client1.send(message1);
        ClientHandler clientHandler = server.getClientHandlers().get(0);
        ClientHandler clientHandler1 = server.getClientHandlers().get(1);

        Thread.sleep(100);
        Mockito.verify(messageHandler).handleMessage(message, clientHandler);
        Mockito.verify(messageHandler).handleMessage(message1, clientHandler1);

        client.terminate();
        client1.terminate();
        terminateServer();
    }
}
