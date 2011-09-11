package de.slopjong.erviz.plain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.slopjong.erviz.model.Entity;
import de.slopjong.erviz.model.EntityAttribute;
import de.slopjong.erviz.plain.LineData;


import static de.slopjong.erviz.plain.PackageUtils.createLinesList;

/**
 * This class represents a text parser which is used for an entity data.
 * 
 * Usage:
 * 
 * <ol>
 *   <li>Creates an object of this class with the target text.
 *   <li>Parse it by calling {@code parse()}.
 *   <li>Retrieve the result of parsing by calling getter methods.
 * </ol>
 * 
 * Not that after excection in {@code parse()} method, the result of getter methods 
 * will not be ensured.
 * 
 * The specified entity data is parsed by an instance of {@code EntityNameLineParser}
 * and an instance of {@code EntityAttrLineParser}.
 * For more information, refer the document of the line parser class.
 * 
 * This class is immutable. All input arguments is given at the object construction.
 * 
 * This class is package private. The instance of this class is used 
 * by other parser classes internally.
 * 
 * @author kono
 * @version 1.0
 * @see de.slopjong.erviz.plain.EntityNameLineParser
 * @see de.slopjong.erviz.plain.EntityAttrLineParser
 */
final class EntityParser {
	
	//input
	private final List<LineData> lines = new ArrayList<LineData>();
	
	//output
	private Entity entity;
	private LineData nameLine;
	private List<LineData> attrLineList;
	
	/**
	 * Constructs an object of this class.
	 * 
	 * @param lines the lines which will be parsed in {@code parse()} method
	 * @throws NullPointerException if a null argument is specified
	 */
	EntityParser(List<LineData> lines) {
		if (lines == null) {
			throw new NullPointerException();
		}
		//copy all elements
		this.lines.addAll(lines);	
	}
	
	/**
	 * Constructs an object of this class without line number data.
	 * This constructor is supporsed to be used for unit test.
	 * 
	 * @param lines the lines which will be parsed in {@code parse()} method
	 */
	EntityParser(String... lines) {
		this(createLinesList(lines));
	}
	
	/**
	 * Returns the {@code Entity} object corresponding to the specified lines.
	 * This method should be called after calling {@code parse()} method.
	 * 
	 * @return the {@code Entity} object
	 */
	Entity getEntity() {
		return this.entity;
	}
	
	/**
	 * Retrieves the entity lines which is specified at construction.
	 * 
	 * @return the entity lines which is specified at construction
	 */
	List<LineData> getLines() {
		return Collections.unmodifiableList(this.lines);
	}
	
	/**
	 * Retrieves the entity name line.
	 * This method should be called after calling {@code parse()} method.
	 * 
	 * @return the entity name line
	 */
	LineData getNameLine() {
		return this.nameLine;
	}
	
	/**
	 * Retrieves the attribute name lines.
	 * This method should be called after calling {@code parse()} method.
	 * 
	 * @return the attribute name lines.
	 */
	List<LineData> getAttrLines() {
		return Collections.unmodifiableList(this.attrLineList);
	}
	
	/**
	 * Parses the text specified by constructor parameters.
	 * The result of parsing will be saved in the object.
	 * 
	 * @throws ParserException if parsing error occured.
	 */
	void parse() throws ParserException {
		
		assert (this.lines.size() > 0) : "no entity lines";
		
		//this map is used for the duplication check
		Map<String, EntityAttribute> map = new HashMap<String, EntityAttribute>();
		
		this.nameLine = this.lines.get(0);
		this.attrLineList = this.lines.subList(1, this.lines.size());
		
		//parse entity attribule lines
		List<EntityAttribute> attrList = new ArrayList<EntityAttribute>();
		for (LineData attrLine : this.attrLineList) {
			
			EntityAttrLineParser parser = new EntityAttrLineParser(attrLine);
			parser.parse();
			
			EntityAttribute attr = new EntityAttribute(parser.getAttrName(), 
					parser.isPrimaryKey(), parser.isForeignKey(), parser.getOptions());
			
			checkDupulication(attr, map, attrLine);
			map.put(attr.getName(), attr);
			attrList.add(attr);
		}
		
		//parse entity name line
		{
			EntityNameLineParser parser = new EntityNameLineParser(this.nameLine);
			parser.parse();
			this.entity = new Entity(parser.getEntityName(), parser.getDependency(), 
					attrList, parser.getOptionMap());
		}
		
	}
	
	//duplication check of entity attribute name
	private void checkDupulication(EntityAttribute attr, Map<String, EntityAttribute> map, 
			LineData line) throws ParserException {
		final String name = attr.getName();
		if (map.containsKey(name)) {
			throw createException(line, Message.ERR_ATTR_NAME_DUP, name);
		}
	}
	
	//private utility method to create exception
	private ParserException createException(LineData line, Message message, Object... params) {
		final String description = message.getText(params);
		return ParserException.create(description, line);
	}
	
}
