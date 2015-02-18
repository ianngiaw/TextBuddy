import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestTBLogic {
	TextBuddyLogic logic = new TextBuddyLogic("mytextfile.txt");
	
	@Test
	public void testExecuteCommand () {
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
		
		String exitResponse = logic.executeCommand("exit");
		assertEquals(null, exitResponse);
	}
}
