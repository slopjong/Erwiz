package de.slopjong.erviz.model;

/**
 * This enum class defines relationship types.
 * These relationship type names are derived form IDEF1X definition.
 * 
 * @author kono
 */
public enum RelationshipType {

	/** Not specified */
	NONE,
	
	/** Identifying relationship. */
	IDENTIFYING,
	
	/** Non-identifying relationship. */
	NON_IDENTIFYING,
	
	/** Non-specific relationship. */
	NON_SCPECIFIC,
	
	/** Complete categorization relationship. */
	CATEGORIZATION_COMPLETE,
	
	/** Incomplete categorization relationship. */
	CATEGORIZATION_INCOMPLETE;
	
}
