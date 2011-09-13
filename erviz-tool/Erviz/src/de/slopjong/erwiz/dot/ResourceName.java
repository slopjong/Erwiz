package de.slopjong.erwiz.dot;

/**
 * This class represents a resource name for this package.
 * 
 * @author kono
 * @version 1.0
 */
enum ResourceName {
	
	//don't add "./" to following paths.
	
	/** graph attributes in dot files */
	GraphAttributes("graph", "resources/graph_attributes.txt"),
	
	/** node attributes in dot files */
	NodeAttributes("node", "resources/node_attributes.txt"),
	
	/** edge attributes in dot files */
	EdgeAttributes("edge", "resources/edge_attributes.txt");
	
	private final String name;
	private final String path;
	
	/**
	 * Creates an instance of this enum class.
	 * 
	 * @param name the name of resource
	 * @param path the path of resource
	 */
	ResourceName(String name, String path) {
		this.name = name;
		this.path = path;
	}
	
	/**
	 * Retrieves the name of resource.
	 * 
	 * @return the name of resource
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Retrieves the path of resource.
	 * If you use {@code Class#getResource()} or {@code Class#getResourceAsStream()},
	 * use {@code ResourceName.class} As a class object.
	 * 
	 * @return the path of resource
	 */
	public String getPath() {
		return this.path;
	}
	
}
