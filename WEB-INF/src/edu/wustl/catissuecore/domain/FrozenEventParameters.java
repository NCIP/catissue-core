/**
 * <p>Title: FrozenEventParameters Class>
 * <p>Description:  Attributes associated with a freezing event of a specimen. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Apr 7, 2005
 */

package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.FrozenEventParametersForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;

/**
 * Attributes associated with a freezing event of a specimen.
 * @hibernate.joined-subclass table="CATISSUE_FROZEN_EVENT_PARAM"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 * @author Aniruddha Phadnis
 */
public class FrozenEventParameters extends SpecimenEventParameters implements java.io.Serializable
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(FrozenEventParameters.class);

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Method applied on specimen to freeze it.
	 */
	protected String method;

	/**
	 * Returns method applied on specimen to freeze it.
	 * @return Method applied on specimen to freeze it.
	 * @see #setMethod(String)
	 * @hibernate.property name="method" type="string"
	 * column="METHOD" length="50"
	 */
	public String getMethod()
	{
		return this.method;
	}

	/**
	 * Sets method applied on specimen to freeze it.
	 * @param method method applied on specimen to freeze it.
	 * @see #getMethod()
	 */
	public void setMethod(String method)
	{
		this.method = method;
	}

	/**
	 * Default Constructor.
	 */
	public FrozenEventParameters()
	{
		super();
	}

	/**
	 * Parameterized constructor.
	 * @param abstractForm AbstractActionForm.
	 * @throws AssignDataException : AssignDataException
	 */
	public FrozenEventParameters(AbstractActionForm abstractForm) throws AssignDataException
	{
		super();
		this.setAllValues(abstractForm);
	}

	/**
	 * This function Copies the data from an FrozenEventParametersForm object to a
	 * FrozenEventParameters object.
	 * @param abstractForm An FrozenEventParametersForm object
	 * containing the information about the frozenEventParameters.
	 * @throws AssignDataException : AssignDataException
	 * */
	@Override
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		try
		{
			final FrozenEventParametersForm form = (FrozenEventParametersForm) abstractForm;
			this.method = form.getMethod();
			super.setAllValues(form);
		}
		catch (final Exception excp)
		{
			FrozenEventParameters.logger.error(excp.getMessage(),excp);
			excp.printStackTrace();
			final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
			throw new AssignDataException(errorKey, null, "FrozenEventParameters.java :");
		}
	}
}