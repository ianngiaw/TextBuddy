package com.textbuddy.main;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class TextBuddy {
	
	// Messages printed
	
	private static final String MESSAGE_FILE_READY = "%1$s is ready for use";
	private static final String MESSAGE_INVALID_FILE_NAME = "invalid file name provided";
	private static final String MESSAGE_NO_ARGUMENTS = "no file name provided";
	private static final String MESSAGE_REQUEST_COMMAND = "command: ";
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. ";
	
	public static void main (String[] args) {
		printWelcome();
		exitIfIncorrectArguments(args);
		String fileName = getFileNameFromArguments(args);
		TextBuddyLogic logic = new TextBuddyLogic(fileName);
		executeUntilExitCommand(new Scanner(System.in), logic);
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
	
	private static void executeUntilExitCommand (Scanner scanner, TextBuddyLogic logic) {
		while (true) {
			String userCommand = requestUserCommand(scanner);
			String response = logic.executeCommand(userCommand);
			printResponse(response);
		}
	}
	
	public static String requestUserCommand (Scanner scanner) {
		System.out.print(MESSAGE_REQUEST_COMMAND);
		String userCommand = scanner.nextLine();
		return userCommand;
	}
	
	// Print methods
	
	private static void printWelcome () {
		System.out.print(MESSAGE_WELCOME);
	}
	
	protected static void printFileReady (String fileName) {
		String message = String.format(MESSAGE_FILE_READY, fileName);
		System.out.println(message);
	}
	
	private static void printResponse (String response) {
		System.out.println(response);
	}
}