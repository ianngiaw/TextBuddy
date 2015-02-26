package com.textbuddy.test;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.textbuddy.main.Command;
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
	
	// tests for private methods
	
	@Test
	public void testExecuteSearchCommandBlank () throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Command command = Command.parseCommand("search");
		Method executeSearchCommand = getPrivateMethod(logic.getClass(), "executeSearchCommand", new Class[]{Command.class});
		String response = (String) executeSearchCommand.invoke(logic, new Object[]{command});
		assertEquals("no search terms", response);
	}
	
	@Test
	public void testExecuteSearchCommandD () throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Command command = Command.parseCommand("search d");
		Method executeSearchCommand = getPrivateMethod(logic.getClass(), "executeSearchCommand", new Class[]{Command.class});
		String response = (String) executeSearchCommand.invoke(logic, new Object[]{command});
		assertEquals("1. d\n7. ad", response);
	}
	
	@Test
	public void testExecuteSearchCommandAa () throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Command command = Command.parseCommand("search aa");
		Method executeSearchCommand = getPrivateMethod(logic.getClass(), "executeSearchCommand", new Class[]{Command.class});
		String response = (String) executeSearchCommand.invoke(logic, new Object[]{command});
		assertEquals("6. aa", response);
	}
	
	@Test
	public void testExecuteSortCommand () throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method executeSortCommand = getPrivateMethod(logic.getClass(), "executeSortCommand", null);
		String response = (String) executeSortCommand.invoke(logic, new Object[]{});
		assertEquals("testTBLogic.txt has been sorted alphabetically", response);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Method getPrivateMethod (Class privateObjectClass, String methodName, Class[] argClasses) {
		Method privateMethod;
		try {
			privateMethod = privateObjectClass.getDeclaredMethod(methodName, argClasses);
		} catch (NoSuchMethodException e) {
			return null;
		} catch (SecurityException e) {
			return null;
		}
		privateMethod.setAccessible(true);
		return privateMethod;
	}
}
