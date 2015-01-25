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
import java.util.Scanner;

/**
 * The TextBuddy program is a simplified command line text file editor 
 * that can add lines to the bottom of text files and can also delete
 * lines from the file.
 * 
 * @author Ngiaw Ting An Ian
 */
public class TextBuddy {

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
	private static final String MESSAGE_INVALID_FILE_NAME_ERROR = "invalid file name provided";
	private static final String MESSAGE_LINE_NUMBER_ERROR = "Line %1$d does not exist";
	private static final String MESSAGE_NO_ARGUMENTS_ERROR = "no file name provided";
	private static final String MESSAGE_REQUEST_COMMAND = "command: ";
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. ";

	// Format of each line printed in the display command
	private static final String LINE_FORMAT = "%1$d. %2$s";
	
	// Valid commands types a user may input
	private static final String ADD_COMMAND = "add";
	private static final String CLEAR_COMMAND = "clear";
	private static final String DELETE_COMMAND = "delete";
	private static final String DISPLAY_COMMAND = "display";
	private static final String EXIT_COMMAND = "exit";
	
	private static Scanner _scanner = new Scanner(System.in);
	
	/**
	 * The main method of the TextBuddy program
	 * 
	 * @param args Command line argument which gives the name of the text file to be edited
	 */
	public static void main (String[] args) {
		exitIfIncorrectArguments(args);
		printWelcomeMessage();
		String fileName = getFileNameFromArguments(args);
		File textFile = setEnvironment(fileName);
		executeUntilExitCommand(textFile, fileName);
	}
	
	private static void exitIfIncorrectArguments (String[] args) {
		exitIfNoArguments(args);
		exitIfInvalidFileName(args);
	}
	
	private static void printWelcomeMessage () {
		System.out.print(MESSAGE_WELCOME);
	}
	
	private static String getFileNameFromArguments (String[] args) {
		return args[0];
	}

	/**
	 * Prepares the text file to be edited.
	 * 
	 * @param fileName The name of the file to be edited
	 * @return A File Object of the provided file name
	 */
	private static File setEnvironment (String fileName) {
		File textFile = initializeTextFile(fileName);
		printFileReadyMessage(fileName);
		return textFile;
	}
	
	/**
	 * A method that runs in an infinite loop requesting for commands
	 * until the user inputs an exit command.
	 * 
	 * @param textFile The file to be edited
	 * @param fileName The name of the file to be edited
	 */
	private static void executeUntilExitCommand (File textFile, String fileName) {
		while (true) {
			String userCommand = requestUserCommand();
			executeCommand(textFile, fileName, userCommand);
		}
	}

	private static void exitIfNoArguments (String[] args) {
		if (args.length == 0) {
			exitWithErrorMessage(MESSAGE_NO_ARGUMENTS_ERROR);
		}
	}
	
	private static void exitIfInvalidFileName (String[] args){
		String fileName = getFileNameFromArguments(args);
		if (!isValidFileName(fileName)) {
			exitWithErrorMessage(MESSAGE_INVALID_FILE_NAME_ERROR);
		}
	}
	
	/**
	 * Creates a new java File Object based on the file name provided
	 * If the file does not exist, it will create a new file with the
	 * provided name.
	 * 
	 * @param fileName The name of the file to be edited
	 * @return java File Object of the file to be edited
	 */
	private static File initializeTextFile (String fileName) {
		File textFile = new File(fileName);
		try {
			textFile.createNewFile();
		} catch (IOException e) {
			exitWithErrorMessage(MESSAGE_FILE_INITIALIZATION_ERROR);
		}
		return textFile;
	}
	
	public static void printFileReadyMessage (String fileName){
		String message = String.format(MESSAGE_FILE_READY, fileName);
		System.out.println(message);
	}
	
	private static String requestUserCommand () {
		System.out.print(MESSAGE_REQUEST_COMMAND);
		String userCommand = _scanner.nextLine();
		return userCommand;
	}
	
	/**
	 * This method chooses the appropriate actions to carry out based on
	 * the user's input command(s)
	 * 
	 * @param textFile File Object of the text file to be edited
	 * @param fileName Name of the file to be edited
	 * @param userCommand Input provided by the user
	 */
	private static void executeCommand (File textFile, String fileName, String userCommand) {
		String commandType = getFirstWord(userCommand);
		switch (commandType) {
			case ADD_COMMAND :
				executeAddCommand(textFile, fileName, userCommand);
				break;
			case CLEAR_COMMAND :
				executeClearCommand(textFile, fileName);
				break;
			case DELETE_COMMAND :
				executeDeleteCommand(textFile, fileName, userCommand, commandType);
				break;
			case DISPLAY_COMMAND :
				executeDisplayCommand(textFile, fileName);
				break;
			case EXIT_COMMAND :
				exitProgram();
				return;
			default :
				printInvalidCommand(userCommand);
				break;
		}
	}
	
