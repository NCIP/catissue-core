package edu.wustl.catissuecore.domain.pathology;


/**
 * Represents concept referent classification
 * @version 1.0
 * @created 26-Sep-2006 4:07:10 PM
 */
public class ConceptReferentClassification 
{

	/**
	 * System generated unique id
	 */
	protected Long id;
	
	/**
	 * name od the classification
	 */
	protected String name;
	
	/**
	 * collection of concept referent
	 */
	protected ConceptReferent conceptReferentCollection;
	

	/**
	 * Constructor
	 */
	public ConceptReferentClassification()
	{

	}

	/**
	 * @return collection of concept referent
	 */
	public ConceptReferent getConceptReferentCollection()
	{
		return conceptReferentCollection;
	}

	/**
	 * @param conceptReferentCollection sets concept referent collection
	 */
	public void setConceptReferentCollection(
			ConceptReferent conceptReferentCollection)
	{
		conceptReferentCollection = conceptReferentCollection;
	}

	/**
	 * @return system generated id
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * @param id sets system generated id
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * @return name of referent classification 
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name sets name of the referent classification
	 */
	public void setName(String name)
	{
		this.name = name;
	}

}