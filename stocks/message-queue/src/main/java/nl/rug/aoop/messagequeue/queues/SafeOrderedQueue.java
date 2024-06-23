package nl.rug.aoop.messagequeue.queues;

import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.messagequeue.message.MessageQueue;

import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * A model for our thread-safe ordered queue.
 */
public class SafeOrderedQueue implements MessageQueue {

    private final Queue<Message> queue;

    /**
     * Constructs a new SafeOrderedQueue instance.
     */
    public SafeOrderedQueue() {
        this.queue = new PriorityBlockingQueue<>();
    }

    /**
     * Enqueues a message into the queue.
     *
     * @param message The message to enqueue.
     * @throws NullPointerException If the provided message is null.
     */

    @Override
    public void enqueue(Message message) {
        if (message != null) {
            queue.add(message);
        } else {
            throw new NullPointerException("message is null");
        }

    }

    /**
     * Dequeues a message from the queue.
     *
     * @return The dequeued message.
     * @throws NoSuchElementException If the queue is empty.
     */
    @Override
    public Message dequeue() {
        Message message = queue.poll();
        if (message != null) {
            return message;
        } else {
            throw new NoSuchElementException();
        }
    }

    /**
     * Gets the current size of the queue.
     *
     * @return The size of the queue.
     */
    @Override
    public int getSize() {
        return queue.size();
    }
}
