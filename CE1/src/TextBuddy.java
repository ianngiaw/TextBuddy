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

public class TextBuddy {

	private static final String ADD_SUCCESS_MESSAGE = "added to %1$s: \"%2$s\"";
	private static final String ADD_FAILURE_MESSAGE = "failed adding \"%2$s\" to %1$s";
	private static final String REQUEST_COMMAND_MESSAGE = "command: ";
	private static final String LINE_NUMBER_ERROR_MESSAGE = "Line %1$d does not exist";
	private static final String DELETE_SUCCESS_MESSAGE = "deleted from %1$s: \"%2$s\"";
	private static final String LINE_FORMAT = "%1$d. %2$s";
	private static final String FILE_EMPTY_MESSAGE = "%1$s is empty";
	private static final String INVALID_COMMAND_MESSAGE = "\"%1$s\" is an invalid command";
	private static final String CLEAR_FAILURE_MESSAGE = "an error had occurred while trying to clear %1$s";
	private static final String CLEAR_SUCCESS_MESSAGE = "all content deleted from %1$s";
	private static final String INVALID_COMMAND_ARGUMENT_ERROR_MESSAGE = "\"%1$s\" is an invalid argument for the %2$s command";
	private static final String EXIT_COMMAND = "exit";
	private static final String CLEAR_COMMAND = "clear";
	private static final String DELETE_COMMAND = "delete";
	private static final String DISPLAY_COMMAND = "display";
	private static final String ADD_COMMAND = "add";
	private static final String FILE_READY_MESSAGE = "%1$s is ready for use";
	private static final String FILE_INITIALIZATION_ERROR_MESSAGE = "Problem in file initialization";
	private static final String INVALID_FILE_NAME_ERROR_MESSAGE = "Invalid File Name";
	private static final String NO_ARGUMENTS_ERROR_MESSAGE = "No arguments.";
	private static final String WELCOME_MESSAGE = "Welcome to TextBuddy. ";
	
