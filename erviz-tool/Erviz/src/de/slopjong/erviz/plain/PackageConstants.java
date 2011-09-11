package de.slopjong.erviz.plain;

/**
 * This class defines some constant values for this package.
 * 
 * @author kono
 * @version 1.0
 */
final class PackageConstants {
	
	/**
	 * The character which are used to represents primary keys.
	 */
	static String ENTITY_ATTR_PK_CHAR = "*";
	
	/**
	 * The character which are used to represents foreign keys.
	 */
	static String ENTITY_ATTR_FK_CHAR = "*";
	
	/**
	 * The character which are used to represents verb directions.
	 */
	static String VERB_DIRECTION_CHAR = "-";
	
	/**
	 * The character which are used to represents assignments of option values.
	 */
	static String OPTION_ASSIGNMENT_CHAR = ":";
	
	/**
	 * The character which are used to represents separation of option values.
	 */
	static String OPTION_SEPARATOR_CHAR = ";";
	
	/**
	 * The character which are used to represents a comment head character.
	 */
	static String COMMENT_HEAD_CHAR = "#";
	
	/**
	 * These characters are used to validate following kinds of text.
	 * 
	 * <ul>
	 * <li>option name
	 * <li>option value
	 * </ul>
	 */
	static final String[] OPTION_INVALID_CHARS = {
		BracketPair.SQUARE.getLeft(),
		BracketPair.SQUARE.getRight(),
		BracketPair.ROUND.getLeft(),
		BracketPair.ROUND.getRight(),
		BracketPair.ANGLE.getLeft(),
		BracketPair.ANGLE.getRight(),
		BracketPair.CURLY.getLeft(),
		BracketPair.CURLY.getRight(),
		":",
		";"
	};
	
	/**
	 * Option names used in entity definition.
	 */
	static final OptionName[] ENTITY_OPTION_NAMES = {
		OptionName.COLOR, 
		OptionName.MARK
	};
	
	/**
	 * Option names used in entity attribute definition.
	 */
	static final OptionName[] ATTR_OPTION_NAMES = {
		OptionName.MARK
	};
	
	/**
	 * Option names used in relationship definition.
	 */
	static final OptionName[] RELATIONSHIP_OPTION_NAMES = {
		OptionName.N1, 
		OptionName.N2, 
		OptionName.VERB_REVERSE
		
	};
	
}
