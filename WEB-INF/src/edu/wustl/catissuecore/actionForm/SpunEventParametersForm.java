/**
 * <p>Title:SpunEventParametersForm Class</p>
 * <p>Description:  This Class handles the Spun event parameters..
 * <p> It extends the EventParametersForm class.    
 * Copyright:    Copyright (c) 2005
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on August 8th, 2005
 */

package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.SpunEventParameters;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * @author Mandar Deshmukh
 *
 * Description:  This Class handles the Spun event parameters..
 */
public class SpunEventParametersForm extends SpecimenEventParametersForm
{

	private static final long serialVersionUID = 1L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(SpunEventParametersForm.class);
	/**
	 * Rotational force applied to specimen.
	 */
	protected String gravityForce;

	/**
	 * Duration for which specimen is spun.
	 */
	protected String durationInMinutes;

	//	/**
	//     * Returns the rotational force applied to specimen. 
	//     * @return The rotational force applied to specimen.
	//     * @see #setGForce(double)
	//     */
	//	public double getGForce()
	//	{
	//		return gForce;
	//	}
	//
	//	/**
	//     * Sets the rotational force applied to specimen.
	//     * @param gForce the rotational force applied to specimen.
	//     * @see #getGForce()
	//     */
	//	public void setGForce(double gForce)
	//	{
	//		this.gForce = gForce;
	//	}

	/**
	 * Returns duration for which specimen is spun. 
	 * @return Duration for which specimen is spun.
	 * @see #setDurationInMinutes(int)
	 */
	public String getDurationInMinutes()
	{
		return durationInMinutes;
	}

	/**
	 * Sets the duration for which specimen is spun.
	 * @param durationInMinutes duration for which specimen is spun.
	 * @see #getDurationInMinutes()
	 */
	public void setDurationInMinutes(String durationInMinutes)
	{
		this.durationInMinutes = durationInMinutes;
	}

	//	 ----- SUPERCLASS METHODS
	/**
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getFormId()
	 * @return SPUN_EVENT_PARAMETERS_FORM_ID
	 */
	public int getFormId()
	{
		return Constants.SPUN_EVENT_PARAMETERS_FORM_ID;
	}

	/**
	* Populates all the fields from the domain object to the form bean.
	* @param abstractDomain An AbstractDomain Object  
	*/
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		try
		{
			super.setAllValues(abstractDomain);
			SpunEventParameters spunEventParametersObject = (SpunEventParameters) abstractDomain;
			this.gravityForce = Utility.toString(spunEventParametersObject.getGravityForce());
			this.durationInMinutes = Utility.toString(spunEventParametersObject
					.getDurationInMinutes());
		}
		catch (Exception excp)
		{
			logger.error(excp.getMessage());
		}
	}

	/**
	 * Overrides the validate method of ActionForm.
	 * @return error ActionErrors instance
	 * @param mapping Actionmapping instance
	 * @param request HttpServletRequest instance
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = super.validate(mapping, request);
		Validator validator = new Validator();

		try
		{
			if (!gravityForce.equals(""))
			{
				if (!validator.isDouble(gravityForce))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalid.gforge",
							ApplicationProperties.getValue("spuneventparameters.gforce")));
				}

				if (Double.parseDouble(gravityForce) <= 0)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"errors.capacity.greaterThan0", ApplicationProperties
									.getValue("spuneventparameters.gforce")));
				}
			}

			if (!durationInMinutes.equals(""))
			{
				if (durationInMinutes.contains(".") || !validator.isNumeric(durationInMinutes))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"errors.invalid.durationinminutes", ApplicationProperties
									.getValue("spuneventparameters.durationinminutes")));
				}

				if (Integer.parseInt(durationInMinutes) < 0)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"errors.capacity.greaterThan0", ApplicationProperties
									.getValue("spuneventparameters.durationinminutes")));
				}
			}
			logger.info("durationInMinutes: " + durationInMinutes);
		}
		catch (Exception excp)
		{
			logger.error(excp.getMessage());
		}
		return errors;
	}

	/**
	 * Resets the values of all the fields.
	 */
	protected void reset()
	{
		//	 	super.reset();
		//        this.gravityForce = 0;
		//        this.durationInMinutes = 0;
	}

	/**
	 * @return Returns the gravityForce.
	 */
	public String getGravityForce()
	{
		return gravityForce;
	}

	/**
	 * @param gravityForce The gravityForce to set.
	 */
	public void setGravityForce(String gravityForce)
	{
		this.gravityForce = gravityForce;
	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}
}