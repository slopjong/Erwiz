package de.slopjong.erwiz.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a utility class to read/write text data.
 * As of current version, the text encoding which this class deal with,  
 * is only UTF-8.
 * 
 * In this class, some static utility method are defined.
 * 
 * @author kono
 * @version 1.0
 */
public final class IoUtils {
	
	private static final String DEFAULT_ENCODING = "UTF8";
	
	/**
	 * Any instance of this class doesn't created.
	 */
	private IoUtils() {
	}
	
	/**
	 * Reads  file lines from the specified input file.
	 * 
	 * @param file fine path
	 * @return line data collection
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static List<String> readFileLines(File file) throws IOException, FileNotFoundException {
		InputStream in = new FileInputStream(file);
		return readLinesCommon(in);
	}
	
	/**
	 * Reads lines from the standard input.
	 * 
	 * @return line data collection
	 * @throws IOException
	 */
	public static List<String> readStandardInputLines() throws IOException {
		InputStream in = System.in;
		return readLinesCommon(in);
	}
	
	/**
	 * Reads lines from the specified input stream.
	 * 
	 * @param in input stream.
	 * @return lines of the specified resource data
	 * @throws IOException
	 */
	public static List<String> readStreamLines(InputStream in) throws IOException {
		return readLinesCommon(in);
	}
	
	//private common method to read text data
	private static List<String> readLinesCommon(InputStream in) throws IOException {
		
		if (in == null) {
			throw new IOException("Input stream is null.");
		}
		
		List<String> list = new ArrayList<String>();
		
		BufferedReader reader = null; //subclass of BufferedReader
		try {
			reader = new BufferedReader(new InputStreamReader(in, DEFAULT_ENCODING));
			
			String line;
			while((line = reader.readLine()) != null){
				list.add(line);
			}
			
			return list;
		} finally {
			close(reader);
		}
	}
	
	/**
	 * Writes the text data to the specified output file.
	 * 
	 * @param file file path
	 * @param text text data
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void writeFileText(File file, String text) throws FileNotFoundException, IOException {
		OutputStream out = new FileOutputStream(file);
		writeLinesCommon(out, text);
	}
	
	/**
	 * Writes the text data to the standard output.
	 * 
	 * @param text text data
	 * @throws IOException
	 */
	public static void writeStandardOutputText(String text) throws IOException {
		OutputStream out = System.out;
		writeLinesCommon(out, text);
	}
	
	/**
	 * Writes the text data to the specified output stream.
	 * 
	 * @param out output stream
	 * @param text text data
	 * @throws IOException
	 */
	public static void writeStreamText(OutputStream out, String text) throws IOException {
		writeLinesCommon(out, text);
	}
	
	//private common method to write text data
	private static void writeLinesCommon(OutputStream out, String text) throws IOException {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(out, DEFAULT_ENCODING));
			writer.write(text);
		} finally {
			close(writer);
		}
	}
	
	//private common method to close
	private static void close(Closeable c) {
		if (c != null) {
			try {
				c.close();
			} catch (IOException ex) {
			}
		}
	}
	
}
