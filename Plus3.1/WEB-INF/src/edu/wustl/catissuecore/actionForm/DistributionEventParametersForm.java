/**
 * <p>Title: TransferEventParametersForm Class</p>
 * <p>Description:  This Class handles the Transfer Event Parameters.
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

import edu.wustl.catissuecore.domain.TransferEventParameters;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class TransferEventParametersForm.
 *
 * @author mandar_deshmukh
 *
 * This Class handles the Transfer Event Parameters.
 */
public class DistributionEventParametersForm extends SpecimenEventParametersForm
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** logger Logger - Generic logger. */
	private static Logger logger = Logger.getCommonLogger(DistributionEventParametersForm.class);

	@Override
	public int getFormId()
	{
		return Constants.DISTRIBUTION_EVENT_PARAMETERS_FORM_ID;
	}

	/**
	 * Populates all the fields from the domain object to the form bean.
	 *
	 * @param abstractDomain An AbstractDomain Object
	 */
	@Override
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		try
		{
			super.setAllValues(abstractDomain);
		}
		catch (final Exception excp)
		{
			DistributionEventParametersForm.logger.error(excp.getMessage(),excp);
		}
	}

	/**
	 * Overrides the validate method of ActionForm.
	 *
	 * @param mapping Actionmapping instance
	 * @param request HttpServletRequest instance
	 *
	 * @return error ActionErrors instance
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		final ActionErrors errors = super.validate(mapping, request);
		final Validator validator = new Validator();

		try
		{
		}
		catch (final Exception excp)
		{
			DistributionEventParametersForm.logger.error(excp.getMessage(),excp);
			excp.printStackTrace();
		}
		return errors;
	}

	/**
	 * Resets the values of all the fields.
	 */
	@Override
	protected void reset()
	{
		

	}
	/* (non-Javadoc)
	 * @see edu.wustl.common.actionForm.AbstractActionForm#setAddNewObjectIdentifier(java.lang.String, java.lang.Long)
	 */
	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}

}
