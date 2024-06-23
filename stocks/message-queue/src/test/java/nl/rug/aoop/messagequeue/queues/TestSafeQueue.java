package nl.rug.aoop.messagequeue.queues;

import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.messagequeue.message.MessageQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class TestSafeQueue {

    MessageQueue queue = null;

    @BeforeEach
    void setUp() {
        queue = new SafeOrderedQueue();
    }

    @Test
    void testQueueConstructor() {
        assertNotNull(queue);
    }

    @Test
    void testQueueEmptyWhenCreated() {
        assertEquals(0, queue.getSize());
    }

    @Test
    void testQueueEnqueue() {
        Message message1 = new Message("header", "body");
        Message message2 = new Message("header", "body");
        Message message3 = new Message("header", "body");

        queue.enqueue(message3);
        queue.enqueue(message1);
        queue.enqueue(message2);

        assertEquals(message1, queue.dequeue());
        assertEquals(message2, queue.dequeue());
        assertEquals(message3, queue.dequeue());
    }

    @Test
    void testGetSize() {
        Message message1 = new Message("header1", "body1");
        Message message2 = new Message("header2", "body2");
        Message message3 = new Message("header3", "body3");

        queue.enqueue(message3);
        queue.enqueue(message1);
        queue.enqueue(message2);

        assertEquals(3, queue.getSize());
    }

    @Test
    void testSingleEnqueue() {
        Message message = new Message("header", "body");
        queue.enqueue(message);
        assertEquals(1, queue.getSize());
    }

    @Test
    void testSingleDequeue() {
        Message message = new Message("header", "body");
        queue.enqueue(message);
        queue.dequeue();
        assertEquals(0, queue.getSize());
    }


    @Test
    void testQueueDequeue() {
        Message message1 = new Message("header", "body");
        queue.enqueue(message1);
        assertEquals(message1, queue.dequeue());
    }

    @Test
    void testNullDequeue() {
        assertThrows(NoSuchElementException.class, () -> queue.dequeue());
    }

    @Test
    void testNullEnqueue() {
        assertThrows(NullPointerException.class, () -> queue.enqueue(null));
        assertEquals(0, queue.getSize());
    }

}


