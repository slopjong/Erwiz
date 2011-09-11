package de.slopjong.erviz.model;

/**
 * This class represents the cardinality of a relationship between two entities,
 * and it has a cardinality information and an optionality information.
 * 
 * This class is immutable. All input arguments is given at the object construction.
 * 
 * @author kono
 * @version 1.0
 * 
 * @see de.slopjong.erviz.model.Relationship
 * @see de.slopjong.erviz.model.CardinalityElement
 * @see de.slopjong.erviz.model.OptionalityElement
 */
public final class CardinalityWithOptionality {
	
	private final CardinalityElement ce1;
	private final OptionalityElement oe1;
	private final CardinalityElement ce2;
	private final OptionalityElement oe2;
	
	/**
	 * Construct an instance of this class
	 * 
	 * @param ce1 the cardinality element of the first entity side
	 * @param oe1 the optionality element of the first entity side
	 * @param ce2 the cardinality element of the second entity side
	 * @param oe2 the optionality element of the second entity side
	 */
	public CardinalityWithOptionality(CardinalityElement ce1, OptionalityElement oe1, 
			CardinalityElement ce2, OptionalityElement oe2) {
		
		this.ce1 = ce1;
		this.oe1 = oe1;
		this.ce2 = ce2;
		this.oe2 = oe2;
	}
	
	/**
	 * Retrieves the cardinality element of the first entity side.
	 * 
	 * @return the cardinality element of the first entity side
	 */
	public CardinalityElement getCardinality1() {
		return this.ce1;
	}
	
	/**
	 * Retrieves the optionality element of the first entity side.
	 * 
	 * @return the optionality element of the first entity side
	 */
	public OptionalityElement getOptionality1() {
		return this.oe1;
	}
	
	/**
	 * Retrieves the cardinality element of the second entity side.
	 * 
	 * @return the cardinality element of the second entity side
	 */
	public CardinalityElement getCardinality2() {
		return this.ce2;
	}
	
	/**
	 * Retrieves the optionality element of the second entity side.
	 * 
	 * @return the optionality element of the second entity side
	 */
	public OptionalityElement getOptionality2() {
		return this.oe2;
	}
	
	/**
	 * Returns a string representation of this object.
	 * 
	 * @param indent indent string
	 * @return a string representation of this object.
	 */
	public String toString(String indent) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(indent);
		sb.append("[").append(this.getClass().getName()).append("]\n");
		
		sb.append(indent);
		sb.append("cardinality1:").append(this.ce1.name().toLowerCase()).append(", ");
		sb.append("optionality1:").append(this.oe1.name().toLowerCase()).append(", ");
		sb.append("cardinality2:").append(this.ce2.name().toLowerCase()).append(", ");
		sb.append("optionality2:").append(this.oe2.name().toLowerCase());
		
		return sb.toString();
	}
	
}
