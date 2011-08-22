package jp.gr.java_conf.simply.erviz.model;

/**
 * This class represents a relationship in Entity-Relationship model.
 * 
 * This class is immutable. All input arguments is given at the object construction.
 * 
 * @author kono
 * @version 1.0
 * @see jp.gr.java_conf.simply.erviz.model.Model
 * @see jp.gr.java_conf.simply.erviz.model.Entity
 * @see jp.gr.java_conf.simply.erviz.model.CardinalityWithOptionality
 * @see jp.gr.java_conf.simply.erviz.model.VerbPhrase
 * @see jp.gr.java_conf.simply.erviz.model.OptionMap
 */
public final class Relationship {
	
	private static int objectCount = 0;
	
	private final String id;
	private final String nameOfEntity1;
	private Dependency dependencyOfEntity1;
	private final String nameOfEntity2;
	private Dependency dependencyOfEntity2;
	private final CardinalityWithOptionality cwo;
	private final VerbPhrase verbPhrase;
	private final OptionMap options;
	private final RelationshipType relType;
	private final ParentOrChild[] parentOrChilArray;
	
	/**
	 * Constructs an instance of this class.
	 * 
	 * @param nameOfEntity1 the name of the first entity of this relathinship
	 * @param dependencyOfEntity1 the dependency of the first entity on this relationship
	 * @param nameOfEntity2 name of the second entity of this relathinship
	 * @param dependencyOfEntity2 the dependency of the second entity on this relationship
	 * @param cwo the {@code CardinalityWithOptionality} object
	 * @param verbPhrase the {@code getVerbPhrase} object
	 * @param options the {@code OptionMap} object
	 */
	public Relationship(String nameOfEntity1, Dependency dependencyOfEntity1,
			String nameOfEntity2, Dependency dependencyOfEntity2,
			CardinalityWithOptionality cwo, VerbPhrase verbPhrase, OptionMap options) {
		
		this.id = "relationship_" + (++objectCount);
		this.nameOfEntity1 = nameOfEntity1;
		this.dependencyOfEntity1 = dependencyOfEntity1;
		this.nameOfEntity2 = nameOfEntity2;
		this.dependencyOfEntity2 = dependencyOfEntity2;
		this.cwo = cwo;
		this.verbPhrase = verbPhrase;
		this.options = options;
		this.relType = judgeRelType(cwo, dependencyOfEntity1, dependencyOfEntity2);
		this.parentOrChilArray = judgeParentOrChild(cwo, dependencyOfEntity1, dependencyOfEntity2);
	}
	
	private static RelationshipType judgeRelType(CardinalityWithOptionality cwo, Dependency dependency1, Dependency dependency2) {
		
		final CardinalityElement c1 = cwo.getCardinality1();
		final CardinalityElement c2 = cwo.getCardinality2();
		
		if (c1 == CardinalityElement.ONE && c2 == CardinalityElement.ONE ||
				c1 == CardinalityElement.ONE && c2 == CardinalityElement.MANY ||
				c1 == CardinalityElement.MANY && c2 == CardinalityElement.ONE) { //one to one, one to many, many to one
			
			if (dependency1 == Dependency.INDEPENDENT && dependency2 == Dependency.INDEPENDENT) {
				return RelationshipType.NON_IDENTIFYING;
			} else {
				return RelationshipType.IDENTIFYING;
			}
			
		} else if (c1 == CardinalityElement.MANY && c2 == CardinalityElement.MANY) { //many to many
			
			return RelationshipType.NON_SCPECIFIC;
			
		} else if (c1 == CardinalityElement.NONE && c2 == CardinalityElement.NONE) { //unknown (both)
			
			if (dependency1 == Dependency.INDEPENDENT && dependency2 == Dependency.INDEPENDENT) {
				return RelationshipType.NONE;
			} else {
				return RelationshipType.IDENTIFYING;
			}
		
		} else { //invalid case
			
			return RelationshipType.NONE;
			
		}
		
	}
	
