package edu.wustl.catissuecore.domain.pathology;


/**
 * @version 1.0
 * @created 26-Sep-2006 4:07:35 PM
 * Represents sematic type
 */
public class SemanticType
{

	/**
	 * identifier
	 */
	protected String identifier;
	
	/**
	 * label
	 */
	protected String label;

	/**
	 * @return identifier
	 */
	public String getIdentifier()
	{
		return identifier;
	}

	/**
	 * @param identifier sets identifier
	 */
	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}

	/**
	 * @return label
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * @param label sets label
	 */
	public void setLabel(String label)
	{
		this.label = label;
	}

	/**
	 * Constructor
	 */
	public SemanticType()
	{

	}

}