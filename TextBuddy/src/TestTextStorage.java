import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;


public class TestTextStorage {
	
	TextStorage textStorage = new TextStorage("testTextStorage.txt");;
	
	@After
	public void clearTextStorage () throws IOException {
		textStorage.clearFile();
	}

	@Test
	public void testTextStorage() {
		TextStorage blank = new TextStorage("");
		assertEquals(false, blank.isUsable());
		
		TextStorage space = new TextStorage(" ");
		assertEquals(false, space.isUsable());
	}

	@Test
	public void testIsUsable() {
		assertEquals(true, textStorage.isUsable());
	}

	@Test
	public void testGetLines() throws IOException {
		List<String> list = new ArrayList<String>();
		String line1 = "line1";
		String line2 = "line2";
		String line3 = "line3";
		
		list.add(line1);
		list.add(line2);
		list.add(line3);
		
		textStorage.addLine(line1);
		textStorage.addLine(line2);
		textStorage.addLine(line3);
	
		assertEquals(list, textStorage.getLines());
	}

	@Test
	public void testIsValidLineNumber() throws IOException {
		List<String> list = new ArrayList<String>();
		String line1 = "line1";
		String line2 = "line2";
		String line3 = "line3";

		list.add(line1);
		list.add(line2);
		list.add(line3);
		
		assertEquals(true, textStorage.isValidLineNumber(1, list));
		assertEquals(false, textStorage.isValidLineNumber(0, list));
		assertEquals(false, textStorage.isValidLineNumber(-1, list));
		assertEquals(true, textStorage.isValidLineNumber(3, list));
		assertEquals(true, textStorage.isValidLineNumber(2, list));
		assertEquals(false, textStorage.isValidLineNumber(4, list));
		assertEquals(false, textStorage.isValidLineNumber(100, list));
	}

}
