package de.slopjong.erwiz.common;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * This class generates the user messages from message ID and parameters.
 * Each object of this class hols a resource bundle object for the messages.
 * This class works together with Enum class which represents message IDs.
 * 
 * @author kono
 */
public final class MessageGenerator {
	
	//error messages
	private static final String ERR_MSG_NOT_FOUND  = "Message ID not found.";
	private static final String ERR_MSG_ID_LINE    = "Message ID: %s";
	private static final String ERR_ILLEGAL_FORMAT = "Message Generation Error. Cause: Illegal format, Message ID: %s";
	private static final String ERR_UNKNOWN        = "Message Generation Error. Cause: Unknown error";
	
	//other constants
	private static final String NEW_LINE = System.getProperties().getProperty("line.separator");
	
	//members
	private ResourceBundle rb;
	private Map<String, String> map;
	
	/**
	 * Constructs a instance of this class.
	 */
	public MessageGenerator() {
		this.map = new HashMap<String, String>();
	}
	
	/**
	 * Sets a resource bundle object.
	 * 
	 * @param rb a resource bundle object
	 */
	public void setResourceBundle(ResourceBundle rb) {
		this.rb = rb;
	}
	
	/**
	 * Adds message data.
	 * 
	 * @param id a message ID
	 * @param message a message string
	 * @throws RuntimeException if error occured.
	 */
	public void addMessage(Enum<?> id, String message) {
		this.map.put(id.name(), message);
	}
	
	/**
	 * Validates resource data to generate user messages.
	 * If error occured, RuntimeException is thrown.
	 * 
	 * @param es a enum set object which contains message IDs.
	 * @throws RuntimeException if validation error occured.
	 */
	public void validate(EnumSet<?> es) {
		
		boolean invalid = false;
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(ERR_MSG_NOT_FOUND).append(NEW_LINE);
			
			for (Enum<?> e : es) {
				if (!this.rb.containsKey(e.name()) && !this.map.containsKey(e.name())) {
					invalid = true;
					final String errMsg = String.format(ERR_MSG_ID_LINE, e.name());
					sb.append(errMsg).append(NEW_LINE);
				}
			}
			
		} catch (Exception ex) {
			throw new RuntimeException(ERR_UNKNOWN, ex);
		}
		
		if (invalid) {
			throw new RuntimeException(sb.toString());
		}
		
	}
	
	/**
	 * Generates message text and returns it.
	 * 
	 * @param id an enum object which represents a message ID
	 * @param params message parameters (optional)
	 * @return the generated message text
	 */
	public String getText(Enum<?> id, Object... params) {
		try {
			String format;
			if (this.rb.containsKey(id.name())) {
				format = this.rb.getString(id.name());
			} else if (this.map.containsKey(id.name())) {
				format = this.map.get(id.name());
			} else {
				return id.name();
			}
			
			return String.format(format.trim(), params);
		} catch (IllegalFormatException ex) {
			return String.format(ERR_ILLEGAL_FORMAT, id);
		} catch (Exception ex) {
			return String.format(ERR_UNKNOWN);
		}
	}
	
}
