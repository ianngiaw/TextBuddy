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
	 * @throws IOException Will be thrown if an I/O Error occurred
	 */
	public void addLine (String line) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(_textFile, true)));
		out.println(line);
		out.close();
	}
	
	public void clearFile () throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(_textFile, false)));
		out.print("");
		out.close();
	}
	
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
	
	public boolean isValidLineNumber (int lineNumber, List<String> lines) {
		boolean isLessThanOrEqualToList = lineNumber <= lines.size();
		boolean isMoreThanZero = lineNumber > 0;
		return isLessThanOrEqualToList && isMoreThanZero;
	}
	
	public void saveLinesToFile (List<String> lines) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(_textFile, false)));
		Iterator<String> iterator = lines.iterator();
		while (iterator.hasNext()) {
			out.println(iterator.next());
		}
		out.close();
	}
}
