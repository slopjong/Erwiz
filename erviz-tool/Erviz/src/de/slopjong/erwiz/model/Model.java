package de.slopjong.erwiz.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents an Entity-Relationship model.
 * 
 * @author kono
 * @version 1.0
 * @see de.slopjong.erwiz.model.Entity
 * @see de.slopjong.erwiz.model.Relationship
 */
public final class Model {
	
	private final List<Entity> entityList = new ArrayList<Entity>();
	private final Map<String, Entity> entityMap = new HashMap<String, Entity>();
	private final List<Relationship> relList = new ArrayList<Relationship>();
	private final OptionMap options;
	
	/**
	 * Creates an instance of this class without entities and relationships.
	 */
	public Model() {
		//no elements
		this(null, null, null);
	}
	
	/**
	 * Creates an instance of this class with entities and relationships.
	 * 
	 * @param entityList entities which is added to this model
	 * @param relList relationships which is added to this model
	 * @param options global options which is applied to this model
	 */
	public Model(List<Entity> entityList, List<Relationship> relList, OptionMap options) {
		
		if (entityList != null) {
			//copy all elements
			this.entityList.addAll(entityList);
			
			//utility maps
			for (Entity e : entityList) {
				this.entityMap.put(e.getName(), e);
			}
		}
		
		if (relList != null) {
			//copy all elements
			this.relList.addAll(relList);
		}
		
		if (options != null) {
			this.options = options;
		} else {
			this.options = new OptionMap(); //empty
		}
		
	}
	
	/**
	 * Retrieves an unmodifiable {@code List} object that contains all entities of 
	 * this Entity-Relationship model.
	 * 
	 * @return an {@code List} object that contains all entities
	 */
	public List<Entity> getEntityList() {
		return Collections.unmodifiableList(this.entityList);
	}
	
	/**
	 * Retrieves an unmodifiable {@code Map} object that contains all entities of 
	 * this Entity-Relationship model. The map maps entity names to entity objects.
	 * 
	 * @return an {@code Map} object that contains all entities
	 */
	public Map<String, Entity> getEntityMap() {
		return 	Collections.unmodifiableMap(this.entityMap);
	}
	
	/**
	 * Retrieves count of entities which this entity contains.
	 * 
	 * @return count of entities which this entity contains.
	 */
	public int getEntityCount() {
		return this.entityList.size();
	}
	
	/**
	 * Retrieves a {@code Entity} object corresponding the specified index.
	 * 
	 * @param index index of entity which this method return
	 * @return a {@code Entity} object corresponding the specified index
	 */
	public Entity getEntity(int index) {
		return this.entityList.get(index);
	}
	
	/**
	 * Retrieves a unmodifiable {@code List} object that contains all relationships of 
	 * this Entity-Relationship model.
	 * 
	 * @return a {@code List} object that contains all relationships
	 */
	public List<Relationship> getRelationshipList() {
		return Collections.unmodifiableList(this.relList);
	}
	
	/**
	 * Retrieves count of relathionships which this entity contains.
	 * 
	 * @return count of relathionships which this entity contains.
	 */
	public int getRelationshipCount() {
		return this.relList.size();
	}
	
	/**
	 * Retrieves a {@code Relationship} object corresponding the specified index.
	 * 
	 * @param index index of relationship which this method return
	 * @return a {@code Relationship} object corresponding the specified index
	 */
	public Relationship getRelationship(int index) {
		return this.relList.get(index);
	}
	
	/**
	 * Retrieves a {@code OptionMap} object.
	 * 
	 * @return options
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
		
		sb.append(indent).append("entity-list:\n");
		for (Entity e : this.entityList) {
			sb.append(e.toString(memberIndent)).append("\n");
			sb.append(memberIndent).append("\n");
		}
		
		sb.append(indent).append("relationship-list:\n");
		for (Relationship e : this.relList) {
			sb.append(e.toString(memberIndent)).append("\n");
			sb.append(memberIndent).append("\n");
		}
		
		return sb.toString();
	}
}
