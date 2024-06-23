package nl.rug.aoop.messagequeue.mqconsumer;

import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.messagequeue.message.MessageQueue;
import nl.rug.aoop.messagequeue.queues.OrderedQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;


public class TestMQueueConsumer {
    MessageQueue queue = null;
    MQueueConsumer mQueueConsumer = null;

    @BeforeEach
    void setUp() {
        queue = new OrderedQueue();
        mQueueConsumer = new MQueueConsumer(queue);
    }

    @Test
    void testMQueueConsumerConstructor() {
        assertNotNull(mQueueConsumer);
    }

    @Test
    void testMQueueConsumerPoll() {
        Message message = new Message("header1", "body1");
        queue.enqueue(message);
        assertEquals(message, mQueueConsumer.poll());
    }

    @Test
    void testMQConsumerPollNull() {
        assertThrows(NoSuchElementException.class, () -> mQueueConsumer.poll());
    }

    @Test
    void testMQConsumerMultiplePoll() {
        Message message1 = new Message("header1", "body1");
        Message message2 = new Message("header2", "body2");
        queue.enqueue(message1);
        queue.enqueue(message2);
        assertEquals(message1, mQueueConsumer.poll());
        assertEquals(message2, mQueueConsumer.poll());
    }

    @Test
    void testNullQueue() {
        assertThrows(IllegalArgumentException.class, () -> new MQueueConsumer(null));
    }

}
