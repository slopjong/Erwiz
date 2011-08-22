package jp.gr.java_conf.simply.erviz.plain;

import jp.gr.java_conf.simply.erviz.plain.LineData;

/**
 * An exception that provides information on an error of text parsing .
 * To construct an instance of this class, call {@code create()} method 
 * with appropriate arguments.
 * 
 * @author kono
 * @version 1.0
 */
final class ParserException extends Exception {
	
	static final long serialVersionUID = 6563811211675551004L;
	
	/**
	 * This is a utility method to create an instance of this class.
	 * If there are no lines, set {@code line} argument to {@code null}.
	 * 
	 * @param description description about the error
	 * @param line the line data of the input file
	 * @return an instance of this exception class.
	 * @throws NullPointerException if a null argument is specified
	 */
	public static ParserException create(String description, LineData line) {
		if (description == null) {
			throw new NullPointerException();
		}
		
		if (line == null) {
			return new ParserException(description);
		} else {
			String format = "%s\n%s %s: %s";
			String message = String.format(format, description, 
					Message.MSG_LINE_NUM.getText(), line.getLineNumber(), line.getLineText());
			return new ParserException(message);	
		}
	}
	
	/**
	 * Constructs an object of this class.
	 * This is used by {@code create()} method.
	 * 
	 * @param message exception message
	 */
	private ParserException(String message) {
		super(message);
	}

}
