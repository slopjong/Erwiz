package de.slopjong.erviz.dot;

import java.util.List;

import de.slopjong.erviz.model.Entity;
import de.slopjong.erviz.model.Relationship;


/**
 * All entity/relationship generators in this package implement this interface.
 * Entity/relationship generators generate node lines and edge lines to wtire dot files.
 * 
 * @author kono
 * @version 1.0
 */
public interface ERGenerator {
	
	/**
	 * Generates dot lines for an entity.
	 * 
	 * @param entity the entity
	 * @return dot lines for an entity
	 */
	List<String> generateEntityLines(Entity entity);
	
	/**
	 * Generates dot lines for a relationship.
	 * 
	 * @param rel the relationship
	 * @param eid1 the id of the first entity
	 * @param eid2 the id of the second entity
	 * @return dot lines for a relationship
	 */
	List<String> generateRelLines(Relationship rel, String eid1, String eid2);
	
}
