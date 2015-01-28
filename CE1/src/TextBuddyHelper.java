import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is a helper class for the TextBuddy class which is
 * created by the main method of the TextBuddy class
 * 
 * @author Ngiaw Ting An, Ian
 *
 */
public class TextBuddyHelper {
	
	// Messages printed
	private static final String MESSAGE_ADD_FAILURE = "failed adding \"%2$s\" to %1$s";
	private static final String MESSAGE_ADD_SUCCESS = "added to %1$s: \"%2$s\"";
	private static final String MESSAGE_CLEAR_FAILURE = "an error had occurred while trying to clear %1$s";
	private static final String MESSAGE_CLEAR_SUCCESS = "all content deleted from %1$s";
	private static final String MESSAGE_DELETE_SUCCESS = "deleted from %1$s: \"%2$s\"";
	private static final String MESSAGE_FILE_EMPTY = "%1$s is empty";
	private static final String MESSAGE_FILE_INITIALIZATION_ERROR = "Problem in file initialization";
	private static final String MESSAGE_FILE_READY = "%1$s is ready for use";
	private static final String MESSAGE_INVALID_COMMAND = "invalid command format: \"%1$s\"";
	private static final String MESSAGE_LINE_NUMBER_ERROR = "Line %1$d does not exist";
	
	// Format of each line printed in the display command
	private static final String LINE_FORMAT = "%1$d. %2$s";
	
	// Valid commands types a user may input
	private static final String ADD_COMMAND = "add";
	private static final String CLEAR_COMMAND = "clear";
	private static final String DELETE_COMMAND = "delete";
	private static final String DISPLAY_COMMAND = "display";
	private static final String EXIT_COMMAND = "exit";
	
	// Instance variables
	private File _textFile;
	private String _fileName;
	
	public TextBuddyHelper (String fileName) {
		_fileName = fileName;
		setEnvironment();
	}
	
	/**
	 * Prepares the text file to be edited. To be called only by constructor.
	 * 
	 * @param fileName The name of the file to be edited
	 * @return A File Object of the provided file name
	 */
	private void setEnvironment () {
		initializeTextFile();
		printFileReadyMessage(_fileName);
	}

	/**
	 * Creates a new java File Object based on the file name provided
	 * If the file does not exist, it will create a new file with the
	 * provided name.
	 * 
	 * @param fileName The name of the file to be edited
	 * @return java File Object of the file to be edited
	 */
	private void initializeTextFile () {
		_textFile = new File(_fileName);
		try {
			_textFile.createNewFile();
		} catch (IOException e) {
			TextBuddy.exitWithErrorMessage(MESSAGE_FILE_INITIALIZATION_ERROR);
		}
	}
	
	/**
	 * This method chooses the appropriate actions to carry out based on
	 * the user's input command(s)
	 * 
	 * @param textFile File Object of the text file to be edited
	 * @param fileName Name of the file to be edited
	 * @param userCommand Input provided by the user
	 */
	public void executeCommand (String userCommand) {
		String commandType = getFirstWord(userCommand);
		switch (commandType) {
			case ADD_COMMAND :
				executeAddCommand(userCommand);
				break;
			case CLEAR_COMMAND :
				executeClearCommand();
				break;
			case DELETE_COMMAND :
				executeDeleteCommand(userCommand);
				break;
			case DISPLAY_COMMAND :
				executeDisplayCommand();
				break;
			case EXIT_COMMAND :
				TextBuddy.exitProgram();
				return;
			default :
				printInvalidCommand(userCommand);
				break;
		}
	}
	
	/**
	 * Executes the add command, which adds a line to the end
	 * of the text file
	 * 
	 * @param userCommand The full command input by the user
	 */
	private void executeAddCommand (String userCommand) {
		String textToAdd = removeFirstWord(userCommand);
		addLine(textToAdd);
	}
	
	/**
	 * Executes the clear command, which clears the entire text file
	 */
	private void executeClearCommand () {
		try {
			clearAllFromFile(_textFile);
			printClearSuccess(_fileName);
		} catch (IOException e) {
			printClearFailure(_fileName);
		}
	}
	
	/**
	 * Executes the delete command, which deletes specified line from
	 * the text file
	 * 
	 * @param userCommand The command input by the user
	 */
	private void executeDeleteCommand (String userCommand) {
		String commandArgument = removeFirstWord(userCommand);
		try {
			int lineToDelete = Integer.parseInt(commandArgument);
			deleteLine(lineToDelete);
		} catch (NumberFormatException e) {
			printInvalidCommand(userCommand);
		}
	}

	/**
	 * Executes the display command, which displays each line in the
	 * text file
	 */
	private void executeDisplayCommand () {
		List<String> lines = getLineList(_textFile);
		displayLines(lines);
	}
	
	/**
	 * Adds a line to the text file, printing a message if the add
	 * command was executed successfully and a message if it failed
	 * 
	 * @param line The line to be added to the end of the _textFile
	 */
	private void addLine (String line) {
		try {
			addLineToFile(_textFile, line);
			printAddSuccess(_fileName, line);
		} catch (IOException e) {
			printAddFailure(_fileName, line);
		}
	}

