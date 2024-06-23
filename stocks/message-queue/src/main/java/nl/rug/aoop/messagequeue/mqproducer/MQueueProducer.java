package nl.rug.aoop.messagequeue.mqproducer;

import nl.rug.aoop.messagequeue.message.MessageQueue;
import nl.rug.aoop.messagequeue.message.Message;

/**
 * A model for the messageQueue producer.
 */
public class MQueueProducer implements MQProducer {
    private final MessageQueue messageQueue;

    /**
     * constructor of the MQProducer.
     *
     * @param messageQueue to operate.
     */
    public MQueueProducer(MessageQueue messageQueue) {
        if (messageQueue != null) {
            this.messageQueue = messageQueue;
        } else {
            throw new IllegalArgumentException("messageQueue cannot be null");
        }
    }

    @Override
    public void put(Message message) {
        messageQueue.enqueue(message);
    }

    public MessageQueue getMessageQueue() {
        return messageQueue;
    }
}