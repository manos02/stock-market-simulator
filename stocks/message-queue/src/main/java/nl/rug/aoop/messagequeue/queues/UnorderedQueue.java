package nl.rug.aoop.messagequeue.queues;

import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.messagequeue.message.MessageQueue;

import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.LinkedList;

/**
 * A model for our unordered queue.
 */
public class UnorderedQueue implements MessageQueue {
    private final Queue<Message> queue ;

    /**
     * A queue is created where queues are ordered based on when they arrive.
     *
     */
    public UnorderedQueue() {
        this.queue = new LinkedList<>();
    }

    /**
     * Enqueues a message to our queue.
     *
     * @param message to be inserted.
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
     * Dequeues a message from our queue.
     *
     * @return the message.
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
     * Checks the size of the queue.
     *
     * @return the size of the queue.
     */

    @Override
    public int getSize() {
        return queue.size();
    }
}