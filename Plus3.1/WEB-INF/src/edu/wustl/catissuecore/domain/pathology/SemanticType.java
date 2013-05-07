/**
 * <p>
 * Title: SemanticType Class>
 * <p>
 * Description: SemanticType domain object.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00 Created on March 07,2007
 */

package edu.wustl.catissuecore.domain.pathology;

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @hibernate.class table="CATISSUE_SEMANTIC_TYPE" Represents sematic type
 */
public class SemanticType extends AbstractDomainObject
{

	/**
	 *
	 */
	private static final long serialVersionUID = 8850664646682528327L;

	/**
	 * identifier.
	 */
	protected Long id;

	/**
	 * label.
	 */
	protected String label;

	/**
	 * @return identifier
	 * @hibernate.id type="long" length="30" column="IDENTIFIER"
	 *               generator-class="native"
	 * @hibernate.generator-param name="sequence"
	 *                            value="CATISSUE_SEMANTIC_TYPE_SEQ"
	 */
	@Override
	public Long getId()
	{
		return this.id;
	}

	/**
	 * Set identifier of the sematic type.
	 * @param identifier
	 *            sets identifier
	 */
	@Override
	public void setId(Long identifier)
	{
		this.id = identifier;
	}

	/**
	 * Returns label of the sematic type.
	 * @return label label of the sematic type
	 * @hibernate.property type="string" length="500" column="LABEL"
	 */
	public String getLabel()
	{
		return this.label;
	}

	/**
	 * Set label of the sematic type.
	 * @param label
	 *            sets label
	 */
	public void setLabel(String label)
	{
		this.label = label;
	}

	/**
	 * Default Constructor of the class.
	 */
	public SemanticType()
	{
		super();
	}

	/**
	 * Method to populate abstractForm from domain Object (non-Javadoc).
	 * @throws AssignDataException : AssignDataException
	 * @see edu.wustl.common.domain.AbstractDomainObject
	 * #setAllValues(edu.wustl.common.actionForm.AbstractActionForm)
	 * @param abstractForm
	 *            abstract form
	 */
	@Override
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		//
	}

}