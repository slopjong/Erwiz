package de.slopjong.erwiz.model;

/**
 * This class represents a verb phrase in a relationship.
 * The instance of this class contains text and direction.
 * 
 * This class is immutable. All input arguments is given at the object construction.
 * 
 * @author kono
 * @version 1.0
 * @see de.slopjong.erwiz.model.Relationship
 * @see de.slopjong.erwiz.model.VerbDirection
 */
public class VerbPhrase {
	
	private String text;
	private VerbDirection direction;
	
	/**
	 * Constructs an instance of this class.
	 * 
	 * @param text the text of verb phrase in a relationship
	 * @param direction the direction of verb phrase in a relationship
	 */
	public VerbPhrase(String text, VerbDirection direction) {
		this.text = text;
		this.direction = direction;
	}
	
	/**
	 * Retrieves the text of verb phrase in a relationship.
	 * 
	 * @return the text of verb phrase in a relationship
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * Retrieves the direction of verb phrase in a relationship.
	 * 
	 * @return the direction of verb phrase in a relationship
	 */
	public VerbDirection getDirection() {
		return direction;
	}
	
	/**
	 * Returns a string representation of this object.
	 * 
	 * @param indent indent string
	 * @return a string representation of this object
	 */
	public String toString(String indent) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(indent);
		sb.append("[").append(this.getClass().getName()).append("]\n");
		
		sb.append(indent);
		sb.append("text:").append(this.text).append(", ");
		sb.append("direction:").append(this.direction.name().toLowerCase());
		
		return sb.toString();
	}
	
}
