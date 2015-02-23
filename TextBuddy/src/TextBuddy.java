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
	private static final String MESSAGE_FILE_READY = "%1$s is ready for use";
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
		printWelcome();
		exitIfIncorrectArguments(args);
		String fileName = getFileNameFromArguments(args);
		TextBuddyLogic logic = new TextBuddyLogic(fileName);
		executeUntilExitCommand(new Scanner(System.in), logic);
	}
	
	/**
	 * A method that exits the program that if the arguments provided are not valid
	 * 
	 * @param args The command line arguments
	 */
	private static void exitIfIncorrectArguments (String[] args) {
		exitIfNoArguments(args);
		exitIfInvalidFileName(args);
	}
	
	/**
	 * Exits the program if no command line arguments are provided
	 * 
	 * @param args The command line arguments
	 */
	private static void exitIfNoArguments (String[] args) {
		if (args.length == 0) {
			exitWithErrorMessage(MESSAGE_NO_ARGUMENTS);
		}
	}
	
	/**
	 * Exits the program if the file name in the arguments are invalid
	 * 
	 * @param args The command line arguments
	 */
	private static void exitIfInvalidFileName (String[] args){
		String fileName = getFileNameFromArguments(args);
		if (!isValidFileName(fileName)) {
			exitWithErrorMessage(MESSAGE_INVALID_FILE_NAME);
		}
	}
	
	/**
	 * Exits the program, printing an error message
	 * 
	 * @param errorMessage The message to be printed before exiting the program
	 */
	public static void exitWithErrorMessage (String errorMessage) {
		System.out.println(errorMessage);
		exitProgram();
	}
	
	public static void exitProgram () {
		System.exit(0);
	}

	/**
	 * A method that returns the name of the file provided in the command line
	 * arguments
	 * 
	 * @param args The command line arguments
	 * @return The name of the file given in the command line arguments
	 */
	private static String getFileNameFromArguments (String[] args) {
		return args[0];
	}
	
	/**
	 * Checks if a string can be used as a valid file name
	 * 
	 * @param fileName The name of the file
	 * @return true if the name is valid, false otherwise
	 */
	private static boolean isValidFileName (String fileName) {
		File file = new File(fileName);
		try {
			file.getCanonicalPath();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	/**
	 * A method that runs in an infinite loop requesting for commands
	 * until the user inputs an exit command.
	 * 
	 * @param textFile The file to be edited
	 * @param fileName The name of the file to be edited
	 */
	private static void executeUntilExitCommand (Scanner scanner, TextBuddyLogic logic) {
		while (true) {
			String userCommand = requestUserCommand(scanner);
			String response = logic.executeCommand(userCommand);
			printResponse(response);
		}
	}
	
	/**
	 * A method that prints a statement requesting input from the user
	 * 
	 * @param scanner A scanner object used to get user input
	 * @return A string of the command entered by the user
	 */
	public static String requestUserCommand (Scanner scanner) {
		System.out.print(MESSAGE_REQUEST_COMMAND);
		String userCommand = scanner.nextLine();
		return userCommand;
	}
	
	private static void printResponse (String response) {
		System.out.println(response);
	}
	
	private static void printWelcome () {
		System.out.print(MESSAGE_WELCOME);
	}
	
	protected static void printFileReady (String fileName) {
		String message = String.format(MESSAGE_FILE_READY, fileName);
		System.out.println(message);
	}
}