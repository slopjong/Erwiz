package jp.gr.java_conf.simply.erviz.plain;

import jp.gr.java_conf.simply.erviz.plain.LineData;
import jp.gr.java_conf.simply.erviz.model.CardinalityElement;
import jp.gr.java_conf.simply.erviz.model.CardinalityWithOptionality;
import jp.gr.java_conf.simply.erviz.model.OptionalityElement;

/**
 * This class represents a text parser which is used for a cardinality.
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
 *   <li>1--*
 *   <li>?--+
 *   <li>----
 * </ul>
 * 
 * A text which is parsed by this class must satisfy the following conditions:
 * 
 * <ol>
 *   <li>The length is 4.
 *   <li>The 1st character and the 4th character must be one of the following characters.<br>
 *      '?', '1', '*', '+', '-'<br>
 *      These character indicates cardinality and optionality.
 *      For more information, refer the implementation of this class.
 *   <li>The 2nd character and the 3rd character must be '-'.
 *   <li>If the 1st character is '-', the 4th character must be '-', and vice versa.
 * </ol>
 * 
 * Some spaces around this four characters text is allowed, and they 
 * will be trimmed automatically.
 * 
 * This class is immutable. All input arguments is given at the object construction.
 * 
 * This class is package private. The instance of this class is used by other parser classes modelly.
 * 
 * @author kono
 * @version 1.0
 */
class CardinalityParser {
	
	//input
	private String text;
	
	//output
	private CardinalityWithOptionality cwo;
	
	//for exception message
	private final LineData line;
	
	/**
	 * Constructs an object of this class.
	 * 
	 * @param text text the text which will be parsed in {@code parse()} method
	 * @param line the line data which contains {@code text} argument.
	 * @throws NullPointerException if a null argument is specified
	 */
	CardinalityParser(String text, LineData line) {
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
	CardinalityParser(String text) {
		this(text, new LineData(0, ""));
	}

	/**
	 * Returns a [@code CardinalityWithOptionality] object.
	 * This method should be called after calling {@code parse()} method.
	 * 
	 * @return a [@code CardinalityWithOptionality] object
	 */
	CardinalityWithOptionality getCwo() {
		return this.cwo;
	}
	
	/**
	 * Parses the target text specified by constructor parameters.
	 * The result of parsing will be saved in the object.
	 * 
	 * @throws ParserException if parsing error occured.
	 */
	void parse() throws ParserException {
		
		if (text.equals("")) {
			throw createException(Message.ERR_BLANK_CARDINALITY);	
		}
		
		if (!text.matches("^.--.$")) {
			throw createException(Message.ERR_INVALID_CARDINALITY_FORMAT, text);
		}
		
		String ch1 = this.text.substring(0, 1); //the first character
		String ch2 = this.text.substring(3, 4); //the last character
		
		CardinalityElement ce1 = toCardinalityElement(ch1);
		OptionalityElement oe1 = toOptionalityElement(ch1);
		CardinalityElement ce2 = toCardinalityElement(ch2);
		OptionalityElement oe2 = toOptionalityElement(ch2);
		
		if (ce1 == CardinalityElement.NONE && ce2 != CardinalityElement.NONE ||
				ce1 != CardinalityElement.NONE && ce2 == CardinalityElement.NONE) {
			
			throw createException(Message.ERR_ONE_CARDINALITY_SYMBOLS, text);
		}
		
		this.cwo = new CardinalityWithOptionality(ce1, oe1, ce2, oe2);
	}
	
	//private utility method
	//If you modify this method, you should also modify toOptionalityElement().
	CardinalityElement toCardinalityElement(String ch) throws ParserException {
		
		assert (ch != null) : "Cardinality element character is null.";
		
		if (ch.equals("-")) {
			return CardinalityElement.NONE;
		}
		
		if (ch.equals("?") || ch.equals("1")) {
			return CardinalityElement.ONE;
		}
		
		if (ch.equals("*") || ch.equals("+")) {
			return CardinalityElement.MANY;
		} 
		
		throw createException(Message.ERR_INVALID_CARDINALITY_CHAR, ch);
	}
	
	//private utility method
	//If you modify this method, you should also modify toCardinalityElement().
	private OptionalityElement toOptionalityElement(String ch) throws ParserException {
		
		assert (ch != null) : "Optionality element character is null.";
		
		if (ch.equals("-")) {
			return OptionalityElement.NONE;
		}
		
		if (ch.equals("?") || ch.equals("*")) {
			return OptionalityElement.OPTIONAL;
		}
		
		if (ch.equals("1") || ch.equals("+")) {
			return OptionalityElement.MANDATORY;
		}
		
		throw createException(Message.ERR_INVALID_CARDINALITY_CHAR, ch);
	}
	
	//private utility method to create exception
	private ParserException createException(Message message, Object... params) {
		final String description = message.getText(params);
		return ParserException.create(description, this.line);
	}
	
}
