package jp.gr.java_conf.simply.erviz.plain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.gr.java_conf.simply.erviz.plain.LineData;
import jp.gr.java_conf.simply.erviz.model.Dependency;
import jp.gr.java_conf.simply.erviz.model.EntityAttribute;
import jp.gr.java_conf.simply.erviz.model.OptionMap;
import jp.gr.java_conf.simply.erviz.model.Entity;
import jp.gr.java_conf.simply.erviz.model.Model;
import jp.gr.java_conf.simply.erviz.model.Relationship;
import jp.gr.java_conf.simply.erviz.model.RelationshipType;

import static jp.gr.java_conf.simply.erviz.plain.BracketPair.CURLY;
import static jp.gr.java_conf.simply.erviz.plain.PackageUtils.removeEnclosingBrackets;

import static jp.gr.java_conf.simply.erviz.plain.PackageUtils.createLinesList;

/**
 * This class is the text parser for entity-relationship model text.
 * 
 * Usage:
 * 
 * <ol>
 *   <li>Creates an object of this class with the target text.
 *   <li>Parse it by calling {@code parse()}.
 *   <li>Retrieve the result of parsing by calling {@code getModel()}.
 * </ol>
 * 
 * Not that after excection in {@code parse()} method, the return value of 
 * {@code getModel()} will not be ensured.
 * 
 * @author kono
 * @version 1.0
 * @see jp.gr.java_conf.simply.erviz.plain.EntityParser
 * @see jp.gr.java_conf.simply.erviz.plain.RelationshipParser
 */
public final class ModelParser {
	
	//constant
	private static final OptionName[] INVOLVED_OPTION_NAMES = {
		OptionName.TITLE, 
		OptionName.TITLE_SIZE, 
		OptionName.LINK_FILES
	};
	
	//input
	private final List<LineData> lines = new ArrayList<LineData>();
	
	//output
	private Model model  = new Model();
	private OptionMap options = new OptionMap(INVOLVED_OPTION_NAMES);
	private List<Exception> exceptionList = new ArrayList<Exception>();
	
	static {
		//initialize messages
		Message.initialize();
	}
	
	/**
	 * Constructs an object of this class.
	 * 
	 * @param lines the lines which will be parsed in {@code parse()} method
	 * @throws NullPointerException if a null argument is specified
	 */
	public ModelParser(List<String> lines) {
		if (lines == null) {
			throw new NullPointerException();
		}
		//copy all elements
		for (int i = 0; i < lines.size(); i++) {
			LineData ld = new LineData(i + 1, lines.get(i)); //the first line number is 1.
			this.lines.add(ld);
		}
	}
	
	/**
	 * Constructs an object of this class without line number data.
	 * This constructor is supporsed to be used for unit test.
	 * 
	 * @param lines the lines which will be parsed in {@code parse()} method
	 */
	ModelParser(String... lines) {
		this(Arrays.asList(lines));
	}
	
	/**
	 * Parses the lines which is specified by {@code SetLines} method.
	 * The result of parsing will be saved in the object.
	 * 
	 * @throws ParserException if parsing error occured.
	 */
	public List<Exception> parse() {
		
		//temporary parser lists
		final List<EntityParser> entityParserList = new ArrayList<EntityParser>();
		final List<RelationshipParser> relParserList = new ArrayList<RelationshipParser>();
		final List<OptionListParser> optionsParserList = new ArrayList<OptionListParser>();
		
		this.exceptionList.clear();
		
		createParsers(entityParserList, relParserList, optionsParserList); //1st stage
		if (!this.exceptionList.isEmpty()) {
			return this.exceptionList;
		}
		
		createModelElements(entityParserList, relParserList, optionsParserList); //2nd stage
		return this.exceptionList;
	}
	
	/**
	 * Get a {@code Model} object which is created by calling {@code parse()} method.
	 * 
	 * @return a {@code Model} object
	 */
	public Model getModel() {
		return this.model;
	}
	
	//create parsers for all entities and relationships
	private void createParsers(List<EntityParser> entityParserList, 
			List<RelationshipParser> relParserList, List<OptionListParser> optionsParserList) {
		
		try {
			List<LineData> entityLines = new ArrayList<LineData>(); //the lines of the current entity
			
			for (LineData line : this.lines) {

				switch (judgeLineType(line)) {
					case UNKNOWN: {
						throw createException(line, Message.ERR_UNKNOWN_LINE_TYPE);
					}
					case BLANK: {
						continue;
					}
					case GLOBAL_OPTIONS: {
						final String optionsText = removeEnclosingBrackets(line.getLineText(), CURLY);
						OptionListParser parser = new OptionListParser(optionsText);
						optionsParserList.add(parser);
						continue;
					}
					case ENTITY_NAME: {
						//end of entity lines
						if (!entityLines.isEmpty()) {
							EntityParser parser = new EntityParser(entityLines);
							entityParserList.add(parser);
							entityLines.clear();
						}
						
						//new entity lines
						entityLines.add(line);
						continue;
					}
					case ENTITY_ATTRIBUTE: {
						if (entityLines.isEmpty()) {
							throw createException(line, Message.ERR_ENTITY_LINES_NOT_OPEND);
						}
						
						entityLines.add(line);
						continue;
					}
					case RELATIONSHIP: {
						//end of entity lines
						if (!entityLines.isEmpty()) {
							EntityParser parser = new EntityParser(entityLines);
							entityParserList.add(parser);
							entityLines.clear();
						}
						
						//a relationship line
						List<LineData> lines = createLinesList(line);
						RelationshipParser parser = new RelationshipParser(lines);
						relParserList.add(parser);	
						continue;
					}
				}
			
			}
			
			//end of entity lines
			if (!entityLines.isEmpty()) {
				EntityParser parser = new EntityParser(entityLines);
				entityParserList.add(parser);
				entityLines.clear();
			}
			
		} catch (ParserException ex) {
			this.exceptionList.add(ex);
			return;
		}
	}
	
