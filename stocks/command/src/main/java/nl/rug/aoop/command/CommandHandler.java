package nl.rug.aoop.command;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;

/**
 * A class for handling the commands.
 */

@Getter
@Slf4j
public class CommandHandler {

    private final Map<String, Object> commandMap;
    private boolean found = false;

    /**
     * Constructs a new CommandHandler instance.
     * Initializes an empty command map to store commands.
     */
    public CommandHandler() {
        this.commandMap = new HashMap<>();
    }

    /**
     * Places a command into the command map.
     *
     * @param commandName The name of the command.
     * @param command     The command object to be associated with the name.
     * @throws NullPointerException If the provided command is null.
     */
    public void placeCommand(String commandName, Command command) {
        if (command != null) {
            commandMap.put(commandName, command);
        } else {
            throw new NullPointerException("command is null");
        }
    }

    /**
     * Finds and executes a command based on the provided command name.
     *
     * @param params A map of parameters, containing the command name under the "header" key.
     */
    public void findCommand(Map<String, Object> params) {
        String command = (String) params.get("header");
        if (commandMap.containsKey(command)) {
            found = true;
            Command commandObject = (Command) commandMap.get(command);
            commandObject.executeCommand(params);
        } else {
            log.error("could not find this command: " + command);
        }
    }

}
