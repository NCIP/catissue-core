
package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.actionForm.DistributionForm;
import edu.wustl.catissuecore.bizlogic.DistributionBizLogic;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.CommonAddEditAction;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;

/**
 * @author Rahul Ner
 * @since v1.1
 */
public class DistributionSubmitAction extends CommonAddEditAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(DistributionSubmitAction.class);

	/**
	 * Overrides the execute method of Action class. Sets the various fields in
	 * DistributionProtocol Add/Edit webpage.
	 *
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @return value for ActionForward object
	 * @throws IOException : IOException
	 * @throws ServletException : ServletException
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException
	{
		try
		{
			return this.executeAction(mapping, form, request, response);
		}
		catch (final ApplicationException e)
		{
			this.logger.error(e.getMessage(), e);
			final ActionErrors errors = new ActionErrors();
			final ActionError error = new ActionError(e.getMessage());
			errors.add(ActionErrors.GLOBAL_ERROR, error);
			this.saveErrors(request, errors);
			return mapping.findForward(Constants.FAILURE);
		}
	}

	/**
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @return value for ActionForward object
	 * @throws IOException : IOException
	 * @throws ServletException : ServletException
	 * @throws ApplicationException : ApplicationException
	 */
	private ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException, ApplicationException
	{

		final DistributionForm dform = (DistributionForm) form;
		long verificationKeyCounter = 0;
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final DistributionBizLogic bizLogic = (DistributionBizLogic) factory
				.getBizLogic(Constants.DISTRIBUTION_FORM_ID);

		boolean barcodeBased = true;
		if (dform.getDistributionBasedOn().intValue() == Constants.LABEL_BASED_DISTRIBUTION)
		{
			barcodeBased = false;
		}

		if (dform.getDistributionType().intValue() == Constants.SPECIMEN_DISTRIBUTION_TYPE)
		{
			System.out.println("Time for loop start:" + System.currentTimeMillis());
			final long startTime = System.currentTimeMillis();
			for (int i = 1; i <= dform.getCounter(); i++)
			{
				final String specimenkey = "DistributedItem:" + i + "_Specimen_id";
				final String verificationStatusKey = "DistributedItem:" + i + "_verificationKey";
				String barcodeKey = "DistributedItem:" + i + "_Specimen_barcode";

				if (!barcodeBased)
				{
					barcodeKey = "DistributedItem:" + i + "_Specimen_label";
				}

				final String barcodeLabel = (String) dform.getValue(barcodeKey);
				final Long specimenId = bizLogic.getSpecimenId(barcodeLabel, dform
						.getDistributionBasedOn());
				final String verificationStatus = (String) dform.getValue(verificationStatusKey);
				if (verificationStatus == null
						|| !verificationStatus.equalsIgnoreCase(Constants.VIEW_CONSENTS))
				{
					dform.setValue(specimenkey, specimenId.toString());
				}
				else
				{
					verificationKeyCounter = verificationKeyCounter + 1;
				}
			}
			if (dform.getCounter() == verificationKeyCounter)
			{
				final ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
				if (errors == null || errors.size() == 0)
				{
					ActionMessages messages = null;
					messages = new ActionMessages();
					messages.add(ActionErrors.GLOBAL_ERROR,
							new ActionError("errors.verify.Consent"));
					if (messages != null)
					{
						this.saveMessages(request, messages);
					}
				}
				return mapping.findForward(Constants.FAILURE);
			}
			System.out.println("Time for loop end:" + System.currentTimeMillis());
			final long endTime = System.currentTimeMillis();
			System.out.println("TIme to execute loop :" + (endTime - startTime));

		}
		else
		{
			final Map tempMap = new HashMap();

			for (int i = 1; i <= dform.getCounter(); i++)
			{
				// String specimenArraykey = "SpecimenArray:" + i + "_id";
				final String specimenArraykey = "DistributedItem:" + i + "_SpecimenArray_id";
				if (dform.getValue(specimenArraykey) != null
						&& !dform.getValue(specimenArraykey).equals(""))
				{
					tempMap.put(specimenArraykey, dform.getValue(specimenArraykey));
					continue;
				}

				String barcodeKey = "DistributedItem:" + i + "_Specimen_barcode";

				if (!barcodeBased)
				{
					barcodeKey = "DistributedItem:" + i + "_Specimen_label";
				}

				final String barcodeLabel = (String) dform.getValue(barcodeKey);
				final Long specimenArrayId = bizLogic.getSpecimenArrayId(barcodeLabel, dform
						.getDistributionBasedOn());
				if (bizLogic.isSpecimenArrayDistributed(specimenArrayId))
				{

					throw AppUtility.getApplicationException(null,
							"errors.distribution.arrayAlreadyDistributed", "");

				}

				tempMap.put(specimenArraykey, specimenArrayId.toString());
			}

			dform.setValues(tempMap);
		}

		ActionForward forward = super.execute(mapping, form, request, response);
		if (forward.getName().equals(Constants.SUCCESS))
		{
			if (dform.getDistributionType().intValue() != Constants.SPECIMEN_DISTRIBUTION_TYPE)
			{
				forward = mapping.findForward("arraySuccess");
			}
		}

		return forward;
	}
}
