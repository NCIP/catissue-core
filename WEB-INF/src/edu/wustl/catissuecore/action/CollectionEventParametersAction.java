/**
 * <p>Title: CollectionEventParametersAction Class>
 * <p>Description:	This class initializes the fields in the CollectionEventParameters Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on Aug 04, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.actionForm.CollectionEventParametersForm;
import edu.wustl.catissuecore.actionForm.EventParametersForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.cde.CDEManager;

/**
 * @author mandar_deshmukh
 *
 * This class initializes the fields in the CollectionEventParameters Add/Edit webpage.
 */
public class CollectionEventParametersAction extends SpecimenEventParametersAction
{

	/**
	 * @param  request object of HttpServletRequest
	 * @param  eventParametersForm : eventParametersForm
	 * @throws Exception generic exception
	 */
	protected void setRequestParameters(HttpServletRequest request,
			EventParametersForm eventParametersForm) throws Exception
	{
		String formName = null;
		boolean readOnlyValue;
		CollectionEventParametersForm collectionEventParametersForm =
			(CollectionEventParametersForm) eventParametersForm;
		if (collectionEventParametersForm.getOperation().equals(Constants.EDIT))
		{
			formName = Constants.COLLECTION_EVENT_PARAMETERS_EDIT_ACTION;
			readOnlyValue = true;
		}
		else
		{
			formName = Constants.COLLECTION_EVENT_PARAMETERS_ADD_ACTION;
			readOnlyValue = false;
		}
		String changeAction = "setFormAction('" + formName + "');";
		request.setAttribute("formName", formName);
		request.setAttribute("readOnlyValue", readOnlyValue);
		request.setAttribute("changeAction", changeAction);
		// set the procedure lists
		List procedureList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_COLLECTION_PROCEDURE, null);
		request.setAttribute("procedureList", procedureList);

		// set the container lists
		List containerList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_CONTAINER, null);
		request.setAttribute("containerList", containerList);
		request.setAttribute("collectionEventParametersAction",
				Constants.COLLECTION_EVENT_PARAMETERS_ADD_ACTION);
	}

}
