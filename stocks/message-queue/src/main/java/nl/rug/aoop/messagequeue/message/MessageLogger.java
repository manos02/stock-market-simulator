package nl.rug.aoop.messagequeue.message;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.networking.messageHandler.Communicator;
import nl.rug.aoop.networking.messageHandler.MessageHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of MessageHandler interface to handle incoming messages.
 */
@Slf4j
public class MessageLogger implements MessageHandler {
    private final CommandHandler commandHandler;

    /**
     * Constructor of the messageLogger.
     *
     * @param commandHandler The command handler responsible for processing messages.
     */
    public MessageLogger(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    /**
     * Handles incoming messages by converting them to Message objects, extracting relevant information,
     * and passing them to the Command Handler.
     *
     * @param messageJson  The JSON-formatted message to be handled.
     * @param communicator The communicator representing the network connection.
     */
    @Override
    public void handleMessage(String messageJson, Communicator communicator) {

        if (!messageJson.isEmpty()) {
            Message newMessage = Message.convertToMessage(messageJson);

            Map<String, Object> commandMap = new HashMap<>();

            commandMap.put("header", newMessage.getHeader());
            commandMap.put("body", newMessage.getBody());
            commandMap.put("communicator", communicator);

            commandHandler.findCommand(commandMap);
        }
    }
}
