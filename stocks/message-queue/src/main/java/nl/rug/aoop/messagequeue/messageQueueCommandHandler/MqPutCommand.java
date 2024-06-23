
package nl.rug.aoop.messagequeue.messageQueueCommandHandler;

import nl.rug.aoop.command.Command;
import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.messagequeue.message.MessageQueue;

import java.util.Map;

/**
 * The `MqPutCommand` class represents a command for putting a message into a message queue.
 */
public class MqPutCommand implements Command {
    private final MessageQueue messageQueue;

    /**
     * Constructor of the MQPutCommand.
     *
     * @param messageQueue The message queue to which messages will be enqueued.
     */
    public MqPutCommand(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    /**
     * Executes the command by enqueuing a message into the message queue.
     *
     * @param params A map of parameters, where the "body" parameter contains the message body.
     */
    @Override
    public void executeCommand(Map<String, Object> params) {
        String body = (String) params.get("body");
        Message message = Message.convertToMessage(body);
        messageQueue.enqueue(message);
    }
}
