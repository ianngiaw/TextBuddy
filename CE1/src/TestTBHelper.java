import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestTBHelper {
	
	private final ByteArrayOutputStream sysout = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errout = new ByteArrayOutputStream();

	@Before
	public void setUpStreams () {
		System.setOut(new PrintStream(sysout));
		System.setErr(new PrintStream(errout));
	}
	
	@Test
	public void testGetFirstWord() {
		String result;
		try {
			result = (String) invokeStaticMethod (TextBuddyLogic.class,
												"getFirstWord",
												new Class[]{String.class},
												new String[]{"first word"});
		} catch (InvocationTargetException e) {
			result = null;
		}
		assertEquals("first", result);
		
		try {
			result = (String) invokeStaticMethod (TextBuddyLogic.class,
												"getFirstWord",
												new Class[]{String.class},
												new String[]{"    first"});
		} catch (InvocationTargetException e) {
			result = null;
		}
		assertEquals("first", result);
		
		try {
			result = (String) invokeStaticMethod (TextBuddyLogic.class,
												"getFirstWord",
												new Class[]{String.class},
												new String[]{""});
		} catch (InvocationTargetException e) {
			result = null;
		}
		assertEquals("", result);
		
		try {
			result = (String) invokeStaticMethod (TextBuddyLogic.class,
												"getFirstWord",
												new Class[]{String.class},
												new String[]{"first    "});
		} catch (InvocationTargetException e) {
			result = null;
		}
		assertEquals("first", result);
	}
	
	@Test
	public void testRemoveFirstWord() {
		String result;
		try {
			result = (String) invokeStaticMethod (TextBuddyLogic.class, "removeFirstWord",
												  new Class[]{String.class},
												  new String[]{"first word"});
			
		} catch (InvocationTargetException e) {
			result = null;
		}
		assertEquals("word", result);
		
		try {
			result = (String) invokeStaticMethod (TextBuddyLogic.class, "removeFirstWord",
												  new Class[]{String.class},
												  new String[]{"word"});
			
		} catch (InvocationTargetException e) {
			result = null;
		}
		assertEquals("", result);
	
		try {
			result = (String) invokeStaticMethod (TextBuddyLogic.class, "removeFirstWord",
												  new Class[]{String.class},
												  new String[]{"     word"});
			
		} catch (InvocationTargetException e) {
			result = null;
		}
		assertEquals("", result);
		
		try {
			result = (String) invokeStaticMethod (TextBuddyLogic.class, "removeFirstWord",
												  new Class[]{String.class},
												  new String[]{"    "});
			
		} catch (InvocationTargetException e) {
			result = null;
		}
		assertEquals("", result);
		
		try {
			result = (String) invokeStaticMethod (TextBuddyLogic.class, "removeFirstWord",
												  new Class[]{String.class},
												  new String[]{"a very very long string"});
			
		} catch (InvocationTargetException e) {
			result = null;
		}
		assertEquals("very very long string", result);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Object invokeStaticMethod (Class targetClass, String methodName,
											Class[] argClasses, Object[] argObjects)
											throws InvocationTargetException {
		try {
			Method method = targetClass.getDeclaredMethod(methodName, argClasses);
			method.setAccessible(true);
			return method.invoke(null, argObjects);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	@After
	public void cleanUpStreams () {
		System.setOut(null);
		System.setErr(null);
	}
}
