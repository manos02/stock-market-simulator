package nl.rug.aoop.messagequeue.message;

/**
 * Interface for our message queues.
 */
public interface MessageQueue {
    /**
     * method to enqueue a message.
     *
     * @param message to be enqueued.
     */
    void enqueue(Message message);

    /**
     * method to dequeue a message.
     *
     * @return the dequeued message.
     */
    Message dequeue();

    /**
     * method to get the size of the queue.
     *
     * @return the size.
     */
    int getSize();

}
