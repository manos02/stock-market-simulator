package nl.rug.aoop.stockexchange.messageManager;

import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.messagequeue.message.MessageQueue;
import nl.rug.aoop.networking.messageHandler.Communicator;
import nl.rug.aoop.networking.messageHandler.MessageHandler;

/**
 * Manages incoming messages by converting them to Message objects and enqueuing them in a MessageQueue.
 */
public class MessageManager implements MessageHandler {

    private final MessageQueue messageQueue;

    /**
     * Constructs a MessageManager with the provided MessageQueue.
     *
     * @param messageQueue The MessageQueue to be associated with this message manager.
     */
    public MessageManager(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    /**
     * Handles incoming messages by converting them to Message objects and enqueuing
     * them in the associated MessageQueue.
     *
     * @param messageJson   The JSON representation of the incoming message.
     * @param communicator  The Communicator responsible for handling the message.
     */
    @Override
    public void handleMessage(String messageJson, Communicator communicator) {
        if (!messageJson.isEmpty()) {
            Message message = Message.convertToMessage(messageJson);
            messageQueue.enqueue(message);
        }
    }
}
