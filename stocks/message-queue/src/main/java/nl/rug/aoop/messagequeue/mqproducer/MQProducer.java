package nl.rug.aoop.messagequeue.mqproducer;

import nl.rug.aoop.messagequeue.message.Message;

/**
 * Interface for the producer.
 */
public interface MQProducer {
    /**
     * producer puts a message in the queue.
     *
     * @param message to be placed.
     */
    void put(Message message);
}
