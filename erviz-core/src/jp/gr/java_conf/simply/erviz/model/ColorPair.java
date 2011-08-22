package jp.gr.java_conf.simply.erviz.model;

/**
 * The instance of this class contains a pair of colors values.
 * One of them indicates a dark color, and the other indicates a light color.
 * 
 * @author kono
 * @version 1.0
 * @see jp.gr.java_conf.simply.erviz.model.OptionMap
 */
public enum ColorPair {
	
	/** Not specified */
	NONE("", ""),
	
	/** White */
	WHITE("#000000", "#ffffff"),
	
	/** Red */
	RED("#c00000", "#fcecec"),
	
	/** Blue */
	BLUE("#000040", "#ececfc"),
	
	/** Green */
	GREEN("#002000", "#d0e0d0"),
	
	/** Yellow */
	YELLOW("#606000", "#fbfbdb"),
	
	/** orange */
	ORANGE("#804000", "#eee0a0");
	
	private String darkColor;
	private String lightColor;
	
	/**
	 * Creates an instance of this class.
	 * 
	 * @param darkColor dark color value
	 * @param lightColor light color value
	 */
	private ColorPair (String darkColor, String lightColor) {
		this.darkColor = darkColor;
		this.lightColor = lightColor;
	}
	
	/**
	 * Retrieves the dark color value.
	 * Its format is like "#ffffff".
	 * 
	 * @return the dark color value
	 */
	public String getDarkColor() {
		return this.darkColor;
	}
	
	/**
	 * Retrieves the light color value.
	 * Its format is like "#ffffff".
	 * 
	 * @return the light color value
	 */
	public String getLightColor() {
		return this.lightColor;
	}
	
	/**
	 * Returns a string representation of this object.
	 * 
	 * @return a string representation of this object.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(this.getClass().getName()).append("] ");
		sb.append("dark color:").append(this.darkColor).append(", ");
		sb.append("light color:").append(this.lightColor);
		return sb.toString();
	}
	
}
