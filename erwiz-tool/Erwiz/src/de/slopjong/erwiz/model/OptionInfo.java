package de.slopjong.erwiz.model;

/**
 * This class represents a option information contained by {@code OptionMap} class.
 * 
 * This class is immutable.
 * 
 * @author kono
 * @see de.slopjong.erwiz.model.OptionMap
 * @see de.slopjong.erwiz.model.OptionInfoHolder
 */
public final class OptionInfo {
	
	private final String name;
	private final Class<?> valueType;
	private final Object dflt;
	
	/**
	 * Constructs an instance of this class.
	 * 
	 * @param name option name
	 * @param valueType value type
	 * @param dflt a default value
	 */
	public OptionInfo(String name, Class<?> valueType, Object dflt) {
		this.name = name;
		this.valueType = valueType;
		this.dflt = dflt;
	}
	
	/**
	 * Retrieves the name in order to register a value to {@code OptionValues} objects.
	 * 
	 * @return the key in order to register a value to {@code OptionValues} objects
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Retrieves the value type of this option.
	 * 
	 * @return the value type of this option.
	 */
	public Class<?> getValueType() {
		return this.valueType;
	}
	
	/**
	 * Retrieves the default value of this option.
	 * 
	 * @return the default value of this option
	 */
	public Object getDefaultValue() {
		return this.dflt;
	}
}
