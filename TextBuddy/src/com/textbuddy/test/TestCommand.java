package com.textbuddy.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.textbuddy.main.Command;


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
		assertEquals(Command.CommandType.DELETE, deleteCommand.getCommandType());
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
	
	@Test
	public void testParseSearchCommand () {
		final Command searchBlankCommand = Command.parseCommand("search");
		assertEquals(Command.CommandType.SEARCH, searchBlankCommand.getCommandType());
		assertEquals("", searchBlankCommand.getCommandArgument());

		final Command searchSpacedCommand = Command.parseCommand("search     ");
		assertEquals(Command.CommandType.SEARCH, searchSpacedCommand.getCommandType());
		assertEquals("", searchSpacedCommand.getCommandArgument());

		final Command search123Command = Command.parseCommand("search 123");
		assertEquals(Command.CommandType.SEARCH, search123Command.getCommandType());
		assertEquals("123", search123Command.getCommandArgument());
		
		final Command searchWrongCseCommand = Command.parseCommand("SeArCh");
		assertEquals(Command.CommandType.INVALID, searchWrongCseCommand.getCommandType());
	}
	
	@Test
	public void testParseSortCommand () {
		final Command sortCommand = Command.parseCommand("sort");
		assertEquals(Command.CommandType.SORT, sortCommand.getCommandType());
		
		final Command sortFrontSpaceCommand = Command.parseCommand("     sort");
		assertEquals(Command.CommandType.SORT, sortFrontSpaceCommand.getCommandType());

		final Command sortEndSpaceCommand = Command.parseCommand("sort     ");
		assertEquals(Command.CommandType.SORT, sortEndSpaceCommand.getCommandType());
		
		final Command sortBothSpaceCommand = Command.parseCommand("     sort     ");
		assertEquals(Command.CommandType.SORT, sortBothSpaceCommand.getCommandType());
		
		final Command sortWrongCaseCommand = Command.parseCommand("SoRt");
		assertEquals(Command.CommandType.INVALID, sortWrongCaseCommand.getCommandType());
	}
}
