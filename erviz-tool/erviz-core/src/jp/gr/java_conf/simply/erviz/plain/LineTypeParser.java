package jp.gr.java_conf.simply.erviz.plain;

import static jp.gr.java_conf.simply.erviz.plain.BracketPair.SQUARE;
import static jp.gr.java_conf.simply.erviz.plain.BracketPair.ROUND;
import static jp.gr.java_conf.simply.erviz.plain.BracketPair.ANGLE;
import static jp.gr.java_conf.simply.erviz.plain.BracketPair.CURLY;
import static jp.gr.java_conf.simply.erviz.plain.PackageUtils.removeComment;
import static jp.gr.java_conf.simply.erviz.plain.PackageUtils.split;
import static jp.gr.java_conf.simply.erviz.plain.PackageUtils.isEnclosedByAnyBrackets;
import static jp.gr.java_conf.simply.erviz.plain.PackageUtils.startsWithAnyLeftBracket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class judges the line type.
 * 
 * @author kono
 * @version 1.0
 */
final class LineTypeParser {
	
	//input
	private final LineData line;
	
	//output
	private LineType lineType;
	
	/**
	 * Constructs an instance of this class.
	 * 
	 * @param line a line data
	 */
	LineTypeParser(LineData line) {
		this.line = line;
	}
	
	/**
	 * Retrieves the line type of the line specified at construction.
	 * 
	 * @return the line type of the line specified at construction.
	 */
	LineType getLineType() {
		return this.lineType;
	}
	
	/**
	 * Prsers the line text which is specified at construction.
	 * The result will be set in this instance.
	 */
	void parse() {
		
		String lineText = this.line.getLineText().trim();
		
		//blank?
		if (lineText.equals("")) {
			this.lineType = LineType.BLANK;
			return;
		}
		
		//comment only?
		lineText = removeComment(lineText).trim();
		if (lineText.equals("")) {
			this.lineType = LineType.COMMENT_ONLY;
			return;
		}
		
		//global options?
		if (isEnclosedByAnyBrackets(lineText, CURLY)) {
			this.lineType = LineType.GLOBAL_OPTIONS;
			return;
		}
		
		//entity attribute?
		if (!startsWithAnyLeftBracket(lineText, SQUARE, ROUND)) {
			this.lineType = LineType.ENTITY_ATTRIBUTE; //attribute name line doesn't start with a left square/round bracket.
			return;
		}
		
		
		//
		// In the following code, judges ENTITY_NAME, RELATIONSHIP, UNKNOWN.
		//
		
		//the local class to count tokens which are enclosed by a pair of brackets
		class CountMap {
			private final Map<String, Integer> map = new HashMap<String, Integer>();
			
			CountMap() {
				for (BracketPair b : BracketPair.values()) {
					this.map.put(b.name(), 0); //initialize
				}
			}
			
			void add(BracketPair b) {
				this.map.put(b.name(), this.map.get(b.name()) + 1); //inclement
			}
			
			int cont(BracketPair b) {
				return this.map.get(b.name());
			}		
		}
		
		//register the numbers of brackets
		final List<String> tokens = split(lineText, SQUARE, ROUND, ANGLE, CURLY);
		final CountMap cm = new CountMap();
		
		for (String token : tokens) {
			INNER_LOOP:
			for (BracketPair b : BracketPair.values()) {
				if (isEnclosedByAnyBrackets(token, b)) {
					cm.add(b);
					break INNER_LOOP;
				}
			}
		}
		
		//judge line type
		final int srCount = cm.cont(SQUARE) + cm.cont(ROUND);
		
		switch (srCount) {
			case 1: {
				this.lineType = LineType.ENTITY_NAME;
				break;
			}
			case 2: {
				this.lineType = LineType.RELATIONSHIP;
				break;
			}
			default: {
				this.lineType = LineType.UNKNOWN;
				break;
			}
		}
		
		//NOTE: Additional validations will be executed by other parsers which is corresponding this line type.
		
	}
	
}
