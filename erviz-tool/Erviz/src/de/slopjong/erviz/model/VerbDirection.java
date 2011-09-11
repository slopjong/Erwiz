package de.slopjong.erviz.model;

/**
 * This enum class represents a direction of a verb phrase in a relationship.
 * 
 * @author kono
 * @version 1.0
 * @see de.slopjong.erviz.model.VerbPhrase
 */
public enum VerbDirection {
	
	/** Not specified */
	NONE,
	
	/** From the first entity to the second entity */
	FIRST_TO_SECOND,
	
	/** From the second entity to the first entity */
	SECOND_TO_FIRST;
	
	/**
	 * Converts the direction to the reverse direction.
	 * 
	 * @return the reverse direction
	 */
	public VerbDirection getReverse() {
		switch (this) {
			case NONE:
				return NONE;
			case FIRST_TO_SECOND:
				return SECOND_TO_FIRST;
			case SECOND_TO_FIRST:
				return FIRST_TO_SECOND;
			default:
				return NONE;
		}
	}
	
}
