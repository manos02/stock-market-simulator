package nl.rug.aoop.command;

/**
 * Interface for creating a command handler.
 */
public interface AbstractCommandHandler {
    /**
     * Adds the commands to the command handler.
     *
     * @return the command Handler.
     */
    CommandHandler createCommandHandler();

}
