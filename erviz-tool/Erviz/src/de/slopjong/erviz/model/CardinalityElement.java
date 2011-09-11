package de.slopjong.erviz.model;

/**
 * This enum class represents one side of cardinality between two entities.
 * For example, one or many of one-to-many cardinality.
 * 
 * @author kono
 * @version 1.0
 * @see de.slopjong.erviz.model.CardinalityWithOptionality
 */
public enum CardinalityElement {
	
	/** Not specified */
	NONE,
	
	/** One */
	ONE,
	
	/** Many */
	MANY;
	
}
