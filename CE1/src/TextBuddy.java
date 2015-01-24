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

	private static final String INVALID_FILE_NAME_MESSAGE = "Invalid File Name";
	private static final String NO_ARGUMENTS_MESSAGE = "No arguments.";
	private static final String WELCOME_MESSAGE = "Welcome to TextBuddy. ";
	private static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) {
		
		exitIfIncorrectArguments(args);
		printWelcomeMessage();
		
		File textFile = new File(args[0]);
		
		try {
			textFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while (true) {
			System.out.print("command: ");
			String command = scanner.next();
			switch (command) {
				case "add" :
					String textToAdd = scanner.nextLine();
					addText(textFile, textToAdd.trim());
					break;
				case "display" :
					List<String> textList = getTextList(textFile);
					if (!displayText(textList))
						System.out.println(textFile.getName()+ " is empty");
					break;
				case "delete" :
					int lineToDelete = scanner.nextInt();
					deleteLine(textFile, lineToDelete);
					break;
				case "clear" :
					try {
						PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(textFile, false)));
						out.print("");
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println("all content deleted from " + textFile.getName());
					break;
				case "exit" :
					exitProgram();
					return;
				default :
					break;
			}
		}
	}

	private static void exitIfIncorrectArguments(String[] args) {
		exitIfIncorrectArguments(args);
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
		System.exit(0);
	}
	
	private static void exitIfNoArguments (String[] args) {
		if (args.length == 0) {
			exitWithErrorMessage(NO_ARGUMENTS_MESSAGE);
		}
		System.out.println(args[0] + " is ready for use");
	}
	
	private static void exitIfInvalidFileName(String[] args){
		if (!isValidFileName(args[0])) {
			exitWithErrorMessage(INVALID_FILE_NAME_MESSAGE);
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
	
	private static List <String> getTextList (File textFile) {
		List<String> textList = new ArrayList<String>();
		String currentLine;
		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new FileReader(textFile));
			while ((currentLine = bufferedReader.readLine()) != null) {
				textList.add(currentLine);
			}
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();	
		}
		return textList;
	}
	
	private static boolean displayText (List<String> textList) {
		Iterator<String> iterator = textList.iterator();
		int lineCount = 0;
		while (iterator.hasNext()) {
			System.out.println(++lineCount + ". " + iterator.next());
		}
		if (lineCount == 0)
			return false;
		return true;
	}
	
	private static void addText (File textFile, String text) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(textFile, true)));
			out.println(text);
			out.close();
			System.out.println("added to " + textFile.getName() + ": \"" + text + "\"");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void deleteLine (File textFile, int line) {
		List<String> textList = getTextList(textFile);
		String removedLine;
		try {
			removedLine = textList.remove(line - 1);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Line " + line + " does not exist");
			return;
		}
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(textFile, false)));
			Iterator<String> iterator = textList.iterator();
			while (iterator.hasNext()) {
				out.println(iterator.next());
			}
			out.close();
			System.out.println("deleted from " + textFile.getName() + ": \"" + removedLine + "\"");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}