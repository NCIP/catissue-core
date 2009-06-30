/**
 * <p>Title: ReceivedEventParametersAction Class>
 * <p>Description:	This class initializes the fields in the ReceivedEventParameters Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on Aug 04, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.actionForm.EventParametersForm;
import edu.wustl.catissuecore.actionForm.ReceivedEventParametersForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.cde.CDEManager;

/**
 * @author mandar_deshmukh
 *
 * This class initializes the fields in the ReceivedEventParameters Add/Edit webpage.
 */
public class ReceivedEventParametersAction extends SpecimenEventParametersAction
{

	protected void setRequestParameters(HttpServletRequest request,
			EventParametersForm eventParametersForm) throws Exception
	{

		//String operation = (String) request.getAttribute(Constants.OPERATION);
		String formName, specimenId = null;

		boolean readOnlyValue;
		ReceivedEventParametersForm receivedEventParametersForm = (ReceivedEventParametersForm) eventParametersForm;
		if (receivedEventParametersForm.getOperation().equals(Constants.EDIT))
		{
			formName = Constants.RECEIVED_EVENT_PARAMETERS_EDIT_ACTION;
			readOnlyValue = true;
		}
		else
		{
			formName = Constants.RECEIVED_EVENT_PARAMETERS_ADD_ACTION;
			specimenId = (String) request.getAttribute(Constants.SPECIMEN_ID);
			readOnlyValue = false;
		}
		// set the ReceivedQuality List.
		List qualityList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_RECEIVED_QUALITY, null);
		request.setAttribute("receivedQualityList", qualityList);
		request.setAttribute("formName", formName);

	}

}
