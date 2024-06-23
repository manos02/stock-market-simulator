package nl.rug.aoop.messagequeue.messageQueueCommandHandler;
import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.messagequeue.message.MessageQueue;
import nl.rug.aoop.messagequeue.queues.SafeOrderedQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestMessageQCommandHandler {
        private MessageQCommandHandler messageQCommandHandler;
        private MessageQueue messageQueue;

        @BeforeEach
        public void setUp() {
            messageQueue = new SafeOrderedQueue();
            messageQCommandHandler = new MessageQCommandHandler(messageQueue);
        }

        @Test
        public void testMQCommandHandlerConstructor() {
            assertNotNull(messageQCommandHandler);
        }

        @Test
        public void testCreateCommandHandler() {
            CommandHandler commandHandler = messageQCommandHandler.createCommandHandler();
            assertNotNull(commandHandler);
        }

}


