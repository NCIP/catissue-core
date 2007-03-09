/**
 * <p>Title: SemanticType Class>
 * <p>Description:  SemanticType domain object.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on March 07,2007
 */
package edu.wustl.catissuecore.domain.pathology;


/**
 * @hibernate.class table="CATISSUE_SEMANTIC_TYPE"
 * Represents sematic type
 */
public class SemanticType
{

	/**
	 * identifier
	 */
	protected Long id;
	
	/**
	 * label
	 */
	protected String label;
	
	/**
	 * @return identifier
	 * @hibernate.id type="long" length="30" column="IDENTIFIER" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_SEMANTIC_TYPE_SEQ"
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * @param identifier sets identifier
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * @return label
	 * @hibernate.property type="string" length="500" column="LABEL"
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