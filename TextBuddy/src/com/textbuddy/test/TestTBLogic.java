package com.textbuddy.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.textbuddy.main.TextBuddyLogic;

public class TestTBLogic {
	TextBuddyLogic logic = new TextBuddyLogic("mytextfile.txt");
	
	@Test
	public void testExecuteSearchCommand () {
		logic.executeCommand("clear");
		
		logic.executeCommand("add line1");
		logic.executeCommand("add line2");
		logic.executeCommand("add line3");
		
		String searchBlankResponse = logic.executeCommand("search");
		assertEquals("no search terms", searchBlankResponse);
		
		String searchNoMatchResponse = logic.executeCommand("search z");
		assertEquals("no match found for \"z\" in mytextfile.txt", searchNoMatchResponse);
		
		String searchLineResponse = logic.executeCommand("search line");
		assertEquals("1. line1\n2. line2\n3. line3", searchLineResponse);

		String search2Response = logic.executeCommand("search 2");
		assertEquals("2. line2", search2Response);
		
		logic.executeCommand("clear");
	}
	
	@Test
	public void testExecuteSortCommand () {
		logic.executeCommand("clear");
		
		String sortEmptyResponse = logic.executeCommand("sort");
		assertEquals("mytextfile.txt is empty", sortEmptyResponse);
		
		logic.executeCommand("add d");
		logic.executeCommand("add c");
		logic.executeCommand("add b");
		logic.executeCommand("add a");
		logic.executeCommand("add ab");
		logic.executeCommand("add aa");
		logic.executeCommand("add ad");
		logic.executeCommand("add ac");
		
		String sortResponse = logic.executeCommand("sort");
		assertEquals("mytextfile.txt has been sorted alphabetically", sortResponse);
		
		logic.executeCommand("clear");
	}
}
