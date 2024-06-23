package nl.rug.aoop.messagequeue.messageQueueCommandHandler;

import nl.rug.aoop.messagequeue.queues.SafeOrderedQueue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import nl.rug.aoop.messagequeue.message.MessageQueue;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class TestMqPutCommand {

    private MqPutCommand mqPutCommand;
    private MessageQueue messageQueue;

    @BeforeEach
    public void setUp() {
        messageQueue = new SafeOrderedQueue();
        mqPutCommand = new MqPutCommand(messageQueue);
    }


    @Test
    void testMqPutCommandConstructor (){
        Assertions.assertNotNull(mqPutCommand);
    }

    @Test
    public void testExecuteCommand() {
        Map<String, Object> params = new HashMap<>();
        String message = "{\"header\":\"MqPut\",\"body\":\"TestMessage\",\"timestamp\":\"2023-09-29T12:00:00\"}";
        params.put("body", message);
        mqPutCommand.executeCommand(params);

        assertEquals(1, messageQueue.getSize());
    }

    @Test
    public void testEmptyMessage() {
        Map<String, Object> params = new HashMap<>();
        params.put("body", null);
        assertThrows(NullPointerException.class, () -> mqPutCommand.executeCommand(params));
    }

}



