package nl.rug.aoop.messagequeue.networkProducer;

import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.messagequeue.mqproducer.MQProducer;
import nl.rug.aoop.networking.client.Client;

/**
 * This class is responsible for
 * producing and sending messages over a network using a Client instance.
 */
public class NetworkProducer implements MQProducer {
    private final Client client;

    /**
     * Constructor of the networkProducer.
     *
     * @param client used for sending messages.
     */
    public NetworkProducer(Client client) {
        this.client = client;
    }

    /**
     * Creates a network message.
     *
     * @param message The message to be converted into a network message.
     * @return A JSON-formatted network message.
     */

    public String createPutMessage(Message message) {
        Message networkMessage = new Message("MqPut", message.convertToJsonString());
        return networkMessage.convertToJsonString();
    }

    /**
     * Puts a message into the message queue by sending it over the network using the associated client.
     *
     * @param message The message to be sent and added to the message queue.
     */
    @Override
    public void put(Message message) {
        client.send(createPutMessage(message));
    }
}
