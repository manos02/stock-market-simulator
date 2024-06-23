package nl.rug.aoop.messagequeue.message;

import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.networking.messageHandler.MessageHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestMessageLogger {
    private MessageHandler messageHandler;
    private CommandHandler commandHandler;

    @BeforeEach
    void setUp() {
        commandHandler = Mockito.mock(CommandHandler.class);
        messageHandler = new MessageLogger(commandHandler);
    }

    @Test
    void testHandleMessage() {
        String message = "{\"header\":\"MqPut\",\"body\":\"TestMessage\",\"timestamp\":\"2023-09-29T12:00:00\"}";
        Message newMessage = Message.convertToMessage(message);

        Map<String, Object> commandMap = new HashMap<>();

        commandMap.put("header", newMessage.getHeader());
        commandMap.put("body", newMessage.getBody());
        commandMap.put("communicator", null);

        messageHandler.handleMessage(message, null);
        Mockito.verify(commandHandler).findCommand(commandMap);
    }

    @Test
    void testHandleEmptyMessage() {
        assertThrows(NullPointerException.class, () -> messageHandler.handleMessage(null, null));
    }
}
