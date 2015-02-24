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

public class TextStorage {
	
	private File _textFile;
	private boolean _isUsable = true;
	
	public TextStorage (String fileName) {
		_textFile = new File(fileName);
		try {
			_textFile.createNewFile();
		} catch (IOException e) {
			_isUsable = false;
		}
	}
	
	public boolean isUsable () {
		return _isUsable;
	}
	
	/**
	 * Adds a line of text provided to the end of a text file.
	 * @param line The text to be added to the end of the file. Must not be null.
	 * @throws IOException Thrown if an I/O Error occurred
	 */
	public void addLine (String line) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(_textFile, true)));
		out.println(line);
		out.close();
	}
	
	/**
	 * Clears the entire file to an empty file
	 * @throws IOException Thrown if an I/O Error occurred
	 */
	public void clearFile () throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(_textFile, false)));
		// Print an empty string so as to ensure all contents of the file is deleted
		out.print("");
		out.close();
	}
	
	/**
	 * Extracts all lines in the text file
	 * @return Every line in the text file in the form of a list of strings, one string per line
	 * @throws IOException Thrown if an I/O Error occurred
	 */
	public List <String> getLines () throws IOException{
		List<String> lines = new ArrayList<String>();
		String currentLine;
		BufferedReader bufferedReader;
		bufferedReader = new BufferedReader(new FileReader(_textFile));
		while ((currentLine = bufferedReader.readLine()) != null) {
			lines.add(currentLine);
		}
		bufferedReader.close();
		return lines;
	}
	
	/**
	 * Checks if a number provided is within the range of the length of the list.
	 * @param lineNumber Any positive integer
	 * @param lines A list of strings used to compare to the integer
	 * @return True if the number is a valid line number, false if otherwise
	 */
	public boolean isValidLineNumber (int lineNumber, List<String> lines) {
		boolean isLessThanOrEqualToList = lineNumber <= lines.size();
		boolean isMoreThanZero = lineNumber > 0;
		return isLessThanOrEqualToList && isMoreThanZero;
	}
	
	/**
	 * Overwrites the entire text file, writing each line in the list as a new line in the file.
	 * @param lines A list of strings to be written to the file.
	 * @throws IOException Thrown if an I/O Error occurred
	 */
	public void saveLinesToFile (List<String> lines) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(_textFile, false)));
		Iterator<String> iterator = lines.iterator();
		while (iterator.hasNext()) {
			out.println(iterator.next());
		}
		out.close();
	}
}
