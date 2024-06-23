package nl.rug.aoop.messagequeue.mqconsumer;

import nl.rug.aoop.messagequeue.message.Message;

/**
 * An interface for the consumer.
 */
public interface MQconsumer {
    /**
     * Consumer polls a message from the queue.
     *
     * @return a message.
     */
    Message poll();
}
