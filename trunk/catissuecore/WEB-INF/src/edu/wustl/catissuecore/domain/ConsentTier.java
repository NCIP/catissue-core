/**
 * <p>Title: ConsentTier Class>
 * <p>Description:   Class for Consent Tiers.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on November 21,2006
 */
package edu.wustl.catissuecore.domain;

import java.io.Serializable;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;


/**
 * @author ashish_gupta
 *@hibernate.class table="CATISSUE_CONSENT_TIER"
 */
public class ConsentTier extends AbstractDomainObject implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The consent statements.
	 */
	protected String statement;

	/**
     * System generated unique id.
     */
    protected Long id;
	/**
	 * @return the statement
	 * @hibernate.property name="statement" type="string" length="500" column="STATEMENT"
	 */
	public String getStatement()
	{
		return statement;
	}

	
	/**
	 * @param statement the statement to set
	 */
	public void setStatement(String statement)
	{
		this.statement = statement;
	}


	/**
	 * @hibernate.id name="id" type="long" column="IDENTIFIER" length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_CONSENT_TIER_SEQ"
	 */
	public Long getId()
	{		
		return id;
	}


	
	public void setAllValues(AbstractActionForm abstractForm) throws AssignDataException
	{
		// TODO Auto-generated method stub
		
	}


	
	public void setId(Long id)
	{
		this.id = id;		
	}
	
}
