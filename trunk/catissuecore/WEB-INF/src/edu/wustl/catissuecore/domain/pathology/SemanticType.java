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

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;


/**
 * @hibernate.class table="CATISSUE_SEMANTIC_TYPE"
 * Represents sematic type
 */
public class SemanticType extends AbstractDomainObject 
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
	 * Set identifier of the sematic type
	 * @param id sets identifier
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * Returns label of the sematic type
	 * @return label label of the sematic type
	 * @hibernate.property type="string" length="500" column="LABEL"
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * Set label of the sematic type
	 * @param label sets label
	 */
	public void setLabel(String label)
	{
		this.label = label;
	}

	/**
	 * Default Constructor of the class
	 */
	public SemanticType()
	{

	}


	
	/**
	 * Method to populate abstractForm from domain Object
	 *  (non-Javadoc)
	 * @see edu.wustl.common.domain.AbstractDomainObject#setAllValues(edu.wustl.common.actionForm.AbstractActionForm)
	 * @param abstractForm abstract form
	 */
	public void setAllValues(AbstractActionForm abstractForm) throws AssignDataException
	{		
		
	}

}