	private static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) {
		exitIfIncorrectArguments(args);
		printWelcomeMessage();
		String fileName = getFileNameFromArguments(args);
		File textFile = setEnvironment(fileName);
		executeUntilExitCommand(textFile, fileName);
	}

	private static String getFileNameFromArguments(String[] args) {
		return args[0];
	}

	private static void executeUntilExitCommand(File textFile, String fileName) {
		while (true) {
			String userCommand = requestUserCommand();
			executeCommand(textFile, fileName, userCommand);
		}
	}

	private static void executeCommand(File textFile, String fileName, String userCommand) {
		String commandType = getFirstWord(userCommand);
		switch (commandType) {
			case ADD_COMMAND :
				executeAddCommand(textFile, fileName, userCommand);
				break;
			case DISPLAY_COMMAND :
				executeDisplayCommand(textFile, fileName);
				break;
			case DELETE_COMMAND :
				executeDeleteCommand(textFile, fileName, userCommand, commandType);
				break;
			case CLEAR_COMMAND :
				executeClearCommand(textFile, fileName);
				break;
			case EXIT_COMMAND :
				exitProgram();
				return;
			default :
				printInvalidCommandMessage(userCommand);
				break;
		}
	}
	
	private static void printInvalidCommandMessage(String userCommand) {
		String message = String.format(INVALID_COMMAND_MESSAGE, userCommand);
		System.out.println(message);
	}

	private static void executeDisplayCommand(File textFile, String fileName) {
		List<String> lines = getLinesList(textFile);
		displayLines(lines, fileName);
	}

	private static void executeAddCommand(File textFile, String fileName, String userCommand) {
		String textToAdd = removeFirstWord(userCommand);
		addTextToFile(textFile, fileName, textToAdd);
	}

	private static void executeDeleteCommand(File textFile, String fileName,
			String userCommand, String commandType) {
		String commandArgument = removeFirstWord(userCommand);
		try {
			int lineToDelete = Integer.parseInt(commandArgument);
			deleteLine(textFile, fileName, lineToDelete);
		} catch (NumberFormatException e) {
			printInvalidCommandArguments(commandArgument, commandType);
		}
	}

	private static void executeClearCommand(File textFile, String fileName) {
		try {
			clearAllFromFile(textFile);
			printClearSuccess(fileName);
		} catch (IOException e) {
			printClearFailure(fileName);
		}
	}

	private static void clearAllFromFile(File textFile) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(textFile, false)));
		out.print("");
		out.close();
	}

	private static void printClearSuccess(String fileName) {
		String clearSuccessMessage = String.format(CLEAR_SUCCESS_MESSAGE, fileName);
		System.out.println(clearSuccessMessage);
	}
	
	private static void printClearFailure(String fileName) {
		String clearFailureMessage = String.format(CLEAR_FAILURE_MESSAGE, fileName);
		System.out.println(clearFailureMessage);
	}

	private static void printInvalidCommandArguments(String argument, String command) {
		String message = String.format(INVALID_COMMAND_ARGUMENT_ERROR_MESSAGE, argument, command);
		System.out.println(message);
	}
	
	private static String getFirstWord (String userCommand) {
		String commandType = userCommand.trim().split("\\s+")[0];
		return commandType;
	}
	
	private static String removeFirstWord (String userCommand) {
		return userCommand.replace(getFirstWord(userCommand), "").trim();
	}

	private static String requestUserCommand() {
		System.out.print(REQUEST_COMMAND_MESSAGE);
		String userCommand = scanner.nextLine();
		return userCommand;
	}

	private static File setEnvironment(String fileName) {
		File textFile = initializeTextFile(fileName);
		printFileReadyMessage(fileName);
		return textFile;
	}
	
	public static void printFileReadyMessage(String fileName){
		String message = String.format(FILE_READY_MESSAGE, fileName);
		System.out.println(message);
	}

	private static File initializeTextFile(String fileName) {
		File textFile = new File(fileName);
		try {
			textFile.createNewFile();
		} catch (IOException e) {
			exitWithErrorMessage(FILE_INITIALIZATION_ERROR_MESSAGE);
		}
		return textFile;
	}

	private static void exitIfIncorrectArguments(String[] args) {
		exitIfNoArguments(args);
		exitIfInvalidFileName(args);
	}

	private static void printWelcomeMessage() {
		System.out.print(WELCOME_MESSAGE);
	}

	private static void exitWithErrorMessage(String errorMessage) {
		System.out.println(errorMessage);
		exitProgram();
	}
	
	private static void exitProgram() {
		scanner.close();
		System.exit(0);
	}
	
	private static void exitIfNoArguments (String[] args) {
		if (args.length == 0) {
			exitWithErrorMessage(NO_ARGUMENTS_ERROR_MESSAGE);
		}
	}
	
	private static void exitIfInvalidFileName(String[] args){
		String fileName = getFileNameFromArguments(args);
		if (!isValidFileName(fileName)) {
			exitWithErrorMessage(INVALID_FILE_NAME_ERROR_MESSAGE);
		}
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
	
	private static List <String> getLinesList (File textFile) {
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
	
	private static void printLine (int lineNumber, String line) {
		String formattedLine = String.format(LINE_FORMAT, lineNumber, line);
		System.out.println(formattedLine);
	}
	
	private static void printFileEmptyMessage(String fileName) {
		String message = String.format(FILE_EMPTY_MESSAGE, fileName);
		System.out.println(message);
	}
	
	private static void addTextToFile (File textFile, String fileName, String text) {
		try {
			addLine(textFile, text);
			printAddSuccess(fileName, text);
		} catch (IOException e) {
			printAddFailure(fileName, text);
		}
	}

	private static void addLine(File textFile, String text) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(textFile, true)));
		out.println(text);
		out.close();
	}

	private static void printAddSuccess(String fileName, String text) {
		String successMessage = String.format(ADD_SUCCESS_MESSAGE, fileName, text);
		System.out.println(successMessage);
	}
	
	private static void printAddFailure(String fileName, String text) {
		String failureMessage = String.format(ADD_FAILURE_MESSAGE, fileName, text);
		System.out.println(failureMessage);
	}
	
	private static void deleteLine (File textFile, String fileName, int lineNumber) {
		List<String> lines = getLinesList(textFile);
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

	private static void printLineRemoveSuccess(String fileName, String removedLine) {
		String successMessage = String.format(DELETE_SUCCESS_MESSAGE, fileName, removedLine);
		System.out.println(successMessage);
	}

	private static void saveLinesToFile(File textFile, List<String> textList)
			throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(textFile, false)));
		Iterator<String> iterator = textList.iterator();
		while (iterator.hasNext()) {
			out.println(iterator.next());
		}
		out.close();
	}

	private static void printLineNumberError(int lineNumber) {
		String message = String.format(LINE_NUMBER_ERROR_MESSAGE, lineNumber);
		System.out.println(message);
	}

	private static boolean isValidLineNumber(int lineNumber,
			List<String> textList) {
		return lineNumber <= textList.size() && lineNumber > 0;
	}
}