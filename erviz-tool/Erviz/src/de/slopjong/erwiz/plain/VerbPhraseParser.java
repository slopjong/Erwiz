package de.slopjong.erwiz.plain;

import de.slopjong.erwiz.model.VerbDirection;
import de.slopjong.erwiz.model.VerbPhrase;
import de.slopjong.erwiz.plain.LineData;

import static de.slopjong.erwiz.plain.PackageConstants.VERB_DIRECTION_CHAR;
import static de.slopjong.erwiz.plain.PackageUtils.isEnclosedByDoubleQuotations;
import static de.slopjong.erwiz.plain.PackageUtils.removeEnclosingDoubleQuotations;
import static de.slopjong.erwiz.plain.PackageUtils.removeFirstCharacter;
import static de.slopjong.erwiz.plain.PackageUtils.removeLastCharacter;


/**
 * This class represents a text parser which is used for a verb phrase which a relationship has.
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
 *   <li>verb_phrase
 *   <li>verb_phrase-
 *   <li>-verb_phrase
 * </ul>
 * 
 * A text which is parsed by this class consists of three elements as follows:
 * 
 * <ol>
 *   <li>A direction mark '-'. This element is optional.
 *   <li>A verb phrase.
 *   <li>A direction mark '-'. This element is optional.
 * </ol>
 * 
 * The left mark '-' and right mark '-' of the verb phrase indicates that 
 * the direction of the verb is "from the second entity to the first entity" and
 * "from the first entity to the second entity", respectively. The text contains
 * the two '-' signs exclusively.
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
final class VerbPhraseParser {
	
	//input
	private String text;
	
	//output
	private VerbPhrase verbPhrase = new VerbPhrase("", VerbDirection.NONE);
	
	//for exception message
	private final LineData line;
	
	/**
	 * Constructs an object of this class.
	 * 
	 * @param text text the text which will be parsed in {@code parse()} method
	 * @param line the line data which contains {@code text} argument.
	 * @throws NullPointerException if a null argument is specified
	 */
	VerbPhraseParser(String text, LineData line) {
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
	VerbPhraseParser(String text) {
		this(text, new LineData(0, ""));
	}
	
	/**
	 * Returns a {@code VerbPhrase} object
	 * This method should be called after calling {@code parse()} method.
	 * 
	 * @return a {@code VerbPhrase} object
	 */
	VerbPhrase getVerbPhrase() {
		return this.verbPhrase;
	}
	
	/**
	 * Parses the target text specified by constructor parameters.
	 * The result of parsing will be saved in the object.
	 * 
	 * @throws ParserException if parsing error occured.
	 */
	void parse() throws ParserException {
		
		//get name string
		String verbText;
		VerbDirection verbDirection;
		
		if (this.text.startsWith(VERB_DIRECTION_CHAR) && 
				this.text.endsWith(VERB_DIRECTION_CHAR)) {
			throw createException(Message.ERR_INVALID_VERB_DIR, this.text);
			
		} else if (this.text.endsWith(VERB_DIRECTION_CHAR)) {
			
			verbDirection = VerbDirection.FIRST_TO_SECOND;
			verbText = removeLastCharacter(this.text);
			
		} else if (this.text.startsWith(VERB_DIRECTION_CHAR)) {
			
			verbDirection = VerbDirection.SECOND_TO_FIRST;
			verbText = removeFirstCharacter(this.text);
			
		} else {
			
			verbDirection = VerbDirection.NONE;
			verbText = this.text;
		}
		
		if (isEnclosedByDoubleQuotations(verbText)) {
			verbText = removeEnclosingDoubleQuotations(verbText);
		}
		
		//blank check of verb phrase
		if (verbText.equals("")) {
			throw createException(Message.ERR_BLANK_VERB_PHRASE);
		}
		
		this.verbPhrase = new VerbPhrase(verbText, verbDirection);
	}
	
	//private utility method to create exception
	private ParserException createException(Message message, Object... params) {
		final String description = message.getText(params);
		return ParserException.create(description, this.line);
	}
	
}
