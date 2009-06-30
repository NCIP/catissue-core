/**
 * <p>Title: EmbeddedEventParametersAction Class>
 * <p>Description:	This class initializes the fields in the EmbeddedEventParameters Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on Aug 5, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.actionForm.EmbeddedEventParametersForm;
import edu.wustl.catissuecore.actionForm.EventParametersForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.cde.CDEManager;

/**
 * This class initializes the fields in the EmbeddedEventParameters Add/Edit webpage.
 
 */
public class EmbeddedEventParametersAction extends SpecimenEventParametersAction
{

	/**
	 * @param request object of HttpServletRequest
	 * @throws Exception generic exception
	 */
	protected void setRequestParameters(HttpServletRequest request,
			EventParametersForm eventParametersForm) throws Exception
	{
		//String operation = (String) request.getAttribute(Constants.OPERATION);
		String formName, specimenId = null;
		EmbeddedEventParametersForm embeddedEventParametersForm = (EmbeddedEventParametersForm) eventParametersForm;
		boolean readOnlyValue;
		if (embeddedEventParametersForm.getOperation().equals(Constants.EDIT))
		{
			formName = Constants.EMBEDDED_EVENT_PARAMETERS_EDIT_ACTION;
			readOnlyValue = true;
		}
		else
		{
			formName = Constants.EMBEDDED_EVENT_PARAMETERS_ADD_ACTION;
			specimenId = (String) request.getAttribute(Constants.SPECIMEN_ID);
			readOnlyValue = false;
		}
		String changeAction = "setFormAction('" + formName + "');";
		request.setAttribute("formName", formName);
		request.setAttribute("readOnlyValue", readOnlyValue);
		request.setAttribute("changeAction", changeAction);
		request.setAttribute("embeddedEventParametersAddAction",
				Constants.EMBEDDED_EVENT_PARAMETERS_ADD_ACTION);

		//set array of EmbeddingMedium
		List embeddingMediumList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_EMBEDDING_MEDIUM, null);
		request.setAttribute("embeddingMediumList", embeddingMediumList);
	}

}