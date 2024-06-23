package nl.rug.aoop.messagequeue.messageQueueIntegration;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.messagequeue.message.MessageLogger;
import nl.rug.aoop.messagequeue.message.MessageQueue;
import nl.rug.aoop.messagequeue.messageQueueCommandHandler.MessageQCommandHandler;
import nl.rug.aoop.messagequeue.networkProducer.NetworkProducer;
import nl.rug.aoop.messagequeue.queues.SafeOrderedQueue;
import nl.rug.aoop.networking.Server.Server;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.networking.messageHandler.MessageHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class MessageQueueIntegrationTest {
    private MessageQueue messageQueue;
    private MessageHandler messageHandler;
    private CommandHandler commandHandler;
    private Server server;
    private Client client;

    @BeforeEach
    void setUp() throws IOException {
        messageQueue = new SafeOrderedQueue();
        MessageQCommandHandler messageQCommandHandler = new MessageQCommandHandler(messageQueue);
        commandHandler = messageQCommandHandler.createCommandHandler();
        messageHandler = new MessageLogger(commandHandler);
        startServer();
        startClient();
    }

    @AfterEach
    void terminate() throws InterruptedException {
        client.terminate();
        server.terminate();
    }
    void startServer() {
        try {
            server = new Server(6200, messageHandler);
            new Thread(server).start();
            log.info("Server started at port " + server.getPort());
        } catch (IOException e) {
            log.error("Server failed, could not start",e);
        }
    }

    void startClient() throws IOException {
        client = new Client(new InetSocketAddress("localhost", 6200), messageHandler);
        new Thread(client).start();
        await().atMost(Duration.ofSeconds(3)).until(client::isRunning);
        assertTrue(client.isRunning());
    }


    @Test
    void testMessageQueueEnqueue() throws InterruptedException {
        NetworkProducer producer = new NetworkProducer(client);
        producer.put(new Message("Test message", "Test Body"));
        Thread.sleep(1000);
        assertEquals(1, messageQueue.getSize());
    }

}
