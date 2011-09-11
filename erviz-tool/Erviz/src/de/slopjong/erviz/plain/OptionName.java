package de.slopjong.erviz.plain;

import de.slopjong.erviz.model.ColorPair;
import de.slopjong.erviz.model.OptionInfo;
import de.slopjong.erviz.model.OptionInfoHolder;

/**
 * This enum class defines option names for a model element.
 * These option names are used in this package.
 * 
 * @author kono
 * @see de.slopjong.erviz.model.OptionInfoHolder
 * @see de.slopjong.erviz.model.OptionInfo
 */
enum OptionName implements OptionInfoHolder {
	
	/** Title of the model */
	TITLE(String.class, ""),
	
	/** Font size of the title */
	TITLE_SIZE(Integer.class, 12),
	
	/** File paths or URLs which entities link to in a ERD */
	LINK_FILES(String.class, ""),
	
	/** Color for elements */
	COLOR(ColorPair.class, ColorPair.NONE),
	
	/** Mark for entities and entity attributes */
	MARK(String.class, ""),
	
	/** Mark for entities and entity attributes */
	N1(String.class, ""),
	
	/** Mark for entities and entity attributes */
	N2(String.class, ""),
	
	/** Reverse flag for verb phrases */
	VERB_REVERSE(Boolean.class, Boolean.FALSE);
	
	/**
	 * The option information which is corresponding to the option name
	 */
	private final OptionInfo option;
	
	/**
	 * Creates an instance of this enum class.
	 * 
	 * @param type the type of the option value
	 * @param dflt the default value
	 */
	private OptionName(Class<?> type, Object dflt) {
		assert (type == dflt.getClass()) : "type unmatched.";
		this.option = new OptionInfo(this.name(), type, dflt);
	}
	
	/**
	 * Retrieves the name used in text data files.
	 * 
	 * @return the name used in text data files.
	 */
	String getNameInFiles() {
		return this.option.getName().toLowerCase().replace('_', '-');
	}
	
	/**
	 * Returns the option information which is corresponding to the option name.
	 * 
	 * @return the option information which is corresponding to the option name
	 */
	@Override
	public OptionInfo getOptionInfo() {
		return this.option;
	}
	
}
