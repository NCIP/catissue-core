/**
 * <p>Title: ProcedureEventParameters Class</p>
 * <p>Description: Attributes associated with a customized procedure that is applied
 * on a specimen to process it.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.ProcedureEventParametersForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.util.logger.Logger;

/**
 * Attributes associated with a customized procedure that is applied on a specimen to process it.
 * @hibernate.joined-subclass table="CATISSUE_PROCEDURE_EVENT_PARAM"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class ProcedureEventParameters extends SpecimenEventParameters
		implements
			java.io.Serializable
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger
			.getLogger(ProcedureEventParameters.class);

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * URL to the document that describes detail information of customized process.
	 */
	protected String url;

	/**
	 * Name of the customized procedure.
	 */
	protected String name;

	/**
	 * Returns the url of the procedure document.
	 * @hibernate.property name="url" type="string" column="URL" length="255"
	 * not-null="true"
	 * @return url of the procedure document.
	 */
	public String getUrl()
	{
		return this.url;
	}

	/**
	 * Sets the url of the procedure document.
	 * @param url url of the procedure document.
	 */
	public void setUrl(String url)
	{
		this.url = url;
	}

	/**
	 * Returns the name of the procedure.
	 * @hibernate.property name="name" type="string" column="NAME" length="255"
	 * not-null="true"
	 * @return name of the procedure.
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Sets the name of the procedure document.
	 * @param name name of the procedure.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * NOTE: Do not delete this constructor. Hibernet uses this by reflection API.
	 * */
	public ProcedureEventParameters()
	{
		super();
	}

	/**
	 *  Parameterised constructor.
	 * @param abstractForm AbstractActionForm.
	 */
	public ProcedureEventParameters(AbstractActionForm abstractForm)
	{
		super();
		this.setAllValues(abstractForm);
	}

	/**
	 * This function Copies the data from an ProcedureEventParametersForm object to a
	 * ProcedureEventParameters object.
	 * @param abstractForm - ProcedureEventParametersForm An ProcedureEventParametersForm object
	 * containing the information about the ProcedureEventParameters.
	 * */
	@Override
	public void setAllValues(IValueObject abstractForm)
	{
		try
		{
			final ProcedureEventParametersForm form = (ProcedureEventParametersForm) abstractForm;

			this.url = form.getUrl();
			this.name = form.getName();

			super.setAllValues(form);
		}
		catch (final Exception excp)
		{
			logger.error(excp.getMessage());
		}
	}
}