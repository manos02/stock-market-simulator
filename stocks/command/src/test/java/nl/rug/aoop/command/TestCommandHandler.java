package nl.rug.aoop.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestCommandHandler {
    CommandHandler commandHandler = null;
    @BeforeEach
    void setUp() {
        commandHandler = new CommandHandler();
    }

    @Test
    void testCommandHandlerConstructor() {
        assertNotNull(commandHandler);
    }

    @Test
    void testHashMapEmpty() {
        assertTrue(commandHandler.getCommandMap().isEmpty());
    }

    @Test
    void testPlacingSingleCommand() {
        Command command = Mockito.mock(Command.class);
        commandHandler.placeCommand("command", command);
        assertEquals(1, commandHandler.getCommandMap().size());
    }

    @Test
    void testPlacingMultipleCommands() {
        Command command = Mockito.mock(Command.class);
        Command command1 = Mockito.mock(Command.class);
        commandHandler.placeCommand("command", command);
        commandHandler.placeCommand("command1", command1);
        assertEquals(2, commandHandler.getCommandMap().size());
    }

    @Test
    void testPlacingNullCommand() {
        assertThrows(NullPointerException.class, () -> commandHandler.placeCommand("nullCommand", null));
    }

    @Test
    void testCommand() {
        Command command = Mockito.mock(Command.class);
        commandHandler.placeCommand("command", command);
        assertEquals(command, commandHandler.getCommandMap().get("command"));
    }


    @Test
    void testFindingCommand() {
        Map<String, Object> commandMap = new HashMap<>();
        Command command = Mockito.mock(Command.class);
        commandMap.put("header", "testCommand");
        commandMap.put("body", "TestMessage");
        commandHandler.placeCommand("testCommand", command);
        commandHandler.findCommand(commandMap);
        assertTrue(commandHandler.isFound());
    }

    @Test
    void testFindingWrongCommand() {
        Map<String, Object> commandMap = new HashMap<>();
        Command command = Mockito.mock(Command.class);
        commandMap.put("header", "wrongCommand");
        commandMap.put("body", "TestMessage");
        commandHandler.placeCommand("CorrectCommand", command);
        commandHandler.findCommand(commandMap);
        assertFalse(commandHandler.isFound());
    }


}
