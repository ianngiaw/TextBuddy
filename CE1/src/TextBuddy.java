import java.io.File;
import java.io.IOException;
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
	private static final String MESSAGE_INVALID_FILE_NAME = "invalid file name provided";
	private static final String MESSAGE_NO_ARGUMENTS = "no file name provided";
	private static final String MESSAGE_REQUEST_COMMAND = "command: ";
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. ";
	
	/**
	 * The main method of the TextBuddy program
	 * 
	 * @param args Command line argument which gives the name of the text file to be edited
	 */
	public static void main (String[] args) {
		printWelcomeMessage();
		exitIfIncorrectArguments(args);
		String fileName = getFileNameFromArguments(args);
		TextBuddyHelper helper = new TextBuddyHelper(fileName);
		executeUntilExitCommand(new Scanner(System.in), helper);
	}
	
	private static void exitIfIncorrectArguments (String[] args) {
		exitIfNoArguments(args);
		exitIfInvalidFileName(args);
	}
	
	private static void exitIfNoArguments (String[] args) {
		if (args.length == 0) {
			exitWithErrorMessage(MESSAGE_NO_ARGUMENTS);
		}
	}
	
	private static void exitIfInvalidFileName (String[] args){
		String fileName = getFileNameFromArguments(args);
		if (!isValidFileName(fileName)) {
			exitWithErrorMessage(MESSAGE_INVALID_FILE_NAME);
		}
	}
	
	public static void exitWithErrorMessage (String errorMessage) {
		System.out.println(errorMessage);
		exitProgram();
	}
	
	public static void exitProgram () {
		System.exit(0);
	}

	private static String getFileNameFromArguments (String[] args) {
		return args[0];
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
	
	private static void printWelcomeMessage () {
		System.out.print(MESSAGE_WELCOME);
	}
	
	/**
	 * A method that runs in an infinite loop requesting for commands
	 * until the user inputs an exit command.
	 * 
	 * @param textFile The file to be edited
	 * @param fileName The name of the file to be edited
	 */
	private static void executeUntilExitCommand (Scanner scanner, TextBuddyHelper helper) {
		while (true) {
			String userCommand = requestUserCommand(scanner);
			helper.executeCommand(userCommand);
		}
	}
	
	public static String requestUserCommand (Scanner scanner) {
		System.out.print(MESSAGE_REQUEST_COMMAND);
		String userCommand = scanner.nextLine();
		return userCommand;
	}
}