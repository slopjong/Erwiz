package jp.gr.java_conf.simply.erviz.plain;

import java.util.List;

import jp.gr.java_conf.simply.erviz.plain.LineData;
import jp.gr.java_conf.simply.erviz.model.OptionMap;

import static jp.gr.java_conf.simply.erviz.plain.PackageUtils.startsWithAnyLeftBracket;
import static jp.gr.java_conf.simply.erviz.plain.PackageUtils.isEnclosedByAnyBrackets;
import static jp.gr.java_conf.simply.erviz.plain.PackageUtils.removeComment;
import static jp.gr.java_conf.simply.erviz.plain.PackageUtils.removeEnclosingBrackets;
import static jp.gr.java_conf.simply.erviz.plain.PackageUtils.split;

import static jp.gr.java_conf.simply.erviz.plain.BracketPair.SQUARE;
import static jp.gr.java_conf.simply.erviz.plain.BracketPair.ROUND;
import static jp.gr.java_conf.simply.erviz.plain.BracketPair.ANGLE;
import static jp.gr.java_conf.simply.erviz.plain.BracketPair.CURLY;

/**
 * This class represents a text parser which is used for an entity attribute name line.
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
 *   <li>*attr_name
 *   <li>attr_name*
 *   <li>*attr_name*
 *   <li>*attr_name {name1: key1; name2: key2; ...}
 *   <li>attr_name* {name1: key1; name2: key2; ...}
 *   <li>*attr_name* {name1: key1; name2: key2; ...}
 * </ul>
 * 
 * An entity attribute line consists of two parts as follows:
 * 
 * <ol>
 *   <li>(1)The first part is the attribute name part.
 *      This part is parsed by an instance of{@code EntityAttrNameParser}.
 *   
 *   <li>(2)The second part is the option list part. This part is optional.
 *      The body of this part is enclosed by curly brackets.
 *      This part is parsed by an instance of{@code OptionListParser}.
 *      
 *      As of this version, no options are supported.
 * </ol>
 * 
 * Some spaces around each parts is allowed, and they will be trimmed automatically.
 * For more information about each part, refer the document about the corresponding parser class.
 * 
 * This class is immutable. All input arguments is given at the object construction.
 * 
 * This class is package private. The instance of this class is used 
 * by other parser classes internally.
 * 
 * @author kono
 * @version 1.0
 * @see jp.gr.java_conf.simply.erviz.plain.EntityAttrNameParser
 * @see jp.gr.java_conf.simply.erviz.plain.OptionListParser
 */
final class EntityAttrLineParser {
	
	//input
	private String text;
	
	//output
	private String attrName = "";
	private boolean isPrimaryKey;
	private boolean isForeignKey;
	private OptionMap options = new OptionMap(OptionName.values());
	
	//output (for debug or unit test)
	private String gap1 = "";
	private String attrNamePart = ""; 
	private String gap2 = "";
	private String optionListPart = "";
	private String gap3 = "";
	
	//for exception message
	private final LineData line;
	
	//for internal control
	private boolean partSplited = false;
	
	/**
	 * Constructs an object of this class.
	 * 
	 * @param line the line data which will be parsed in {@code parse()} method
	 */
	EntityAttrLineParser(LineData line) {
		if (line == null) {
			throw new NullPointerException();
		}
		this.text = removeComment(line.getLineText().trim());
		this.line = line;
	}
	
	/**
	 * Constructs an object of this class.
	 * This constructor is supporsed to be used for unit test.
	 * 
	 * @param lineText the line text which will be parsed in {@code parse()} method
	 */
	EntityAttrLineParser(String lineText) {
		this(new LineData(0, lineText));
	}
	
	/**
	 * Returns the name of target attribute.
	 * This method should be called after calling {@code parse()} method.
	 * 
	 * @return an entity attribute name text
	 */
	String getAttrName() {
		return this.attrName;
	}
	
	/**
	 * Returns true if this attribute is a primary key.
	 * 
	 * @return true if this attribute is a primary key.
	 */
	boolean isPrimaryKey() {
		return this.isPrimaryKey;
	}
	
	/**
	 * Returns true if this attribute is a foreign key.
	 * 
	 * @return true if this attribute is a foreign key.
	 */
	boolean isForeignKey() {
		return this.isForeignKey;
	}
	
	/**
	 * Returns a {@code OptionMap} object which contains option values.
	 * 
	 * @return a {@code OptionMap} object
	 */
	OptionMap getOptions() {
		return this.options;
	}
	
	/**
	 * Returns the attribute name part.
	 * This method can be called before calling {@code parse()} method.
	 * 
	 * Note that this is a helper method for debug or unit test.
	 * 
	 * @return the attribute name part text
	 */
	String getAttrNamePart() {
		splitIntoParts();
		return this.attrNamePart;
	}
	
