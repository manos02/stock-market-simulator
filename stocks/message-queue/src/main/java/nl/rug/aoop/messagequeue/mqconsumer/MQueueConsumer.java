package nl.rug.aoop.messagequeue.mqconsumer;

import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.messagequeue.message.MessageQueue;

/**
 * A model for the messageQueue consumer.
 */
public class MQueueConsumer implements MQconsumer {
    private final MessageQueue messageQueue;

    /**
     * constructor of the MQConsumer.
     *
     * @param messageQueue to operate.
     */
    public MQueueConsumer(MessageQueue messageQueue) {
        if (messageQueue != null) {
            this.messageQueue = messageQueue;
        } else {
            throw new IllegalArgumentException("messageQueue cannot be null");
        }
    }

    @Override
    public Message poll() {
        return messageQueue.dequeue();
    }
}