	/**
	 * Iterates through a list of strings printing out each string
	 * beginning with its index number
	 * 
	 * @param lineList A list of strings to be printed
	 */
	private void displayLines (List<String> lineList) {
		Iterator<String> iterator = lineList.iterator();
		int lineCount = 0;
		while (iterator.hasNext()) {
			printLine(++lineCount, iterator.next());
		}
		if (lineCount == 0) {
			printFileEmptyMessage(_fileName);
		}
	}
	
	/**
	 * Deletes a specified line from _textFile
	 * 
	 * @param lineNumber The line to be deleted
	 */
	private void deleteLine (int lineNumber) {
		List<String> lines = getLineList(_textFile);
		String removedLine;
		if (isValidLineNumber(lineNumber, lines)) {
			removedLine = lines.remove(lineNumber - 1);
		} else {
			printLineNumberError(lineNumber);
			return;
		}
		try {
			saveLinesToFile(_textFile, lines);
			printLineRemoveSuccess(_fileName, removedLine);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Static methods
	
	/**
	 * Adds a string of text to the end of a file
	 * 
	 * @param textFile The file to edited
	 * @param text The string to be added 
	 * @throws IOException
	 */
	private static void addLineToFile (File textFile, String text) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(textFile, true)));
		out.println(text);
		out.close();
	}

	/**
	 * Clears an entire file of its contents
	 * 
	 * @param textFile The file to be cleared
	 * @throws IOException
	 */
	private static void clearAllFromFile (File textFile) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(textFile, false)));
		out.print("");
		out.close();
	}
	
	/**
	 * Takes in an text file and converts each line in the text
	 * to a string, storing each string in a List Object
	 * 
	 * @param textFile File Object where the text is stored
	 * @return List Object containing each line of the text file
	 */
	private static List <String> getLineList (File textFile) {
		List<String> lines = new ArrayList<String>();
		String currentLine;
		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new FileReader(textFile));
			while ((currentLine = bufferedReader.readLine()) != null) {
				lines.add(currentLine);
			}
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();	
		}
		return lines;
	}
	
	private static boolean isValidLineNumber (int lineNumber, List<String> textList) {
		boolean isLessThanOrEqualToList = lineNumber <= textList.size();
		boolean isMoreThanZero = lineNumber > 0;
		return isLessThanOrEqualToList && isMoreThanZero;
	}

	/**
	 * Clears the contents of a text file, then saves each string
	 * in a list of strings as a new line in a text file
	 * 
	 * @param textFile The text file to be edited
	 * @param textList A list of strings to be added to the text file
	 * @throws IOException
	 */
	private static void saveLinesToFile (File textFile, List<String> textList) throws IOException {
		// Used PrintWriter as it more user friendly
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(textFile, false)));
		Iterator<String> iterator = textList.iterator();
		while (iterator.hasNext()) {
			out.println(iterator.next());
		}
		out.close();
	}
	
	// Text operations
	
	private static String getFirstWord (String userCommand) {
		String commandType = userCommand.trim().split("\\s+")[0];
		return commandType;
	}

	private static String removeFirstWord (String userCommand) {
		return userCommand.replace(getFirstWord(userCommand), "").trim();
	}
	
	// Print methods
	
	private static void printAddSuccess (String fileName, String text) {
		String successMessage = String.format(MESSAGE_ADD_SUCCESS, fileName, text);
		System.out.println(successMessage);
	}

	private static void printAddFailure (String fileName, String text) {
		String failureMessage = String.format(MESSAGE_ADD_FAILURE, fileName, text);
		System.out.println(failureMessage);
	}

	private static void printClearSuccess (String fileName) {
		String clearSuccessMessage = String.format(MESSAGE_CLEAR_SUCCESS, fileName);
		System.out.println(clearSuccessMessage);
	}
	
	private static void printClearFailure (String fileName) {
		String clearFailureMessage = String.format(MESSAGE_CLEAR_FAILURE, fileName);
		System.out.println(clearFailureMessage);
	}
	
	private static void printLine (int lineNumber, String line) {
		String formattedLine = String.format(LINE_FORMAT, lineNumber, line);
		System.out.println(formattedLine);
	}
	
	private static void printLineNumberError (int lineNumber) {
		String message = String.format(MESSAGE_LINE_NUMBER_ERROR, lineNumber);
		System.out.println(message);
	}

	private static void printLineRemoveSuccess (String fileName, String removedLine) {
		String successMessage = String.format(MESSAGE_DELETE_SUCCESS, fileName, removedLine);
		System.out.println(successMessage);
	}
	
	private static void printFileEmptyMessage (String fileName) {
		String message = String.format(MESSAGE_FILE_EMPTY, fileName);
		System.out.println(message);
	}
	
	private static void printFileReadyMessage (String fileName){
		String message = String.format(MESSAGE_FILE_READY, fileName);
		System.out.println(message);
	}
	
	private static void printInvalidCommand (String userCommand) {
		String message = String.format(MESSAGE_INVALID_COMMAND, userCommand);
		System.out.println(message);
	}
}