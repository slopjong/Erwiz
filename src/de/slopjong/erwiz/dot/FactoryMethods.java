package de.slopjong.erwiz.dot;

import de.slopjong.erwiz.model.ErdNotation;
import de.slopjong.erwiz.model.OptionMap;

/**
 * This class has a factory method for this package.
 * 
 * @author kono
 * @version 1.0
 */
final class FactoryMethods {
	
	/**
	 * The factory method for entity/relationship generators.
	 * 
	 * @param notation ERD nortation
	 * @param direction the rank direction of dot file
	 * @param globalOptions the global options
	 * @return the entity/relationship generator object
	 */
	static ERGenerator createERGenerator(ErdNotation notation, RankDirection direction, OptionMap globalOptions) {
		switch (notation) {
			case IE:
				return new IeERGenerator(direction, globalOptions, false);
			case IE_STRICT:
				return new IeERGenerator(direction, globalOptions, true);
			case IDEF1X:
				return new Idef1xERGenerator(direction, globalOptions);
			case NONE:
				assert false : "notation not specified";
				return new DefaultERGenerator(direction, globalOptions);
			default:
				assert false : "unknown notation : [" + notation + "]";
				return new DefaultERGenerator(direction, globalOptions);	
		}
	}
	
}
