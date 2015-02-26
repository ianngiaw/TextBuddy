package com.textbuddy.test;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.textbuddy.main.TextBuddyLogic;

public class TestTBLogic {
	TextBuddyLogic logic = new TextBuddyLogic("testTBLogic.txt");
	
	@Before
	public void setupLogic () {
		logic.executeCommand("add d");
		logic.executeCommand("add c");
		logic.executeCommand("add b");
		logic.executeCommand("add a");
		logic.executeCommand("add ab");
		logic.executeCommand("add aa");
		logic.executeCommand("add ad");
		logic.executeCommand("add ac");
	}
	
	@After
	public void tearDownLogic () {
		logic.executeCommand("clear");
	}
	
	@Test
	public void testExecuteCommandSearchBlank () {
		String searchBlankResponse = logic.executeCommand("search");
		assertEquals("no search terms", searchBlankResponse);
	}
	
	@Test
	public void testExecuteCommandSearchZ () {
		String searchZResponse = logic.executeCommand("search z");
		assertEquals("no match found for \"z\" in testTBLogic.txt", searchZResponse);
	}
	
	@Test
	public void testExecuteCommandSearchD () {
		String searchDResponse = logic.executeCommand("search d");
		assertEquals("1. d\n7. ad", searchDResponse);
	}
	
	@Test
	public void testExecuteCommandSearchAa () {
		String searchAaResponse = logic.executeCommand("search aa");
		assertEquals("6. aa", searchAaResponse);
	}
	
	@Test
	public void testExecuteCommandSortEmpty () {
		logic.executeCommand("clear");
		String sortEmptyResponse = logic.executeCommand("sort");
		assertEquals("testTBLogic.txt is empty", sortEmptyResponse);
	}
	
	@Test
	public void testExecuteCommandSortFilled () {
		String sortResponse = logic.executeCommand("sort");
		assertEquals("testTBLogic.txt has been sorted alphabetically", sortResponse);
	}
}
