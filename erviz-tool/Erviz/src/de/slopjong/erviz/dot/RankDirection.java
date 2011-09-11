package de.slopjong.erviz.dot;

/**
 * This class represents the rank direction in DOT files.
 * 
 * @author kono
 * @version 1.0
 */
enum RankDirection {
	
	/** left-to-right */
	LEFT_TO_RIGHT("LR"),
	
	/** right-to-left */
	TOP_TO_BOTTOM("TB");
	
	private final String value;
	
	/***
	 * Creates an instance of this class.
	 * 
	 * @param value the direction value string
	 */
	RankDirection(String value) {
		this.value = value;
	}
	
	/**
	 * Retrieves the direction value string.
	 * 
	 * @return the direction value string
	 */
	String getValue() {
		return this.value;
	}
}