	private static ParentOrChild[] judgeParentOrChild(CardinalityWithOptionality cwo, Dependency dependency1, Dependency dependency2) {
		
		//4 result types
		final ParentOrChild[] PARENT_CHILD = new ParentOrChild[]{ParentOrChild.PARENT, ParentOrChild.CHILD};
		final ParentOrChild[] CHILD_PARENT = new ParentOrChild[]{ParentOrChild.CHILD, ParentOrChild.PARENT};
		final ParentOrChild[] CHILD_CHILD = new ParentOrChild[]{ParentOrChild.CHILD, ParentOrChild.CHILD};
		final ParentOrChild[] NONE_NONE = new ParentOrChild[]{ParentOrChild.NONE, ParentOrChild.NONE};
		
		final CardinalityElement c1 = cwo.getCardinality1();
		final CardinalityElement c2 = cwo.getCardinality2();
		
		if (c1 == CardinalityElement.ONE && c2 == CardinalityElement.ONE) {
			
			if (dependency1 == Dependency.INDEPENDENT && dependency2 == Dependency.INDEPENDENT) {
				return PARENT_CHILD; //the first entity is regarded as the parent
			}
			
			if (dependency1 == Dependency.INDEPENDENT && dependency2 == Dependency.DEPENDENT) {
				return PARENT_CHILD;
			}
			
			if (dependency1 == Dependency.DEPENDENT && dependency2 == Dependency.INDEPENDENT) {
				return CHILD_PARENT;
				
			}
			
			return NONE_NONE; //invalid case
		}
		
		if (c1 == CardinalityElement.ONE && c2 == CardinalityElement.MANY) {
			return PARENT_CHILD;
		}
		
		if (c1 == CardinalityElement.MANY && c2 == CardinalityElement.ONE) {
			return CHILD_PARENT;
		}
		
		if (c1 == CardinalityElement.MANY && c2 == CardinalityElement.MANY) {
			return CHILD_CHILD;
		}
		
		if (c1 == CardinalityElement.NONE && c2 == CardinalityElement.NONE) {
			
			if (dependency1 == Dependency.INDEPENDENT && dependency2 == Dependency.INDEPENDENT) {
				return NONE_NONE; //can't judge
			}
			
			if (dependency1 == Dependency.INDEPENDENT && dependency2 == Dependency.DEPENDENT) {
				return PARENT_CHILD;
			}
			
			if (dependency1 == Dependency.DEPENDENT && dependency2 == Dependency.INDEPENDENT) {
				return CHILD_PARENT;
			}
			
			return NONE_NONE; //invalid case
		}
		
		return NONE_NONE; //invalid case
	}
	
	/**
	 * Retrieves the id of this relationship object.
	 * 
	 * @return the id of this relationship object
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * Retrieves the name of the first entity of this relationship.
	 * 
	 * @return the name the first entity of this relationship
	 */
	public String getNameOfEntity1() {
		return this.nameOfEntity1;
	}
	
	/**
	 * Retrieves the dependency of the first entity on this relationship.
	 * 
	 * @return the dependency of the first entity on this relationship
	 */
	public Dependency getDependencyOfEntity1() {
		return this.dependencyOfEntity1;
	}
	
	/**
	 * Retrieves the name of the second entity of this relationship.
	 * 
	 * @return the name the second entity of this relationship
	 */
	public String getNameOfEntity2() {
		return this.nameOfEntity2;
	}
	
	/**
	 * Retrieves the dependency of the second entity on this relationship.
	 * 
	 * @return the dependency of the second entity on this relationship
	 */
	public Dependency getDependencyOfEntity2() {
		return this.dependencyOfEntity2;
	}
	
	/**
	 * Retrieve the {@code CardinalityWithOptionality} object.
	 * 
	 * @return the {@code CardinalityWithOptionality} object
	 */
	public CardinalityWithOptionality getCardinality() {
		return this.cwo;
	}
	
	/**
	 * Retrieve the {@code getVerbPhrase} object.
	 * 
	 * @return the {@code getVerbPhrase} object
	 */
	public VerbPhrase getVerbPhrase() {
		return this.verbPhrase;
	}
	
	/**
	 * Retrieves the {@code OptionMap} object of this relationship.
	 * 
	 * @return the {@code OptionMap} object
	 */
	public OptionMap getOptions() {
		return this.options;
	}
	
	/**
	 * Retrieves the type of this relationship.
	 * 
	 * @return the type of this relationship
	 */
	public RelationshipType getRelType() {
		return this.relType;
	}
	
	/**
	 * Retrieves the parent-child information of this relationship.
	 * This method returns a array which has two elements.
	 * The first element corresponds to the first entity, and the second element
	 * corresponds the second entity.
	 * 
	 * @return the parent-child information of this relationship
	 */
	public ParentOrChild[] getParentOrChildArray() {
		return this.parentOrChilArray;
	}
	
	/**
	 * Returns a string representation of this object.
	 * 
	 * @param indent indent string
	 * @return a string representation of this object.
	 */
	public String toString(String indent) {
		
		StringBuilder sb = new StringBuilder();
		String memberIndent = indent + "\t";
		
		sb.append(indent);
		sb.append("[").append(this.getClass().getName()).append("]\n");
		
		sb.append(indent);
		sb.append("id:").append(this.id).append(", ");
		sb.append("relationship-type:").append(this.relType).append(", ");
		sb.append("entity1-name:").append(this.nameOfEntity1).append(", ");
		sb.append("entity1-dependency:").append(this.dependencyOfEntity1.name().toLowerCase()).append(", ");
		sb.append("entity2-name:").append(this.nameOfEntity2).append(", ");
		sb.append("entity2-dependency:").append(this.dependencyOfEntity2.name().toLowerCase()).append(", ");
		
		sb.append(indent);
		sb.append("cardinality/optionality:\n");
		sb.append(this.cwo.toString(memberIndent)).append("\n");
		
		sb.append(indent);
		sb.append("verb phrase:\n");
		sb.append(this.verbPhrase.toString(memberIndent)).append("\n");
		
		sb.append(indent);
		sb.append("options:\n");
		sb.append(this.options.toString(memberIndent));
		
		return sb.toString();
	}
	
}
