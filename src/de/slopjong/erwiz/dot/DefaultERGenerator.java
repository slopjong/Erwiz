package de.slopjong.erwiz.dot;

import static de.slopjong.erwiz.dot.PackageUtils.escapeLabel;
import static de.slopjong.erwiz.dot.PackageUtils.joinDotAttributes;
import static de.slopjong.erwiz.dot.PackageUtils.wrapLabel;

import java.util.ArrayList;
import java.util.List;

import de.slopjong.erwiz.model.CardinalityElement;
import de.slopjong.erwiz.model.CardinalityWithOptionality;
import de.slopjong.erwiz.model.ColorPair;
import de.slopjong.erwiz.model.Dependency;
import de.slopjong.erwiz.model.Entity;
import de.slopjong.erwiz.model.EntityAttribute;
import de.slopjong.erwiz.model.OptionMap;
import de.slopjong.erwiz.model.OptionalityElement;
import de.slopjong.erwiz.model.ParentOrChild;
import de.slopjong.erwiz.model.Relationship;
import de.slopjong.erwiz.model.RelationshipType;
import de.slopjong.erwiz.model.VerbPhrase;


/**
 * Default entity/relationship dot generator.
 * This class may have some subclasses.
 * 
 * @author kono
 * @version 1.0
 * @see de.slopjong.erwiz.dot.ERGenerator
 */
class DefaultERGenerator implements ERGenerator {

	private final RankDirection direction;
	private final OptionMap globalOptions;
	
	/**
	 * Creates an instance of this class.
	 * 
	 * @param direction the rank direction of dot file
	 * @param globalOptions the global options
	 */
	DefaultERGenerator(RankDirection direction, OptionMap globalOptions) {
		this.direction = direction;
		this.globalOptions = (globalOptions != null) ? globalOptions : new OptionMap();
	}
	
	/**
	 * Generates dot lines for an entity.
	 * 
	 * @param entity the entity
	 * @return dot lines for an entity
	 */
	public final List<String> generateEntityLines(Entity entity) {
		
		List<String> list = new ArrayList<String>();
		list.add(generateEntityCommentLine(entity));
		list.add(generateEntityLine(entity));
		
		return list;
	}
	
	/**
	 * Generates dot lines for a relationship.
	 * 
	 * @param rel the relationship
	 * @param eid1 the id of the first entity
	 * @param eid2 the id of the second entity
	 * @return dot lines for a relationship
	 */
	public final List<String> generateRelLines(Relationship rel, String eid1, String eid2) {
		
		if (rel.getOptions().getBoolean(OptionName.VERB_REVERSE)) {
			rel = reverseVerbDirection(rel);
		}
		
		List<String> list = new ArrayList<String>();
		list.add(generateRelCommentLine(rel));
		list.add(generateRelLine(rel, eid1, eid2));
		
		return list;
	}
	
	/**
	 * Generates a comment line for the specified entity.
	 * 
	 * @param entity the entity
	 * @return a comment line
	 */
	private final String generateEntityCommentLine(Entity entity) {
		final String format = "//E [%s]";
		return String.format(format, entity.getName());
	}
	
	/**
	 * Generates a comment line for the specified relationship.
	 * 
	 * @param rel the relationship
	 * @return a comment line
	 */
	private final String generateRelCommentLine(Relationship rel) {
		final String format = "//R [%s]--[%s]";
		return String.format(format, rel.getNameOfEntity1(), rel.getNameOfEntity2());
	}
	
