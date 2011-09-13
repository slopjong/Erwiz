package de.slopjong.erwiz.plain;

import java.util.EnumSet;

import de.slopjong.erwiz.common.MessageGenerator;
import de.slopjong.erwiz.common.ResourceUtils;


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
	
	//parsing lines
	ERR_UNKNOWN_LINE_TYPE,
	ERR_ENTITY_LINES_NOT_OPEND,
	ERR_REL_LINES_NUM,
	ERR_ENTITY_NAME_DUP,
	ERR_ATTR_NAME_DUP,
	
	//parsing line which has some parts
	ERR_INVALID_TEXT_BEFORE,
	ERR_INVALID_TEXT_BETWEEN,
	ERR_INVALID_TEXT_AFTER,
	ERR_DEPENDENT_ENTITIES,
	ERR_DEPENDENT_PARENT,
	ERR_DEPENDENT_ENTITY_MTM,
	
	//parsing part
	ERR_INVALID_BRACKETS,
	
	//parsing text
	ERR_BLANK_ENTITY_NAME,
	ERR_BLANK_ATTR_NAME,
	ERR_BLANK_CARDINALITY,
	ERR_BLANK_VERB_PHRASE,
	ERR_INVALID_CARDINALITY_FORMAT,
	ERR_INVALID_CARDINALITY_CHAR,
	ERR_INVALID_OPTION_CHAR,
	ERR_INVALID_VERB_DIR,
	ERR_ONE_CARDINALITY_SYMBOLS,
	
	//parsing option list
	ERR_INVALID_OPTION_VALUE,
	ERR_BLANK_OPTION_NVPAIR,
	ERR_BLANK_OPTION_NAME,
	ERR_COLON_NOT_FOUND,
	ERR_COLON_TOO_MANY,
	ERR_OPTION_NAME_DUP,
	
	//line types
	LT_ENTITY,
	LT_ATTR,
	LT_REL,
	
	//part names
	PN_ENTITY_NAME,
	PN_ATTR_NAME,
	PN_CARDINALITY,
	PN_ENTITY_NAME_1,
	PN_ENTITY_NAME_2,
	PN_VERB_PHRASE,
	PN_OPTION_LIST,
	
	//misc
	MSG_LINE_NUM;
	
	//constants
	private static final String RESOURCE_NAME = Message.class.getPackage().getName()+".resources.messages";
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
