package jp.gr.java_conf.simply.erviz.plain;

import java.util.HashMap;
import java.util.Map;

import jp.gr.java_conf.simply.erviz.plain.LineData;

import static jp.gr.java_conf.simply.erviz.plain.PackageConstants.OPTION_INVALID_CHARS;
import static jp.gr.java_conf.simply.erviz.plain.PackageConstants.OPTION_ASSIGNMENT_CHAR;
import static jp.gr.java_conf.simply.erviz.plain.PackageConstants.OPTION_SEPARATOR_CHAR;
import static jp.gr.java_conf.simply.erviz.plain.PackageUtils.findFirstCharacter;
import static jp.gr.java_conf.simply.erviz.plain.PackageUtils.splitText;
import static jp.gr.java_conf.simply.erviz.plain.PackageUtils.isEnclosedByDoubleQuotations;
import static jp.gr.java_conf.simply.erviz.plain.PackageUtils.removeEnclosingDoubleQuotations;

/**
 * This class represents a text parser which is used for an option list.
 * 
 * Usage:
 * 
 * <ol>
 *   <li>Creates an object of this class with the target text.
 *   <li>Parse it by calling {@code parse()}.
 *   <li>Retrieve the result of parsing by calling getter methods.
 * </ol>
 * 
 * Not that after excection in {@code parse()} method, the result of getter methods 
 * will not be ensured.
 * 
 * The example of target text:
 * 
 * <ul>
 *   <li>name1: value1
 *   <li>name1: value1; name2: value2
 * </ul>
 * 
 * A text which is parsed by this class consists of some pairs of option name and value.
 * Each pair is separated by semicolon, and a name and a value in a pair is separated by colon.
 * This option list text can be blank.
 * 
 * Some spaces around each name, value, semicolon or comma is allowed, 
 * and they will be trimmed automatically. 
 * 
 * The option names are case insensitive. For example, "name", "NAME" and "Name" are NOT distinguished. 
 * On the other hand, the option values are case sensitive. Therefore "value", "VALUE" and "Value" 
 * are distinguished.
 * 
 * This class is immutable. All input arguments is given at the object construction.
 * 
 * This class is package private. The instance of this class is used by other parser classes internally.
 * 
 * @author kono
 * @version 1.0
 */
final class OptionListParser {
	
	//input
	private String text;
	
	//output
	private Map<String, String> optionMap = new HashMap<String, String>();
	
	//for exception message
	private final LineData line;
	
	/**
	 * Constructs an object of this class.
	 * 
	 * @param text text the text which will be parsed in {@code parse()} method
	 * @param line the line data which contains {@code text} argument.
	 * @throws NullPointerException if a null argument is specified
	 */
	OptionListParser(String text, LineData line) {
		if (text == null || line == null) {
			throw new NullPointerException();
		}
		this.text = text.trim();
		this.line = line;
	}
	
	/**
	 * Constructs an object of this class.
	 * This constructor is supporsed to be used for unit test.
	 * 
	 * @param text the text which will be parsed in {@code parse()} method
	 * @throws NullPointerException if a null argument is specified
	 */
	OptionListParser(String text) {
		this(text, new LineData(0, ""));
	}
	
	/**
	 * Returns true if the option list is emply, false otherwise;
	 * 
	 * @return true if the option list is emply, false otherwise;
	 */
	boolean isEmpty() {
		return this.optionMap.isEmpty();
	}
	
	/**
	 * Returns the value corresponding the specified name.
	 * If the value corresponding the specified name is not found,
	 * this method returns null.
	 * 
	 * @param name an option name 
	 * @return the value corresponding the specified name, or null if not found.
	 */
	String getOptionValue(String name) {
		return this.optionMap.get(name.toUpperCase()); //DON'T remove toUpperCase()
	}
	
	/**
	 * Parses the target text specified by constructor parameters.
	 * The result of parsing will be saved in the object.
	 * 
	 * @throws ParserException if parsing error occured.
	 */
	void parse() throws ParserException {
		
		this.optionMap.clear();
		
		//get name string
		String optionList = this.text;
		
		//blank check
		if (optionList.equals("")) {
			return; //no option list
		}
		
		//get option array
		String[] KeyValuePairs = splitText(optionList, OPTION_SEPARATOR_CHAR);
		
		//option loop
		for (int i = 0; i < KeyValuePairs.length; i++) {
			String pair = KeyValuePairs[i];
			
			if (pair.equals("")) {
				throw createException(Message.ERR_BLANK_OPTION_NVPAIR, i);
			}
			
			String[] tokens = splitText(pair, OPTION_ASSIGNMENT_CHAR);
			if (tokens.length <= 1) { //may not be zero
				throw createException(Message.ERR_COLON_NOT_FOUND, pair);	
			} else if (tokens.length >= 3) {
				throw createException(Message.ERR_COLON_TOO_MANY, pair);	
			}
			
			String name = tokens[0].toUpperCase(); //DON'T remove toUpperCase()
			String value = tokens[1];
			
			if (name.equals("")) {
				throw createException(Message.ERR_BLANK_OPTION_NAME, pair);
			}
			
			//option name
			{
				String ch1 = findFirstCharacter(name, OPTION_INVALID_CHARS);
				if (ch1 != null) {
					throw createException(Message.ERR_INVALID_OPTION_CHAR, ch1);
				}
			}
			
			//option value
			if (isEnclosedByDoubleQuotations(value)) {
				value = removeEnclosingDoubleQuotations(value);
			} else{
				String ch2 = findFirstCharacter(value, OPTION_INVALID_CHARS);
				if (ch2 != null) {
					throw createException(Message.ERR_INVALID_OPTION_CHAR, ch2);
				}
			}
			
			if (this.optionMap.containsKey(name)) {
				throw createException(Message.ERR_OPTION_NAME_DUP, name);	
			}
			
			this.optionMap.put(name, value);
		}
		
	}
	
	//private utility method to create exception
	private ParserException createException(Message message, Object... params) {
		final String description = message.getText(params);
		return ParserException.create(description, this.line);
	}
	
}