	/**
	 * Generates an entity line for the specified entity.
	 * 
	 * @param entity the entity
	 * @return an entity line
	 */
	private final String generateEntityLine(Entity entity) {
		
		String[] array = new String[6];
		
		array[0] = "shape=" + generateNodeShape(entity);
		
		String nodeLabel = generateNodeLabel(entity);
		if (direction == RankDirection.TOP_TO_BOTTOM) {
			//As of this version, TOP_TO_BOTTOM is never specified by the invoker.
			nodeLabel = "{" + nodeLabel + "}";
		}
		array[1] = "label=\"" + nodeLabel + "\"";
		
		final String dc = generateDarkColor(entity);
		array[2] = !dc.equals("") ? ("color=\"" + dc + "\"") : "";
		
		final String lc = generateLightColor(entity);
		array[3] = !lc.equals("") ? ("fillcolor=\"" + lc + "\"") : "";
		
		final String url = generateURL(entity);
		array[4] = !url.equals("") ? ("URL=\"" + url + "\"") : "";
		
		final String tooltip = generateToolTip(entity);
		array[5] = !tooltip.equals("") ? ("tooltip=\"" + tooltip + "\"") : "";
		
		final String format = "%s [%s]";
		final String attributes = joinDotAttributes(array);
		return String.format(format, entity.getId(), attributes);
	}
	
	/**
	 * Generates a relationship line for the specified edge.
	 * 
	 * @param rel the relationship
	 * @param eid1 the id of the first entity
	 * @param eid2 the id the second entity
	 * @return a relationship line
	 */
	private final String generateRelLine(Relationship rel, String eid1, String eid2) {
		
		//the first entity side and the second entity side
		final CardinalityWithOptionality cwo = rel.getCardinality();
		final CardinalityElement[] ceArray = {cwo.getCardinality1(), cwo.getCardinality2()};
		final OptionalityElement[] oeArray = {cwo.getOptionality1(), cwo.getOptionality2()};
		final ParentOrChild[] parentOrChildArray = rel.getParentOrChildArray();
		final String[] numberArray = {
				rel.getOptions().getString(OptionName.N1),
				rel.getOptions().getString(OptionName.N2)
		};
		final String[] arrowNames = {"arrowtail", "arrowhead"};
		final String[] labelNames = {"taillabel", "headlabel"};
		
		//arrow style and arow label
		String[] array = new String[6];
		for (int i = 0; i < 2; i++) {
			
			//NOTE: generateArrowStyle() and generateArrowLabel() may be overridden.
			
			String arrow = generateArrowStyle(ceArray[i], oeArray[i], parentOrChildArray[i]);
			array[2 * i] = arrowNames[i] + "=" + arrow;
			
			String label = numberArray[i];
			if (!label.equals("")) { //N1 or N2 specified
				label = escapeLabel(wrapLabel(numberArray[i]));
			} else {
				label = generateArrowLabel(ceArray[i], oeArray[i], parentOrChildArray[i]);
			}
			
			array[2 * i + 1] = labelNames[i] + "=\"" + label + "\"";
		}
		
		//edge style
		{
			array[4] = "style=" + generateEdgeStyle(rel);
		}
		
		//edge label
		{
			array[5] = "label=\"" + generateEdgeLabel(rel) + "\"";
		}
		
		//result
		final String format = "%s -> %s [%s]";
		final String attributes = joinDotAttributes(array);
		return String.format(format, eid1, eid2, attributes);
	}
	
	/**
	 * Generates node style string to wite dot file.
	 * This method is overridable.
	 * 
	 * @param entity the entity
	 * @return the generated string
	 */
	String generateNodeShape(Entity entity) {
		if (entity.getDependency() == Dependency.INDEPENDENT) {
			return "record";
		} else {
			return "Mrecord"; //round box record.
		}
	}
	
