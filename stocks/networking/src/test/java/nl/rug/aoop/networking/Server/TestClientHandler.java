package nl.rug.aoop.networking.Server;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.networking.messageHandler.MessageHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;

@Slf4j
public class TestClientHandler {
    private Boolean serverStarted;
    private Socket socket;
    private ServerSocket serverSocket;
    private InetSocketAddress address;
    private Client client;
    private final MessageHandler messageHandler = Mockito.mock(MessageHandler.class);


    @BeforeEach
    public void startTempServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(0);
                serverStarted = true;
                socket = serverSocket.accept();
                log.info("Server started");
            } catch (IOException e) {
                log.error("Could not start server");
            }
        }
        ).start();
        await().atMost(Duration.ofSeconds(3)).until(() -> serverStarted);
    }


    public void startTempClient() throws IOException {
        address = new InetSocketAddress("localhost", serverSocket.getLocalPort());
        client = new Client(address, messageHandler);
        new Thread(client).start();
        await().atMost(Duration.ofSeconds(3)).until(client::isRunning);
        assertTrue(client.isRunning());
    }

    @AfterEach
    public void stopTempServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                log.info("Server stopped");
            }
        } catch (IOException e) {
            log.error("Error while stopping the server", e);
        }
    }

    public void stopTempClient() {
        client.terminate();
    }

    @Test
    void testClientHandlerConstructor() throws IOException {
        startTempClient();

        MessageHandler messageHandler1 = Mockito.mock(MessageHandler.class);
        ClientHandler clientHandler = new ClientHandler(socket, 0, messageHandler1);
        assertNotNull(clientHandler);
        stopTempClient();
    }

    @Test
    void testClientHandlerId() throws IOException {
        startTempClient();
        MessageHandler messageHandler1 = Mockito.mock(MessageHandler.class);
        ClientHandler clientHandler = new ClientHandler(socket, 0, messageHandler1);
        assertEquals(0, clientHandler.getId());
        stopTempClient();
    }

    @Test
    void testConstructorWithoutRunningClient() throws IOException {
        MessageHandler messageHandler1 = Mockito.mock(MessageHandler.class);
        assertThrows(NullPointerException.class, () -> new ClientHandler(socket, 0, messageHandler1));
    }

    @Test
    void testClientHandlerSocket() throws IOException {
        startTempClient();
        MessageHandler messageHandler1 = Mockito.mock(MessageHandler.class);
        ClientHandler clientHandler = new ClientHandler(socket, 0, messageHandler1);
        assertEquals(clientHandler.getSocket(), socket);
        stopTempClient();
    }

    @Test
    void testClientHandlerRun() throws IOException {
        startTempClient();

        MessageHandler messageHandler1 = Mockito.mock(MessageHandler.class);
        ClientHandler clientHandler = new ClientHandler(socket, 0, messageHandler1);
        new Thread(clientHandler).start();
        await().atMost(Duration.ofSeconds(3)).until(clientHandler::isRunning);

        assertTrue(clientHandler.isRunning());
        stopTempClient();

    }

    @Test
    public void testClientHandlerTermination() throws IOException {
        startTempClient();

        MessageHandler messageHandler1 = Mockito.mock(MessageHandler.class);
        ClientHandler clientHandler = new ClientHandler(socket, 0, messageHandler1);
        new Thread(clientHandler).start();
        await().atMost(Duration.ofSeconds(3)).until(clientHandler::isRunning);
        clientHandler.terminate();
        assertFalse(clientHandler.isRunning());
        stopTempClient();

    }

    @Test
    public void testReadMessage() throws IOException, InterruptedException {
        startTempClient();

        MessageHandler messageHandler1 = Mockito.mock(MessageHandler.class);
        ClientHandler clientHandler = new ClientHandler(socket, 0, messageHandler1);
        new Thread(clientHandler).start();
        await().atMost(Duration.ofSeconds(3)).until(clientHandler::isRunning);

        String message = "new message";
        client.send(message);
        Thread.sleep(3000);
        Mockito.verify(messageHandler1).handleMessage(message, clientHandler);

        stopTempClient();
    }

    @Test
    public void testSendMessage() throws IOException, InterruptedException {
        startTempClient();

        MessageHandler messageHandler1 = Mockito.mock(MessageHandler.class);
        ClientHandler clientHandler = new ClientHandler(socket, 0, messageHandler1);
        String message = "new message";
        clientHandler.send(message);
        Thread.sleep(1000);
        Mockito.verify(messageHandler).handleMessage(message, client);
    }

    @Test
    public void testSendNullMessage() throws IOException {
        startTempClient();

        MessageHandler messageHandler1 = Mockito.mock(MessageHandler.class);
        ClientHandler clientHandler = new ClientHandler(socket, 0, messageHandler1);

        assertThrows(IllegalArgumentException.class, () -> clientHandler.send(null));
    }

    @Test
    public void testClientDisconnection() throws IOException {
        startTempClient();

        MessageHandler messageHandler1 = Mockito.mock(MessageHandler.class);
        ClientHandler clientHandler = new ClientHandler(socket, 0, messageHandler1);
        new Thread(clientHandler).start();
        await().atMost(Duration.ofSeconds(3)).until(clientHandler::isRunning);

        stopTempClient();
        await().atMost(Duration.ofSeconds(3)).until(() -> !clientHandler.isRunning());

        assertFalse(clientHandler.isRunning());
    }
}
