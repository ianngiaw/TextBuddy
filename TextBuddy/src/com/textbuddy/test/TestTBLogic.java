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
	
	@Test
	public void testExecuteCommand () {
		logic.executeCommand("clear");
		
		String add1Response = logic.executeCommand("add line1");
		assertEquals("added to mytextfile.txt: \"line1\"", add1Response);
		
		String add2Response = logic.executeCommand("   add line2   ");
		assertEquals("added to mytextfile.txt: \"line2\"", add2Response);
		
		String add3Response = logic.executeCommand("   add    line3   ");
		assertEquals("added to mytextfile.txt: \"line3\"", add3Response);
		
		String displayResponse = logic.executeCommand("display");
		assertEquals("1. line1\n2. line2\n3. line3", displayResponse);
		
		String display1Response = logic.executeCommand("display 1");
		assertEquals("1. line1\n2. line2\n3. line3", display1Response);
		
		String deleteInvalidResponse = logic.executeCommand("delete");
		assertEquals("invalid command format: \"delete\"", deleteInvalidResponse);
		
		String deleteInvalidLineResponse = logic.executeCommand("delete 5");
		assertEquals("Line 5 does not exist", deleteInvalidLineResponse);
		
		String deleteResponse = logic.executeCommand("delete 2");
		assertEquals("deleted from mytextfile.txt: \"line2\"", deleteResponse);
		
		String clearResponse = logic.executeCommand("clear");
		assertEquals("all content deleted from mytextfile.txt", clearResponse);

		String clear1Response = logic.executeCommand("clear 1");
		assertEquals("all content deleted from mytextfile.txt", clear1Response);
	}
}
