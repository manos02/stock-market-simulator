package nl.rug.aoop.command;

import java.util.Map;

/**
 * Interface for all the commands.
 */

public interface Command {

    /**
     * Abstract implementation of the execution of the command.
     *
     * @param params  map of parameters, containing the object.
     */

    void executeCommand(Map<String, Object> params);
}
