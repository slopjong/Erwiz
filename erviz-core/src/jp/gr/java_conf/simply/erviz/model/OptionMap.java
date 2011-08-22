package jp.gr.java_conf.simply.erviz.model;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a map which maps option names to option values for a model element.
 * 
 * @author kono
 * @see jp.gr.java_conf.simply.erviz.model.Model
 * @see jp.gr.java_conf.simply.erviz.model.Entity
 * @see jp.gr.java_conf.simply.erviz.model.EntityAttribute
 * @see jp.gr.java_conf.simply.erviz.model.Relationship
 * @see jp.gr.java_conf.simply.erviz.model.OptionInfoHolder
 * @see jp.gr.java_conf.simply.erviz.model.OptionInfo
 */
public final class OptionMap {
	
	private Map<String, Object> map = new HashMap<String, Object>();
	
	/**
	 * Creates an instance of this class.
	 */
	public OptionMap() {
	}
	
	/**
	 * Creates an instance of this class with default values.
	 * 
	 * @param optionInfoHolders the option holders which are used to set default values.
	 */
	public OptionMap(OptionInfoHolder... optionInfoHolders) {
		
		for (OptionInfoHolder holder : optionInfoHolders) {
			setDefaultValue(holder);
		}
		
	}
	
	/**
	 * Registers a Boolean option value.
	 * 
	 * @param holder the option information holder
	 * @param value the option value
	 * @throws IllegalArgumentException if type error occured
	 */
	public void setBoolean(OptionInfoHolder holder, Boolean value) {
		setValue(holder, value, Boolean.class);
	}
	
	/**
	 * Retrieves a Boolean option value.
	 * 
	 * @param holder option name
	 * @return option value
	 * @throws IllegalArgumentException if type error occured
	 */
	public Boolean getBoolean(OptionInfoHolder holder) {
		return (Boolean)getValue(holder, Boolean.class);
	}
	
	/**
	 * Registers a Integer option value.
	 * 
	 * @param holder the option information holder
	 * @param value the option value
	 * @throws IllegalArgumentException if type error occured
	 */
	public void setInteger(OptionInfoHolder holder, Integer value) {
		setValue(holder, value, Integer.class);
	}
	
	/**
	 * Retrieves a Integer option value.
	 * 
	 * @param holder option name
	 * @return option value
	 * @throws IllegalArgumentException if type error occured
	 */
	public Integer getInteger(OptionInfoHolder holder) {
		return (Integer)getValue(holder, Integer.class);
	}
	
	/**
	 * Registers a Double option value.
	 * 
	 * @param holder the option information holder
	 * @param value the option value
	 * @throws IllegalArgumentException if type error occured
	 */
	public void setDouble(OptionInfoHolder holder, Double value) {
		setValue(holder, value, Double.class);
	}
	
	/**
	 * Retrieves a Double option value.
	 * 
	 * @param holder option name
	 * @return option value
	 * @throws IllegalArgumentException if type error occured
	 */
	public Double getDouble(OptionInfoHolder holder) {
		return (Double)getValue(holder, Double.class);
	}
	
	/**
	 * Registers a String option value.
	 * 
	 * @param holder the option information holder
	 * @param value the option value
	 * @throws IllegalArgumentException if type error occured
	 */
	public void setString(OptionInfoHolder holder, String value) {
		setValue(holder, value, String.class);
	}
	
	/**
	 * Retrieves a String option value.
	 * 
	 * @param holder option name
	 * @return option value
	 * @throws IllegalArgumentException if type error occured
	 */
	public String getString(OptionInfoHolder holder) {
		return (String)getValue(holder, String.class);
	}
	
	/**
	 * Registers a ColorPair option value.
	 * 
	 * @param holder the option information holder
	 * @param value the option value
	 * @throws IllegalArgumentException if type error occured
	 */
	public void setColorPair(OptionInfoHolder holder, ColorPair value) {
		setValue(holder, value, ColorPair.class);
	}
	
	/**
	 * Retrieves a ColorPair option value.
	 * 
	 * @param holder option name
	 * @return option value
	 * @throws IllegalArgumentException if type error occured
	 */
	public ColorPair getColorPair(OptionInfoHolder holder) {
		return (ColorPair)getValue(holder, ColorPair.class);
	}
	
	/**
	 * Registers a default value.
	 * This is a private common method.
	 */
	private void setDefaultValue(OptionInfoHolder holder) {
		
		final OptionInfo info = holder.getOptionInfo();
		
		if (!this.map.containsKey(info.getName()) || this.map.get(info.getName()) == null) {
				this.map.put(info.getName(), info.getDefaultValue());
		}
		
	}
	
	/**
	 * Registers a option value.
	 * This is a private common method.
	 */
	private void setValue(OptionInfoHolder holder, Object value, Class<?> expectedClass) {
		final OptionInfo info = holder.getOptionInfo();
		
		if (info.getValueType() != expectedClass) {
			final String invoker = "set" + expectedClass.getName();
			final String message = inapplicableMethodErrorMsg(invoker, info);
			throw new IllegalArgumentException(message);
		}
		
		if (info.getValueType() != value.getClass()) {
			final String message = valueTypeErrorMsg(info, value);
			throw new IllegalArgumentException(message);
		}
		
		this.map.put(info.getName(), value);
	}
	
	/**
	 * Retrieves a option value.
	 * This is a private common method.
	 */
	private Object getValue(OptionInfoHolder holder, Class<?> expectedClass) {
		final OptionInfo info = holder.getOptionInfo();
		
		if (info.getValueType() != expectedClass) {
			final String invoker = "get" + expectedClass.getName();
			final String message = inapplicableMethodErrorMsg(invoker, info);
			throw new IllegalArgumentException(message);
		}
		
		Object value = this.map.get(info.getName());
		
		if (value != null && info.getValueType() != value.getClass()) {
			final String message = valueTypeErrorMsg(info, value);
			throw new IllegalArgumentException(message);
		}
		
		return value;
	}
	
	/**
	 * private utility method
	 */
	private String inapplicableMethodErrorMsg(String methodName, OptionInfo info) {
		return "The called method is inapplicable for the option. " + 
			"method name : [" + methodName + "]  option name : [" + info.getName() + 
				"]  option type : [" + info.getClass().getName()+ "]";
	}
	
	/**
	 * private utility method
	 */
	private String valueTypeErrorMsg(OptionInfo info, Object value) {
		return "The option type and the value type are unmatched. " + 
			"option name : [" + info.getName() + "]  option type : [" + info.getValueType().getName() + 
				"]  value type : [" + value.getClass().getName() + "]";
	}
	
	/**
	 * Returns a string representation of this object.
	 * 
	 * @param indent indent string
	 * @return a string representation of this object.
	 */
	public String toString(String indent) {
		
		final StringBuilder sb = new StringBuilder();
		
		sb.append(indent);
		sb.append("[").append(this.getClass().getName()).append("]\n");
		
		sb.append(indent);
		for (String key : this.map.keySet()) {
			Object value = this.map.get(key);
			sb.append(key.toLowerCase()).append("=").append(value).append(", ");
		}
		
		return sb.toString().replace(", $", "");
	}
	
}
