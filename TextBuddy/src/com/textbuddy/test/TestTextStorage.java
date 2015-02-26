package com.textbuddy.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.textbuddy.main.TextStorage;

public class TestTextStorage {

	static final String LINE_1 = "line1";
	static final String LINE_2 = "line2";
	static final String LINE_3 = "line3";
	
	TextStorage textStorage;
	List<String> list;
	
	@Before
	public void setupTextStorage () throws IOException {
		textStorage = new TextStorage("testTextStorage.txt");
		textStorage.addLine(LINE_1);
		textStorage.addLine(LINE_2);
		textStorage.addLine(LINE_3);
	}
	
	@Before
	public void setupLists () {
		list = new ArrayList<String>();
		list.add(LINE_1);
		list.add(LINE_2);
		list.add(LINE_3);
	}
	
	@After
	public void clearTextStorage () throws IOException {
		textStorage.clearFile();
	}

	@Test(expected=IOException.class)
	public void testTextStorageBlank () throws IOException {
		new TextStorage("");
	}
	
	@Test(expected=IOException.class)
	public void testTextStorageSpace () throws IOException {
		new TextStorage(" ");
	}

	@Test
	public void testGetLines() throws IOException {	
		assertEquals(list, textStorage.getLines());
	}

	@Test
	public void testIsValid1 () throws IOException {
		assertEquals(true, textStorage.isValidLineNumber(1, list));
	}

	@Test
	public void testIsValid0 () throws IOException {
		assertEquals(false, textStorage.isValidLineNumber(0, list));
	}
	
	@Test
	public void testIsValidNeg1 () throws IOException {
		assertEquals(false, textStorage.isValidLineNumber(-1, list));
	}
	
	@Test
	public void testIsValid3 () throws IOException {
		assertEquals(true, textStorage.isValidLineNumber(3, list));
	}

	@Test
	public void testIsValid2 () throws IOException {
		assertEquals(true, textStorage.isValidLineNumber(2, list));
	}
	
	@Test
	public void testIsValid4 () throws IOException {
		assertEquals(false, textStorage.isValidLineNumber(4, list));
	}
	
	@Test
	public void testIsValid100 () throws IOException {
		assertEquals(false, textStorage.isValidLineNumber(100, list));
	}
}