	/**
	 * Returns the option list part.
	 * This method can be called before calling {@code parse()} method.
	 * 
	 * Note that this is a helper method for debug or unit test.
	 * 
	 * @return the option list part text
	 */
	String getOptionListPart() {
		splitIntoParts();
		return this.optionListPart;
	}
	
	/**
	 * Returns the specified gap text.
	 * This method can be called before calling {@code parse()} method.
	 * 
	 * Note that this is a helper method for debug or unit test.
	 * 
	 * @param partNumber any part number from 1 to 3
	 * @return the specified gap text
	 */
	String getGapText(int partNumber) {
		splitIntoParts();
		return (new String[] {this.gap1, this.gap2, this.gap3})[partNumber - 1];
	}
	
	/**
	 * Parses the target text specified by constructor parameters.
	 * The result of parsing will be saved in the object.
	 * 
	 * @throws ParserException if parsing error occured.
	 */
	void parse() throws ParserException {
		splitIntoParts();
		parseAllParts();
	}
	
	private void splitIntoParts() {
		
		if (this.partSplited) {
			return; //this method had been executed.
		} else {
			this.partSplited = true;
		}
		
		List<String> list = split(this.text, SQUARE, ROUND, ANGLE, CURLY);
		
		//find option part
		int optionIndex = list.size(); //greater than any index value
		for (int i = 0; i < list.size(); i++) {
			if (startsWithAnyLeftBracket(list.get(i), CURLY)) { //check left bracket only
				optionIndex = i;
				break;
			}
		}
		
		//split into parts
		for (int i = 0; i < list.size(); i++) {
			final String field = list.get(i);
			
			if (i < optionIndex) {
				this.attrNamePart += field;
			} else if (i == optionIndex) {
				this.optionListPart += field;
			} else {
				this.gap3 += field;
			}
		}
		
		//neglect spaces
		this.gap1 = this.gap1.trim();
		this.attrNamePart = this.attrNamePart.trim();
		this.gap2 = this.gap2.trim();
		this.optionListPart = this.optionListPart.trim();
		this.gap3 = this.gap3.trim();
		
	}
	
	private void parseAllParts() throws ParserException {
		
		//before the attribute name part (must not exist)
		if (!this.gap1.equals("")) {
			throw createException(Message.ERR_INVALID_TEXT_BEFORE, 
					Message.PN_ATTR_NAME.getText(), Message.LT_ATTR.getText(), this.gap1);
		}
		
		//the attribute name part
		{
			EntityAttrNameParser parser = new EntityAttrNameParser(this.attrNamePart, this.line);
			parser.parse();
			
			this.attrName = parser.getAttrName();
			this.isPrimaryKey = parser.isPrimaryKey();
			this.isForeignKey = parser.isForeignKey();
		}
		
		//between the attribute name part and option list part (must not exist)
		if (!this.gap2.equals("")) {
			throw createException(Message.ERR_INVALID_TEXT_BETWEEN,
					Message.PN_ATTR_NAME.getText(), Message.PN_OPTION_LIST.getText(), 
					Message.LT_ATTR.getText(), this.gap2);
		}
		
		//option list part (optional)
		if (!this.optionListPart.equals("")) {
			
			if (!isEnclosedByAnyBrackets(this.optionListPart, CURLY)) { //right bracket not found
				throw createException(Message.ERR_INVALID_BRACKETS, Message.PN_OPTION_LIST.getText(), this.optionListPart);
			}
			
			final String optionListText = removeEnclosingBrackets(this.optionListPart, CURLY);
			final OptionListParser parser = new OptionListParser(optionListText, this.line);
			parser.parse();
			
			for (OptionName optionName : PackageConstants.ATTR_OPTION_NAMES) {
				
				final String optionValue = parser.getOptionValue(optionName.getNameInFiles());
				
				try {
					PackageUtils.setOptionValue(this.options, optionName, optionValue);
				} catch (IllegalArgumentException ex) {
					throw createException(Message.ERR_INVALID_OPTION_VALUE, optionName.name(), optionValue);
				}
				
			}
			
		}
		
		//after option list part (must not exist)
		if (!this.gap3.equals("")) {
			throw createException(Message.ERR_INVALID_TEXT_AFTER, Message.PN_OPTION_LIST.getText(), 
					Message.LT_ATTR.getText(), this.gap3);
		}
		
	}
	
	//private utility method to create exception
	private ParserException createException(Message message, Object... params) {
		final String description = message.getText(params);
		return ParserException.create(description, this.line);
	}
	
}
