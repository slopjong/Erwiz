package de.slopjong.erviz.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents an entity in Entity-Relationship model.
 * 
 * This class is immutable. All input arguments is given at the object construction.
 * 
 * @author kono
 * @version 1.0
 * @see de.slopjong.erviz.model.Model
 * @see de.slopjong.erviz.model.EntityAttribute
 * @see de.slopjong.erviz.model.OptionMap
 */
public final class Entity {

	private static int objectCount = 0;
	
	private final int idNum;
	private final String id;
	private final String name;
	private final List<EntityAttribute> attributeList = new ArrayList<EntityAttribute>();
	private final Dependency dependency;
	private final OptionMap options;
	
	/**
	 * Constructs an entity attribute object.
	 * 
	 * @param name the name of this entity
	 * @param dependency the dependency of this entity
	 * @param attrList a {@code List} object that contains all attribute objects of this entity
	 * @param options the {@code OptionMap} object
	 */
	public Entity(String name, Dependency dependency, List<EntityAttribute> attrList, 
			OptionMap options) {
		
		this(0, name, dependency, attrList, options);
	}
	
	/**
	 * Constructs an entity attribute object.
	 * 
	 * @param idNum id number (zero means auto number)
	 * @param name the name of this entity
	 * @param dependency the dependency of this entity
	 * @param attrList a {@code List} object that contains all attribute objects of this entity
	 * @param options the {@code OptionMap} object
	 */
	private Entity(int idNum, String name, Dependency dependency, List<EntityAttribute> attrList, 
			OptionMap options) {
		
		if (name == null) {
			throw new NullPointerException("the specified name is null");
		}
		
		this.idNum = (idNum > 0) ? idNum : ++objectCount;
		this.id = "entity_" + this.idNum;
		this.name = name;
		this.dependency = dependency;
		this.attributeList.addAll(attrList);
		this.options = options;
	}
	
	/**
	 * Retrieves the id of this entity object.
	 * 
	 * @return the id of this entity object
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * Retrieves the name of this entity object.
	 * 
	 * @return the name of this entity object
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Retrieves the dependency of this entity.
	 * 
	 * @return the dependency of this entity
	 */
	public Dependency getDependency() {
		return this.dependency;
	}
	
	/**
	 * Retrieves an unmodifiable {@code List} object that contains 
	 * all attribute objects of this entity.
	 * 
	 * @return an unmodifiable {@code List} object that contains all attribute objects of this entity
	 */
	public List<EntityAttribute> getAttributeList() {
		return Collections.unmodifiableList(this.attributeList);
	}
	
	/**
	 * Retrieves count of attributs which this entity contains.
	 * 
	 * @return count of attributs which this entity contains.
	 */
	public int getAttributeCount() {
		return this.attributeList.size();
	}
	
	/**
	 * Retrieves a {@code EntityAttribute} object corresponding the specified index.
	 * 
	 * @param index index of attribute which this method return
	 * @return a {@code EntityAttribute} object corresponding the specified index
	 */
	public EntityAttribute getAttribute(int index) {
		return this.attributeList.get(index);
	}
	
	/**
	 * Retrieves the {@code OptionMap} object of this relationship.
	 * 
	 * @return the {@code OptionMap} object
	 */
	public OptionMap getOptions() {
		return this.options;
	}
	
	/**
	 * Retrieves the entity whose dependency is dependent.
	 * The all member values except for the dependency value, aren't changed.
	 * 
	 * @return the entity whose dependency is dependent.
	 */
	public Entity getDependentEntity() {
		if (this.dependency == Dependency.DEPENDENT) {
			return this;
		} else {
			//the same entity id is used.
			return new Entity(this.idNum, this.name, Dependency.DEPENDENT, this.attributeList, this.options);
		}
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
		sb.append("id:").append(this.id).append(", ");
		sb.append("name:").append(this.name).append(", ");
		sb.append("dependency:").append(this.dependency.name().toLowerCase()).append("\n");
		
		sb.append(indent);
		sb.append("attribute-list:\n");
		for (EntityAttribute attribute : this.attributeList) {
			sb.append(attribute.toString(memberIndent)).append("\n");
		}
		
		sb.append(indent);
		sb.append("options:\n");
		sb.append(this.options.toString(memberIndent));
		
		return sb.toString();
	}
	
}
