package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.actionForm.EventParametersForm;
import edu.wustl.catissuecore.actionForm.FluidSpecimenReviewEventParametersForm;

import edu.wustl.catissuecore.util.global.Constants;

public class FluidSpecimenReviewEventParametersAction extends SpecimenEventParametersAction
{
@Override
protected void setRequestParameters(HttpServletRequest request, EventParametersForm eventParametersForm) throws Exception
{
	 String operation=Constants.OPERATION ;
     String formName,specimenId=null;
     specimenId = (String) request.getAttribute(Constants.SPECIMEN_ID);
     
     FluidSpecimenReviewEventParametersForm fluidSpecimenReviewEventParametersForm=(FluidSpecimenReviewEventParametersForm)eventParametersForm;

     boolean readOnlyValue;
     if (fluidSpecimenReviewEventParametersForm.getOperation().equals(Constants.EDIT))
     {
         formName = Constants.FLUID_SPECIMEN_REVIEW_EVENT_PARAMETERS_EDIT_ACTION;
         readOnlyValue = true;
     }
     else
     {
         formName = Constants.FLUID_SPECIMEN_REVIEW_EVENT_PARAMETERS_ADD_ACTION;
			
         readOnlyValue = false;
     }
        String changeAction = "setFormAction('" + formName + "');";
	    request.setAttribute("formName", formName);
		request.setAttribute("readOnlyValue", readOnlyValue);
		request.setAttribute("changeAction", changeAction);
		request.setAttribute("specimenId", specimenId);
		request.setAttribute("fluidSpecimenReviewEventParametersAddAction", Constants.FLUID_SPECIMEN_REVIEW_EVENT_PARAMETERS_ADD_ACTION);
		

	// TODO Auto-generated method stub
	
}
}
