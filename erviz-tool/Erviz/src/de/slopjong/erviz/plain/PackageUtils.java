package de.slopjong.erviz.plain;

import java.util.ArrayList;
import java.util.List;

import de.slopjong.erviz.model.ColorPair;
import de.slopjong.erviz.model.OptionMap;
import de.slopjong.erviz.plain.LineData;


import static de.slopjong.erviz.plain.PackageConstants.COMMENT_HEAD_CHAR;

/**
 * This is a utility class for this package.
 * 
 * In this class, some static utility methods are defined.
 * 
 * @author kono
 * @version 1.0
 */
final class PackageUtils {
	
	/**
	 * Any instance of this class doesn't created.
	 */
	private PackageUtils() {	
	}
	

// comment ==================================================================================
	
	/**
	 * Removes comment string from the specified line text.
	 * If the specified line text doesn't have comment, this method return it simply.
	 * The result text is trimmed automatically.
	 * 
	 * @param line the line text
	 * @return the line text whose comment is removed
	 */
	static String removeComment(String line) {
		int pos = indexOfWithQuote(line, COMMENT_HEAD_CHAR);
		if (pos < 0) {
			return line;
		}
		
		return line.substring(0, pos);
	}
	
// brackets ==================================================================================
	
	/**
	 * Returns true if the specified text is enclosed by any pair in 
	 * the specified pairs of brackets, false otherwise.
	 * The bracket pairs which can be checked are specified by the second argument.
	 * 
	 * @param text the target text
	 * @param pairs the array of the bracket pairs
	 * @return true if the specified text is enclosed, false otherwise
	 * @see de.slopjong.erviz.plain.BracketPair
	 */
	static boolean isEnclosedByAnyBrackets(String text, BracketPair... pairs) {
		for (BracketPair pair : pairs) {
			if (text.startsWith(pair.getLeft()) && text.endsWith(pair.getRight())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns true if the specified text starts with the any left bracket in
	 * the specified pairs of brackets, false otherwise.
	 * 
	 * @param text the target text
	 * @param pairs the array of the bracket pairs
	 * @return true if the specified text starts with the any left bracket, false otherwise
	 * @see de.slopjong.erviz.plain.BracketPair
	 */
	static boolean startsWithAnyLeftBracket(String text, BracketPair... pairs) {
		for (BracketPair pair : pairs) {
			if (text.startsWith(pair.getLeft())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns true if the specified text starts with the any left or right bracket in
	 * the specified pairs of brackets, false otherwise.
	 * 
	 * @param text the target text
	 * @param pairs the array of the bracket pairs
	 * @return true if the specified text starts with the any left or right bracket, false otherwise
	 * @see de.slopjong.erviz.plain.BracketPair
	 */
	static boolean startsWithAnyBracket(String text, BracketPair... pairs) {
		for (BracketPair pair : pairs) {
			if (text.startsWith(pair.getLeft()) || text.startsWith(pair.getRight())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns true if the specified text ends with the any right bracket in
	 * the specified pairs of brackets, false otherwise.
	 * 
	 * @param text the target text
	 * @param pairs the array of the bracket pairs
	 * @return true if the specified text ends with the any right bracket, false otherwise
	 * @see de.slopjong.erviz.plain.BracketPair
	 */
	static boolean endsWithAnyRightBracket(String text, BracketPair... pairs) {
		for (BracketPair pair : pairs) {
			if (text.endsWith(pair.getRight())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns true if the specified text contains the any left bracket in
	 * the specified pairs of brackets, false otherwise.
	 * 
	 * This method neglects the characters which is enclosed by double quotations.
	 * 
	 * @param text the target text
	 * @param pairs the array of the bracket pairs
	 * @return true if the specified text contains the any left bracket, false otherwise
	 * @see de.slopjong.erviz.plain.BracketPair
	 */
	static boolean containsAnyLeftBracket(String text, BracketPair... pairs) {
		for (BracketPair pair : pairs) {
			if (indexOfWithQuote(text, pair.getLeft()) >= 0) { //contains
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns true if the specified text contains the any right bracket in
	 * the specified pairs of brackets, false otherwise.
	 * 
	 * This method neglects the characters which is enclosed by double quotations.
	 * 
	 * @param text the target text
	 * @param pairs the array of the bracket pairs
	 * @return true if the specified text contains the any right bracket, false otherwise
	 * @see de.slopjong.erviz.plain.BracketPair
	 */
	static boolean containsAnyRightBracket(String text, BracketPair... pairs) {
		for (BracketPair pair : pairs) {
			if (indexOfWithQuote(text, pair.getRight()) >= 0) { //contains
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns true if the specified text contains the any bracket in
	 * the specified pairs of brackets, false otherwise.
	 * 
	 * This method neglects the characters which is enclosed by double quotations.
	 * 
	 * @param text the target text
	 * @param pairs the array of the bracket pairs
	 * @return true if the specified text contains the any bracket, false otherwise
	 */
	static boolean containsAnyBracket(String text, BracketPair... pairs) {
		for (BracketPair pair : pairs) {
			if (indexOfWithQuote(text, pair.getLeft()) >= 0 ||
					indexOfWithQuote(text, pair.getRight()) >= 0) { //contains
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Removes the enclosing brackets form the specified text.
	 * The bracket pairs which can be removed are specified by the second argument.
	 * If the specified text is not enclosed by brackets, this method return it simply.
	 * The result text is trimmed automatically.
	 * 
	 * @param text the target text
	 * @param pairs the array of the bracket pairs which can be removed
	 * @return the result text
	 * @see de.slopjong.erviz.plain.BracketPair
	 */
	static String removeEnclosingBrackets(String text, BracketPair... pairs) {
		for (BracketPair pair : pairs) {
			if (isEnclosedByAnyBrackets(text, pair)) {
				return text.substring(1, text.length() - 1).trim();
			}
		}
		return text;
	}
	
	/**
	 * <p>Splits the specified text into some fields. The delimiters are specified 
	 * by the {@code BracketPair} objects, and each element of the result list is 
	 * enclosed the specified brackets.
	 * 
	 * <p>If extra text exists beween fields, it is added the result list.
	 * An elements of the result list can be a zero-length text.
	 * 
	 * @param text the target text
	 * @param pairs bracket pairs
	 * @return the result list
	 * @see de.slopjong.erviz.plain.BracketPair
	 */
	static List<String> split(String text, BracketPair... pairs) {
		
		List<String> list = new ArrayList<String>();
		
		while (!text.equals("")) {
			int pos = findFirstLeftBracketPosition(text, pairs);
			if (pos < 0) { //left bracket not found
				list.add(text);
				break;
			}
			
			if (pos > 0) { //some characters before the left bracket
				list.add(text.substring(0, pos));
				text = text.substring(pos);
			}
			String lch = text.substring(0, 1);
			BracketPair pair = BracketPair.findByLeftBracket(lch);
			
			pos = findFirstRightBracketPosition(text, pair); //only one bracket pair
			if (pos < 0) { //right bracket not found
				list.add(text);
				break;
			}
			
			list.add(text.substring(0, pos + 1));
			text = text.substring(pos + 1);
		}
		
		return list;
	}
	
	/**
	 * private utility method
	 * This method neglects the characters which is enclosed by double quotations.
	 */
	private static int findFirstLeftBracketPosition(String text, BracketPair... pairs) {
		return findFirstBracketPosition(text, 0, pairs);
	}
	
	/**
	 * private utility method
	 * This method neglects the characters which is enclosed by double quotations.
	 */
	private static int findFirstRightBracketPosition(String text, BracketPair... pairs) {
		return findFirstBracketPosition(text, 1, pairs);
	}
	
	/**
	 * private utility method
	 * This method neglects the characters which is enclosed by double quotations.
	 */
	private static int findFirstBracketPosition(String text, int side, BracketPair... pairs) {
		assert (side == 0 || side == 1) : "side number is invalid [" + side + "]";
		
		int pos1 = -1; //-1 means "not found"
		
		for (BracketPair pair : pairs) {
			int pos2 = indexOfWithQuote(text, pair.getStringArray()[side]);
			if (pos2 >= 0 && (pos1 == -1 || pos2 < pos1)) {
				pos1 = pos2;
			}
		}
		
		return pos1;
	}
	
// characters ==================================================================================
	
	/**
	 * Finds the first character which the specified character array contains.
	 * This method returns the found character.
	 * 
	 * This method neglects the characters which is enclosed by double quotations.
	 * 
	 * @param text the target
	 * @param characters a character array 
	 * @return the found character.
	 */
	static String findFirstCharacter(String text, String... characters) {
		int pos = findFirstCharacterPosition(text, characters);
		return (pos >= 0) ? text.substring(pos, pos + 1) : null;
	}
	
	/**
	 * Finds the first character which the specified character array contains.
	 * This method returns the position that the character is found at.
	 * If the text doesn't contain any specified character, returns -1;
	 * 
	 * This method neglects the characters which is enclosed by double quotations.
	 * 
	 * @param text the target
	 * @param characters a character array 
	 * @return the position that the character is found at
	 */
	static int findFirstCharacterPosition(String text, String... characters) {
		
		int pos1 = -1; //-1 means "not found"
		
		for (String ch2 : characters) {
			int pos2 = indexOfWithQuote(text, ch2);
			if (pos2 >= 0 && (pos1 == -1 || pos2 < pos1)) {
				pos1 = pos2;
			}
		}
		
		return pos1;
	}
	
	/**
	 * Private utility method.
	 * 
	 * This method works like String#indexOf() method, but it neglects the characters
	 * which are enclosed by double quotations.
	 */
	private static int indexOfWithQuote(String text, String ch) {
		
		boolean quoted = false;
		
		for (int i = 0; i < text.length(); i++) {
			String ch2 = text.substring(i, i + 1);
			
			if (!quoted && ch2.equals(ch)) {
				return i;
			} else if (ch2.equals("\"")) {
				quoted = !quoted;
			} else {
				continue;
			}
		}
		
		return -1;
	}
	
	/**
	 * Removes the first character form the specified text.
	 * The result is trimmed automatically.
	 * 
	 * @param text the target text
	 * @return the result text
	 */
	static String removeFirstCharacter(String text) {
		if (text.equals("")) {
			return text;
		}
		return text.substring(1, text.length()).trim();
	}
	
	/**
	 * Removes the last character form the specified text.
	 * The result is trimmed automatically.
	 * 
	 * @param text the target text
	 * @return the result text
	 */
	static String removeLastCharacter(String text) {
		if (text.equals("")) {
			return text;
		}
		return text.substring(0, text.length() - 1).trim();
	}
	
// quotation ==================================================================================
	
	/**
	 * Return true if the specified text is enclosed by double quotations.
	 * 
	 * @param text the target text
	 * @return true if the specified text is enclosed by double quotations
	 */
	static boolean isEnclosedByDoubleQuotations(String text) {
		return (text.length() >= 2 && text.startsWith("\"") && text.endsWith("\""));
	}
	
	/**
	 * Removes the enclosing double quotations form the specified text.
	 * 
	 * @param text the target text
	 * @return the result text
	 */
	static String removeEnclosingDoubleQuotations(String text) {
		if (text.length() < 2) {
			return text;
		}
		return text.substring(1, text.length() - 1);
	}
	
// split string ==================================================================================
	
	/**
	 * Splits the specified text by the specified delimiter.
	 * If a delimiter character appears btween double quotations,
	 * this method regard it as a normal character which doesn't work as a delimiter.
	 * 
	 * Each element of the result array is trimmed automatically.
	 * 
	 * @param text the target
	 * @return the result array.
	 */
	static String[] splitText(String text, String delimiter) {
		
		if (text == null) {
			return new String[]{};
		}
		
		text = text.trim();
		
		if (text.equals("")) {
			return new String[]{};
		}
		
		List<String> list = new ArrayList<String>();
		String temp = "";
		boolean quote = false;
		for (int i = 0; i < text.length(); i++) {
			String ch = text.substring(i, i + 1);
			
			if (ch.equals("\"")) {
				quote = !quote;
				temp += ch;
			} else if (!quote && ch.equals(delimiter)) {
				list.add(temp.trim());
				temp = "";
			} else {
				temp += ch;
			}
		}
		list.add(temp.trim());
		
		return list.toArray(new String[]{});
	}
	
// options ==================================================================================
	
	/**
	 * This method sets the option value and the option name to the specified option map.
	 * 
	 * @param optionMap the option map in which the optin name and the option value will be set
	 * @param optionName the option name which has option information
	 * @param optionValue the option values
	 */
	static void setOptionValue(OptionMap optionMap, OptionName optionName, Object optionValue)
			throws IllegalArgumentException {
		
		if (optionValue == null) {
			return;
		}
		
		final Class<?> optionValueType = optionName.getOptionInfo().getValueType();
		
		if (optionValueType == String.class) {
			
			optionMap.setString(optionName, (String)optionValue);
			
		} else if (optionValueType == Boolean.class) {
			
			optionMap.setBoolean(optionName, toBoolean((String)optionValue));
			
		} else if (optionValueType == Integer.class) {
			
			optionMap.setInteger(optionName, toInteger((String)optionValue));
			
		} else if (optionValueType == Double.class) {
			
			optionMap.setDouble(optionName, toDouble((String)optionValue));
			
		} else if (optionValueType == ColorPair.class) {
			
			optionMap.setColorPair(optionName, toColorPair((String)optionValue));
			
		} else {
			
			throw new IllegalArgumentException("Not implemented for this class : [" + optionValueType.getName() + "]");
			
		}
		
	}
	
	/**
	 * Converts the specified value string to the Boolean value.
	 * If the specified string is null, blank or 'false', this method returns false.
	 * If it is "true", this method returns true. These comparation is case insensitive.
	 * Otherwise, this method returns null.
	 * 
	 * @param value a value string
	 * @return a {@code Boolean} object
	 * @throws IllegalArgumentException if format error has occured
	 */
	static Boolean toBoolean(String value) throws IllegalArgumentException {
		if (value == null || value.equals("")) {
			//the option not specified or a blank value specified
			return false;
		}
		
		if (value.equalsIgnoreCase("true")) {
			return true;
		}
		
		if (value.equalsIgnoreCase("false")) {
			return false;
		}
		
		throw new IllegalArgumentException("invalid boolean value : " + value);
	}
	
	/**
	 * Converts the specified value string to the Integer value.
	 * If the specified string is null or blank, this method returns null.
	 * 
	 * @param value a value string
	 * @return a {@code Integer} object
	 * @throws IllegalArgumentException if format error has occured
	 */
	static Integer toInteger(String value) throws IllegalArgumentException {
		if (value == null || value.equals("")) {
			//the option not specified or a blank value specified
			return null;
		}
		
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("invalid boolean value : " + value);
		}
	}
	
	/**
	 * Converts the specified value string to the Double value.
	 * If the specified string is null or blank, this method returns null.
	 * 
	 * @param value a value string
	 * @return a {@code Double} object
	 * @throws IllegalArgumentException if format error has occured
	 */
	static Double toDouble(String value) throws IllegalArgumentException {
		if (value == null || value.equals("")) {
			//the option not specified or a blank value specified
			return null;
		}
		
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("invalid boolean value : " + value);
		}
	}
	
	/**
	 * Converts the specified value string to a {@code ColorPair} object.
	 * if the specified string is null, blank or 'none', this method returns NONE;
	 * if the specified name is invalid, this method returns null.
	 * 
	 * @param value color name
	 * @return a {@code ColorPair} object.
	 * @throws IllegalArgumentException if the specified color is unknown
	 */
	static ColorPair toColorPair(String value) throws IllegalArgumentException {
		if (value == null || value.equals("")) {
			//color not specified or "color=" specified
			return ColorPair.NONE;
		}
		
		try {
			return ColorPair.valueOf(value.toUpperCase());
			
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException("invalid color pair value : " + value);
		}
	}
	
// line data ==================================================================================
	
	/**
	 * A utility method to create a {@code List<LineData>} object.
	 * 
	 * @param lines the lines which is set into the list object.
	 * @return the created {a@code List<LineData>} object.
	 */
	static List<LineData> createLinesList(String... lines) {
		List<LineData> list = new ArrayList<LineData>();
		for (int i = 0; i < lines.length; i++) {
			list.add(new LineData(i + 1, lines[i])); //the first line number is 1.
		}
		return list;
	}
	
	/**
	 * A utility method to create a {@code List<LineData>} object.
	 * 
	 * @param lines the lines which is set into the list object.
	 * @return the created {a@code List<LineData>} object.
	 */
	static List<LineData> createLinesList(LineData... lines) {
		List<LineData> list = new ArrayList<LineData>();
		for (LineData line : lines) {
			list.add(line);
		}
		return list;
	}
	
}
