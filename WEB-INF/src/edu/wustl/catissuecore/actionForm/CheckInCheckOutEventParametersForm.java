/**
 * <p>Title: CheckInCheckOutEventParametersForm Class</p>
 * <p>Description:  This Class handles the CheckIn CheckOut Event Parameters. 
 * <p> It extends the EventParametersForm class.    
 * Copyright:    Copyright (c) 2005
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 28th, 2005
 */

package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
* This Class handles the CheckIn CheckOut Event Parameters.
*/
public class CheckInCheckOutEventParametersForm extends SpecimenEventParametersForm
{

	private static final long serialVersionUID = 1L;
	/**
	 * Type of the movement e.g. Check-in or Check-out.
	 */
	protected String storageStatus;
	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger
			.getLogger(ClinicalStudyRegistrationForm.class);

	/**
	 * @return Returns the storageStatus.
	 * Type of the movement e.g. Check-in or Check-out.
	 */
	public String getStorageStatus()
	{
		return storageStatus;
	}

	/**
	 * @param storageStatus The storageStatus to set.
	 */
	public void setStorageStatus(final String storageStatus)
	{
		this.storageStatus = storageStatus;
	}

	// ---- super class methods
	// ----- SUPERCLASS METHODS
	/** 
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getFormId()
	 * @return CHECKIN_CHECKOUT_EVENT_PARAMETERS_FORM_ID
	 * 
	 */
	public int getFormId()
	{
		return Constants.CHECKIN_CHECKOUT_EVENT_PARAMETERS_FORM_ID;
	}

	/** 
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setAllValues(edu.wustl.catissuecore.domain.AbstractDomainObject)
	 * @param abstractDomain An abstractDomain object. 
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		super.setAllValues(abstractDomain);
		final CheckInCheckOutEventParameter checkInCheckOutEventParameterObject = (CheckInCheckOutEventParameter) abstractDomain;
		this.storageStatus = Utility.toString(checkInCheckOutEventParameterObject
				.getStorageStatus());
	}

	/**
	 * Overrides the validate method of ActionForm.
	 * @return error ActionErrors instance
	 * @param mapping Actionmapping instance
	 * @param request HttpServletRequest instance
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{

		final ActionErrors errors = super.validate(mapping, request);
		final Validator validator = new Validator();

		try
		{
			// checks the storageStatus
			// changed from isEmpty to isValidOption as per bug 294 textbox to dropdown
			if (!validator.isValidOption(storageStatus))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties
								.getValue("checkincheckouteventparameter.storagestatus")));
			}
		}
		catch (Exception excp)
		{
			logger.error(excp.getMessage());
		}
		return errors;
	}

	/**
	 * Method to reset class Attributes
	 */
	protected void reset()
	{
		//      	super.reset();
		//        this.storageStatus = null;
	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}

}