	/**
	 * Generates node label string to wite dot file.
	 * This method is overridable.
	 * 
	 * @param entity the entity
	 * @return the generated string
	 */
	String generateNodeLabel(Entity entity) {
		
		final StringBuilder sb = new StringBuilder();
		final String betweenNameAndMark = "  "; //space:2
		final List<EntityAttribute> attrList = entity.getAttributeList();
		
		//entity name
		{
			String entityName = entity.getName();
			String mark = entity.getOptions().getString(OptionName.MARK);
			if (!mark.equals("")) {
				entityName += betweenNameAndMark + mark;
			}
			entityName = escapeLabel(entityName);
			sb.append(entityName);
			
			if (attrList.isEmpty()) {
				return sb.toString(); //no attributes
			}
		}
		
		sb.append("|{{");
		
		//the first column to show primary keys
		for (EntityAttribute attr : attrList) {
			sb.append(attr.isPrimaryKey() ? "*" : " "); //PRIMARY KEY
			if (attr != attrList.get(attrList.size() - 1)) {
				sb.append("|"); // not last
			}
		}
		
		sb.append("}|{");
		
		//the second column to show attribute names and foreign keys
		for (EntityAttribute attr : attrList) {
			String attrName = escapeLabel(attr.getName());
			sb.append(attrName);
			
			if (attr.isForeignKey()) {
				sb.append(escapeLabel(" (FK)")); //FOREIGN KEY
			}
			
			String mark = attr.getOptions().getString(OptionName.MARK);
			if (!mark.equals("")) {
				mark = betweenNameAndMark + mark;
				mark = escapeLabel(mark);
				sb.append(mark);
			}
			
			sb.append("\\l"); //left-align
			if (attr != attrList.get(attrList.size() - 1)) {
				sb.append("|"); // not last
			}
		}
		
		sb.append("}}");
		
		return sb.toString();
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
	String generateArrowStyle(CardinalityElement ce, OptionalityElement oe, ParentOrChild parentOrChild) {	
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
	String generateArrowLabel(CardinalityElement ce, OptionalityElement oe, ParentOrChild parentOrChild) {
		return "";
	}
	
	/**
	 * Generates edge style string to wite dot file.
	 * This method is overridable.
	 * 
	 * @param rel the relationship
	 * @return the generated string
	 */
	String generateEdgeStyle(Relationship rel) {
		if (rel.getRelType() == RelationshipType.NON_IDENTIFYING) {
			return "dashed";
		} else {
			return "solid";
		}
	}
	
	/**
	 * Generates edge label string to wite dot file.
	 * This method is overridable.
	 * 
	 * @param rel the relationship
	 * @return the generated string
	 */
	String generateEdgeLabel(Relationship rel) {
		final VerbPhrase vp = rel.getVerbPhrase();
		
		if (vp.getText().equals("")) {
			return "";
		}
		
		String label;
		switch (vp.getDirection()) {
			case FIRST_TO_SECOND:
				label = "[" + vp.getText() + ">";
				break;
			case SECOND_TO_FIRST:
				label = "<" + vp.getText() + "]";
				break;
			case NONE:
				//label = "[" + vp.getText() + "]";
				label = vp.getText();
				break;
			default:
				assert false : "unknown verb direction [" + vp.getDirection() + "]";
				//label = "[" + vp.getText() + "]";
				label = vp.getText();
				break;
		}
		
		return escapeLabel(wrapLabel(label));
	}
	
	//private utility method for readability
	private final String generateDarkColor(Entity entity) {
		ColorPair pair = entity.getOptions().getColorPair(OptionName.COLOR);
		if (pair != ColorPair.NONE) {
			return pair.getDarkColor();
		} else {
			return "";
		}
	}
	
	//private utility method for readability
	private final String generateLightColor(Entity entity) {
		ColorPair pair = entity.getOptions().getColorPair(OptionName.COLOR);
		if (pair != ColorPair.NONE) {
			return pair.getLightColor();
		} else {
			return "";
		}
	}
	
	//private utility method for readability
	private final String generateURL(Entity entity) {
		String value = this.globalOptions.getString(OptionName.LINK_FILES);
		if (value != null && !value.equals("")) {
			return value.replaceAll("\\$\\{entity\\}", entity.getName()); //${entity} -> entity name
		} else {
			return "";
		}
	}
	
	//private utility method for readability
	private final String generateToolTip(Entity entity) {
		return escapeLabel(entity.getName());
	}
	
	//private utility method for readability
	private Relationship reverseVerbDirection(Relationship rel) {
		return new Relationship(
			rel.getNameOfEntity1(),
			rel.getDependencyOfEntity1(),
			rel.getNameOfEntity2(),
			rel.getDependencyOfEntity2(),
			rel.getCardinality(),
			new VerbPhrase(
				rel.getVerbPhrase().getText(), 
				rel.getVerbPhrase().getDirection().getReverse()
			),
			rel.getOptions()
		);
	}
	
}
