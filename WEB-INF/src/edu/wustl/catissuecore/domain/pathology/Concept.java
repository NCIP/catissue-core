package edu.wustl.catissuecore.domain.pathology;


/**
 * @version 1.0
 * @created 26-Sep-2006 4:07:10 PM
 * Represents Concept associated with pathology report.
 */
public class Concept 
{

	/**
	 * 
	 */
	protected String conceptUniqueIdentifier;
	
	/**
	 * System generated unique ID
	 */
	protected Long id;
	
	/**
	 * Name of the concept
	 */
	protected String name;
	
	/**
	 * semantic type of the concept
	 */
	protected SemanticType semanticType;
	
	/**
	 * Concept referent collection
	 */
	protected ConceptReferent conceptReferentCollection;

	/**
	 * Constructor
	 */
	public Concept()
	{

	}

	/**
	 * @return collection of concept referent collection.
	 */
	public ConceptReferent getConceptReferentCollection()
	{
		return conceptReferentCollection;
	}

	/**
	 * @param conceptReferentCollection sets collection of concept referent
	 */
	public void setConceptReferentCollection(ConceptReferent conceptReferentCollection)
	{
		this.conceptReferentCollection = conceptReferentCollection;
	}

	/**
	 * @return concept unique identifier.
	 */
	public String getConceptUniqueIdentifier()
	{
		return conceptUniqueIdentifier;
	}

	/**
	 * @param conceptUniqueIdentifier sets concepts unique identifier
	 */
	public void setConceptUniqueIdentifier(String conceptUniqueIdentifier)
	{
		this.conceptUniqueIdentifier = conceptUniqueIdentifier;
	}

	/**
	 * @return system generated id for the concept
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * @param id sests system generated id
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * @return name of the concept
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name sets name of the concept
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return symantice type for the concept
	 */
	public SemanticType getSemanticType()
	{
		return semanticType;
	}

	/**
	 * @param semanticType sets semantic type
	 */
	public void setSemanticType(SemanticType semanticType)
	{
		this.semanticType = semanticType;
	}
	
}