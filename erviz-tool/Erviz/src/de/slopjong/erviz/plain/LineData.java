package de.slopjong.erviz.plain;

/**
 * This class represents the line data of a text file data.
 * The instance of this class has two fields as follows:
 * 
 * <ul>
 * <li>line number
 * <li>line text
 * </ul>
 * 
 * @author kono
 * @version 1.0
 */
final class LineData {
	
	private int lineNumber;
	private String lineText;
	
	/**
	 * Construct an object of this class.
	 * 
	 * @param lineNumber line number
	 * @param lineText line number
	 */
	LineData(int lineNumber, String lineText) {
		this.lineNumber = lineNumber;
		this.lineText = ((lineText != null) ? lineText : "");
	}
	
	/**
	 * Returns line number
	 * 
	 * @return line number
	 */
	int getLineNumber() {
		return lineNumber;
	}
	
	/**
	 * Returns line text
	 * 
	 * @return line text
	 */
	String getLineText() {
		return lineText;
	}
}
