package de.slopjong.erviz.plain;

import java.util.List;

import de.slopjong.erviz.model.CardinalityElement;
import de.slopjong.erviz.model.CardinalityWithOptionality;
import de.slopjong.erviz.model.Dependency;
import de.slopjong.erviz.model.OptionMap;
import de.slopjong.erviz.model.VerbDirection;
import de.slopjong.erviz.model.VerbPhrase;
import de.slopjong.erviz.plain.LineData;


import static de.slopjong.erviz.plain.BracketPair.ANGLE;
import static de.slopjong.erviz.plain.BracketPair.CURLY;
import static de.slopjong.erviz.plain.BracketPair.ROUND;
import static de.slopjong.erviz.plain.BracketPair.SQUARE;
import static de.slopjong.erviz.plain.PackageUtils.isEnclosedByAnyBrackets;
import static de.slopjong.erviz.plain.PackageUtils.removeComment;
import static de.slopjong.erviz.plain.PackageUtils.removeEnclosingBrackets;
import static de.slopjong.erviz.plain.PackageUtils.split;
import static de.slopjong.erviz.plain.PackageUtils.startsWithAnyLeftBracket;

/**
 * This class represents a text parser which is used for a relationship line.
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
 *   <li>[entity_name1] ?--* [entity_name2]
 *   <li>(entity_name1) 1--+ [entity_name2] <verb_phrase>
 *   <li>[entity_name1] ?--1 (entity_name2) <verb_phrase-> {name1: value1}
 *   <li>[entity_name1] ---- [entity_name2] <-verb_phrase> {name1: value1; name2: value2}
 * </ul>
 * 
 * A relationship line consists of five parts as follows:
 * 
 * <ol>
 *   <li>The first part is the fitst entity part.
 *      The body of this part is enclosed by square brackets or round brackets.
 *      Square brackets indicates the independent entity, and round brackets indicates
 *      the dependent entity on this relationship. This entity name is parsed by 
 *      an instance of {@code EntityNameParser}.
 * 
 *   <li>The second part is the cardinality part.
 * 	    This part is positioned between two entity parts.
 *      This part is parsed by an instance of{@code CardinalityParser}.
 * 
 *   <li>The third part is the second entity part.
 *      The body of this part is enclosed by square brackets or round brackets.
 *      Square brackets indicates the independent entity, and round brackets indicates
 *      the dependent entity on this relationship. This entity name is parsed by 
 *      an instance of {@code EntityNameParser}.
 *      
 *   <li>The fourth part is the verb phrase part. This part is optional.
 *      The body of this part is enclosed by angle brackets.
 *      This part is parsed by an instance of{@code VerbPhraseParser}.
 *    
 *   <li>The fifth part is the option list part. This part is optional.
 *      The body of this part is enclosed by curly brackets.
 *      This part is parsed by an instance of{@code OptionListParser}.
 *      
 *      As of this version, the option supported this class is only "reverse".
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
 * @see de.slopjong.erviz.plain.EntityNameParser
 * @see de.slopjong.erviz.plain.CardinalityParser
 * @see de.slopjong.erviz.plain.VerbPhraseParser
 * @see de.slopjong.erviz.plain.OptionListParser
 */
final class RelationshipLineParser {
	
	//input
	private String text;
	
	//output
	private String nameOfEntity1 = "";
	private Dependency dependencyOfEntity1 = Dependency.NONE;
	private String nameOfEntity2 = "";
	private Dependency dependencyOfEntity2 = Dependency.NONE;
	private CardinalityWithOptionality cwo;
	private VerbPhrase verbPhrase = new VerbPhrase("", VerbDirection.NONE);
	private OptionMap options = new OptionMap(OptionName.values());
	
	//output (for debug or unit test)
	private String gap1 = "";
	private String entityPart1 = ""; 
	private String gap2 = "";
	private String cardinalityPart = "";
	private String gap3 = "";
	private String entityPart2 = "";
	private String gap4 = "";
	private String verbPhrasePart = "";
	private String gap5 = "";
	private String optionListPart = "";
	private String gap6 = "";
	
	//for exception message
	private final LineData line;
	
	//for internal control
	private boolean partSplited = false;
	
