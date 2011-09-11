package de.slopjong.erviz.plain;

/**
 * An instance of this class represents a pair of brackets.
 * 
 * @author kono
 * @version 1.0
 */
enum BracketPair {
	
	/** Square bracktes ('[' and ']') */
	SQUARE("[", "]"),
	
	/** Round bracktes ('(' and ')') */
	ROUND("(", ")"),
	
	/** Angle bracktes ('<' and '>') */
	ANGLE("<", ">"),
	
	/** Curly bracktes ('{' and '}') */
	CURLY("{", "}");
	
	private String[] brackets;
	
	/**
	 * Constructs an instance of this class
	 * 
	 * @param left a left bracket
	 * @param right a right bracket
	 */
	private BracketPair (String left, String right) {
		this.brackets = new String[] {left, right};
	}
	
	/**
	 * Gets the left bracket of the pair.
	 * 
	 * @return the left bracket of the pair.
	 */
	String getLeft() {
		return this.brackets[0];
	}
	
	/**
	 * Gets the right bracket of the pair.
	 * 
	 * @return the right bracket of the pair.
	 */
	String getRight() {
		return this.brackets[1];
	}
	
	/**
	 * Gets the the  brackets array object.
	 * the first element is the left character and the second character 
	 * is the right character.
	 * 
	 * @return the  brackets array object
	 */
	String[] getStringArray() {
		return this.brackets;
	}
	
	/**
	 * Finds {@code BracketPair} object which is corresponding to
	 * the specified left bracket.
	 * 
	 * @param left the left bracket
	 * @return the found {@code BracketPair} object, null if not found.
	 */
	static BracketPair findByLeftBracket(String left) {
		for (BracketPair pair : BracketPair.values()) {
			if (pair.getLeft().equals(left)) {
				return pair;
			}
		}
		return null;	
	}
	
}
