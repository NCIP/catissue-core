/**
 * <p>Title: TissueSpecimenReviewEventParametersAction Class>
 * <p>Description:	This class initializes the fields in the
 *  TissueSpecimenReviewEventParameters Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on Aug 31, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.actionForm.EventParametersForm;
import edu.wustl.catissuecore.actionForm.TissueSpecimenReviewEventParametersForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.cde.CDEManager;

/**
 * @author mandar_deshmukh
 * This class initializes the fields in the TissueSpecimenReviewEventParameters Add/Edit webpage.
 *
 */
public class TissueSpecimenReviewEventParametersAction extends SpecimenEventParametersAction
{
	/**
	* @param request object of HttpServletRequest
	* @param eventParametersForm : eventParametersForm
	* @throws Exception : Exception
	*/
	protected void setRequestParameters(HttpServletRequest request,
			EventParametersForm eventParametersForm) throws Exception
	{

		//String operation = (String) request.getAttribute(Constants.OPERATION);
		String formName, specimenId = null;

		boolean readOnlyValue;
		TissueSpecimenReviewEventParametersForm tissueSpecimenReviewEventParametersForm =
			(TissueSpecimenReviewEventParametersForm) eventParametersForm;
		if (tissueSpecimenReviewEventParametersForm.getOperation().equals(Constants.EDIT))
		{
			formName = Constants.TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_EDIT_ACTION;
			readOnlyValue = true;
		}
		else
		{
			formName = Constants.TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_ADD_ACTION;
			specimenId = (String) request.getAttribute(Constants.SPECIMEN_ID);
			readOnlyValue = false;
		}
		String changeAction = "setFormAction('" + formName + "');";
		request.setAttribute("formName", formName);
		request.setAttribute("readOnlyValue", readOnlyValue);
		request.setAttribute("changeAction", changeAction);

		//		set array of histological quality
		List histologicalQualityList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_HISTOLOGICAL_QUALITY, null);
		request.setAttribute("histologicalQualityList", histologicalQualityList);
	}

}
