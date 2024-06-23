package nl.rug.aoop.messagequeue.mqproducer;

import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.messagequeue.message.MessageQueue;
import nl.rug.aoop.messagequeue.queues.OrderedQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class TestMQueueProducer {
    MessageQueue queue = null;
    MQueueProducer mQueueProducer = null;

    @BeforeEach
    void setUp() {
        queue = new OrderedQueue();
        mQueueProducer = new MQueueProducer(queue);
    }

    @Test
    void testMQueueProducerConstructor() {
        assertNotNull(mQueueProducer);
    }

    @Test
    void testMQueueProducerPut() {
        assertEquals(0, queue.getSize());
        Message message = new Message("header1", "body1");
        mQueueProducer.put(message);
        assertEquals(1, queue.getSize());
    }

    @Test
    void testMQProducerMessageNotNull() {
        assertNotNull(mQueueProducer.getMessageQueue());
    }

    @Test
    void testNullQueue() {
        assertThrows(IllegalArgumentException.class, () -> new MQueueProducer(null));
    }

    @Test
    void insertingNullMessage() {
        assertThrows(NullPointerException.class, () -> mQueueProducer.put(null));
    }

}
