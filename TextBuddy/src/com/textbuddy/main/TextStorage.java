package com.textbuddy.main;

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
	
	public TextStorage (String fileName) throws IOException {
		_textFile = new File(fileName);
		_textFile.createNewFile();
	}
	
	// Adds a line of text provided to the end of a text file
	public void addLine (String line) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(_textFile, true)));
		out.println(line);
		out.close();
	}
	
	// Clears the entire file to become an empty file
	public void clearFile () throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(_textFile, false)));
		// Print an empty string so as to ensure all contents of the file is deleted
		out.print("");
		out.close();
	}
	
	// Extracts all lines in the text file to a list of strings
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
	
	// Checks if a number provided is within the range of the length of the list
	public boolean isValidLineNumber (int lineNumber, List<String> lines) {
		boolean isLessThanOrEqualToList = lineNumber <= lines.size();
		boolean isMoreThanZero = lineNumber > 0;
		return isLessThanOrEqualToList && isMoreThanZero;
	}
	
	// Overwrites the entire text file, writing each line in the list as a new line in the file
	public void saveLinesToFile (List<String> lines) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(_textFile, false)));
		Iterator<String> iterator = lines.iterator();
		while (iterator.hasNext()) {
			out.println(iterator.next());
		}
		out.close();
	}
}
