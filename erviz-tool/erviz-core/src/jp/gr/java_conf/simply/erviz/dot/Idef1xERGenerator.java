package jp.gr.java_conf.simply.erviz.dot;

import jp.gr.java_conf.simply.erviz.model.CardinalityElement;
import jp.gr.java_conf.simply.erviz.model.OptionMap;
import jp.gr.java_conf.simply.erviz.model.OptionalityElement;
import jp.gr.java_conf.simply.erviz.model.ParentOrChild;
import static jp.gr.java_conf.simply.erviz.dot.PackageUtils.escapeLabel;
import static jp.gr.java_conf.simply.erviz.dot.PackageUtils.wrapLabel;

/**
 * This class is an entity/relationship dot generator for IDEF1X notation,
 * and a subclass of {@code DefaultERGenerator} class.
 * 
 * @author kono
 * @version 1.0
 * @see jp.gr.java_conf.simply.erviz.dot.DefaultERGenerator
 */
final class Idef1xERGenerator extends DefaultERGenerator {
	
	/**
	 * Creates an instance of this class.
	 * 
	 * @param direction the rank direction of dot file
	 * @param globalOptions the global options
	 */
	Idef1xERGenerator(RankDirection direction, OptionMap globalOptions) {
		super(direction, globalOptions);
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
		
		if (ce == CardinalityElement.NONE) {
			return "none";
		}
		
		if (parentOrChild == ParentOrChild.PARENT) {
			
			if (oe == OptionalityElement.MANDATORY) {
				return "none";
			} else {
				return "odiamond";
			}
			
		} else if (parentOrChild == ParentOrChild.CHILD) {
			
			return "dot";
			
		} else if (parentOrChild == ParentOrChild.NONE) {
			
			return "none";
			
		} else {
			
			return "none"; //invalid type
			
		}
		
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
		
		String label = "";
		
		if (parentOrChild == ParentOrChild.CHILD) {
			
			if (ce == CardinalityElement.ONE && oe == OptionalityElement.MANDATORY) {
				
				label = "1";
				
			} else if (ce == CardinalityElement.ONE && oe == OptionalityElement.OPTIONAL) {
				
				label = "Z";
				
			} else if (ce == CardinalityElement.MANY && oe == OptionalityElement.MANDATORY) {
				
				label = "P";
				
			}
			
		}
		
		return escapeLabel(wrapLabel(label));
	}
	
}
