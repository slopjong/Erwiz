package de.slopjong.erviz.model;

/**
 * A class represents an attribute of an entity.
 * This class is immutable.
 * 
 * @author kono
 * @version 1.0
 * @see de.slopjong.erviz.model.Entity
 * @see de.slopjong.erviz.model.OptionMap
 */
public final class EntityAttribute {
	
	private final String name;
	private final boolean isPrimaryKey;
	private final boolean isForeignKey;
	private final OptionMap options;
	
	/**
	 * Constructs an entity attribute object.
	 * 
	 * @param name the name of this attribute
	 * @param isPrimaryKey true if this attribute is the part of the primary key of the entity. 
	 * @param isForeignKey true if this attribute is the part of the foreign key of the entity. 
	 * @param options a {@code OptionMap} object
	 */
	public EntityAttribute(String name, boolean isPrimaryKey, boolean isForeignKey,
			OptionMap options) {
		this.name = name;
		this.isPrimaryKey = isPrimaryKey;
		this.isForeignKey = isForeignKey;
		this.options = options;
	}
	
	/**
	 * Returns the name of this attribute.
	 * 
	 * @return the name of this attribute.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns true if this attribute is the primary key of the entity, otherwise false.
	 * 
	 * @return true if this attribute is the primary key of the entity, otherwise false.
	 */
	public boolean isPrimaryKey() {
		return this.isPrimaryKey;
	}
	
	/**
	 * Returns true if this attribute is the foreign key of the entity, otherwise false.
	 * 
	 * @return true if this attribute is the foreign key of the entity, otherwise false.
	 */
	public boolean isForeignKey() {
		return this.isForeignKey;
	}
	
	/**
	 * Retrieve the {@code OptionMap} object of this relationship.
	 * 
	 * @return the {@code OptionMap} object
	 */
	public OptionMap getOptions() {
		return this.options;
	}
	
	/**
	 * Returns a string representation of this object.
	 * 
	 * @param indent indent string
	 * @return a string representation of this object.
	 */
	public String toString(String indent) {
		
		StringBuilder sb = new StringBuilder();
		String memberIndent = indent + "\t";
		
		sb.append(indent);
		sb.append("[").append(this.getClass().getName()).append("]\n");
		
		sb.append(indent);
		sb.append("name:").append(this.name).append(", ");
		sb.append("primary-key:").append(this.isPrimaryKey).append(", ");
		sb.append("foreign-key:").append(this.isForeignKey).append("\n");
		
		sb.append(indent);
		sb.append("options:\n");
		sb.append(this.options.toString(memberIndent));
		
		return sb.toString();
	}
	
}
