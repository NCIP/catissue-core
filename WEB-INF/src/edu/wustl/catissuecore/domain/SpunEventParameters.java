/**
 * <p>Title: SpunEventParameters Class>
 * <p>Description:  Attributes associated with a spinning event of a specimen. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Apr 7, 2005
 */

package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.SpunEventParametersForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.util.logger.Logger;

/**
 * Attributes associated with a spinning event of a specimen.
 * @hibernate.joined-subclass table="CATISSUE_SPUN_EVENT_PARAMETERS"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 * @author Aniruddha Phadnis
 */
public class SpunEventParameters extends SpecimenEventParameters implements java.io.Serializable
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(SpunEventParameters.class);
	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Rotational force applied to specimen.
	 */
	protected Double gravityForce;

	/**
	 * Duration for which specimen is spun.
	 */
	protected Integer durationInMinutes;

	/**
	 * Returns the rotational force applied to specimen.
	 * @return The rotational force applied to specimen.
	 * @see #setGravityForce(Double)
	 * @hibernate.property name="gravityForce" type="double"
	 * column="GFORCE" length="30"
	 */
	public Double getGravityForce()
	{
		return this.gravityForce;
	}

	/**
	 * Sets the rotational force applied to specimen.
	 * @param gravityForce - gForce the rotational force applied to specimen.
	 * @see #getGravityForce()
	 */
	public void setGravityForce(Double gravityForce)
	{
		this.gravityForce = gravityForce;
	}

	/**
	 * Returns duration for which specimen is spun.
	 * @return Duration for which specimen is spun.
	 * @see #setDurationInMinutes(Integer)
	 * @hibernate.property name="durationInMinutes" type="int"
	 * column="DURATION_IN_MINUTES" length="30"
	 */
	public Integer getDurationInMinutes()
	{
		return this.durationInMinutes;
	}

	/**
	 * Sets the duration for which specimen is spun.
	 * @param durationInMinutes duration for which specimen is spun.
	 * @see #getDurationInMinutes()
	 */
	public void setDurationInMinutes(Integer durationInMinutes)
	{
		this.durationInMinutes = durationInMinutes;
	}

	/**
	 * Default Constructor.
	 * NOTE: Do not delete this constructor. Hibernet uses this by reflection API.
	 */
	public SpunEventParameters()
	{
		super();
	}

	/**
	 *  Parameterised constructor.
	 * @param abstractForm AbstractActionForm.
	 */
	public SpunEventParameters(AbstractActionForm abstractForm)
	{
		super();
		this.setAllValues(abstractForm);
	}

	/**
	 * This function Copies the data from an SpunEventParametersForm object to a SpunEventParameters object.
	 * @param abstractForm - SpunEventParametersForm An SpunEventParametersForm object
	 * containing the information about the SpunEventParameters.
	 */
	@Override
	public void setAllValues(IValueObject abstractForm)
	{
		try
		{
			final SpunEventParametersForm form = (SpunEventParametersForm) abstractForm;

			if (form.getGravityForce() != null && form.getGravityForce().trim().length() > 0)
			{
				this.gravityForce = Double.parseDouble(form.getGravityForce());
			}
			if (form.getDurationInMinutes() != null
					&& form.getDurationInMinutes().trim().length() > 0)
			{
				this.durationInMinutes = Integer.parseInt(form.getDurationInMinutes());
			}

			super.setAllValues(form);
		}
		catch (final Exception excp)
		{
			logger.error(excp.getMessage());
		}
	}
}