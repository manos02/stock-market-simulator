package nl.rug.aoop.networking.messageHandler;

/**
 * Interface for handling messages.
 */
public interface MessageHandler {
    /**
     * Handles the message received.
     *
     * @param message to be handled.
     * @param communicator that receives the message.
     */
    void handleMessage(String message, Communicator communicator);
}