	//execute parsing and create entity objects and relationship objects.
	private void createModelElements(
			List<EntityParser> entityParserList, 
			List<RelationshipParser> relParserList, 
			List<OptionListParser> optionsParserList) {
		
		for (OptionListParser parser : optionsParserList) {
			try {
				parser.parse();
				
				for (OptionName optionName : INVOLVED_OPTION_NAMES) {
					
					final String optionValue = parser.getOptionValue(optionName.getNameInFiles());
					PackageUtils.setOptionValue(this.options, optionName, optionValue);
					
				}
				
			} catch (ParserException ex) {
				this.exceptionList.add(ex);
			}
		}
		
		//invalid options has found
		if (this.exceptionList.size() > 0) {
			return;
		}
		
		//this map is used for:
		//(1)the duplication check of entity names on parsing entities
		//(2)the existence check of entity names on parsing relationships
		Map<String, Entity> map = new HashMap<String, Entity>();
		
		List<Entity> entityList = new ArrayList<Entity>();
		for (EntityParser parser : entityParserList) {
			try {
				parser.parse();
				Entity entity = parser.getEntity();
				checkEntityDupulication(entity, map, parser.getNameLine());
				map.put(entity.getName(), entity);
				entityList.add(entity);
			} catch (ParserException ex) {
				this.exceptionList.add(ex);
			}
		}
		
		//invalid entities has found
		if (this.exceptionList.size() > 0) {
			return;
		}
		
		List<Relationship> relList = new ArrayList<Relationship>();
		for (RelationshipParser parser : relParserList) {
			try {
				parser.parse();
				Relationship rel = parser.getRelationship();
				
				addUndefinedEntity(rel, map, entityList);
				
				relList.add(rel);
			} catch (ParserException ex) {
				this.exceptionList.add(ex);
			}
		}
		
		//invalid relationship has found
		if (this.exceptionList.size() > 0) {
			return;
		}
		
		//modify dependency of entity automatically
		SetEntityDependency(entityList, relList);
		
		this.model = new Model(entityList, relList, options);
	}
	
	//private utility method
	private LineType judgeLineType(LineData line) {
		LineTypeParser parser = new LineTypeParser(line);
		parser.parse();
		return parser.getLineType();
	}
	
	//duplication check of entity name
	private void checkEntityDupulication(Entity entity, Map<String, Entity> map, 
			LineData line) throws ParserException {
		if (map.containsKey(entity.getName())) {
			throw createException(line, Message.ERR_ENTITY_NAME_DUP, entity.getName());
		}
	}
	
	//exsistence check of entity names in a relationship
	//if undefined, add the corresponding entity to the entity list.
	private void addUndefinedEntity(Relationship rel, Map<String, Entity> map, List<Entity> entityList) {
		
		for (int i = 1; i <= 2; i++) {
			final String name = (i == 1) ? rel.getNameOfEntity1() : rel.getNameOfEntity2();
			
			if (!map.containsKey(name)) {
				final Entity entity = new Entity(name, Dependency.INDEPENDENT, 
						new ArrayList<EntityAttribute>(), new OptionMap(OptionName.values()));
				
				map.put(name, entity);
				entityList.add(entity);
			}
		}
		
	}
	
	//private utility method to create exception
	private ParserException createException(LineData line, Message message, Object... params) {
		final String description = message.getText(params);
		return ParserException.create(description, line);
	}
	
	//private utility method to set entity dependency
	private void SetEntityDependency(List<Entity> entityList, List<Relationship> relList) {
		ENTITY_LOOP:
		for (int i = 0; i < entityList.size(); i++) {
			final Entity entity = entityList.get(i);
			
			if (entity.getDependency() == Dependency.DEPENDENT) {
				continue; //dependent is specified by user
			}
			
			for (EntityAttribute attr : entity.getAttributeList()) {
				if (attr.isPrimaryKey() && attr.isForeignKey()) {
					//If the entity has a primary foreign key, it must be dependent.
					entityList.set(i, entity.getDependentEntity());
					continue ENTITY_LOOP;
				}
			}
			
			for (Relationship rel : relList) {
				if (rel.getRelType() != RelationshipType.IDENTIFYING) {
					continue;
				}
				
				if (rel.getNameOfEntity1().equals(entity.getName()) && rel.getDependencyOfEntity1() == Dependency.DEPENDENT ||
						rel.getNameOfEntity2().equals(entity.getName()) && rel.getDependencyOfEntity2() == Dependency.DEPENDENT) {
					
					//If the entity is dependent in a identifying relationship, it must be dependent.
					entityList.set(i, entity.getDependentEntity());
					continue ENTITY_LOOP;
				}
			}
			
		}
	}
	
	
	
}
