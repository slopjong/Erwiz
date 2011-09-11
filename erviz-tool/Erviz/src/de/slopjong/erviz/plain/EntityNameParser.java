package de.slopjong.erviz.plain;

import static de.slopjong.erviz.plain.PackageUtils.isEnclosedByDoubleQuotations;
import static de.slopjong.erviz.plain.PackageUtils.removeEnclosingDoubleQuotations;
import de.slopjong.erviz.plain.LineData;


/**
 * This class represents a text parser which is used for an entity name.
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
 *   <li>entity_name
 * </ul>
 * 
 * A text which is parsed by this class consists of three elements as follows:
 * 
 * <ol>
 *   <li>A left bracket '[' or '('.
 *   <li>An entity name.
 *   <li>A right bracket ']' or ')'.
 * </ol>
 * 
 * Square brackets indicates the independent entity, and round brackets indicates
 * the dependent entity. Some spaces around each elements is allowed, and they will 
 * be trimmed automatically.
 * 
 * This class is immutable. All input arguments is given at the object construction.
 * 
 * This class is package private. The instance of this class is used by other parser classes internally.
 * 
 * @author kono
 * @version 1.0
 */
final class EntityNameParser {
	
	//input
	private final String text;
	
	//output
	private String entityName = "";
	
	//for exception message
	private final LineData line;
	
	/**
	 * Constructs an object of this class.
	 * 
	 * @param text text the text which will be parsed in {@code parse()} method
	 * @param line the line data which contains {@code text} argument.
	 * @throws NullPointerException if a null argument is specified
	 */
	EntityNameParser(String text, LineData line) {
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
	EntityNameParser(String text) {
		this(text, new LineData(0, ""));
	}
	
	/**
	 * Returns the name of the target entity.
	 * This method should be called after calling {@code parse()} method.
	 * 
	 * @return an entity name text
	 */
	String getEntityName() {
		return this.entityName;
	}
	
	/**
	 * Parses the target text specified by constructor parameters.
	 * The result of parsing will be saved in the object.
	 * 
	 * @throws ParserException if parsing error occured.
	 */
	void parse() throws ParserException {
		
		//get name string
		this.entityName = this.text;
		if (isEnclosedByDoubleQuotations(this.entityName)) {
			this.entityName = removeEnclosingDoubleQuotations(this.entityName);
		}
		
		//blank check
		if (this.entityName.equals("")) {
			throw createException(Message.ERR_BLANK_ENTITY_NAME, this.text);
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
