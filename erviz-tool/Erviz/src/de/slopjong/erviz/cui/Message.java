package de.slopjong.erviz.cui;

import java.util.EnumSet;

import de.slopjong.erviz.common.MessageGenerator;
import de.slopjong.erviz.common.ResourceUtils;


/**
 * This class defines user messages.
 * This is a common class for this package.
 * 
 * Note: {@code initialize()} must be called before calling {@code getText()}.
 * 
 * @author kono
 */
enum Message {
	
	//Application Name
	APP_NAME,
	
	//Help Message
	HELP,
	
	//Error Messages
	ERR_INVALID_OPTION,
	ERR_INPUT_FILE_NOT_FOUND,
	ERR_INPUT_EXCEPTION,
	ERR_OUTPUT_FILE_NOT_FOUND,
	ERR_OUTPUT_EXCEPTION,
	ERR_TEXT_PARSING,
	ERR_DOT_GENERATION,
	ERR_UNKNOWN,
	
	//Debug Messages
	DBG_DEBUG_MODE_ON,
	DBG_READING_TEXT_COMPLETED,
	DBG_PARSING_TEXT_COMPLETED,
	DBG_GENERATING_DOT_COMPLETED,
	DBG_WRITING_DOT_COMPLETED,
	
	//Detail Messages for Cmmand Line Options
	CLO_UNKNOWN_ERD_NOTATION,
	CLO_UNKNOWN_COLOR_NAME,
	CLO_UNKNOWN_OPTION,
	CLO_INVALID_PARAM,
	
	//Misc
	MISC_STDIN,
	MISC_STDOUT,
	;
	
	private static final String RESOURCE_NAME = "de.slopjong.erviz.cui.resources.messages";
	private static MessageGenerator MESSAGE_GENERATOR;
	
	/**
	 * Initialize resource data to generate user messages.
	 * If error occured, the process is terminated.
	 */
	public static void initialize() {
		try {
			//initialize
			MESSAGE_GENERATOR = new MessageGenerator();
			MESSAGE_GENERATOR.setResourceBundle(ResourceUtils.getResourceBundle(RESOURCE_NAME));
			MESSAGE_GENERATOR.addMessage(APP_NAME, ResourceUtils.readAppNameText());
			
			//validate
			final EnumSet<Message> es = EnumSet.allOf(Message.class);
			MESSAGE_GENERATOR.validate(es);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Generates message text and returns it.
	 * 
	 * @param params message parameters
	 * @return the generated message text
	 */
	public String getText(Object... params) {
		return MESSAGE_GENERATOR.getText(this, params);
	}
	
}
