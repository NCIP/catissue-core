/**
 * <p>Title: QuickEventsSpecimenSearchAction Class</p>
 * <p>Description:  This class validates the entries from the
 *  Quickevents webpage and directs the flow accordingly. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 3, 2006
 */

package edu.wustl.catissuecore.action;


import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.QuickEventsForm;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * <p>Title: QuickEventsSpecimenSearchAction Class</p>
 * <p>Description:  This class validates the entries from the
 *  Quickevents webpage and directs the flow accordingly. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 3, 2006
 */

public class QuickEventsSpecimenSearchAction extends BaseAction
{

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final QuickEventsForm qEForm = (QuickEventsForm) form;
		Logger.out.debug(qEForm.getSpecimenEventParameter());
		String pageOf = Constants.SUCCESS;
		String cpQuery = request.getParameter(Constants.CP_QUERY);
		request.setAttribute(Constants.CP_QUERY, cpQuery);
		pageOf = this.validate(request, qEForm);

		if (pageOf.equals(Constants.SUCCESS))
		{
			//DefaultBizLogic bizLogic = BizLogicFactory.getDefaultBizLogic();
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
			String specimenFound = "0";
			String errorString = "";
			String invalidLabels="";
			String specimenLabel = "";
			String specimenIds="";
			if (("1").equals(qEForm.getCheckedButton()))
			{
				specimenLabel = qEForm.getSpecimenLabel();
				
				// Handle Multiple Specimen Case
				
				if(specimenLabel.contains(","))
				{
					//List<String> labels=this.populateSpecimenLabelList(specimenLabel);
					List<String> labels=AppUtility.getListOnCommaToken(specimenLabel);
					Iterator<String> lblIterator=labels.iterator();
					while(lblIterator.hasNext())
					{
						String singleSpecimenLabel=lblIterator.next();
						specimenFound = this.isExistingSpecimen(Constants.SYSTEM_LABEL, singleSpecimenLabel,bizLogic);
						if(("0").equalsIgnoreCase(specimenFound) && singleSpecimenLabel !=null)
						{
							invalidLabels = invalidLabels + singleSpecimenLabel+" ,";
						}
						else
						{
							specimenIds =specimenIds + specimenFound+",";
						}
					}
					if(invalidLabels.contains(","))
					{
						invalidLabels=invalidLabels.substring(0,invalidLabels.length()-1);
					}
					if(specimenIds.contains(","))
					{
						specimenIds=specimenIds.substring(0,specimenIds.length()-1);
					}
				}
				else
				{
					specimenFound = this.isExistingSpecimen(Constants.SYSTEM_LABEL, specimenLabel,bizLogic);
					specimenIds=specimenFound;
				}	
				
				errorString = ApplicationProperties.getValue("quickEvents.specimenLabel");
				errorString =errorString +" "+invalidLabels;
			}
			else if (("2").equals(qEForm.getCheckedButton()))
			{
				final String barCode = qEForm.getBarCode();
				specimenFound = this
						.isExistingSpecimen(Constants.SYSTEM_BARCODE, barCode, bizLogic);
				errorString = ApplicationProperties.getValue("quickEvents.barcode");
			}

			if (! ("0").equals(specimenFound) && invalidLabels.equals("") )
			{
				request.setAttribute(Constants.SPECIMEN_ID, specimenIds);
				request.setAttribute(Constants.SPECIMEN_LABEL, specimenLabel);
				final String selectedEvent = qEForm.getSpecimenEventParameter();
				request.setAttribute(Constants.EVENT_SELECTED, selectedEvent);

				request.setAttribute("isQuickEvent", "true");
				pageOf = Constants.SUCCESS;
			}
			else
			{
				ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
				if (errors == null)
				{
					errors = new ActionErrors();
				}
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"quickEvents.specimen.notExists", errorString));
				this.saveErrors(request, errors);

				pageOf = Constants.FAILURE;
			}
		}
		if(Constants.SUCCESS.equals(pageOf))
		{
			pageOf = (Validator.isEmpty(cpQuery)||"null".equals(cpQuery))?Constants.SUCCESS:"cpQuerySuccess";
		}

		return mapping.findForward(pageOf);
	}

	/**
	 * This method validates the formbean.
	 * @param request : request
	 * @param form : form
	 * @return String : String
	 */
	private String validate(HttpServletRequest request, QuickEventsForm form)
	{
		final Validator validator = new Validator();
		ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
		String pageOf = Constants.SUCCESS;
		if (errors == null)
		{
			errors = new ActionErrors();
		}
		if (form.getCheckedButton().equals("1")
				&& !validator.isValidOption(form.getSpecimenLabel()))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
					ApplicationProperties.getValue("quickEvents.specimenLabel")));
			pageOf = Constants.FAILURE;
		}
		if (("2").equals(form.getCheckedButton()) && Validator.isEmpty(form.getBarCode()))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
					ApplicationProperties.getValue("quickEvents.barcode")));
			pageOf = Constants.FAILURE;
		}
		// resolved bug#4121
		int errorCount = 0;
		for (int iCount = 0; iCount < Constants.EVENT_PARAMETERS.length; iCount++)
		{
			if (!Constants.EVENT_PARAMETERS[iCount].equalsIgnoreCase(form
					.getSpecimenEventParameter()))
			{
				errorCount = errorCount + 1;
			}
		}
		if (errorCount == Constants.EVENT_PARAMETERS.length)
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
					ApplicationProperties.getValue("valid.quickEvents.eventparameters")));
			pageOf = Constants.FAILURE;
		}
		this.saveErrors(request, errors);
		return pageOf;
	}

	/**
	 *
	 * @param sourceObject : sourceObject
	 * @param value : value
	 * @param bizlogic : bizlogic
	 * @return String : String
	 * @throws Exception : Exception
	 */
	private String isExistingSpecimen(String sourceObject, String value, IBizLogic bizlogic)
			throws Exception
	{
		String returnValue = "0";

		final String sourceObjectName = Specimen.class.getName();
		final String[] selectColumnName = {Constants.SYSTEM_IDENTIFIER};
		final String[] whereColumnName = {sourceObject, Status.ACTIVITY_STATUS.toString(), Status.ACTIVITY_STATUS.toString()};
		//"storageContainer."+Constants.SYSTEM_IDENTIFIER
		final String[] whereColumnCondition = {"=", "!=", "!="};
		final Object[] whereColumnValue = {value, Status.ACTIVITY_STATUS_DISABLED.toString(), Status.ACTIVITY_STATUS_CLOSED.toString()};
		final String joinCondition = Constants.AND_JOIN_CONDITION;

		final List list = bizlogic.retrieve(sourceObjectName, selectColumnName, whereColumnName,
				whereColumnCondition, whereColumnValue, joinCondition);

		Logger.out.debug("MD 04-July-06 : - ><><>< " + sourceObject + " : " + value);
		String specimenID = "0";
		if (!list.isEmpty())
		{
			final Object obj = list.get(0);
			Logger.out.debug("04-July-06 :- " + obj.getClass().getName());
			final Long specimen = (Long) obj;
			specimenID = specimen.toString();

			returnValue = specimenID;
		}
		else
		{
			returnValue = "0";
		}
		Logger.out.debug("MD 04-July-06 : - ><><>< " + sourceObject + " : " + value
				+ " Found SpecimenID:  " + returnValue);

		return returnValue;
	}

}
