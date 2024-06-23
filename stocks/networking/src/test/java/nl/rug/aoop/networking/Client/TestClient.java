package nl.rug.aoop.networking.Client;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.networking.messageHandler.MessageHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;

@Slf4j
public class TestClient {
    private PrintWriter serverOut;
    private Boolean serverStarted;
    private Socket socket;
    private ServerSocket serverSocket;
    private BufferedReader serverIn;
    private InetSocketAddress address;
    private Client client;

    private final MessageHandler serverMessageHandler = mock(MessageHandler.class);

    private final MessageHandler clientMessageHandler = mock(MessageHandler.class);

    private void startTempServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(0);
                serverStarted = true;
                socket = serverSocket.accept();
                serverOut = new PrintWriter(socket.getOutputStream(), true);
                serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                log.info("Server started");
            } catch (IOException e) {
                log.error("Could not start server", e);
            }
        }
        ).start();
        await().atMost(Duration.ofSeconds(1)).until(() -> serverStarted);
    }

    private void tempServerReadingMessages() {
        while (true) {
            try {
                String fromClient = serverIn.readLine();
                if (fromClient == null) {
                    stopTempServer();
                    break;
                }
                serverMessageHandler.handleMessage(fromClient, null);
                System.out.println(fromClient);
                break;
            } catch (IOException e) {
                log.error("error reading from client", e);
            }
        }
    }

    public void startTempClient() throws IOException {
        address = new InetSocketAddress("localhost", serverSocket.getLocalPort());
        client = new Client(address, clientMessageHandler);
        new Thread(client).start();
        await().atMost(Duration.ofSeconds(3)).until(client::isRunning);
        assertTrue(client.isRunning());
    }

    private void stopTempServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                log.info("Server stopped");
            }
        } catch (IOException e) {
            log.error("Error while stopping the server", e);
        }
    }

    private void stopTempClient() {
        client.terminate();
    }


    @Test
    void testClientConstructor() throws IOException {
        startTempServer();
        startTempClient();
        assertNotNull(client);
        stopTempServer();
        stopTempClient();
    }

    @Test
    void testClientAddress() throws IOException {
        startTempServer();
        startTempClient();
        assertEquals(client.getAddress(), address);
        stopTempClient();
        stopTempServer();
    }

    @Test
    void testClientWrongAddress() throws IOException {
        InetSocketAddress address = new InetSocketAddress("localhost", 6300);
        MessageHandler mockHandler = mock(MessageHandler.class);
        assertThrows(ConnectException.class, () -> new Client(address, mockHandler));
    }

    @Test
    public void testConstructorWithRunningServer() throws IOException {
        startTempServer();
        startTempClient();
        assertTrue(client.isConnected());
        stopTempClient();
        stopTempServer();
    }

    @Test
    public void testConstructorWithoutRunningServer() throws IOException {
        InetSocketAddress address = new InetSocketAddress("localhost", 12345);
        MessageHandler mockHandler = mock(MessageHandler.class);
        assertThrows(IOException.class, () -> new Client(address, mockHandler));
    }

    @Test
    public void testClientPort() throws IOException {
        startTempServer();
        startTempClient();
        assertEquals(client.getAddress().getPort(), serverSocket.getLocalPort());
        stopTempClient();
        stopTempServer();
    }

    @Test
    public void testConstructorInvalidPort() {
        assertThrows(IllegalArgumentException.class, () -> {
            MessageHandler mockHandler = mock(MessageHandler.class);
            new Client(new InetSocketAddress("localhost", -1), mockHandler);
        });
    }

    @Test
    public void testConstructorInvalidHost() {
        InetSocketAddress address = new InetSocketAddress("invalidhost", 0);
        MessageHandler mockHandler = mock(MessageHandler.class);
        assertThrows(IOException.class, () -> new Client(address, mockHandler));
    }

    @Test
    public void testRun() throws IOException {
        startTempServer();
        startTempClient();
        assertTrue(client.isRunning());
        stopTempClient();
        stopTempServer();
    }

    @Test
    public void testRunReadSingleMessage() throws IOException, InterruptedException {
        startTempServer();
        startTempClient();

        String message = "hello";
        serverOut.println(message);
        Thread.sleep(100);
        Mockito.verify(clientMessageHandler).handleMessage(message, client);
        stopTempClient();
        stopTempServer();
    }


    @Test
    public void testSendMessage() throws IOException {
        startTempServer();
        startTempClient();

        String message = "hello server";
        client.send(message);
        tempServerReadingMessages();

        Mockito.verify(serverMessageHandler).handleMessage(message, null);

        stopTempClient();
        stopTempServer();
    }


    @Test
    public void testSendNullMessage() throws IOException {
        startTempServer();
        startTempClient();

        assertThrows(IllegalArgumentException.class, () -> client.send(null));

        stopTempClient();
        stopTempServer();
    }

    @Test
    public void testClientTermination() throws IOException {
        startTempServer();
        startTempClient();
        stopTempClient();
        assertFalse(client.isRunning());
        stopTempServer();
    }

}