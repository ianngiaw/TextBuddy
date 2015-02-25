package com.textbuddy.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.textbuddy.main.Command;


public class TestCommand {
	
	@Test
	public void testParseSearchBlank () {
		final Command searchBlankCommand = Command.parseCommand("search");
		assertEquals(Command.CommandType.SEARCH, searchBlankCommand.getCommandType());
		assertEquals("", searchBlankCommand.getCommandArgument());
	}
	
	@Test
	public void testParseSearchSpace () {
		final Command searchSpacedCommand = Command.parseCommand("search     ");
		assertEquals(Command.CommandType.SEARCH, searchSpacedCommand.getCommandType());
		assertEquals("", searchSpacedCommand.getCommandArgument());
	}
	
	@Test
	public void testParseSearch123 () {
		final Command search123Command = Command.parseCommand("search 123");
		assertEquals(Command.CommandType.SEARCH, search123Command.getCommandType());
		assertEquals("123", search123Command.getCommandArgument());
	}
	
	@Test
	public void testParseSearchWrongCase () {
		final Command searchWrongCseCommand = Command.parseCommand("SeArCh");
		assertEquals(Command.CommandType.INVALID, searchWrongCseCommand.getCommandType());
	}
	
	@Test
	public void testParseSortNormal () {
		final Command sortNormal = Command.parseCommand("sort");
		assertEquals(Command.CommandType.SORT, sortNormal.getCommandType());
	}
	
	@Test
	public void testParseSortFrontSpace () {
		final Command sortFrontSpace = Command.parseCommand("     sort");
		assertEquals(Command.CommandType.SORT, sortFrontSpace.getCommandType());
	}
	
	@Test
	public void testParseSortEndSpace () {
		final Command sortEndSpace = Command.parseCommand("sort     ");
		assertEquals(Command.CommandType.SORT, sortEndSpace.getCommandType());
	}
	
	@Test
	public void testParseSortBothSpace () {
		final Command sortBothSpace = Command.parseCommand("     sort     ");
		assertEquals(Command.CommandType.SORT, sortBothSpace.getCommandType());
	}
	
	@Test
	public void testParseSortWrongCase () {
		final Command sortWrongCase = Command.parseCommand("SoRt");
		assertEquals(Command.CommandType.INVALID, sortWrongCase.getCommandType());
	}
}
