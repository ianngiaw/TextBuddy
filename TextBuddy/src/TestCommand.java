import static org.junit.Assert.*;

import org.junit.Test;


public class TestCommand {

	@Test
	public void testParseCommand() {
		final Command addCommand = Command.parseCommand("add line");
		assertEquals(Command.CommandType.ADD, addCommand.getCommandType());
		assertEquals("line", addCommand.getCommandArgument());
		
		final Command addBlankCommand = Command.parseCommand("add");
		assertEquals(Command.CommandType.ADD, addBlankCommand.getCommandType());
		assertEquals("", addBlankCommand.getCommandArgument());
		
		final Command clearCommand = Command.parseCommand("clear");
		assertEquals(Command.CommandType.CLEAR, clearCommand.getCommandType());
		
		final Command displayCommand = Command.parseCommand("display");
		assertEquals(Command.CommandType.DISPLAY, displayCommand.getCommandType());

		final Command deleteCommand = Command.parseCommand("delete 1");
		assertEquals(Command.CommandType.DISPLAY, deleteCommand.getCommandType());
		assertEquals("1", deleteCommand.getCommandArgument());
		
		final Command exitCommand = Command.parseCommand("exit");
		assertEquals(Command.CommandType.EXIT, exitCommand.getCommandType());
		
		final Command invalidBlankCommand = Command.parseCommand("");
		assertEquals(Command.CommandType.INVALID, invalidBlankCommand.getCommandType());
		
		final Command invalidSpacedCommand = Command.parseCommand(" ");
		assertEquals(Command.CommandType.INVALID, invalidSpacedCommand.getCommandType());
		
		final Command invalidTypedCommand = Command.parseCommand("alksdjlak askldja");
		assertEquals(Command.CommandType.INVALID, invalidTypedCommand.getCommandType());
	}
}
