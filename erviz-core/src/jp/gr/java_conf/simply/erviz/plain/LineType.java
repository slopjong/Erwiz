package jp.gr.java_conf.simply.erviz.plain;

/**
 * This enum class represents a line type.
 * 
 * @author kono
 * @version 1.0
 */
enum LineType {
	
	/** An unknown line type. */
	UNKNOWN,
	
	/** A blank line which may contain some space characters. */
	BLANK,
	
	/** A comment only line which behave like a blank line. */
	COMMENT_ONLY,
	
	/** A line which has global options. */
	GLOBAL_OPTIONS,
	
	/** An entity name line. */
	ENTITY_NAME,
	
	/** An entity attribute line. */
	ENTITY_ATTRIBUTE,
	
	/** A relationship line. */
	RELATIONSHIP
	
}
