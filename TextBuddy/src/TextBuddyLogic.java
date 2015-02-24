import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class TextBuddyLogic {
	
	// Unformatted response messages returned
	
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
	private static final String MESSAGE_SEARCH_NO_TERMS = "no search terms";
	private static final String MESSAGE_SEARCH_NO_MATCH = "no match found for \"%1$s\" in %2$s";
	private static final String MESSAGE_SEARCH_FAILURE = "search failed";
	private static final String MESSAGE_SORT_FAILURE = "sort failed";
	private static final String MESSAGE_SORT_SUCCESS = "%1$s has been sorted alphabetically";
	
	// Format of each line printed in the display or search command
	
	private static final String LINE_FORMAT = "%1$d. %2$s";
	
	// Instance variables
	
	private String _fileName;
	private TextStorage _textStorage;
	
	public TextBuddyLogic (String fileName) {
		_fileName = fileName;
		setUpStorage();
	}
	
	private void setUpStorage () {
		_textStorage = new TextStorage(_fileName);
		if (_textStorage.isUsable()) {
			TextBuddy.printFileReady(_fileName);
		} else {
			TextBuddy.exitWithErrorMessage(MESSAGE_FILE_INITIALIZATION_ERROR);
		}
	}
	
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
		} else if (commandType == Command.CommandType.SEARCH) {
			return executeSearchCommand(command);
		} else if (commandType == Command.CommandType.SORT) {
			return executeSortCommand();
		} else {
			return formatResponseInvalidCommand(userCommand);
		}
	}
	
	// Execution methods for each specified command

	private String executeAddCommand (Command command) {
		String line = command.getCommandArgument();
		try {
			_textStorage.addLine(line);
		} catch (IOException e) {
			return formatResponseAddFailure(line);
		}
		return formatResponseAddSuccess(line);
	}
	
	private String executeClearCommand () {
		try {
			_textStorage.clearFile();;
		} catch (IOException e) {
			return formatResponseClearFailure();
		}
		return formatResponseClearSuccess();
	}
	
	private String executeDeleteCommand (Command command) {
		String commandArgument = command.getCommandArgument();
		int lineToDelete = -1;
		try {
			lineToDelete = Integer.parseInt(commandArgument);
		} catch (NumberFormatException e) {
			return formatResponseInvalidCommand(command.getOriginalCommand());
		}
		return deleteLine(lineToDelete);
	}

	private String executeDisplayCommand () {
		List<String> lines;
		try {
			lines = _textStorage.getLines();
		} catch (IOException e) {
			return formatResponseDisplayFailure();
		}
		return formatDisplayLines(lines);
	}

	private String executeSearchCommand(Command command) {
		// Response if no search terms are given
		if (command.getCommandArgument().equals("")) {
			return MESSAGE_SEARCH_NO_TERMS;
		}
		List<String> lines;
		try {
			lines = _textStorage.getLines();
		} catch (IOException e) {
			return MESSAGE_SEARCH_FAILURE;
		}
		return formatSearchResults(lines, command.getCommandArgument());
	}

	private String executeSortCommand() {
		List<String> lines;
		try {
			lines = _textStorage.getLines();
		} catch (IOException e) {
			return MESSAGE_SORT_FAILURE;
		}
		
		// Response if there is nothing to sort
		if (lines.isEmpty()) {
			return formatResponseFileEmpty();
		}
		
		lines.sort(null);
		try {
			_textStorage.saveLinesToFile(lines);
		} catch (IOException e) {
			return MESSAGE_SORT_FAILURE;
		}
		
		return formatResponseSortSuccess();
	}
	
	private String deleteLine (int lineNumber) {
		List<String> lines;
		try {
			lines = _textStorage.getLines();
		} catch (IOException e1) {
			return formatResponseLineRemoveFailure();
		}
		
		String removedLine;
		if (_textStorage.isValidLineNumber(lineNumber, lines)) {
			removedLine = lines.remove(lineNumber - 1);
		} else {
			return formatResponseLineNumberError(lineNumber);
		}
		
		try {
			_textStorage.saveLinesToFile(lines);
		} catch (IOException e) {
			return formatResponseLineRemoveFailure();
		}
		
		return formatResponseLineRemoveSuccess(removedLine);
	}

	private String formatDisplayLines (List<String> lines) {
		String formattedLines = "";
		Iterator<String> iterator = lines.iterator();
		
		int lineCount = 0;
		while (iterator.hasNext()) {
			formattedLines += formatLine(++lineCount, iterator.next()) + "\n";
		}
		
		if (lineCount == 0) {
			return formatResponseFileEmpty();
		} else {
			formattedLines = formattedLines.substring(0, formattedLines.length()-1);
		}
		
		return formattedLines;
	}

	private String formatSearchResults (List<String> lines, String searchTerm) {
		String searchResults = "";
		Iterator<String> iterator = lines.iterator();
		
		int lineCount = 1;
		while (iterator.hasNext()) {
			String line = iterator.next();
			if (line.contains(searchTerm)) {
				searchResults += formatLine(lineCount, line) + "\n";
			}
			lineCount++;
		}
		
		if (searchResults.equals("")) {
			return formatResponseSearchNoMatch(searchTerm);
		}
		
		return searchResults.substring(0, searchResults.length()-1);
	}
	
	// String formatting methods
	
	private static String formatLine (int lineNumber, String line) {
		return String.format(LINE_FORMAT, lineNumber, line);
	}
	
	private String formatResponseAddSuccess (String line) {
		return String.format(MESSAGE_ADD_SUCCESS, _fileName, line);
	}

	private String formatResponseAddFailure (String line) {
		return String.format(MESSAGE_ADD_FAILURE, _fileName, line);
	}

	private String formatResponseClearSuccess () {
		return String.format(MESSAGE_CLEAR_SUCCESS, _fileName);
	}
	
	private String formatResponseClearFailure () {
		return String.format(MESSAGE_CLEAR_FAILURE, _fileName);
	}
	
	private String formatResponseDisplayFailure () {
		return String.format(MESSAGE_DISPLAY_FAILURE, _fileName);
	}
	
	private String formatResponseLineNumberError (int lineNumber) {
		return String.format(MESSAGE_LINE_NUMBER_ERROR, lineNumber);
	}

	private String formatResponseLineRemoveSuccess (String removedLine) {
		return String.format(MESSAGE_DELETE_SUCCESS, _fileName, removedLine);
	}
	
	private String formatResponseLineRemoveFailure () {
		return String.format(MESSAGE_DELETE_FAILURE, _fileName);
	}
	
	private String formatResponseFileEmpty () {
		return String.format(MESSAGE_FILE_EMPTY, _fileName);
	}
	
	private String formatResponseSearchNoMatch (String searchTerm) {
		return String.format(MESSAGE_SEARCH_NO_MATCH, searchTerm, _fileName);
	}
	
	private String formatResponseSortSuccess () {
		return String.format(MESSAGE_SORT_SUCCESS, _fileName);
	}
	
	private static String formatResponseInvalidCommand (String userCommand) {
		return String.format(MESSAGE_INVALID_COMMAND, userCommand);
	}
}