	private static void exitWithErrorMessage (String errorMessage) {
		System.out.println(errorMessage);
		exitProgram();
	}
	
	private static boolean isValidFileName (String fileName) {
		File file = new File(fileName);
		try {
			file.getCanonicalPath();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	private static String getFirstWord (String userCommand) {
		String commandType = userCommand.trim().split("\\s+")[0];
		return commandType;
	}
	
	private static void executeAddCommand (File textFile, String fileName, String userCommand) {
		String textToAdd = removeFirstWord(userCommand);
		addTextToFile(textFile, fileName, textToAdd);
	}
	
	private static void executeClearCommand (File textFile, String fileName) {
		try {
			clearAllFromFile(textFile);
			printClearSuccess(fileName);
		} catch (IOException e) {
			printClearFailure(fileName);
		}
	}
	
	private static void executeDeleteCommand (File textFile, String fileName, String userCommand, String commandType) {
		String commandArgument = removeFirstWord(userCommand);
		try {
			int lineToDelete = Integer.parseInt(commandArgument);
			deleteLine(textFile, fileName, lineToDelete);
		} catch (NumberFormatException e) {
			printInvalidCommand(userCommand);
		}
	}

	private static void executeDisplayCommand (File textFile, String fileName) {
		List<String> lines = getLineList(textFile);
		displayLines(lines, fileName);
	}
	
	private static void exitProgram () {
		_scanner.close();
		System.exit(0);
	}
	
	private static void printInvalidCommand (String userCommand) {
		String message = String.format(MESSAGE_INVALID_COMMAND, userCommand);
		System.out.println(message);
	}

	private static String removeFirstWord (String userCommand) {
		return userCommand.replace(getFirstWord(userCommand), "").trim();
	}
	
	private static void addTextToFile (File textFile, String fileName, String text) {
		try {
			addLine(textFile, text);
			printAddSuccess(fileName, text);
		} catch (IOException e) {
			printAddFailure(fileName, text);
		}
	}
	
	private static void clearAllFromFile (File textFile) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(textFile, false)));
		out.print("");
		out.close();
	}

	private static void printClearSuccess (String fileName) {
		String clearSuccessMessage = String.format(MESSAGE_CLEAR_SUCCESS, fileName);
		System.out.println(clearSuccessMessage);
	}
	
	private static void printClearFailure (String fileName) {
		String clearFailureMessage = String.format(MESSAGE_CLEAR_FAILURE, fileName);
		System.out.println(clearFailureMessage);
	}
	
	private static void deleteLine (File textFile, String fileName, int lineNumber) {
		List<String> lines = getLineList(textFile);
		String removedLine;
		if (isValidLineNumber(lineNumber, lines)) {
			removedLine = lines.remove(lineNumber - 1);
		} else {
			printLineNumberError(lineNumber);
			return;
		}
		try {
			saveLinesToFile(textFile, lines);
			printLineRemoveSuccess(fileName, removedLine);
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	private static void displayLines (List<String> textList, String fileName) {
		Iterator<String> iterator = textList.iterator();
		int lineCount = 0;
		while (iterator.hasNext()) {
			printLine(++lineCount, iterator.next());
		}
		if (lineCount == 0) {
			printFileEmptyMessage(fileName);
		}
	}

	private static void addLine (File textFile, String text) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(textFile, true)));
		out.println(text);
		out.close();
	}
	
	private static void printAddSuccess (String fileName, String text) {
		String successMessage = String.format(MESSAGE_ADD_SUCCESS, fileName, text);
		System.out.println(successMessage);
	}

	private static void printAddFailure (String fileName, String text) {
		String failureMessage = String.format(MESSAGE_ADD_FAILURE, fileName, text);
		System.out.println(failureMessage);
	}

	private static boolean isValidLineNumber (int lineNumber, List<String> textList) {
		boolean isLessThanOrEqualToList = lineNumber <= textList.size();
		boolean isMoreThanZero = lineNumber > 0;
		return isLessThanOrEqualToList && isMoreThanZero;
	}
	
	private static void printLineNumberError (int lineNumber) {
		String message = String.format(MESSAGE_LINE_NUMBER_ERROR, lineNumber);
		System.out.println(message);
	}
	
	private static void saveLinesToFile (File textFile, List<String> textList) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(textFile, false)));
		Iterator<String> iterator = textList.iterator();
		while (iterator.hasNext()) {
			out.println(iterator.next());
		}
		out.close();
	}

	private static void printLineRemoveSuccess (String fileName, String removedLine) {
		String successMessage = String.format(MESSAGE_DELETE_SUCCESS, fileName, removedLine);
		System.out.println(successMessage);
	}
	
	private static void printLine (int lineNumber, String line) {
		String formattedLine = String.format(LINE_FORMAT, lineNumber, line);
		System.out.println(formattedLine);
	}
	
	private static void printFileEmptyMessage (String fileName) {
		String message = String.format(MESSAGE_FILE_EMPTY, fileName);
		System.out.println(message);
	}
}