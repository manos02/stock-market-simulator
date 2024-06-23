package nl.rug.aoop.messagequeue.messageQueueCommandHandler;

import nl.rug.aoop.command.AbstractCommandHandler;
import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.messagequeue.message.MessageQueue;

/**
 * A class for handling commands related to a messageQueue.
 */
public class MessageQCommandHandler implements AbstractCommandHandler {
    private final MessageQueue messageQueue;

    /**
     * Constructor of the messageQueue command handler.
     *
     * @param messageQueue to be used for the commands.
     */
    public MessageQCommandHandler(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    /**
     * Add the specified commands for the messageQueue.
     *
     * @return the command handler with the commands.
     */
    @Override
    public CommandHandler createCommandHandler() {
        CommandHandler commandHandler = new CommandHandler();
        commandHandler.placeCommand("MqPut", new MqPutCommand(messageQueue));
        return commandHandler;
    }
}
