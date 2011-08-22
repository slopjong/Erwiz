package jp.gr.java_conf.simply.erviz.plain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.gr.java_conf.simply.erviz.plain.LineData;
import jp.gr.java_conf.simply.erviz.model.Relationship;

import static jp.gr.java_conf.simply.erviz.plain.PackageUtils.createLinesList;

/**
 * This class represents a text parser which is used for a relationship data.
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
 * The specified relationship data is parsed by an instance of{@code RelationshipLineParser}.
 * For more information, refer the document of the line parser class.
 * 
 * This class is immutable. All input arguments is given at the object construction.
 * 
 * This class is package private. The instance of this class is used 
 * by other parser classes internally.
 * 
 * @author kono
 * @version 1.0
 * @see jp.gr.java_conf.simply.erviz.plain.RelationshipLineParser
 */
final class RelationshipParser {
	
	//input
	private final List<LineData> lines = new ArrayList<LineData>();
	
	//output
	private Relationship rel;
	
	/**
	 * Constructs an object of this class.
	 * 
	 * @param lines the lines which will be parsed in {@code parse()} method
	 * @throws NullPointerException if a null argument is specified
	 */
	RelationshipParser(List<LineData> lines) {
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
	RelationshipParser(String... lines) {
		this(createLinesList(lines));
	}
	
	/**
	 * Returns the {@code Relationship} object corresponding to the specified line.
	 * This method should be called after calling {@code parse()} method.
	 * 
	 * @return the {@code Relationship} object
	 */
	Relationship getRelationship() {
		return this.rel;
	}
	
	/**
	 * Retrieves the relationsip lines which is specified at construction.
	 * 
	 * @return the relationsip lines which is specified at construction
	 */
	List<LineData> getLines() {
		return Collections.unmodifiableList(this.lines);
	}
	
	/**
	 * Parses the text specified by constructor parameters.
	 * The result of parsing will be saved in the object.
	 * 
	 * @throws ParserException if parsing error occured.
	 */
	void parse() throws ParserException {
		
		if (this.lines.size() != 1) {
			throw createException(null, Message.ERR_REL_LINES_NUM);
		}
		
		final LineData line = this.lines.get(0);
		
		//parse entity name line
		RelationshipLineParser parser = new RelationshipLineParser(line);
		parser.parse();
		this.rel = new Relationship(
				parser.getNameOfEntity1(), parser.getDependecyOfEntity1(),
				parser.getNameOfEntity2(), parser.getDependecyOfEntity2(),
				parser.getCardinality(), parser.getVerbPhrase(), parser.getOptions());
	}
	
	//private utility method to create exception
	private ParserException createException(LineData line, Message message, Object... params) {
		final String description = message.getText(params);
		return ParserException.create(description, line);
	}
	
}
