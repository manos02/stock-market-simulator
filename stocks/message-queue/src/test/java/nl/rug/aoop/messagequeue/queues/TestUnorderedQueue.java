package nl.rug.aoop.messagequeue.queues;

import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.messagequeue.message.MessageQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class TestUnorderedQueue {

    MessageQueue queue = null;

    @BeforeEach
    void setUp() {
        queue = new UnorderedQueue();
    }

    @Test
    void testQueueConstructor() {
        assertNotNull(queue);
        assertEquals(0, queue.getSize());
    }


    @Test
    void testGetSize() {
        Message message1 = new Message("header", "body");
        Message message2 = new Message("header", "body");
        Message message3 = new Message("header", "body");

        assertEquals(0, queue.getSize());

        queue.enqueue(message3);
        queue.enqueue(message1);
        queue.enqueue(message2);

        assertEquals(3, queue.getSize());
        queue.dequeue();
        assertEquals(2, queue.getSize());
        queue.dequeue();
        assertEquals(1, queue.getSize());
        queue.dequeue();
        assertEquals(0, queue.getSize());
    }

    @Test
    void testQueueOrdering() {
        Message message1 = new Message("header", "body");
        Message message2 = new Message("header", "body");
        Message message3 = new Message("header", "body");

        queue.enqueue(message3);
        queue.enqueue(message1);
        queue.enqueue(message2);

        assertEquals(message3, queue.dequeue());
        assertEquals(message1, queue.dequeue());
        assertEquals(message2, queue.dequeue());
    }

    @Test
    void testQueueDequeue() {
        Message message1 = new Message("header", "body");
        queue.enqueue(message1);
        assertEquals(message1, queue.dequeue());
        assertThrows(NoSuchElementException.class, () -> queue.dequeue());
    }

    @Test
    void testNullEnqueue() {
        assertThrows(NullPointerException.class, () -> queue.enqueue(null));
        assertEquals(0, queue.getSize());
    }

    @Test
    void testNullNonNullMessages() {
        Message message1 = new Message("header1", "body1");
        Message message3 = new Message("header3", "body3");

        queue.enqueue(message1);
        assertThrows(NullPointerException.class, () -> queue.enqueue(null));
        queue.enqueue(message3);

        assertEquals(2, queue.getSize());
    }

}
