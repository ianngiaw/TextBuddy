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

	public static void main(String[] args) {
		System.out.print("Welcome to TextBuddy. ");
		Scanner myScanner = new Scanner(System.in);
		
		if (!checkArgExists(args)) {
			return;
		}
		
		File textFile = new File(args[0]);
		
		try {
			textFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while (true) {
			System.out.print("command: ");
			String command = myScanner.next();
			switch (command) {
				case "add" :
					String textToAdd = myScanner.nextLine();
					addText(textFile, textToAdd.trim());
					break;
				case "display" :
					List<String> textList = getTextList(textFile);
					if (!displayText(textList))
						System.out.println(textFile.getName()+ " is empty");
					break;
				case "delete" :
					int lineToDelete = myScanner.nextInt();
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
					return;
				default :
					break;
			}
		}
	}

	private static boolean checkArgExists (String[] args) {
		if (args == null || args[0] == null) {
			return false;
		}
		System.out.println(args[0] + " is ready for use");
		return true;
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