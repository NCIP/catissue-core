/*
 * Created on Jul 29, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.DefaultValueManager;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * @author mandar_deshmukh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ReceivedEventParametersForm extends SpecimenEventParametersForm
{

	private static final long serialVersionUID = 1L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger
			.getLogger(ReceivedEventParametersForm.class);
	/**
	 * Grossly evaluated quality of the received specimen.
	 */
	protected String receivedQuality = (String) DefaultValueManager
			.getDefaultValue(Constants.DEFAULT_RECEIVED_QUALITY);

	/**
	 * Returns the receivedQuality of the specimen.
	 * @hibernate.property name="receivedQuality" type="string" column="RECEIVED_QUALITY" length=50"
	 * @return receivedQuality of the specimen.
	 */
	public String getReceivedQuality()
	{
		return this.receivedQuality;
	}

	/**
	 * Sets the receivedQuality of the SPECIMEN.
	 * @param receivedQuality receivedQuality of the SPECIMEN.
	 */
	public void setReceivedQuality(String receivedQuality)
	{
		this.receivedQuality = receivedQuality;
	}

	//	 ---- super class methods
	// ----- SUPERCLASS METHODS
	/**
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getFormId()
	 * @return RECEIVED_EVENT_PARAMETERS_FORM_ID
	 */
	@Override
	public int getFormId()
	{
		return Constants.RECEIVED_EVENT_PARAMETERS_FORM_ID;
	}

	/**
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setAllValues(edu.wustl.catissuecore.domain.AbstractDomainObject)
	 * @param abstractDomain An AbstractDomain Object  
	 */
	@Override
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		super.setAllValues(abstractDomain);
		final ReceivedEventParameters receivedEventParameterObject = (ReceivedEventParameters) abstractDomain;
		this.receivedQuality = CommonUtilities.toString(receivedEventParameterObject
				.getReceivedQuality());
	}

	/**
	 * Overrides the validate method of ActionForm.
	 * @return error ActionErrors instance
	 * @param mapping Actionmapping instance
	 * @param request HttpServletRequest instance
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{

		final ActionErrors errors = super.validate(mapping, request);
		final Validator validator = new Validator();

		try
		{
			// checks the receivedQuality
			if (!validator.isValidOption(this.receivedQuality))
			{
				logger.debug(" not a valid option");
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("receivedeventparameters.receivedquality")));
			}
			logger.debug(this.receivedQuality + " is a valid option");
		}
		catch (final Exception excp)
		{
			logger.error(excp.getMessage());
		}
		return errors;
	}

	/**
	 * Resets the values of all the fields.
	 * This method defined in ActionForm is overridden in this class.
	 */
	@Override
	protected void reset()
	{
		//         super.reset();
		//         this.receivedQuality = null;
	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}

}