	/**
	 * Constructs an object of this class.
	 * 
	 * @param line the line data which will be parsed in {@code parse()} methodd
	 * @throws NullPointerException if a null argument is specified
	 */
	RelationshipLineParser(LineData line) {
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
	public RelationshipLineParser(String lineText) {
		this(new LineData(0, lineText));
	}
	
	/**
	 * Returns the first entity name.
	 * This method should be called after calling {@code parse()} method.
	 * 
	 * @return the first entity name
	 */
	String getNameOfEntity1() {
		return this.nameOfEntity1;
	}
	
	/**
	 * Retrieves the dependency of the first entity on this relationship.
	 * 
	 * @return the dependency of the first entity on this relationship
	 */
	Dependency getDependecyOfEntity1() {
		return this.dependencyOfEntity1;
	}
	
	/**
	 * Returns the second entity name.
	 * This method should be called after calling {@code parse()} method.
	 * 
	 * @return the second entity name
	 */
	String getNameOfEntity2() {
		return this.nameOfEntity2;
	}
	
	/**
	 * Retrieves the dependency of the second entity on this relationship.
	 * 
	 * @return the dependency of the second entity on this relationship
	 */
	Dependency getDependecyOfEntity2() {
		return this.dependencyOfEntity2;
	}
	
	/**
	 * Returns a [@code CardinalityWithOptionality] object.
	 * This method should be called after calling {@code parse()} method.
	 * 
	 * @return a [@code CardinalityWithOptionality] object
	 */
	CardinalityWithOptionality getCardinality() {
		return this.cwo;
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
	 * Returns a {@code OptionMap} object which contains option values.
	 * 
	 * @return a {@code OptionMap} object
	 */
	OptionMap getOptions() {
		return this.options;
	}
	
	/**
	 * Returns the first entity part.
	 * This method can be called before calling {@code parse()} method.
	 * 
	 * Note that this is a helper method for debug or unit test.
	 * 
	 * @return the name part text
	 */
	String getEntityPart1() {
		splitIntoParts();
		return this.entityPart1;
	}
	
	/**
	 * Returns the cardinality part.
	 * This method can be called before calling {@code parse()} method.
	 * 
	 * Note that this is a helper method for debug or unit test.
	 * 
	 * @return the name part text
	 */
	 String getCardinalityPart() {
		splitIntoParts();
		return this.cardinalityPart;
	}
	
	/**
	 * Returns the second entity part.
	 * This method can be called before calling {@code parse()} method.
	 * 
	 * Note that this is a helper method for debug or unit test.
	 * 
	 * @return the name part text
	 */
	String getEntityPart2() {
		splitIntoParts();
		return this.entityPart2;
	}
	
	/**
	 * Returns the verb phrase part.
	 * This method can be called before calling {@code parse()} method.
	 * 
	 * Note that this is a helper method for debug or unit test.
	 * 
	 * @return the option part text
	 */
	String getVerbPhrasePart() {
		splitIntoParts();
		return this.verbPhrasePart;
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
	 * @param partNumber any part number from 1 to 6
	 * @return the specified gap text
	 */
	String getGapText(int partNumber) {
		splitIntoParts();
		return (new String[] {this.gap1, this.gap2, this.gap3, 
			this.gap4, this.gap5, this.gap6})[partNumber - 1];
	}
	
	/**
	 * Parses the text specified by calling {@code setTargetText()}.
	 * The result will be held in this object.
	 * 
	 * @throws ParserException if parsing error occured.
	 */
	void parse() throws ParserException {
		splitIntoParts();
		parseAllParts();
		validate();
	}
	
	private void splitIntoParts() {
		
		if (this.partSplited) {
			return; //this method had been executed.
		} else {
			this.partSplited = true;
		}
		
		List<String> list = split(this.text, SQUARE, ROUND, ANGLE, CURLY);
		
		//find the first entity part
		int entity1Index = list.size(); //greater than any index value
		for (int i = 0; i < list.size(); i++) {
			if (startsWithAnyLeftBracket(list.get(i), SQUARE, ROUND)) { //check left bracket only
				entity1Index = i;
				break;
			}
		}
		
		//find the second entity part
		int entity2Index = list.size(); //greater than any index value
		for (int i = entity1Index + 1; i < list.size(); i++) {
			if (startsWithAnyLeftBracket(list.get(i), SQUARE, ROUND)) { //check left bracket only
				entity2Index = i;
				break;
			}
		}
		
		//find verb phrase part
		int vpIndex = entity2Index; //same as previous part
		for (int i = entity2Index + 1; i < list.size(); i++) {
			if (startsWithAnyLeftBracket(list.get(i), ANGLE)) { //check left bracket only
				vpIndex = i;
				break;
			}
		}
		
		//find option part
		int optionIndex = vpIndex; //same as previous part
		for (int i = vpIndex + 1; i < list.size(); i++) {
			if (startsWithAnyLeftBracket(list.get(i), CURLY)) { //check left bracket only
				optionIndex = i;
				break;
			}
		}
		
		//split into parts
		for (int i = 0; i < list.size(); i++) {
			final String field = list.get(i);
			
			if (i < entity1Index) {
				this.gap1 += field;
			} else if (i == entity1Index) {
				this.entityPart1 += field;
			} else if (i < entity2Index) {
				this.cardinalityPart += field;
			} else if (i == entity2Index) {
				this.entityPart2 += field;
			} else if (i < vpIndex) {
				this.gap4 += field;
			} else if (i == vpIndex) {
				this.verbPhrasePart += field;
			} else if (i < optionIndex) {
				this.gap5 += field;
			} else if (i == optionIndex) {
				this.optionListPart += field;
			} else {
				this.gap6 += field;
			}
			
		}
		
		//neglect spaces
		this.gap1 = this.gap1.trim();
		this.entityPart1 = this.entityPart1.trim();
		this.gap2 = this.gap2.trim();
		this.cardinalityPart = this.cardinalityPart.trim();
		this.gap3 = this.gap3.trim();
		this.entityPart2 = this.entityPart2.trim();
		this.gap4 = this.gap4.trim();
		this.verbPhrasePart = this.verbPhrasePart.trim();
		this.gap5 = this.gap5.trim();
		this.optionListPart = this.optionListPart.trim();
		this.gap6 = this.gap6.trim();
		
	}
	
	private void parseAllParts() throws ParserException {
		
		//before the 1st entity part (must not exist)
		if (!this.gap1.equals("")) {
			throw createException(Message.ERR_INVALID_TEXT_BEFORE, Message.PN_ENTITY_NAME_1.getText(), 
					Message.LT_REL.getText(), this.gap1);
		}
		
		//the 1st entity part
		{
			if (!isEnclosedByAnyBrackets(this.entityPart1, SQUARE, ROUND)) { //right bracket not found
				throw createException(Message.ERR_INVALID_BRACKETS, Message.PN_ENTITY_NAME_1.getText(), this.entityPart1);
			}
			
			String name = removeEnclosingBrackets(this.entityPart1, SQUARE, ROUND);
			EntityNameParser parser = new EntityNameParser(name, this.line);
			parser.parse();
			this.nameOfEntity1 = parser.getEntityName();
			this.dependencyOfEntity1 = isEnclosedByAnyBrackets(this.entityPart1, SQUARE) ?
					Dependency.INDEPENDENT : Dependency.DEPENDENT;
		}
		
		//between the 1st entity part and cardinality part (must not exist)
		if (!this.gap2.equals("")) {
			throw createException(Message.ERR_INVALID_TEXT_BETWEEN,
					Message.PN_ENTITY_NAME_1.getText(), Message.PN_CARDINALITY.getText(), 
					Message.LT_REL.getText(), this.gap2);
		}
		
		//cardinality part
		{
			CardinalityParser parser = new CardinalityParser(this.cardinalityPart, this.line);
			parser.parse();
			this.cwo = parser.getCwo();
		}
		
		//between the cardinality part and the 2nd entity part (must not exist)
		if (!this.gap3.equals("")) {
			throw createException(Message.ERR_INVALID_TEXT_BETWEEN,
					Message.PN_CARDINALITY.getText(), Message.PN_ENTITY_NAME_2.getText(), 
					Message.LT_REL.getText(), this.gap3);
		}
		
		//the 2nd entity part
		{
			if (!isEnclosedByAnyBrackets(this.entityPart2, SQUARE, ROUND)) { //right bracket not found
				throw createException(Message.ERR_INVALID_BRACKETS, Message.PN_ENTITY_NAME_2.getText(), this.entityPart2);
			}
			
			String name = removeEnclosingBrackets(this.entityPart2, SQUARE, ROUND);
			EntityNameParser parser = new EntityNameParser(name, this.line);
			parser.parse();
			this.nameOfEntity2 = parser.getEntityName();
			this.dependencyOfEntity2 = isEnclosedByAnyBrackets(this.entityPart2, SQUARE) ?
					Dependency.INDEPENDENT : Dependency.DEPENDENT;
		}
		
		//between the 2nd entity part and the verb phrase part (must not exist)
		if (!this.gap4.equals("")) {
			throw createException(Message.ERR_INVALID_TEXT_BETWEEN,
					Message.PN_ENTITY_NAME_2.getText(), Message.PN_VERB_PHRASE.getText(), 
					Message.LT_REL.getText(), this.gap4);
		}
		
		//the verb phrase part (optional)
		if (!this.verbPhrasePart.equals("")) {
			if (!isEnclosedByAnyBrackets(this.verbPhrasePart, ANGLE)) { //right bracket not found
				throw createException(Message.ERR_INVALID_BRACKETS, Message.PN_VERB_PHRASE.getText(), this.verbPhrasePart);
			}
			
			String vp = removeEnclosingBrackets(this.verbPhrasePart, ANGLE);
			VerbPhraseParser parser = new VerbPhraseParser(vp, this.line);
			parser.parse();
			this.verbPhrase = parser.getVerbPhrase();
		}
		
		//between the verb phrase part and the option list part (must not exist)
		if (!this.gap5.equals("")) {
			throw createException(Message.ERR_INVALID_TEXT_BETWEEN,
					Message.PN_VERB_PHRASE.getText(), Message.PN_OPTION_LIST.getText(), 
					Message.LT_REL.getText(), this.gap5);
		}
		
		//the option list part (optional)
		if (!this.optionListPart.equals("")) {
			if (!isEnclosedByAnyBrackets(this.optionListPart, CURLY)) { //right bracket not found
				throw createException(Message.ERR_INVALID_BRACKETS, Message.PN_OPTION_LIST.getText(), this.optionListPart);
			}
			
			String optionListText = removeEnclosingBrackets(this.optionListPart, CURLY);
			OptionListParser parser = new OptionListParser(optionListText, this.line);
			parser.parse();
			
			for (OptionName optionName : PackageConstants.RELATIONSHIP_OPTION_NAMES) {
				
				final String optionValue = parser.getOptionValue(optionName.getNameInFiles());
				
				try {
					PackageUtils.setOptionValue(this.options, optionName, optionValue);
				} catch (IllegalArgumentException ex) {
					throw createException(Message.ERR_INVALID_OPTION_VALUE, optionName.name(), optionValue);
				}
				
			}
			
		}
		
		//after the option list part (must not exist)
		if (!this.gap6.equals("")) {
			throw createException(Message.ERR_INVALID_TEXT_AFTER,
					Message.PN_OPTION_LIST.getText(), Message.LT_REL.getText(), this.gap6);
		}
		
	}
	
	private void validate() throws ParserException {
		
		//the number of dependent entities must be at most one.
		if (this.dependencyOfEntity1 == Dependency.DEPENDENT && this.dependencyOfEntity2 == Dependency.DEPENDENT) {
			throw createException(Message.ERR_DEPENDENT_ENTITIES);
		}
		
		//if one to many, parent (one) mustn't be dependent.
		if (this.cwo.getCardinality1() == CardinalityElement.ONE && 
				this.cwo.getCardinality2() == CardinalityElement.MANY &&
				this.dependencyOfEntity1 == Dependency.DEPENDENT) {
			
			throw createException(Message.ERR_DEPENDENT_PARENT);
		}
		
		//if many to one, parent (one) mustn't be dependent.
		if (this.cwo.getCardinality1() == CardinalityElement.MANY && 
				this.cwo.getCardinality2() == CardinalityElement.ONE &&
				this.dependencyOfEntity2 == Dependency.DEPENDENT) {
			
			throw createException(Message.ERR_DEPENDENT_PARENT);
		}
		
		//if many to many, two entities must be independent.
		if (this.cwo.getCardinality1() == CardinalityElement.MANY && 
				this.cwo.getCardinality2() == CardinalityElement.MANY &&
				!(this.dependencyOfEntity1 == Dependency.INDEPENDENT && this.dependencyOfEntity2 == Dependency.INDEPENDENT)) {
			
			throw createException(Message.ERR_DEPENDENT_ENTITY_MTM);
		}
		
	}
	
	//private utility method to create exception
	private ParserException createException(Message message, Object... params) {
		final String description = message.getText(params);
		return ParserException.create(description, this.line);
	}
	
}
