package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.OrderBiospecimenArrayForm;
import edu.wustl.catissuecore.actionForm.OrderPathologyCaseForm;
import edu.wustl.catissuecore.actionForm.OrderSpecimenForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.logger.Logger;

public class DirectDistributeAction extends BaseAction
{

	private transient Logger logger = Logger.getCommonLogger(DirectDistributeAction.class);
	 /**
    * @param mapping ActionMapping object
    * @param form ActionForm object
    * @param request HttpServletRequest object
    * @param response HttpServletResponse object
    * @return ActionForward object
    * @throws Exception object
    */
   public ActionForward executeAction(ActionMapping mapping, ActionForm form,
           HttpServletRequest request, HttpServletResponse response) throws Exception
   {
	  
	   logger.debug("Inside DirectDistributeSpecAction ");
	   String typeOf = request.getParameter("typeOf");
	   Long orderId = null;
	   if(typeOf.equals(Constants.SPECIMEN_ORDER_FORM_TYPE))
	   {
		   OrderSpecimenForm orderSpecimenForm = (OrderSpecimenForm) form;
		   orderId = orderSpecimenForm.getId();
	   }
	   else if(typeOf.equals(Constants.ARRAY_ORDER_FORM_TYPE))
       {
		   OrderBiospecimenArrayForm orderArrayForm = (OrderBiospecimenArrayForm) form;
		   orderId = orderArrayForm.getId();
	   }
	   else
	   {
		   OrderPathologyCaseForm pathologyForm = (OrderPathologyCaseForm) form;
		   orderId = pathologyForm.getId();
	   }
	   logger.debug("order Id ::"+orderId);
	   request.setAttribute("id", orderId.toString());
  	   
	   return mapping.findForward("success");
   }
}
