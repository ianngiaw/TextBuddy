public class Command {
	private CommandType _commandType;
	private String _commandArgument;
	private String _originalCommand;
	
	// Valid commands types a user may input
	private static final String ADD_COMMAND = "add";
	private static final String CLEAR_COMMAND = "clear";
	private static final String DELETE_COMMAND = "delete";
	private static final String DISPLAY_COMMAND = "display";
	private static final String EXIT_COMMAND = "exit";
	
	protected static enum CommandType {
		ADD,
		CLEAR,
		DELETE,
		DISPLAY,
		EXIT,
		INVALID
	};
	
	private Command (CommandType commandType, String argument, String userCommand) {
		this._commandType = commandType;
		this._commandArgument = argument;
		this._originalCommand = userCommand;
	}
	
	public static Command parseCommand (String userCommand) {
		String firstWord = extractFirstWord(userCommand);
		CommandType commandType = interpretCommandType(firstWord);
		String message = removeFirstWord(userCommand);
		return new Command(commandType, message, userCommand);
	}
	
	public CommandType getCommandType () {
		return this._commandType;
	}
	
	public String getCommandArgument () {
		return this._commandArgument;
	}
	
	public String getOriginalCommand () {
		return this._originalCommand;
	}
	
	private static CommandType interpretCommandType (String firstWord) {
		switch (firstWord) {
			case ADD_COMMAND :
				return CommandType.ADD;
			case CLEAR_COMMAND :
				return CommandType.CLEAR;
			case DELETE_COMMAND :
				return CommandType.DELETE;
			case DISPLAY_COMMAND :
				return CommandType.DISPLAY;
			case EXIT_COMMAND :
				return CommandType.EXIT;
			default:
				return CommandType.INVALID;
		}
	}
	
	private static String extractFirstWord (String userCommand) {
		String commandType = userCommand.trim().split("\\s+")[0];
		return commandType;
	}

	private static String removeFirstWord (String userCommand) {
		return userCommand.replace(extractFirstWord(userCommand), "").trim();
	}
}