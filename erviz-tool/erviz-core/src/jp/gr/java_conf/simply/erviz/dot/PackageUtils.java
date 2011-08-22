package jp.gr.java_conf.simply.erviz.dot;

/**
 * This is a utility class for this package.
 * 
 * In this class, some static utility methods are defined.
 * 
 * @author kono
 * @version 1.0
 */
final class PackageUtils {
	
	/**
	 * Any instance of this class doesn't created.
	 */
	private PackageUtils() {
	}
	
	/**
	 * Joins dot attributes with comma.
	 * This method neglects blank strings of parameters.
	 * 
	 * @param params strings which will be joined
	 * @return the result string
	 */
	static String joinDotAttributes(String... params) {
		StringBuilder sb = new StringBuilder();
		
		for (String s : params) {
			if (s != null && !s.trim().equals("")) {
				sb.append(s.trim()).append(", ");
			}
		}
		
		return sb.toString().replaceFirst(", $", "");
	}
	
	/**
	 * Wraps the specified label text.
	 * This method prevents the label text from the incomplete image in png, jpeg, etc.
	 * 
	 * @param label the label text
	 * @return the wrapped label text
	 */
	static String wrapLabel(String label) {
		return !label.equals("") ? (" " + label + " ") : "";
	}
	
	/**
	 * Replaces the special characters in a label text to the escape sequences.
	 * 
	 * @param text the label text
	 * @return the replaced text
	 */
	static String escapeLabel(String text) {
		text= text.replace("\\", "\\\\"); //this replace must be the first
		text= text.replace("[", "\\[");
		text= text.replace("]", "\\]");
		text= text.replace("(", "\\(");
		text= text.replace(")", "\\)");
		text= text.replace("<", "\\<");
		text= text.replace(">", "\\>");
		text= text.replace("{", "\\{");
		text= text.replace("}", "\\}");
		text= text.replace("|", "\\|");
		text= text.replace(" ", "\\ "); //space
		text= text.replace("\t", "\\ \\ \\ \\ "); //4 spaces
		return text;
	}
	
}
