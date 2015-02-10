/**
 * 
 */
package com.terokallio.wagesystem;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Handles CSV file read operations.
 * @author terokallio
 * @version 0.1
 */
public class CSVFileReader {

	private BufferedReader br = null;
	private String splitMark = ",";
	
	/**
	 * Constructor
	 * Reads file to BufferedReader
	 * Tries to locate file based on filename given, both from FileReader, 
	 * and from SystemResources.
	 * @param fileName
	 */
	public CSVFileReader(String fileName) {
		
		try {
			this.br = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException ex) {
			// Failed to find the file, 
			// so lets try to find the file one more time from resources
			InputStream file = ClassLoader.getSystemResourceAsStream(fileName);
			try {
				this.br = new BufferedReader(new InputStreamReader(file, "UTF-8"));
			} catch (Exception e) {
				System.err.println("Could not find CSV File.");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Create ArrayList of WorkShift objects
	 * @return ArrayList<WorkShift> All work shifts
	 * @throws Exception
	 */
	public ArrayList<WorkShift> readFileContents() throws Exception {
		
		String line = null;
		ArrayList<WorkShift> wsc = new ArrayList<>();

		// read CSV File, line by line.
		// and set each workshift to ArrayList<WorkShift>
		int i = 0;
		while ((line = br.readLine()) != null) {
			String[] data = line.split(splitMark);
			if (i == 0) {
				i++; continue;
			}
			WorkShift w = new WorkShift(data[0], data[1], data[2],
					data[3], data[4]);
			wsc.add(w);
		}

		return wsc;
	}
}
