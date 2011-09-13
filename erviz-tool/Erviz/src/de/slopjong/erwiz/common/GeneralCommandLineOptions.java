package de.slopjong.erwiz.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to parse command line options by other classes.
 * This class is immutable.
 * 
 * @author kono
 */
public final class GeneralCommandLineOptions {

	//input
	private final String[] args;
	
	//output
	private final List<String> optioList = new ArrayList<String>();
	private final Map<String, List<String>> optionParamMap = new HashMap<String, List<String>>();
	private final List<String> unknownParamList = new ArrayList<String>();
	
	/**
	 * Constructs a object of this class.
	 * 
	 * @param args commandline arguments
	 * @throws NullPointerException if args is null.
	 */
	public GeneralCommandLineOptions(String[] args) {
		if (args == null) {
			throw new NullPointerException("arguments not specified.");
		}
		this.args = args;
	}
	
	/**
	 * Parses command line options which is set by using constructor.
	 * The result is saved in this object.
	 */
	public void parse() {
		
		List<String> paramList = this.unknownParamList; //parameters before the first option
		
		for (String arg : this.args) {
			if (arg.startsWith("-")) {
				this.optioList.add(arg);
				paramList = new ArrayList<String>();
				this.optionParamMap.put(arg, paramList);
			} else {
				paramList.add(arg);
			}
		}
		
	}
	
	/**
	 * Returns true if at least one option is specified, false otherwise.
	 * 
	 * @return true if at least one option is specified, false otherwise.
	 */
	public boolean hasOption() {
		return !this.optioList.isEmpty();
	}
	
	/**
	 * Returns true if the option is specified, false otherwise.
	 * 
	 * @param option a option string
	 * @return true if the option is specified, false otherwise
	 */
	public boolean has(String option) {
		return this.optionParamMap.containsKey(option);
	}
	
	/**
	 * Retrieves the specified option list.
	 * 
	 * @return the specified option list
	 */
	public List<String> getAllOptions() {
		return Collections.unmodifiableList(this.optioList);
	}
	
	/**
	 * Retrieves the parameters list corresponding to the specified option.
	 * The list object is unmodifiabl.
	 * 
	 * @param option a option string
	 * @return the parameters corresponding to the specified option
	 */
	public List<String> getOptionParams(String option) {
		List<String> list = Collections.unmodifiableList(this.optionParamMap.get(option));
		if (list == null) {
			list = Collections.emptyList();
		}
		return list;
	}
	
	/**
	 * Retrieves the parameters list which doesn't belong to a option.
	 * The list object is unmodifiabl.
	 * 
	 * @return the parameters list which doesn't belong to a option
	 */
	public List<String> getUnknownParams() {
		return Collections.unmodifiableList(this.unknownParamList);
	}
	
}
