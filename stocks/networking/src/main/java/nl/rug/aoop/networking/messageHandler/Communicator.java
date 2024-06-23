package nl.rug.aoop.networking.messageHandler;

/**
 * Interface for a communicator that handles sending messages and termination.
 */
public interface Communicator {
    /**
     * Sends a message using the communicator.
     *
     * @param message The message to be sent.
     */
    void send(String message);

    /**
     * Terminates the communicator.
     */
    void terminate();
}
