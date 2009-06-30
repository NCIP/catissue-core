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
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author ashish_gupta
 *@hibernate.class table="CATISSUE_CONSENT_TIER"
 */
public class ConsentTier extends AbstractDomainObject implements Serializable
{

	/**
	 * Serial Version ID of the class.
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
	 * @hibernate.id name="id" type="long" column="IDENTIFIER" length="30" unsaved-value="null"
	 * generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_CONSENT_TIER_SEQ"
	 * @return Long type.
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * Set all values.
	 * @param abstractForm of IValueObject type.
	 * @throws AssignDataException assignDataException.
	 */
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		//
	}

	/**
	 * Set Id.
	 * @param identifier of Long type.
	 */
	public void setId(Long identifier)
	{
		this.id = identifier;
	}

	/**
	 * Default Constructor.
	 */
	public ConsentTier()
	{
		super();
	}

	/**
	 * Parameterized Constructor.
	 * @param consentTier of ConsentTier type.
	 */
	public ConsentTier(ConsentTier consentTier)
	{
		super();
		if (consentTier.getId() != null && consentTier.getId().toString().trim().length() > 0)
		{
			this.id = consentTier.getId();
		}
		this.statement = consentTier.getStatement();
	}
}