package jp.gr.java_conf.simply.erviz.plain;

import static jp.gr.java_conf.simply.erviz.plain.PackageConstants.ENTITY_ATTR_FK_CHAR;
import static jp.gr.java_conf.simply.erviz.plain.PackageConstants.ENTITY_ATTR_PK_CHAR;
import static jp.gr.java_conf.simply.erviz.plain.PackageUtils.isEnclosedByDoubleQuotations;
import static jp.gr.java_conf.simply.erviz.plain.PackageUtils.removeEnclosingDoubleQuotations;
import static jp.gr.java_conf.simply.erviz.plain.PackageUtils.removeFirstCharacter;
import static jp.gr.java_conf.simply.erviz.plain.PackageUtils.removeLastCharacter;

import jp.gr.java_conf.simply.erviz.plain.LineData;

/**
 * This class represents a text parser which is used for an entity attribute name.
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
 *   <li>attr_name
 *   <li>*attr_name
 *   <li>attr_name*
 *   <li>*attr_name*
 * </ul>
 * 
 * A text which is parsed by this class consists of three elements as follows:
 * 
 * <ol>
 *   <li>A primary key mark '*'. This element is optional.
 *   <li>An entity attribute name.
 *   <li>A foreigh key mark '*'. This element is optional.
 * </ol>
 * 
 * Some spaces around each elements is allowed, and they will be trimmed automatically. 
 * 
 * This class is immutable. All input arguments is given at the object construction.
 * 
 * This class is package private. The instance of this class is used by other parser classes internally.
 * 
 * @author kono
 * @version 1.0
 */
final class EntityAttrNameParser {
	
	//input
	private String text;
	
	//output
	private String attrName = "";
	private boolean isPrimaryKey = false;
	private boolean isForeignKey = false;
	
	//for exception message
	private final LineData line;
	
	/**
	 * Constructs an object of this class.
	 * 
	 * @param text text the text which will be parsed in {@code parse()} method
	 * @param line the line data which contains {@code text} argument.
	 * @throws NullPointerException if a null argument is specified
	 */
	EntityAttrNameParser(String text, LineData line) {
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
	EntityAttrNameParser(String text) {
		this(text, new LineData(0, ""));
	}
	
	/**
	 * Returns the name of entity attribute.
	 * This method should be called after calling {@code parse()} method.
	 * 
	 * @return an entity attribute name text
	 */
	String getAttrName() {
		return this.attrName;
	}
	
	/**
	 * Returns true if the attribute is a primary key, false otherwise.
	 * This method should be called after calling {@code parse()} method.
	 * 
	 * @return true if this attribute is a primary key, false otherwise.
	 */
	boolean isPrimaryKey() {
		return this.isPrimaryKey;
	}
	
	/**
	 * Returns true if the attribute is a foreign key, false otherwise.
	 * This method should be called after calling {@code parse()} method.
	 * 
	 * @return true if this attribute is a foreign key, false otherwise.
	 */
	boolean isForeignKey() {
		return this.isForeignKey;
	}
	
	/**
	 * Parses the target text specified by constructor parameters.
	 * The result of parsing will be saved in the object.
	 * 
	 * @throws ParserException if parsing error occured.
	 */
	void parse() throws ParserException {
		
		//judge primary key and foreign key
		this.isPrimaryKey = this.text.startsWith(ENTITY_ATTR_PK_CHAR);
		this.isForeignKey = this.text.endsWith(ENTITY_ATTR_FK_CHAR);
		
		//get name string
		this.attrName = this.text;
		
		if (this.isPrimaryKey) {
			this.attrName = removeFirstCharacter(this.attrName);
		}
		
		if (this.isForeignKey) {
			this.attrName = removeLastCharacter(this.attrName);
		}
		
		if (isEnclosedByDoubleQuotations(this.attrName)) {
			this.attrName = removeEnclosingDoubleQuotations(this.attrName);
		}
		
		if (this.attrName.equals("")) {
			throw createException(Message.ERR_BLANK_ATTR_NAME);
		}
		
		//Name validation is NOT needed.
		//An entity attribute name allows all characters.
		
	}
	
	//private utility method to create exception
	private ParserException createException(Message message, Object... params) {
		final String description = message.getText(params);
		return ParserException.create(description, this.line);
	}
	
}
