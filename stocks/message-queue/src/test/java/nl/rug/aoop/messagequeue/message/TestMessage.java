package nl.rug.aoop.messagequeue.message;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class TestMessage {

    private Message message;
    private String messageHeader;
    private String messageBody;


    @BeforeEach
    void setUp() {
        messageHeader = "header";
        messageBody = "body";
        message = new Message(messageHeader, messageBody);
    }


    @Test
    void testMessageConstructor() {
        assertEquals(messageHeader, message.getHeader());
        assertEquals(messageBody, message.getBody());
        assertNotNull(message.getTimestamp());
    }

    @Test
    void testMessageImmutable() {
        List<Field> fields = List.of(Message.class.getDeclaredFields());
        fields.forEach(field -> {
            assertTrue(Modifier.isFinal(field.getModifiers()), field.getName() + " is not final");
        });
    }

    @Test
    void testMessageNullValues() {
        Message nullMessage = new Message(null, null);
        assertNull(nullMessage.getHeader());
        assertNull(nullMessage.getBody());
        assertNotNull(nullMessage.getTimestamp());
    }

    @Test
    void testMessageEquality() {
        Message message1 = new Message("header1", "body1");
        Message message2 = new Message("header1", "body1");
        Message message3 = new Message("header2", "body2");

        assertNotEquals(message1, message2);
        assertNotEquals(message1, message3);
    }


}
