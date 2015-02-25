package com.textbuddy.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.textbuddy.main.Command;


public class TestCommand {
	
	@Test
	public void testParseSearchBlank () {
		final Command searchBlank = Command.parseCommand("search");
		assertEquals(Command.CommandType.SEARCH, searchBlank.getCommandType());
		assertEquals("", searchBlank.getCommandArgument());
	}
	
	@Test
	public void testParseSearchSpace () {
		final Command searchSpaced = Command.parseCommand("search     ");
		assertEquals(Command.CommandType.SEARCH, searchSpaced.getCommandType());
		assertEquals("", searchSpaced.getCommandArgument());
	}
	
	@Test
	public void testParseSearch123 () {
		final Command search123 = Command.parseCommand("search 123");
		assertEquals(Command.CommandType.SEARCH, search123.getCommandType());
		assertEquals("123", search123.getCommandArgument());
	}
	
	@Test
	public void testParseSearchWrongCase () {
		final Command searchWrongCase = Command.parseCommand("SeArCh");
		assertEquals(Command.CommandType.INVALID, searchWrongCase.getCommandType());
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
