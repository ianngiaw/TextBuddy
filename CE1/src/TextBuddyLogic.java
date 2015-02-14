import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * This is a helper class for the TextBuddy class which is
 * created by the main method of the TextBuddy class
 * 
 * @author Ngiaw Ting An, Ian
 *
 */
public class TextBuddyLogic {
	
	// Messages printed
	private static final String MESSAGE_ADD_FAILURE = "failed adding \"%2$s\" to %1$s";
	private static final String MESSAGE_ADD_SUCCESS = "added to %1$s: \"%2$s\"";
	private static final String MESSAGE_CLEAR_FAILURE = "an error had occurred while trying to clear %1$s";
	private static final String MESSAGE_CLEAR_SUCCESS = "all content deleted from %1$s";
	private static final String MESSAGE_DELETE_FAILURE = "failed to delete from %1$s";
	private static final String MESSAGE_DELETE_SUCCESS = "deleted from %1$s: \"%2$s\"";
	private static final String MESSAGE_DISPLAY_FAILURE = "failed to display %1$s";
	private static final String MESSAGE_FILE_EMPTY = "%1$s is empty";
	private static final String MESSAGE_FILE_INITIALIZATION_ERROR = "Problem in file initialization";
	private static final String MESSAGE_INVALID_COMMAND = "invalid command format: \"%1$s\"";
	private static final String MESSAGE_LINE_NUMBER_ERROR = "Line %1$d does not exist";
	
	// Format of each line printed in the display command
	private static final String LINE_FORMAT = "%1$d. %2$s";
	
	// Instance variables
	private String _fileName;
	private TextStorage _textStorage;
	
	public TextBuddyLogic (String fileName) {
		_fileName = fileName;
		setupStorage();
	}
	
	/**
	 * Prepares the text file to be edited. To be called only by constructor.
	 * 
	 * @param fileName The name of the file to be edited
	 * @return A File Object of the provided file name
	 */
	private void setupStorage () {
		_textStorage = new TextStorage(_fileName);
		if (_textStorage.isUsable()) {
			TextBuddy.printFileReady(_fileName);
		} else {
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
	public String executeCommand (String userCommand) {
		Command command = Command.parseCommand(userCommand);
		final Command.CommandType commandType = command.getCommandType();
		
		if (commandType == Command.CommandType.ADD) {
			return executeAddCommand(command);			
		} else if (commandType == Command.CommandType.CLEAR) {
			return executeClearCommand();
		} else if (commandType == Command.CommandType.DELETE) {
			return executeDeleteCommand(command);
		} else if (commandType == Command.CommandType.DISPLAY) {
			return executeDisplayCommand();
		} else if (commandType == Command.CommandType.EXIT) {
			TextBuddy.exitProgram();
			return null;
		} else {
			return responseInvalidCommand(userCommand);
		}
	}
	
	private String executeAddCommand (Command command) {
		String line = command.getCommandArgument();
		try {
			_textStorage.addLine(line);
		} catch (IOException e) {
			return responseAddFailure(line);
		}
		return responseAddSuccess(line);
	}
	
	private String executeClearCommand () {
		try {
			_textStorage.clearFile();;
		} catch (IOException e) {
			return responseClearFailure();
		}
		return responseClearSuccess();
	}
	
	private String executeDeleteCommand (Command command) {
		String commandArgument = command.getCommandArgument();
		int lineToDelete = -1;
		try {
			lineToDelete = Integer.parseInt(commandArgument);
		} catch (NumberFormatException e) {
			return responseInvalidCommand(command.getOriginalCommand());
		}
		return deleteLine(lineToDelete);
	}

	/**
	 * Executes the display command, which displays each line in the
	 * text file
	 */
	private String executeDisplayCommand () {
		List<String> lines;
		try {
			lines = _textStorage.getLines();
		} catch (IOException e) {
			return responseDisplayFailure();
		}
		return formatLines(lines);
	}

	private String formatLines (List<String> lineList) {
		String formattedLines = "";
		Iterator<String> iterator = lineList.iterator();
		int lineCount = 0;
		while (iterator.hasNext()) {
			formattedLines += formatLine(++lineCount, iterator.next()) + "\n";
		}
		if (lineCount == 0) {
			return responseFileEmpty();
		} else {
			formattedLines = formattedLines.substring(0, formattedLines.length()-1);
		}
		return formattedLines;
	}
	
	private String deleteLine (int lineNumber) {
		List<String> lines;
		try {
			lines = _textStorage.getLines();
		} catch (IOException e1) {
			return responseLineRemoveFailure();
		}
		String removedLine;
		if (_textStorage.isValidLineNumber(lineNumber, lines)) {
			removedLine = lines.remove(lineNumber - 1);
		} else {
			return responseLineNumberError(lineNumber);
		}
		try {
			_textStorage.saveLinesToFile(lines);
		} catch (IOException e) {
			return responseLineRemoveFailure();
		}
		return responseLineRemoveSuccess(removedLine);
	}
	
	// String formatting methods
	
	private static String formatLine (int lineNumber, String line) {
		return String.format(LINE_FORMAT, lineNumber, line);
	}
	
	private String responseAddSuccess (String line) {
		return String.format(MESSAGE_ADD_SUCCESS, _fileName, line);
	}

	private String responseAddFailure (String line) {
		return String.format(MESSAGE_ADD_FAILURE, _fileName, line);
	}

	private String responseClearSuccess () {
		return String.format(MESSAGE_CLEAR_SUCCESS, _fileName);
	}
	
	private String responseClearFailure () {
		return String.format(MESSAGE_CLEAR_FAILURE, _fileName);
	}
	
	private String responseDisplayFailure () {
		return String.format(MESSAGE_DISPLAY_FAILURE, _fileName);
	}
	
	private String responseLineNumberError (int lineNumber) {
		return String.format(MESSAGE_LINE_NUMBER_ERROR, lineNumber);
	}

	private String responseLineRemoveSuccess (String removedLine) {
		return String.format(MESSAGE_DELETE_SUCCESS, _fileName, removedLine);
	}
	
	private String responseLineRemoveFailure () {
		return String.format(MESSAGE_DELETE_FAILURE, _fileName);
	}
	
	private String responseFileEmpty () {
		return String.format(MESSAGE_FILE_EMPTY, _fileName);
	}
	
	private static String responseInvalidCommand (String userCommand) {
		return String.format(MESSAGE_INVALID_COMMAND, userCommand);
	}
}