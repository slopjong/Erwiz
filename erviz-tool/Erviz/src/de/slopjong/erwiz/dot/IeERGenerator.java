package de.slopjong.erwiz.dot;

import de.slopjong.erwiz.model.CardinalityElement;
import de.slopjong.erwiz.model.Entity;
import de.slopjong.erwiz.model.OptionMap;
import de.slopjong.erwiz.model.OptionalityElement;
import de.slopjong.erwiz.model.ParentOrChild;
import de.slopjong.erwiz.model.Relationship;

/**
 * This class is an entity/relationship dot generator for James Martin's IE (Information Enginnering) notation,
 * and a subclass of {@code DefaultERGenerator} class.
 * 
 * @author kono
 * @version 1.0
 * @see de.slopjong.erwiz.dot.DefaultERGenerator
 */
final class IeERGenerator extends DefaultERGenerator {
	
	private final boolean isStrict;
	
	/**
	 * Creates an instance of this class.
	 * 
	 * @param direction the rank direction of dot file
	 * @param globalOptions the global options
	 * @param isStrict true if strict mode is specified, false otherwise
	 */
	IeERGenerator(RankDirection direction, OptionMap globalOptions, boolean isStrict) {
		super(direction, globalOptions);
		this.isStrict = isStrict;
	}
	
	/**
	 * Generates node style string to wite dot file.
	 * This method is overridable.
	 * 
	 * @param entity the entity
	 * @return the generated string
	 */
	@Override
	String generateNodeShape(Entity entity) {
		if (this.isStrict) {
			return "record";
		} else {
			return super.generateNodeShape(entity);
		}
	}
	
	/**
	 * Generates edge style string to wite dot file.
	 * This method is overridable.
	 * 
	 * @param rel the relationship
	 * @return the generated string
	 */
	@Override
	String generateEdgeStyle(Relationship rel) {
		if (this.isStrict) {
			return "solid";
		} else {
			return super.generateEdgeStyle(rel);
		}
	}
	
	/**
	 * Generates arrow style string to wite dot file.
	 * This method is overridable.
	 * 
	 * @param ce the cardinality element
	 * @param oe the optionality element
	 * @param parentOrChild the parent-child information of the entity in the relationship
	 * @return the generated string
	 */
	@Override
	String generateArrowStyle(CardinalityElement ce, OptionalityElement oe, ParentOrChild parentOrChild) {
		
		if (ce == CardinalityElement.ONE && oe == OptionalityElement.OPTIONAL) {
			return "teeodot";
		}
		
		if (ce == CardinalityElement.ONE && oe == OptionalityElement.MANDATORY) {
			return "teetee";
		}	
		
		if (ce == CardinalityElement.MANY && oe == OptionalityElement.OPTIONAL) {
			return "crowodot";
		}
		
		if (ce == CardinalityElement.MANY && oe == OptionalityElement.MANDATORY) {
			return "crowtee";
		}
		
		return "none";
	}
	
	/**
	 * Generates arrow label string to wite dot file.
	 * This method is overridable.
	 * 
	 * @param ce the cardinality element
	 * @param oe the optionality element
	 * @param parentOrChild the parent-child information of the entity in the relationship
	 * @return the generated string
	 */
	@Override
	String generateArrowLabel(CardinalityElement ce, OptionalityElement oe, ParentOrChild parentOrChild) {
		return "";
	}
	
}
